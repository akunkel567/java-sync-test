/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.test;

import de.complex.activerecord.ActiveRecord;
import de.complex.activerecord.config.ActiveRecordConfig;
import de.complex.database.firebird.FirebirdDb;
import de.complex.jclxsync.soap.SoapHandler;
import de.complex.jclxsync.xml.XmlConverter;
import de.complex.tools.config.ApplicationConfig;

/**
 *
 * @author akunkel
 */
public class SpeicherLeckTest {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        // TODO code application logic here
//
//        new SpeicherLeckTest();
//    }
//
//    public SpeicherLeckTest() {
//
//        // FirebirdDb db = new FirebirdDb("SYSDBA", "hippo", "192.168.50.104", "D:/work/java/clxActiveRecord/db/activerecord.fdb", "");
//        FirebirdDb db = new FirebirdDb("SYSDBA", "hippo", "192.168.50.114", "bjw", "");
//
//
//        ApplicationConfig.loadConfig("conf/jClxWebSync.conf");
//		  ActiveRecordConfig.configFilePath = "activeRecord";
//
//           ActiveRecord ar = new ActiveRecord(db, "ada");
//
//		  for (;;) {
//
//            ActiveRecord[] ars = ar.findAllById(10000);
//
//
//				SnJob snjob = new SnJob(4711, "ada", 10000, "U", "", "ada");
//				XmlConverter xc = new XmlConverter();
//
//				SoapHandler sh = new SoapHandler();
//				String xml = xc.fromActiveRecords(ars);
//            System.out.println(xml);
//
//				sh.sendXmlData(snjob, xml);
//
//            xml = null;
//				ars = null;
//				snjob = null;
//				xc = null;
//				sh = null;
//        }
//
////        SpLeck s = new SpLeck(4711);
////        System.out.println("jetzt kommts raus: " + s.getI());
////
////        System.out.println("jetzt kommts zum 2. mal raus: " + s.getI());
//
//
////        do {
////            Vector v = new Vector();
////
////            for (int i = 0; i < 250000; i++) {
////                SpLeck s = new SpLeck(i);
////                v.add(s);
////                System.out.println(s);
////            }
////
////            //v = null;
////
////        } while (true);
//    }
}
