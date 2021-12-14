package de.complex.daiber.jclxsync.job;

import de.complex.clxproductsync.MainApp;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.tools.config.ApplicationConfig;
import org.apache.log4j.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.Assert.*;

public class ExcelBestandUploadTest {

    @org.junit.Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new ConsoleAppender(new SimpleLayout()));
    }

    @org.junit.Test
    public void run() throws Exception {
        MainApp.debug = true;

        String iniFilename = "./conf/clxProductSync.properties";
        ApplicationConfig.loadConfig(iniFilename);

        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(iniFilename)));

        System.setProperty("complex.axis.default.timeout", String.valueOf(1000 * 60 * 10)); // 10 Minuten

        FirebirdDbPool.createInstance();
        Connection con = FirebirdDbPool.getInstance().getConnection();
        ExcelBestandUpload job = new ExcelBestandUpload(prop);

//        System.out.println("Weeknumber " + job.getWeekNumber());
//        System.out.println("Year " + job.getYear());

        job.run();

        con.close();
    }
}