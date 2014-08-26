/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.eventhandler.fileevent;

/**
 *
 * @author kunkel
 */
public class FileConvertException extends Exception {

	/**
	 * Constructs an instance of
	 * <code>ClxFileException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public FileConvertException(String msg) {
		super(msg);
	}

	public FileConvertException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileConvertException(Throwable cause) {
		super(cause);
	}
	
	
}
