/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.clxproductsync.soap.tablesync.TableIdList;
import de.complex.clxproductsync.soap.tablesync.TablesyncPort;
import de.complex.clxproductsync.soap.tablesync.TablesyncService;
import de.complex.clxproductsync.soap.tablesync.TablesyncServiceLocator;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

/**
 *
 * @author kunkel
 */
public class TablesyncTest {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws ServiceException, MalformedURLException, RemoteException {
		// TODO code application logic here

		TablesyncService service = new TablesyncServiceLocator();
		TablesyncPort stub = service.getTablesyncPort(new URL("http://backend.daibersync.ak/tablesync"));

		TableIdList sa = stub.getTableIdList("test", "test123", "webartpreistyppreise");
		
		System.out.println("->" + sa.getSize());
		System.out.println("->" + sa.getIdList());

	}

}
