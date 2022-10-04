/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.config;

import de.complex.exception.ExceptionHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "eventconfig")
public class FirebirdEventConfig {

//	@XmlElement(name = "thread")
//	private List<ThreadConfig> threads = new ArrayList<ThreadConfig>();
    @XmlElementWrapper(name = "events")
    @XmlElement(name = "event")
    private List<EventConfig> eventConfigs = new ArrayList<EventConfig>();

    public FirebirdEventConfig() {
    }

//	public List<ThreadConfig> getThreads() {
//		return threads;
//	}
//
//	public void setThreads(List<ThreadConfig> threads) {
//		this.threads = threads;
//	}
    public List<EventConfig> getEventConfigs() {
        return eventConfigs;
    }

    public void setEventConfigs(List<EventConfig> eventConfigs) {
        this.eventConfigs = eventConfigs;
    }

    public String toXml() {
        String ret = null;
        Writer w = null;

        try {
            JAXBContext context = JAXBContext.newInstance(FirebirdEventConfig.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            try {
                w = new StringWriter();
                m.marshal(this, w);
                ret = w.toString();
            } finally {
                try {
                    w.close();
                } catch (Exception e) {
                }
            }
        } catch (JAXBException ex) {
            System.out.println(ExceptionHelper.ExceptionToString(ex));
        }

        return ret;
    }

    public static FirebirdEventConfig factory(String filename) {
        File file = new File(filename);
        FirebirdEventConfig config = null;

        if (!file.exists()) {
            System.out.println("ConfigFile Not Found: " + file.getName());
        } else {
            try {
                JAXBContext context = JAXBContext.newInstance(FirebirdEventConfig.class);
                Unmarshaller um = context.createUnmarshaller();
                try {
                    config = (FirebirdEventConfig) um.unmarshal(new FileReader(file));
                } catch (FileNotFoundException ex) {
                    System.out.println(ExceptionHelper.ExceptionToString(ex));
                }
            } catch (JAXBException ex) {
                System.out.println(ExceptionHelper.ExceptionToString(ex));
            }
        }
        return config;
    }

    public static FirebirdEventConfig factory(InputStream input) {
        FirebirdEventConfig config = null;

        try {
            JAXBContext context = JAXBContext.newInstance(FirebirdEventConfig.class);
            Unmarshaller um = context.createUnmarshaller();
            config = (FirebirdEventConfig) um.unmarshal(input);
        } catch (JAXBException ex) {
            System.out.println(ExceptionHelper.ExceptionToString(ex));
        }
        return config;
    }
}
