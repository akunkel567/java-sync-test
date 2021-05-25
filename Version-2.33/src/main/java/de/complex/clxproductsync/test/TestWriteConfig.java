/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.clxproductsync.config.EventConfig;
import de.complex.clxproductsync.config.FirebirdEventConfig;
import de.complex.tools.xml.XmlHelper;
import java.io.File;

/**
 *
 * @author kunkel
 */
public class TestWriteConfig {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new TestWriteConfig();
    }

    public TestWriteConfig() {

        FirebirdEventConfig c = new FirebirdEventConfig();

        c.getEventConfigs().add(new EventConfig("eventname", "actionname"));

        XmlHelper xmlH = new XmlHelper();
        System.out.println(xmlH.toXml(c));
        xmlH.toFile(c, new File("configTest.xml"));

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
