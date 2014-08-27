/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.activerecord.ActiveRecord;
import de.complex.activerecord.config.ActiveRecordConfig;
import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.eventhandler.fileevent.FileConvertException;
import de.complex.clxproductsync.xml.XmlConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * @author kunkel
 */
public class CreateXmlDataTest {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws SQLException, FileConvertException {
		new CreateXmlDataTest();
	}

	public CreateXmlDataTest() throws SQLException, FileConvertException {
		Logger logger = Logger.getRootLogger();

		logger.addAppender(new ConsoleAppender(new SimpleLayout()));
		logger.setLevel(Level.DEBUG);


		ActiveRecordConfig.configFilePath = "activerecord";

		FirebirdDb db = new FirebirdDb("SYSDBA", "hippo", "192.168.50.63", "clxProdukt", "","UTF8","UTF8");

		ActiveRecord ar = new ActiveRecord(db, "ada");
		ActiveRecord[] ars = ar.findAllById(10000);

		for (ActiveRecord a : ars) {
			System.out.println(a.getValue("ada_anschrift"));

			String xmlStr = new XmlConverter().fromActiveRecords(ars);
			System.out.println(xmlStr);
			try {
				FileWriter fw = new FileWriter(new File("fileXml.xml"));
				fw.write(xmlStr);
				fw.close();

			} catch (IOException ex) {
				logger.error("", ex);
			}
		}
	}
}
