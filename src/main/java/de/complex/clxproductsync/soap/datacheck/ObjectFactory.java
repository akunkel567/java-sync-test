
package de.complex.clxproductsync.soap.datacheck;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.complex.clxproductsync.soap.datacheck package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.complex.clxproductsync.soap.datacheck
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Spoolcheck }
     * 
     */
    public Spoolcheck createSpoolcheck() {
        return new Spoolcheck();
    }

    /**
     * Create an instance of {@link SpoolcheckList }
     * 
     */
    public SpoolcheckList createSpoolcheckList() {
        return new SpoolcheckList();
    }

    /**
     * Create an instance of {@link ReturnnokList }
     * 
     */
    public ReturnnokList createReturnnokList() {
        return new ReturnnokList();
    }

    /**
     * Create an instance of {@link Returnnok }
     * 
     */
    public Returnnok createReturnnok() {
        return new Returnnok();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

}
