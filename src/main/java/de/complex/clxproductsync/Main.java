/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 *
 * @author Kunkel
 */
public class Main implements WrapperListener {

    private static Logger logger = LogManager.getLogger(Main.class);
    MainApp mainApp;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Start the application.  If the JVM was launched from the native
        //  Wrapper then the application will wait for the native Wrapper to
        //  call the application's start method.  Otherwise the start method
        //  will be called immediately
        WrapperManager.start(new Main(args), args);
    }

    private Main(String[] args) {
    }

    private Main() {
    }

    public Integer start(String[] args) {
        Main.logger.info("Application start");

        mainApp = new MainApp(args);
        mainApp.start(args, false);

        return null;
    }

    public int stop(int exitCode) {
        Main.logger.error("Application stop exitCode: " + exitCode);

        mainApp.stop();

        return exitCode;
    }

    public void controlEvent(int event) {
        Main.logger.error("controlEvent: " + event);

        if (WrapperManager.isControlledByNativeWrapper()) {
            // The Wrapper will take care of this event
        } else {
            // We are not being controlled by the Wrapper, so
            //  handle the event ourselves.
            if ((event == WrapperManager.WRAPPER_CTRL_C_EVENT)
                    || (event == WrapperManager.WRAPPER_CTRL_CLOSE_EVENT)
                    || (event == WrapperManager.WRAPPER_CTRL_SHUTDOWN_EVENT)) {
                WrapperManager.stop(0);
            }
        }
    }
}
