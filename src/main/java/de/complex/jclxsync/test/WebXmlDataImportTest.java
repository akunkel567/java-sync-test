/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.complex.jclxsync.test;

import de.complex.activerecord.config.ActiveRecordConfig;
import de.complex.database.firebird.FirebirdDb;
import de.complex.jclxsync.eventhandler.socketevent.WebDataEventHandler;
import de.complex.jclxsync.xml.XmlCol;
import de.complex.jclxsync.xml.XmlData;
import de.complex.jclxsync.xml.XmlRow;
import de.complex.jclxsync.xml.XmlTable;
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
