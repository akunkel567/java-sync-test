/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.tools;

import de.complex.transfer.sftp.SftpSession;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author kunkel
 */
public class SftpTransfer {

    private static final Logger LOG = LogManager.getLogger(SftpTransfer.class);

    private String host;
    private String username;
    private String password;
    private String remoteIn;
    private File file;

    public SftpTransfer(String host, String username, String password, String remoteIn, File file) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.remoteIn = remoteIn;
        this.file = file;
    }

    public void send() throws SftpUploadException {

        SftpSession session = null;
        String remotePath = null;
        try {
            session = SftpSession.getInstance();
            session.connect(host, username, password);
            ReaderInputStream inStream = new ReaderInputStream(new FileReader(this.file));
            try {
                remotePath = remoteIn + (remoteIn.endsWith("/") ? "" : "/") + file.getName();
                session.upload(inStream, remotePath);
            } finally {
                inStream.close();
            }
        } catch (Exception ex) {
            LOG.error(String.format("SFTP-Übertragung fehlgeschlagen. Host: %s User: %s RemotePath: %s", host, username, remotePath), ex);
            throw new SftpUploadException(String.format("SFTP-Übertragung fehlgeschlagen. Host: %s User: %s RemotePath: %s", host, username, remotePath), ex);
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (IOException ex) {
                // nix			
            }
        }
    }
}
