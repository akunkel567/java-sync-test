/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class ClxFtp {

    private static Logger logger = Logger.getLogger(ClxFtp.class);
    private FTPClient ftpclient;
    private String user;
    private String password;
    private String host;
    private boolean passiveMode = false;

    public ClxFtp() {
        this("", "", "");
    }

    public ClxFtp(String host, String user, String password) {
        this(host, user, password, false);
    }

    public ClxFtp(String host, String user, String password, boolean passiveMode) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.passiveMode = passiveMode;

        this.ftpclient = new FTPClient();
    }

    public ClxFtp(Properties prop) {
        this.host = prop.getProperty("ftp.host", "");
        this.user = prop.getProperty("ftp.username", "");
        this.password = prop.getProperty("ftp.password", "");

        if (prop.getProperty("ftp.passivemode", "false").equalsIgnoreCase("true")) {
            this.passiveMode = true;
        } else {
            this.passiveMode = false;
        }

        this.ftpclient = new FTPClient();
    }

    public boolean uploadFile(File file, String remotePath) {
        try {
            ClxFtp.logger.debug("FTP Upload File host:" + host + " user:" + user + " pass:" + password + " file:" + file.getName() + " remote:" + remotePath);

            if (this.connect(host)) {
                if (this.login(user, password)) {
                    if (this.upload(file, remotePath)) {
                        ClxFtp.logger.debug("Upload OK: " + file.getName());
                        this.logout();

                        return true;
                    } else {
                        ClxFtp.logger.error("Upload ERROR: " + file.getName());
                        return false;
                    }
                } else {
                    ClxFtp.logger.error("Login ERROR: ");
                    return false;
                }
            } else {
                ClxFtp.logger.error("Connecting ERROR: ");
                return false;
            }
        } catch (Exception e) {
            ClxFtp.logger.error(e);
            return false;
        } finally {
            if (this.isConneted()) {
                this.disconnect();
            }
        }
    }

    public boolean downloadFiles(String remotePath, String localPath, boolean deleteAfterDownload) {
        return this.downloadFiles(remotePath, localPath, deleteAfterDownload, null);
    }

    public boolean downloadFiles(String remotePath, String localPath, boolean deleteAfterDownload, String filenameSuffix) {
        return this.downloadFiles(remotePath, localPath, deleteAfterDownload, filenameSuffix, false);
    }

    public boolean downloadFiles(String remotePath, String localPath, boolean deleteAfterDownload, String filenameSuffix, boolean passiveMode) {

        try {
            ClxFtp.logger.debug("FTP DownloadFiles host:" + host + " user:" + user + " pass:" + password + " local:" + localPath + " remote:" + remotePath);
//			ftp = new Ftp_new(host, user, password, passiveMode);
            if (this.connect(host)) {
                if (this.login(user, password)) {
                    if (this.download(remotePath, localPath, deleteAfterDownload, filenameSuffix)) {
                        ClxFtp.logger.debug("Download OK remotePath: " + remotePath + " localPath: " + localPath);
                        this.logout();

                        return true;
                    } else {
                        ClxFtp.logger.error("Download Error remotePath: " + remotePath + " localPath: " + localPath);
                        return false;
                    }
                } else {
                    ClxFtp.logger.error("Login ERROR: ");
                    return false;
                }
            } else {
                ClxFtp.logger.error("Connecting ERROR: ");
                return false;
            }
        } catch (Exception e) {
            ClxFtp.logger.error(e);
            return false;
        } finally {
            if (this.isConneted()) {
                this.disconnect();
            }
        }
    }

    private boolean isConneted() {
        return this.ftpclient.isConnected();
    }

    private boolean connect(String host) {
        if (this.ftpclient.isConnected()) {
            try {
                this.ftpclient.disconnect();
            } catch (IOException ex) {
                // ..nix
            }
        }

        try {
            if (!this.ftpclient.isConnected()) {
                int reply;
                this.ftpclient.connect(this.getHost());

                // After connection attempt, you should check the reply code to verify success
                reply = this.ftpclient.getReplyCode();

                if (!FTPReply.isPositiveCompletion(reply)) {
                    this.ftpclient.disconnect();
                    ClxFtp.logger.error("FTP server refused connection.");
                    return false;
                }
            }
        } catch (IOException e) {
            ClxFtp.logger.error(e, e);

            if (this.ftpclient.isConnected()) {
                try {
                    this.ftpclient.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }

            ClxFtp.logger.error("Could not connect to server.", e);
            return false;
        }

        return true;
    }

    private boolean connect() {
        return this.connect(this.getHost());
    }

    private boolean login(String user, String password) {
        try {
            return this.ftpclient.login(user, password);
        } catch (IOException ex) {
            ClxFtp.logger.error("Login-error", ex);
            return false;
        }
    }

    private boolean login() {
        return this.login(this.getUser(), this.getPassword());
    }

    private boolean logout() {
        try {
            return this.ftpclient.logout();
        } catch (IOException ex) {
            ClxFtp.logger.error("Logout-Error", ex);
            return false;
        }
    }

    public boolean download(String remotePath, String localPath, boolean deleteAfterDownload) {
        return this.download(remotePath, localPath, deleteAfterDownload, null);
    }

    public boolean download(String remotePath, String localPath, boolean deleteAfterDownload, String filenameSuffix) {
        try {
            ClxFtp.logger.debug("remotePath: " + remotePath + " localPath: " + localPath);
            FTPFile[] files = this.ftpclient.listFiles(remotePath);
            if (files != null) {
                ClxFtp.logger.debug("Count files/directories: " + files.length);
            }
            for (FTPFile file : files) {
                if (file.isFile()) {
                    ClxFtp.logger.debug("File name: " + file.getName() + " size: " + file.getSize());

                    if (filenameSuffix != null && !filenameSuffix.equals("")) {
                        ClxFtp.logger.debug("Suffix : " + filenameSuffix);
                        //wenn suffix angegeben
                        if (file.getName().endsWith(filenameSuffix)) {
                            ClxFtp.logger.debug("File name: " + file.getName() + " Suffix passt, File downloaden");
                            if (this.download(remotePath, file.getName(), localPath) && deleteAfterDownload) {
                                this.ftpclient.deleteFile(remotePath.equals("") ? file.getName() : remotePath + "/" + file.getName());
                            }
                        } else {
                            ClxFtp.logger.debug("File name: " + file.getName() + " Suffix passt nicht, File �berspringen");
                        }
                    } else {
                        ClxFtp.logger.debug("kein Suffix vorgegeben");
                        // kein suffix angegeben, immer download
                        if (this.download(remotePath, file.getName(), localPath) && deleteAfterDownload) {
                            this.ftpclient.deleteFile(remotePath.equals("") ? file.getName() : remotePath + "/" + file.getName());
                        }
                    }
                }
            }
            return true;
        } catch (IOException ex) {
            ClxFtp.logger.error(ex, ex);
            return false;
        }
    }

    public boolean download(String remotePath, String name, String localPath) {

        String remoteFile = remotePath.equals("") ? name : remotePath + "/" + name;

        BufferedOutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(localPath + System.getProperty("file.separator") + name));

            ClxFtp.logger.debug("Download Filename: " + remoteFile);
            long lastLastSize = -1;
            long lastSize = 0;

            int MAXCHECK = 10;
            int MINCHECK = 3;

            int okCheckCount = 0;
            int maxCheckCount = 0;

            boolean fileOK = false;

            while (true) {
                maxCheckCount++;
                ClxFtp.logger.debug("LastSize Filename: " + remoteFile);

                FTPFile[] file = this.ftpclient.listFiles(remoteFile);
                lastSize = file[0].getSize();

                ClxFtp.logger.debug("LastSize     :" + lastSize);
                ClxFtp.logger.debug("lastLastSize :" + lastLastSize);

                if (lastSize == lastLastSize) {
                    ClxFtp.logger.debug("Size ist gleich");
                    okCheckCount++;

                    // nur wenn Datei mehrfach gleiche LastModificationTime hat dann gehts weiter
                    if (okCheckCount >= MINCHECK) {
                        ClxFtp.logger.debug("MinCheck OK, Datei Downloaden");
                        fileOK = true;
                        break;
                    } else {
                        ClxFtp.logger.debug("MinCheck NOT OK, weiter pruefen");
                    }
                } else {
                    okCheckCount = 0;
                    lastLastSize = lastSize;
                    ClxFtp.logger.debug("Size nicht gleich");
                }

                // wenn sich die LastModificationTime nach x-Versuchen immer noch �ndert dann File �berspringen
                if (maxCheckCount >= MAXCHECK) {
                    ClxFtp.logger.debug("MaxCheck NOT OK");
                    break;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ClxFtp.logger.error("Sleep", ex);
                }
            }

            if (fileOK) {
                if (this.ftpclient.retrieveFile(remoteFile, output)) {
                    System.out.println("ok");
                    return true;
                } else {
                    System.out.println("error");
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException ioe) {
            ClxFtp.logger.error(ioe, ioe);
            return false;
        } finally {
            try {
                output.flush();
                output.close();
                output = null;
            } catch (IOException ioe) {
                // nix
            }
        }
    }

    public boolean uploadFiles(String localPath, String remotePath, boolean deleteAfterUpload) {

        File localDir = new File(localPath);

        File files[] = localDir.listFiles();

        for (File file : files) {
            if (this.upload(file, remotePath) && deleteAfterUpload) {
                file.delete();
            }
        }

        return true;
    }

    public boolean upload(File file, String remotePath) {
        String remoteFile = remotePath.equals("") ? file.getName() : remotePath + "/" + file.getName();

        ClxFtp.logger.debug(remoteFile);

        FileInputStream input;
        try {
            input = new FileInputStream(file);
            
            if(this.passiveMode){
                this.ftpclient.enterLocalPassiveMode();
            }
            
            this.ftpclient.storeFile(remoteFile, input);
            input.close();
            input = null;
        } catch (IOException ioe) {
            ClxFtp.logger.error(ioe, ioe);
            input = null;
            return false;
        }

        return true;
    }

    private void disconnect() {
        try {
            this.ftpclient.disconnect();
        } catch (IOException ex) {
            ClxFtp.logger.error("Disconnect-Error", ex);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public FTPClient getFtpclient() {
        return ftpclient;
    }
}
