/*
 * AbstractSocketEventHandler.java
 *
 * Created on 17. Juli 2006, 15:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent;

import de.complex.database.firebird.FirebirdDb;
import de.complex.clxproductsync.eventhandler.socketevent.config.SocketConfig;
import de.complex.clxproductsync.eventhandler.socketevent.config.TableConfig;
import de.complex.clxproductsync.soap.SoapHandler;
import de.complex.tools.config.ApplicationConfig;
import de.complex.tools.mail.Mailer;
import de.complex.tools.xml.XmlHelper;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;

/**
 *
 * @author kunkel
 */
public abstract class AbstractSocketEventHandler {

    private static Logger logger = Logger.getLogger(AbstractSocketEventHandler.class);
    private String handleEventType;
    private Mailer mailer = null;
    private Map<String, TableConfig> tableConfigs = new HashMap<String, TableConfig>();
    private boolean pause = false;
    public Properties prop;
    private boolean running = false;
    public int currentWebid = 0;
    private HashMap<Integer, Vector> map = new HashMap<Integer, Vector>();
    public static final String SOCKETCONFIGFILENAME = "socketconfig.xml";

    public boolean doSendError(int typeId) {
        return doSendError(typeId, currentWebid);
    }

    public boolean doSendError(int typeId, int webid) {
        Integer type = new Integer(typeId);
        Integer id = new Integer(webid);

        Vector vec = map.get(type);

        if (vec == null) {
            vec = new Vector();
            vec.add(id);
            map.put(type, vec);
            return true;
        } else {
            if (vec.contains(id)) {
                return false;
            } else {
                vec.add(id);
                return true;
            }
        }
    }

    public AbstractSocketEventHandler(Properties prop) {
        this.prop = prop;
    }

    /**
     * Creates a new instance of AbstractSocketEventHandler
     */
    public AbstractSocketEventHandler(String handleEventType) {
        AbstractSocketEventHandler.logger.debug("New AbstractSocketEventHandler handleEventType: " + handleEventType);

        this.handleEventType = handleEventType;

        SocketConfig sc = null;

        String socketConfigFile = null;
        if (ApplicationConfig.getValue("isDebug", "false").equalsIgnoreCase("true")) {
            socketConfigFile = "conf/" + SOCKETCONFIGFILENAME;
        } else {
            socketConfigFile = "../conf/" + SOCKETCONFIGFILENAME;
        }

        File scFile = new File(socketConfigFile);
        if (scFile.exists()) {
            logger.debug("external SocketConfig: " + scFile.getName());
            sc = (SocketConfig) new XmlHelper().factory(new File(socketConfigFile), SocketConfig.class);
        } else {
            logger.debug("internal SocketConfig: " + scFile.getName());
            try {
                sc = (SocketConfig) new XmlHelper().factory(AbstractSocketEventHandler.class.getResourceAsStream("/config/" + SOCKETCONFIGFILENAME), SocketConfig.class);
            } catch (JAXBException ex) {
                logger.error(ex);
                throw new RuntimeException(ex);
            }
        }

        if (sc != null) {
            AbstractSocketEventHandler.logger.debug("SocketConfig Anzahl TableConfigs: " + sc.getTableConfigs().size());
            for (TableConfig tc : sc.getTableConfigs()) {
                tableConfigs.put(tc.getTableName().toLowerCase(), tc);
                AbstractSocketEventHandler.logger.debug("SocketConfig tablename: " + tc.getTableName());
            }
        } else {
            AbstractSocketEventHandler.logger.debug("keine SocketConfig vorhanden");
        }

        AbstractSocketEventHandler.logger.debug("New AbstractSocketEventHandler pause status: " + this.pause);
    }

    /**
     * Creates a new instance of AbstractSocketEventHandler
     */
    // für kompatibilität mit dem alten jMobis....
    public AbstractSocketEventHandler(Properties prop, String handleEventType) {
        this.prop = prop;
        this.handleEventType = handleEventType;
        SocketConfig sc = null;

        File xmlFile = null;

        if (ApplicationConfig.getValue("isDebug", "false").equalsIgnoreCase("true")) {
            sc = (SocketConfig) new XmlHelper().factory(new File("conf/socketconfig.xml"), SocketConfig.class);
        } else {
            sc = (SocketConfig) new XmlHelper().factory(new File("../conf/socketconfig.xml"), SocketConfig.class);
        }

        if (sc != null) {
            for (TableConfig tc : sc.getTableConfigs()) {
                tableConfigs.put(tc.getTableName().toLowerCase(), tc);
                AbstractSocketEventHandler.logger.debug("SocketConfig tablename: " + tc.getTableName());
            }
        }
    }

    synchronized public static String[] getEventNames() {
        return new String[]{WebDataEventHandler.EVENTTYPE};
    }

    public static AbstractSocketEventHandler socketEventHandlerFactory(String eventname, FirebirdDb db) {
        AbstractSocketEventHandler.logger.debug("socketEventHandlerFactory eventname: " + eventname);

        AbstractSocketEventHandler haendler = null;

        if (eventname.equalsIgnoreCase(WebDataEventHandler.EVENTTYPE)) {
            haendler = new WebDataEventHandler(db);
        } else {
            haendler = null;
        }

        // weil z.Zt. der Eventname mehrfach hintereinander zusammenhängen kommen kann
        if (haendler == null) {
            AbstractSocketEventHandler.logger.error("Eventname pruefen, ist unbekannt - eventname: " + eventname);
            if (eventname.toUpperCase().startsWith(WebDataEventHandler.EVENTTYPE.toUpperCase())) {
                haendler = new WebDataEventHandler(db);
            }
        }

        AbstractSocketEventHandler.logger.debug("HandlerType: " + haendler);

        return haendler;

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public abstract String getHandleEventType();

    public abstract void run();

    public void showAxisFault(AxisFault af) {
//		AbstractSocketEventHandler.logger.error("AxisFault " + this.getClass().getName() + " a *******************************************************");
//		AbstractSocketEventHandler.logger.error("AxisFault " + this.getClass().getName() + " - getFaultCode (LocalPart) :" + af.getFaultCode().getLocalPart());
//		AbstractSocketEventHandler.logger.error("AxisFault " + this.getClass().getName() + " - getFaultCode (Prefix) :" + af.getFaultCode().getPrefix());
//		AbstractSocketEventHandler.logger.error("AxisFault " + this.getClass().getName() + " - getFaultString :" + af.getFaultString());
//		AbstractSocketEventHandler.logger.error("AxisFault " + this.getClass().getName() + " - getMessage :" + af.getMessage());
//		AbstractSocketEventHandler.logger.error("AxisFault " + this.getClass().getName() + " e *******************************************************");
//
//		af.printStackTrace();
//
//		String errorStr = "AxisFault " + this.getClass().getName() + " a *******************************************************"
//				  + "\nAxisFault " + this.getClass().getName() + " - getFaultCode (LocalPart) :" + af.getFaultCode().getLocalPart()
//				  + "\nAxisFault " + this.getClass().getName() + " - getFaultCode (Prefix) :" + af.getFaultCode().getPrefix()
//				  + "\nAxisFault " + this.getClass().getName() + " - getFaultString :" + af.getFaultString()
//				  + "\nAxisFault " + this.getClass().getName() + " - getMessage :" + af.getMessage()
//				  + "\nAxisFault " + this.getClass().getName() + " e *******************************************************";
//
//
//		if (this.mailer != null) {
//			this.mailer.postErrorMail(" Axis-Error", errorStr, "AxisFault", af.hashCode());
//		}
//
    }

    public Mailer getMailer() {
        return mailer;
    }

    public void setMailer(Mailer mailer) {
        this.mailer = mailer;
    }

    public Map<String, TableConfig> getTableConfigs() {
        return tableConfigs;
    }

    public void setPause(boolean pause) {
        AbstractSocketEventHandler.logger.info("****** setPause: " + pause + " *******************");
        // rausgenommen hat fehlerr verursacht
        //this.pause = pause;
    }

    public boolean isPause() {
        return pause;
    }

    public void reset() {
    }
}
