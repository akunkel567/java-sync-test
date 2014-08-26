/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.soap;

import de.complex.jclxsync.job.WebTableCheck;
import de.complex.jclxsync.soap.tablesync.TableIdList;
import de.complex.jclxsync.soap.tablesync.TablesyncPort;
import de.complex.jclxsync.soap.tablesync.TablesyncService;
import de.complex.jclxsync.soap.tablesync.TablesyncServiceLocator;
import de.complex.tools.config.ApplicationConfig;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class TablesyncHandler {

	private static Logger logger = Logger.getLogger(TablesyncHandler.class);

	public static TableIdList getTableIdList(final String tablename) throws RemoteCallException {
		TablesyncHandler.logger.debug("call getTableIdList");
		RemoteCall<TableIdList> r = new TablesyncRemoteCall<TableIdList>() {
			@Override
			public TableIdList runTablesync(TablesyncPort stub) throws AxisFault, ServiceException, MalformedURLException, RemoteException, RemoteCallException {
				return  stub.getTableIdList(ApplicationConfig.getValue("ws_username").trim(), ApplicationConfig.getValue("ws_password").trim(), tablename.toLowerCase());
			}
		};

		return r.start();
	}

	private static class TablesyncRemoteCall<T> extends RemoteCall {

		protected T runTablesync(TablesyncPort stub) throws AxisFault, ServiceException, MalformedURLException, RemoteException, RemoteCallException {
			throw new IllegalStateException("implement me");
		}

		protected T run() throws AxisFault, ServiceException, MalformedURLException, RemoteException, RemoteCallException {
			TablesyncService service = new TablesyncServiceLocator();
			TablesyncPort stub = service.getTablesyncPort(new URL(ApplicationConfig.getValue("ws_url").trim() + "/tablesync"));

			return runTablesync(stub);
		}
	}
}
