/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.exception;

import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class ClxUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    Logger logger = Logger.getLogger(ClxUncaughtExceptionHandler.class);

    public void uncaughtException(Thread t, Throwable e) {
        logger.error(t.getName(), e);
    }
}
