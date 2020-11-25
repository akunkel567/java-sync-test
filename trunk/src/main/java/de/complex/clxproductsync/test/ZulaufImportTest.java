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
import org.apache.log4j.BasicConfigurator;

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
        BasicConfigurator.configure();

        //Properties prop = new Properties();
        //prop.load(new FileReader("f/conjClxSync.properties"));
        CDHZulaufinfo zi = new CDHZulaufinfo(null);

        zi.run();

    }
}
