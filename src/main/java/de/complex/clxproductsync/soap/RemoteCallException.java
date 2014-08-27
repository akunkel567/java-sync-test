/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.soap;

/**
 *
 * @author kunkel
 */
public class RemoteCallException extends Exception {

	/**
	 * Constructs an instance of
	 * <code>ClxSoapTransferException</code> with the specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public RemoteCallException(String msg) {
		super(msg);
	}

	public RemoteCallException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemoteCallException(Throwable cause) {
		super(cause);
	}
}
