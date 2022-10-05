
package de.complex.clxproductsync.soap.datacheck;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Status complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Status"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="sadt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="now" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sekunden" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="countsnjobs" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="countsnjobtypes" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="countwebspools" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="countsnjobwebs" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Status", propOrder = {

})
public class Status {

    @XmlElement(required = true)
    protected String sadt;
    @XmlElement(required = true)
    protected String now;
    protected int sekunden;
    protected int countsnjobs;
    protected int countsnjobtypes;
    protected int countwebspools;
    protected int countsnjobwebs;

    /**
     * Ruft den Wert der sadt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSadt() {
        return sadt;
    }

    /**
     * Legt den Wert der sadt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSadt(String value) {
        this.sadt = value;
    }

    /**
     * Ruft den Wert der now-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNow() {
        return now;
    }

    /**
     * Legt den Wert der now-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNow(String value) {
        this.now = value;
    }

    /**
     * Ruft den Wert der sekunden-Eigenschaft ab.
     * 
     */
    public int getSekunden() {
        return sekunden;
    }

    /**
     * Legt den Wert der sekunden-Eigenschaft fest.
     * 
     */
    public void setSekunden(int value) {
        this.sekunden = value;
    }

    /**
     * Ruft den Wert der countsnjobs-Eigenschaft ab.
     * 
     */
    public int getCountsnjobs() {
        return countsnjobs;
    }

    /**
     * Legt den Wert der countsnjobs-Eigenschaft fest.
     * 
     */
    public void setCountsnjobs(int value) {
        this.countsnjobs = value;
    }

    /**
     * Ruft den Wert der countsnjobtypes-Eigenschaft ab.
     * 
     */
    public int getCountsnjobtypes() {
        return countsnjobtypes;
    }

    /**
     * Legt den Wert der countsnjobtypes-Eigenschaft fest.
     * 
     */
    public void setCountsnjobtypes(int value) {
        this.countsnjobtypes = value;
    }

    /**
     * Ruft den Wert der countwebspools-Eigenschaft ab.
     * 
     */
    public int getCountwebspools() {
        return countwebspools;
    }

    /**
     * Legt den Wert der countwebspools-Eigenschaft fest.
     * 
     */
    public void setCountwebspools(int value) {
        this.countwebspools = value;
    }

    /**
     * Ruft den Wert der countsnjobwebs-Eigenschaft ab.
     * 
     */
    public int getCountsnjobwebs() {
        return countsnjobwebs;
    }

    /**
     * Legt den Wert der countsnjobwebs-Eigenschaft fest.
     * 
     */
    public void setCountsnjobwebs(int value) {
        this.countsnjobwebs = value;
    }

}
