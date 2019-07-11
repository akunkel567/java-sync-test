/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.exception;

/**
 *
 * @author akunkel
 */
public class InitException extends Exception {

    /**
     * Creates a new instance of <code>InitException</code> without detail
     * message.
     */
    public InitException() {
    }

    /**
     * Constructs an instance of <code>InitException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public InitException(String msg) {
        super(msg);
    }
    
    public InitException(Throwable cause) {
        super(cause);
    }
 
    public InitException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    
}
