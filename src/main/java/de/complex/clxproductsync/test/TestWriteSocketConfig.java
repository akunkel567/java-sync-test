/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.clxproductsync.eventhandler.socketevent.config.ProcedureConfig;
import de.complex.clxproductsync.eventhandler.socketevent.config.SocketConfig;
import de.complex.clxproductsync.eventhandler.socketevent.config.TableConfig;
import de.complex.tools.xml.XmlHelper;
import java.io.File;

/**
 *
 * @author kunkel
 */
public class TestWriteSocketConfig {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new TestWriteSocketConfig();
    }

    public TestWriteSocketConfig() {

        SocketConfig sc = new SocketConfig();

        TableConfig tc = new TableConfig("HalloTable");
        tc.getProcedureConfigs().add(new ProcedureConfig("procname1", "paramname1"));
        tc.getProcedureConfigs().add(new ProcedureConfig("procname2", "paramname2"));
        tc.getProcedureConfigs().add(new ProcedureConfig("procname3", "paramname3"));

        sc.getTableConfigs().add(tc);
        sc.getTableConfigs().add(tc);

        XmlHelper xmlH = new XmlHelper();
        System.out.println(xmlH.toXml(sc));

        //		XmlConverterConfig conf = new XmlConverterConfig();
//
//		conf.setTablename("ada");
//		conf.setMappingname("mappname");
//      conf.setPrimaryKeyName("pkneu");
//
////		conf.getFields().put("adaid", new FieldMapping("adaid", "ada1"));
////		conf.getFields().put("name1", new FieldMapping("name1", "ada4"));
//
//		XmlHelper xmlH = new XmlHelper();
//		System.out.println(xmlH.toXml(conf));
//		xmlH.toFile(conf, new File("configTest.xml"));
    }
}
