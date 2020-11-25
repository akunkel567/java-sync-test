/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 *
 * @author kunkel
 */
public class ConfigTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO code application logic here
        Configuration config;
        try {
            config = new PropertiesConfiguration("./conf/jClxSync.properties");

            System.out.println(config.getString("fileRootPath", "hallo"));
            System.out.println(config.getString("fileSeparator", "hallo"));
            System.out.println(config.getInt("snjoblimit", 1000));

        } catch (ConfigurationException ex) {
            Logger.getLogger(ConfigTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
