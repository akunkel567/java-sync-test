/*
 * ImageEventHandler.java
 *
 * Created on 7. November 2006, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.fileevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.dao.SnJob;
import de.complex.clxproductsync.dao.SnJobDAO;
import de.complex.clxproductsync.exception.JobException;
import de.complex.clxproductsync.soap.RemoteCallException;
import de.complex.clxproductsync.soap.SoapHandler;

import de.complex.tools.config.ApplicationConfig;
import de.complex.tools.mail.Mailer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.util.Properties;

import org.apache.axis.AxisFault;
import org.apache.commons.codec.binary.Base64;

import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class FileEventHandler {

	private static Logger logger = Logger.getLogger(FileEventHandler.class);
	private FirebirdDb db = null;
	private SnJobDAO snJobDAO = null;

	/**
	 * Creates a new instance of ImageEventHandler
	 */
	public FileEventHandler(FirebirdDb db) {
		this.db = db;
		this.snJobDAO = new SnJobDAO(db);
	}

	public void run() {
		FileEventHandler.logger.debug("FileEvent run");
		Integer idList[] = null;
		boolean uploadOK;

		SnJob snJobs[] = snJobDAO.getNextJobs(FileEventChecker.EVENTNAME, 100);
		SnJob currJob = null;
		if (snJobs.length != 0) {
			for (SnJob job: snJobs) { // schleife snjob's
				currJob = job;
				FileEventHandler.logger.info("Job start - " + currJob);
				try {
					if (job.getSnJobFremdId() != 0) {
						// mit id
						if (!job.getFileName().equals("")) { // mit Filename, einzelnes Upload
							// Einzelner Artikel - BilderUpload
							if (job.getSnJobTyp().equalsIgnoreCase("D")) {
								// delete
								this.deleteFile(job, job.getSnJobFremdId(), job.getHerkunft(), job.getFileName());
								snJobDAO.setSnJobDone(job);
								FileEventHandler.logger.info("Job done - " + job);

							} else {
								// upload
								this.uploadFile(job, job.getSnJobFremdId(), job.getHerkunft(), job.getFileName());
								snJobDAO.setSnJobDone(job);
								FileEventHandler.logger.info("Job done - " + job);
							}
						} else {
							throw new Exception("uebertragung/loeschen aller Files zu einem Detsail ist nicht implementiert!");
							// ohne Filename - alle Files zu id
//							if (job.getSnJobTyp().equalsIgnoreCase("D")) {
//								// delete
//								FileEventHandler.logger.info("Delete Alle Files zu einer Id ist nicht implementiert. Snjob Done...!");
//								snJobDAO.setSnJobDone(job);
//								FileEventHandler.logger.info("Job: " + job + " done");
//
//							} else {
//								// upload
//								if (this.uploadFiles(job.getSnJobFremdId(), job.getHerkunft())) {
//									snJobDAO.setSnJobDone(job);
//								}
//							}
						}
					} else {
						FileEventHandler.logger.debug("FileEvent-Job ohne Fremdid " + currJob);
						throw new JobException("FileEvent-Job ohne Fremdid");
					}
				} catch (Exception e) {
					FileEventHandler.logger.error("Job error - " + currJob, e);
				}
			}
		} else {
			FileEventHandler.logger.info("FileEvent nothing to do");
		}
	}

	private void uploadFile(SnJob job, int fremdId, String herkunft, String fileName) throws RemoteCallException, FileNotFoundException {
		String rootPath = ApplicationConfig.getValue("fileRootPath", ".");
		String fileSeparator = ApplicationConfig.getValue("fileSeparator", "\\");

		String path = rootPath + fileSeparator + herkunft + fileSeparator + fremdId + fileSeparator + fileName;

		FileEventHandler.logger.debug("FilePath: " + path);

		File file = new File(path);
		FileEventHandler.logger.debug("File: " + file.getAbsolutePath());
		
		if(!file.exists()){
			throw new FileNotFoundException("File: " + file.getAbsolutePath() + " not found");
		}

		SoapHandler.sendFileData(job, herkunft, fremdId, file);
	}

	private void deleteFile(SnJob job, int fremdId, String herkunft, String fileName) throws RemoteCallException {
		SoapHandler.deleteFile(job, herkunft, fremdId, fileName);
	}

	// Upload alle Files zu einer Angk
	private boolean uploadFiles(int fremdId, String herkunft) {
		boolean result = true;

		throw new IllegalStateException("not implemented yet");


//		String filePath;
//		InputStream inStream = null;
//		byte b[] = null;
//		Base64 base64 = null;
//		Answer answer = null;
//
//		FileService service = null;
//		FilePort stub = null;
//
//		String pathProp = "";
//
//		try {
//			if (service == null) {
//				service = new FileServiceLocator();
//			}
//			if (stub == null) {
//				try {
//					stub = service.getfilePort(new URL(prop.getProperty("wsURL").trim() + "/file"));
//				} catch (javax.xml.rpc.ServiceException se) {
//					FileEventHandler.logger.error(se);
//				} catch (java.net.MalformedURLException mue) {
//					FileEventHandler.logger.error(mue);
//				}
//			}
//
//			// bei erweiterung hier anpassen
//			if (herkunft.equalsIgnoreCase("ANGK")) {
//				pathProp = this.prop.getProperty("angkFilePath");
//			} else {
//				FileEventHandler.logger.error("kein Property f�r gesetze Herkunft");
//				result = false;
//			}
//
//			filePath = pathProp + System.getProperty("file.separator") + fremdId;
//			FileEventHandler.logger.info("File-Path :" + filePath);
//
//			logger.info("DeleteAllFiles zu fremdId :" + fremdId + " herkunft:" + herkunft);
//			stub.deleteAllFiles(prop.getProperty("wsUser").trim(), prop.getProperty("wsPass").trim(), herkunft, fremdId);
//
//			File files[] = new File(filePath).listFiles();
//
//			if (files != null) {
//				FileEventHandler.logger.info("Anzahl Files :" + files.length);
//			}
//
//			if (files != null) {
//				FileEventHandler.logger.info("uebertrage All zu fremdId :" + fremdId + " herkunft :" + herkunft);
//				for (int j = 0; j < files.length; j++) {
//					if (files[j].getName().toLowerCase().endsWith(".jpg")) { // nur .jpg
//						this.uploadFile(fremdId, herkunft, files[j].getName());
//					}
//				}
//			} else {
//				FileEventHandler.logger.info("keine Dateien im Verzeichnis : " + filePath + " gefunden...!");
//			}
//		} catch (java.rmi.RemoteException re) {
//			FileEventHandler.logger.error("SOAP-Error - File Upload");
//			FileEventHandler.logger.error(re);
//
//			stub = null;
//			service = null;
//
//			if (ApplicationConfig.hasObject("mailer")) {
//				((Mailer) ApplicationConfig.getObject("mailer")).postErrorMail(" SOAP-Error ", "File-Upload Id :" + fremdId, this.getHandleEventType(), 0);
//			}
//
//
//			result = false;
//		}
//		return result;
	}

	public void uploadAll() {
	}

	protected int delete(int dataId) {
		return -1;
	}

	protected int upload(int dataId) {
		return -1;
	}

}
