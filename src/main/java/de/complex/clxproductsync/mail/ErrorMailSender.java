/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.mail;

import de.complex.tools.mail.Mailer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class ErrorMailSender {

	private static Logger logger = Logger.getLogger(ErrorMailSender.class);
	private Mailer mailer;
	private final String ERRORMAILLOGPATH = "errorMailLog";

	public ErrorMailSender(Mailer mailer) {
		this.mailer = mailer;
	}

	public void sendErrorMail(String errorType, long jobId, String errorSubject, String errorText) {
		File errorMailLog = new File(ERRORMAILLOGPATH);
		if (!errorMailLog.exists()) {
			errorMailLog.mkdir();
		}

		File errorTypePath = new File(errorMailLog.getAbsolutePath() + File.separatorChar + errorType);
		if (!errorTypePath.exists()) {
			errorTypePath.mkdir();
		}

		File file = new File(errorTypePath.getAbsolutePath() + File.separatorChar + jobId + ".txt");

		ErrorMailSender.logger.debug("Path: " + file.getAbsolutePath());

		if (!file.exists()) {
			ErrorMailSender.logger.debug("Send ErrorMail");
			this.mailer.postErrorMail(errorSubject, errorText);

			Writer w;
			try {
				w = new FileWriter(file);
				w.write(errorText);
				w.close();
			} catch (IOException ex) {
				ErrorMailSender.logger.error("ErrorMail-Logfile konnte nicht angelegt werden.", ex);
			}
		} else {
			ErrorMailSender.logger.info("ErrorMail nicht wiederholt gesendet");
		}
	}
}
