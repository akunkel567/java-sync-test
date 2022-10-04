/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.soap;

import de.complex.clxproductsync.dao.SnJob;
import de.complex.clxproductsync.eventhandler.fileevent.FileConvertException;
import de.complex.util.lang.StringTool;
import jakarta.xml.ws.BindingProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.complex.clxproductsync.eventhandler.fileevent.FileConverter;
import de.complex.clxproductsync.soap.transfer.*;
import de.complex.tools.config.ApplicationConfig;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;


/**
 *
 * @author kunkel
 */
public class SoapHandler {

    private static Logger logger = LogManager.getLogger(SoapHandler.class);

    public SoapHandler() {
    }

    public static TransferPort getTransferPort(){
        TransferService service = new TransferService();
        TransferPort port = service.getTransferPort();

        String endpointURL = ApplicationConfig.getValue("ws_url").trim() + "/transfer";
        BindingProvider bp = (BindingProvider) port;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

        return port;
    }

    public static void sendXmlData(final SnJob snJob, final String xmlData) throws RemoteCallException {
        SoapHandler.logger.debug("call sendXmlData");

        RemoteCall<Void> r = new TransferRemoteCall<Void>() {
            @Override
            public Void runTransfer(TransferPort stub) throws MalformedURLException, RemoteException, RemoteCallException {
                int errorCount = 0;

                SnjobIn snjobIn = new SnjobIn();
                snjobIn.setEventname(snJob.getEventName());
                snjobIn.setFremdid(snJob.getSnJobFremdId());
                snjobIn.setHerkunft(snJob.getHerkunft() == null ? "" : snJob.getHerkunft());
                snjobIn.setJobtyp(snJob.getSnJobTyp() == null ? "" : snJob.getSnJobTyp());
                snjobIn.setSnjobid(snJob.getSnJobId());
                snjobIn.setSau(snJob.getSau() == null ? "" : snJob.getSau());
                snjobIn.setSadt(snJob.getSadt() == null ? "" : snJob.getSadt());
                snjobIn.setScu(snJob.getScu() == null ? "" : snJob.getScu());
                snjobIn.setScdt(snJob.getScdt() == null ? "" : snJob.getScdt());

                SoapAnswerList soapAnswerList = getTransferPort().setDataFromXml(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), snjobIn, xmlData);

                if (soapAnswerList != null) {
                    for (SoapAnswer answer : soapAnswerList.getItem()) {
                        if (answer.getStatus() != 0) {
                            errorCount++;
                        }
                    }
                    if (errorCount != 0) {
                        throw new RemoteCallException(snJob.toString());
                    }
                }

                return null;
            }
        };

        r.start();
    }

    public static void sendFileData(SnJob snJob, final String fremdkz, final int fremdid, final File file) throws RemoteCallException {
        SoapHandler.sendFileData(snJob, fremdkz, fremdid, file, null);
    }

    public static void sendFileData(SnJob snJob, final String fremdkz, final int fremdid, final File file, String remoteFilename) throws RemoteCallException {
        SoapHandler.logger.debug("call sendFileData fremdkz: " + fremdkz + " fremdid: " + fremdid + " file: " + file.getAbsolutePath());

        RemoteCall<Void> r = new TransferRemoteCall<Void>() {
            @Override
            public Void runTransfer(TransferPort stub) throws MalformedURLException, RemoteException, RemoteCallException {

                byte[] fileData;

                try {
                    fileData = FileConverter.toBase64byteArray(file);
                } catch (FileConvertException ex) {
                    throw new RemoteCallException(ex);
                }

                SoapHandler.logger.debug("SendFileData fileData length: " + fileData.length);

                if (fileData != null) {

                    FiledataIn fileDataIn = new FiledataIn();
                    fileDataIn.setFremdid(fremdid);
                    fileDataIn.setFremdkz(fremdkz);

                    if (StringTool.isEmpty(remoteFilename)) {
                        fileDataIn.setFilename(file.getName());
                    } else {
                        fileDataIn.setFilename(remoteFilename);
                    }
                    SoapHandler.logger.debug("SendFileData remoteFilename: " + fileDataIn.getFilename());

                    fileDataIn.setContent(fileData);

                    SoapAnswer answer = stub.setFile(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), fileDataIn);
                    SoapHandler.logger.debug("SendFileData answer: " + answer);

                    if (answer != null) {
                        SoapHandler.logger.debug("SendFileData answer status: " + answer.getStatus());

                        if (answer.getStatus() != 0) {
                            SoapHandler.logger.error("SendFileData answer.status " + answer.getStatus());
                            throw new RemoteCallException("SendFileData answer.status " + answer.getStatus());
                        }
                    } else {
                        throw new RemoteCallException("SendFileData answer is null");
                    }
                }
                return null;
            }
        };

        r.start();
    }

    public static void deleteFile(SnJob snJob, final String fremdkz, final int fremdid, final String filename) throws RemoteCallException {
        SoapHandler.logger.debug("call deleteFile");

        RemoteCall<Void> r = new TransferRemoteCall<Void>() {
            @Override
            public Void runTransfer(TransferPort stub) throws MalformedURLException, RemoteException, RemoteCallException {

                FiledataIn fileDataIn = new FiledataIn();
                fileDataIn.setFremdid(fremdid);
                fileDataIn.setFremdkz(fremdkz);
                fileDataIn.setFilename(filename);
                fileDataIn.setContent(new byte[0]);

                SoapAnswer answer = stub.deleteFile(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), fileDataIn);

                if (answer != null) {
                    if (answer.getStatus() != 0) {
                        throw new RemoteCallException("deleteFile answer.status " + answer.getStatus());
                    }
                } else {
                    throw new RemoteCallException("deleteFile - answer is null");
                }

                return null;
            }
        };

        r.start();
    }

    public static SnjobwebList getSnJobWebList(final int limit) throws RemoteCallException {
        SoapHandler.logger.debug("call getSnJobWebList");
        RemoteCall<SnjobwebList> r = new TransferRemoteCall<>() {
            @Override
            public SnjobwebList runTransfer(TransferPort stub) throws MalformedURLException, RemoteException {
                return getTransferPort().getListFromSnjobweb(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), limit);

            }
        };

        return r.start();
    }

    public static XmlOut getXmlData(final long snjobwebid) throws RemoteCallException {

        RemoteCall<XmlOut> r = new TransferRemoteCall<XmlOut>() {
            @Override
            public XmlOut runTransfer(TransferPort stub) throws MalformedURLException, RemoteException {
                return getTransferPort().getXmlFromSnjobweb(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), snjobwebid);
            }
        };
        return r.start();
    }

    public static void setSnJobWebOK(final long snjobwebid) throws RemoteCallException {

        RemoteCall<Void> r = new TransferRemoteCall<Void>() {
            @Override
            public Void runTransfer(TransferPort stub) throws MalformedURLException, RemoteException, RemoteCallException {

                SoapAnswer answer = getTransferPort().setSnjobwebOk(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), snjobwebid);

                if (answer != null) {
                    if (answer.getStatus() != 0) {
                        throw new RemoteCallException("setSnJobWebOK answer.status " + answer.getStatus());
                    }
                } else {
                    throw new RemoteCallException("setSnJobWebOK - answer is null");
                }

                return null;
            }
        };

        r.start();
    }

    private static class TransferRemoteCall<T> extends RemoteCall {

        protected T runTransfer(TransferPort stub) throws MalformedURLException, RemoteException, RemoteCallException {
            throw new IllegalStateException("implement me");
        }

        @Override
        protected T run() throws MalformedURLException, RemoteException, RemoteCallException {
            TransferPort stub = getTransferPort();

            return runTransfer(stub);
        }
    }
};
