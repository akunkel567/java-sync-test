/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync;

import de.complex.activerecord.config.ActiveRecordConfig;
import de.complex.database.firebird.FirebirdDb;
import de.complex.exception.ExceptionHelper;
import de.complex.clxproductsync.config.EventConfig;
import de.complex.clxproductsync.config.FirebirdEventConfig;
import de.complex.clxproductsync.eventhandler.dbevent.WebsyncEventManager;
import de.complex.clxproductsync.eventhandler.fileevent.FileEventManager;
import de.complex.clxproductsync.eventhandler.socketevent.SocketEventManager;
import de.complex.clxproductsync.exception.ClxUncaughtExceptionHandler;
import de.complex.clxproductsync.exception.InitException;
import de.complex.database.firebird.FirebirdDbPool;
import de.complex.clxproductsync.job.SpoolcheckUploadJob;
import de.complex.tools.config.ApplicationConfig;
import de.complex.tools.ssl.InstallCert;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Kunkel
 */
public class MainApp extends Thread {

    private static Logger logger = LogManager.getLogger(MainApp.class);
    public String versionString = null;
    public static boolean debug = false;
    FirebirdDb db = null;
    private boolean closedIsRunning = false;
    public static final String EVENTCONFIGFILENAME = "eventconfig.xml";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(args);
        mainApp.start(args, true);
    }

    public MainApp(String[] args) {
        MainApp.logger.info("MainApp create");
    }

    public int close() {
        closedIsRunning = true;

        this.interrupt();

        System.out.println("MainApplication close...!");
        MainApp.logger.info("MainApplication close...!");

        return 0;
    }

    public int start(String[] args, boolean setDebug) {
        if (args.length == 0) {
            System.out.println(MainApp.USAGEPARAMETER);
            System.exit(0);
        } else {
            for (int k = 0; k < args.length; k++) {

                if (args[k].equalsIgnoreCase("-h")) {
                    System.out.println(MainApp.USAGEPARAMETER);
                    System.exit(0);
                }

                if (args[k].equalsIgnoreCase("-r")) {
                    return this.snModeRun(args, setDebug);
                }

                if (args[k].equalsIgnoreCase("-loadCert")) {
                    String iniFilename = null;

                    if (setDebug) {
                        iniFilename = "conf/clxProductSync.properties";
                    } else {
                        iniFilename = "../conf/clxProductSync.properties";
                    }

                    if (!ApplicationConfig.loadConfig(iniFilename)) {
                        MainApp.logger.fatal("Config Error...!");
                        System.err.println("Config Error...!");
                        System.exit(1);
                    }

                    if (ApplicationConfig.hasValue("truststore")) {
                        System.setProperty("javax.net.ssl.trustStore", ApplicationConfig.getValue("truststore", "../conf/jssecacerts"));
                    }
                    if (ApplicationConfig.hasValue("truststorepassword")) {
                        System.setProperty("javax.net.ssl.trustStorePassword", ApplicationConfig.getValue("truststorepassword", "changeit"));
                    }

                    String host = null;
                    try {
                        if (args[k + 1] != null) {
                            host = args[k + 1];
                            try {
                                System.out.println("Load Certificate from Host: " + host);

                                InstallCert.loadCert(host, "", "");
                            } catch (Exception ex) {
                                System.out.println(ExceptionHelper.ExceptionToString(ex));
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Parameter Hostname fehlt\n");
                        System.out.println(MainApp.USAGEPARAMETER);
                        System.exit(0);
                    }

                    System.exit(0);

                }
            }
        }
        return 0;
    }

    public int snModeRun(String[] args, boolean setDebug) {

        MainApp.debug = setDebug;
        MainApp.logger.info("MainApp starting");

        MainApp.logger.info("");
        MainApp.logger.info("clxProductSync");
        MainApp.logger.info("Complex GmbH & Co. KG, Aschaffenburg");
        MainApp.logger.info("");

        String defaultCharacterEncoding = System.getProperty("file.encoding");
        MainApp.logger.info("defaultCharacterEncoding by property: " + defaultCharacterEncoding);
        MainApp.logger.info("defaultCharacterEncoding by charSet: " + Charset.defaultCharset());
        try {
            this.versionString = loadTextResource("de.complex.clxproductsync.resources", "version.info");
            MainApp.logger.info(this.versionString);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        MainApp.logger.info("");

        String iniFilename;

        if (MainApp.debug) {
            iniFilename = "conf/clxProductSync.properties";
        } else {
            iniFilename = "../conf/clxProductSync.properties";
        }

        if (!ApplicationConfig.loadConfig(iniFilename)) {
            MainApp.logger.fatal("Config Error...!");
            System.err.println("Config Error...!");
            System.exit(1);
        }

        if (MainApp.debug) {
            ApplicationConfig.setValue("isDebug", "true");
        } else {
            ApplicationConfig.setValue("isDebug", "false");
        }

        if (ApplicationConfig.hasValue("truststore")) {
            System.setProperty("javax.net.ssl.trustStore", ApplicationConfig.getValue("truststore", "../conf/jssecacerts"));
        }
        if (ApplicationConfig.hasValue("truststorepassword")) {
            System.setProperty("javax.net.ssl.trustStorePassword", ApplicationConfig.getValue("truststorepassword", "changeit"));
        }

        MainApp.logger.info("trustStore: " + System.getProperty("javax.net.ssl.trustStore", " Standard Runtime-Wert"));
        MainApp.logger.info("pastrustStorePassword: " + System.getProperty("javax.net.ssl.trustStorePassword", " Standard Runtime-Wert"));

        if (ApplicationConfig.hasValue("ws_url")) {
            MainApp.logger.info("webservice: " + ApplicationConfig.getValue("ws_url"));
        } else {
            MainApp.logger.error("webservice nicht konfiguriert. ");
            System.err.println("webservice nicht konfiguriert.");
            System.exit(1);
        }

        if (ApplicationConfig.hasValue("database")) {
            MainApp.logger.info("database: " + ApplicationConfig.getValue("database"));
        } else {
            MainApp.logger.info("database nicht konfiguriert. ");
            System.err.println("database nicht konfiguriert.");
            System.exit(1);
        }

        MainApp.logger.info("");

        if (MainApp.debug) {
            ActiveRecordConfig.configFilePath = "activerecord";
        } else {
            ActiveRecordConfig.configFilePath = "../activerecord";
        }

        // DatabaseEvents ActionHandlerConfig
        String eventconfigFile = null;
        if (MainApp.debug) {
            eventconfigFile = "conf/" + EVENTCONFIGFILENAME;
        } else {
            eventconfigFile = "../conf/" + EVENTCONFIGFILENAME;
        }

        File evcFile = new File(eventconfigFile);

        FirebirdEventConfig eventconfig;
        if (evcFile.exists()) {
            eventconfig = FirebirdEventConfig.factory(eventconfigFile);
        } else {
            eventconfig = FirebirdEventConfig.factory(MainApp.class.getResourceAsStream("/config/" + EVENTCONFIGFILENAME));
        }

        if (eventconfig == null) {
            MainApp.logger.info("eventconfig nicht geladen.");
            System.err.println("eventconfig nicht geladen.");
            System.exit(1);
        }

        ApplicationConfig.setObject("eventconfig", eventconfig);

        this.start(); // und jetzt in den run -Mode

        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            // nix
        }

        return 0;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("MainApp");
        MainApp.logger.info("MainApp start...");

        Thread.setDefaultUncaughtExceptionHandler(new ClxUncaughtExceptionHandler());

        SchedulerFactory sf = null;
        Scheduler sched = null;

        ExecutorService tPool = Executors.newFixedThreadPool(10);
        try {

            FirebirdDbPool.setMinPoolSize(1);
            FirebirdDbPool.setMaxPoolSize(30);
            FirebirdDbPool.setMaxIdleTime(1000 * 20);

            FirebirdDbPool.createInstance();

            WebsyncEventManager.setJobCheckInterval(30 * 1000);
            FileEventManager.setJobCheckInterval(30 * 1000);

            System.setProperty("complex.axis.default.timeout", String.valueOf(1000 * 60 * 10)); // 10 Minuten

            MainApp.logger.info("***** EventConfig START *****");
            FirebirdEventConfig eventconfig = (FirebirdEventConfig) ApplicationConfig.getObject("eventconfig");
            // Eventsteuerung
            ArrayList list = new ArrayList();
            String[] eventnames = null;
            for (EventConfig evc : eventconfig.getEventConfigs()) {
                String eventname = evc.getEventName();
                MainApp.logger.info("Eventname: " + eventname);
                list.add(evc.getEventName());
            }
            eventnames = (String[]) list.toArray(new String[0]);
            list = null;
            MainApp.logger.info("***** EventConfig ENDE *****");

            try {
                Properties p = new Properties();
                p.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");
                p.setProperty("org.quartz.scheduler.instanceId", "1");
                p.setProperty("org.quartz.scheduler.rmi.export", "false");
                p.setProperty("org.quartz.scheduler.rmi.proxy", "false");
                p.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
                p.setProperty("org.quartz.threadPool.threadCount", "4");
                p.setProperty("org.quartz.threadPool.threadPriority", "5");
                p.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

                sf = new StdSchedulerFactory(p);
                sched = sf.getScheduler();

                CronTrigger trigger = null;
                JobDetail job = null;
                Date dt = null;

                // Spoolcheckerupload job
                final String DEFAULT_SPOOLCHECK_INTVERVAL = "0 * * * * ?";
                String spoolcheckInterval = ApplicationConfig.getValue("spoolcheck_interval", DEFAULT_SPOOLCHECK_INTVERVAL);
                job = newJob(SpoolcheckUploadJob.class).withIdentity("spoolcheck_job", "group").build();
                trigger = newTrigger().forJob(job).withIdentity("spoolchecker_trigger", "group").withSchedule(CronScheduleBuilder.cronSchedule(spoolcheckInterval)).build();

                dt = sched.scheduleJob(job, trigger);
                MainApp.logger.info("SpoolcheckUploadJob: " + dt.toString());

                sched.start();

            } catch (SchedulerException ex) {
                System.err.println("FatalError:" + ex.getMessage());
                MainApp.logger.error(ex, ex);

                throw new InitException(ex);
            }

            Future wemFuture = null;
            Future semFuture = null;
            Future fileFuture = null;

            // Hauptschleife.... Todo Überwachung der EventManager....
            while (!this.isInterrupted()) {
                try {
                    MainApp.logger.debug("is running..!");

                    if (ApplicationConfig.getValue("doDbEvent", "true").equalsIgnoreCase("true")) {
                        if (wemFuture != null && !wemFuture.isDone()) {
                            MainApp.logger.debug("WebsyncEventManager is running");
                        } else {
                            MainApp.logger.error("WebsyncEventManager is null/done restart");
                            wemFuture = tPool.submit(new WebsyncEventManager(eventnames, eventconfig));
                        }
                    } else {
                        MainApp.logger.error("WebsyncEventManager ist abgeschaltet");

                    }

                    if (ApplicationConfig.getValue("doSocketEvent", "true").equalsIgnoreCase("true")) {
                        if (semFuture != null && !semFuture.isDone()) {
                            MainApp.logger.debug("SocketEventManager is running");
                        } else {
                            MainApp.logger.error("SocketEventManager is null/done restart");
                            semFuture = tPool.submit(new SocketEventManager());
                        }
                    } else {
                        MainApp.logger.error("SocketEventManager ist abgeschaltet");

                    }

                    if (ApplicationConfig.getValue("doFileEvent", "true").equalsIgnoreCase("true")) {
                        if (fileFuture != null && !fileFuture.isDone()) {
                            MainApp.logger.debug("FileEvent is running");
                        } else {
                            MainApp.logger.error("FileEvent is null/done restart");
                            fileFuture = tPool.submit(new FileEventManager());
                        }
                    } else {
                        MainApp.logger.error("FileEvent ist abgeschaltet");
                    }

                    Trigger t = null;
                    try {
                        t = sched.getTrigger(new TriggerKey("spoolchecker_trigger", "group"));
                        if (t != null) {
                            MainApp.logger.debug(t.getKey() + " naechster Start: " + t.getNextFireTime());
                        } else {
                            MainApp.logger.debug(" kein spoolchecker_trigger aktiv");
                        }

                        t = sched.getTrigger(new TriggerKey("cdhbestand_trigger", "group"));
                        if (t != null) {
                            MainApp.logger.debug(t.getKey() + " naechster Start: " + t.getNextFireTime());
                        } else {
                            MainApp.logger.debug(" kein cdhbestand_trigger aktiv");
                        }

                        t = sched.getTrigger(new TriggerKey("cdhzulaufinfo_trigger", "group"));
                        if (t != null) {
                            MainApp.logger.debug(t.getKey() + " naechster Start: " + t.getNextFireTime());
                        } else {
                            MainApp.logger.debug(" kein cdhzulaufinfo_trigger aktiv");
                        }

                        t = sched.getTrigger(new TriggerKey("excelbestand_trigger", "group"));
                        if (t != null) {
                            MainApp.logger.debug(t.getKey() + " naechster Start: " + t.getNextFireTime());
                        } else {
                            MainApp.logger.debug(" kein excelbestand_trigger aktiv");
                        }
                    } catch (SchedulerException ex) {
                        MainApp.logger.fatal(ex);
                    }

                    Thread.yield();

                    try {
                        Thread.sleep(15 * 1000);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                } catch (Exception e) {
                    MainApp.logger.error("Error", e);
                }
            }

        } catch (InitException ex) {
            logger.fatal(ex, ex);

        } finally {
            tPool.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!tPool.awaitTermination(10, TimeUnit.SECONDS)) {
                    tPool.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!tPool.awaitTermination(10, TimeUnit.SECONDS)) {
                        System.err.println("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                tPool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }

            try {
                if (sched != null) {
                    sched.shutdown();
                }
            } catch (SchedulerException ignore) {
                logger.warn(ignore, ignore);
            }

            FirebirdDbPool.getInstance().poolShutdown();

        }

        if (!closedIsRunning) {
            MainApp.logger.error("MainApplication unerwartet beendet...!");
            throw new RuntimeException("MainApplication unerwartet beendet...!");
        }
    }

    private InputStream getResourceStream(String pkgname, String fname) {
        String resname = "/" + pkgname.replace('.', '/') + "/" + fname;
        Class clazz = getClass();
        InputStream is = clazz.getResourceAsStream(resname);
        return is;
    }

    private String loadTextResource(String pkgname, String fname) throws IOException {
        String ret = null;
        InputStream is = getResourceStream(pkgname, fname);
        if (is != null) {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int c = is.read();
                if (c == -1) {
                    break;
                }
                sb.append((char) c);
            }
            is.close();
            ret = sb.toString();
        }
        return ret;
    }
    public static final String USAGEPARAMETER = "Parameter Error...!!!\n"
            + "Usage:\n"
            + "java -jar jClxWebSync.jar <Parameter>\n\n"
            + "Parameter : \n"
            + "-h - dieser Hinweis\n"
            + "-r - Schnittstellenmodus\n"
            + "-loadCert <host>[:port] - Lade SSL-Certifikat, Parameter im Conf-File beachten\n\n";
}
