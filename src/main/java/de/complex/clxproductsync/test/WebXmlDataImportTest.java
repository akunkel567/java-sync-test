/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.complex.clxproductsync.test;

import de.complex.activerecord.config.ActiveRecordConfig;
import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.eventhandler.socketevent.WebDataEventHandler;
import de.complex.clxproductsync.xml.XmlCol;
import de.complex.clxproductsync.xml.XmlData;
import de.complex.clxproductsync.xml.XmlRow;
import de.complex.clxproductsync.xml.XmlTable;
import de.complex.tools.config.ApplicationConfig;
import de.complex.tools.xml.XmlHelper;
import java.io.File;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author kunkel
 */
public class WebXmlDataImportTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new WebXmlDataImportTest().run();
    }

	public WebXmlDataImportTest() {
	}

	public void run() {

//		if (!ApplicationConfig.loadConfig("conf/jClxWebSync.conf")) {
//			System.err.println("Config Error...!");
//			System.exit(1);
//		}
//
//		PropertyConfigurator.configure("conf/log4j.properties");
//
//		ActiveRecordConfig.configFilePath = "activerecord";
//
//		FirebirdDb db = new FirebirdDb();
//		WebDataEventHandler evh = new WebDataEventHandler(db);
//
//		XmlData xmlData = (XmlData) new XmlHelper().factory(new File("testimport.xml"), XmlData.class );
//
//		System.out.println(evh.saveXmlTable(xmlData.getTable(), null));

	}


}
