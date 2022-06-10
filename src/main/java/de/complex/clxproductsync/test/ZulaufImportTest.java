/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.daiber.jclxsync.job.CDHZulaufinfo;
import de.complex.tools.config.ApplicationConfig;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 *
 * @author kunkel
 */
public class ZulaufImportTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        ApplicationConfig.loadConfig("conf/jClxSync.properties");
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.DEBUG);

        //Properties prop = new Properties();
        //prop.load(new FileReader("f/conjClxSync.properties"));
        CDHZulaufinfo zi = new CDHZulaufinfo(null);

        zi.run();

    }
}
