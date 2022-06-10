/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.soap;

import de.complex.exception.ExceptionHelper;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kunkel
 */
abstract class RemoteCall<T> {

    private static Logger logger = LogManager.getLogger(RemoteCall.class);

    protected T run() throws AxisFault, ServiceException, MalformedURLException, RemoteException, RemoteCallException {
        throw new IllegalStateException("implement me");
    }

    public T start() throws RemoteCallException {
        try {
            return run();
        } catch (AxisFault af) {
            logger.debug(AxisHelper.getAxisFaultString(af), af);
            throw new RemoteCallException(af);
        } catch (javax.xml.rpc.ServiceException se) {
            logger.debug(se);
            throw new RemoteCallException(se);
        } catch (java.net.MalformedURLException mue) {
            logger.debug(mue);
            throw new RemoteCallException(mue);
        } catch (java.rmi.RemoteException re) {
            logger.debug(re);
            throw new RemoteCallException(re);
        }
    }
}
