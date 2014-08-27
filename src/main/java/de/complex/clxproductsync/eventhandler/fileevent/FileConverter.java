/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.fileevent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class FileConverter {

	private static Logger logger = Logger.getLogger(FileConverter.class);

	public static byte[] toBase64byteArray(File file) throws FileConvertException {
		byte inByte[] = null;
		byte b[] = null;

		inByte = getFileAsByteArray(file);
		if (inByte != null) {

			Base64 base64 = new Base64();
			b = base64.encode(inByte);
		}

		return b;
	}

	public static String toBase64String(File file) throws FileConvertException {
		byte inByte[] = null;
		byte b[] = null;
		String outString = null;

		inByte = getFileAsByteArray(file);
		if (inByte != null) {

			Base64 base64 = new Base64();
			b = base64.encode(inByte);
			outString = new String(b);
		}

		return outString;
	}

	private static byte[] getFileAsByteArray(File file) throws FileConvertException {
		InputStream inStream = null;
		byte b[] = null;
		String str = null;
		Base64 base64 = null;
		byte inByte[] = null;
		try {

//			if (!file.exists()) {
//				throw new FileNotFoundException("File " + file.getAbsolutePath() + " not exists");
//			}
//
//			if (!file.canRead()) {
//				throw new FileConvertException("File " + file.getAbsolutePath() + " can not read");
//			}
//
//			if (file.exists() && file.canRead()) {
				inByte = new byte[(int) file.length()];

				try {
					inStream = new FileInputStream(file);
					inStream.read(inByte);

				} finally {
					try {
						inStream.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

//			}
		} catch (NullPointerException npe) {
			FileConverter.logger.debug("", npe);
			throw new FileConvertException(npe);
		} catch (FileNotFoundException fnf) {
			FileConverter.logger.debug("", fnf);
			throw new FileConvertException(fnf);
		} catch (IOException ioe) {
			FileConverter.logger.debug("", ioe);
			throw new FileConvertException(ioe);
		}

		return inByte;
	}
}
