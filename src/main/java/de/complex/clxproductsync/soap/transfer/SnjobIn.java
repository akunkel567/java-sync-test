
package de.complex.clxproductsync.soap.transfer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SnjobIn complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SnjobIn"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="snjobid" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="eventname" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="herkunft" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fremdid" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="jobtyp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sau" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sadt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="scu" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="scdt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SnjobIn", propOrder = {

})
public class SnjobIn {

    protected long snjobid;
    @XmlElement(required = true)
    protected String eventname;
    @XmlElement(required = true)
    protected String herkunft;
    protected long fremdid;
    @XmlElement(required = true)
    protected String jobtyp;
    @XmlElement(required = true)
    protected String sau;
    @XmlElement(required = true)
    protected String sadt;
    @XmlElement(required = true)
    protected String scu;
    @XmlElement(required = true)
    protected String scdt;

    /**
     * Ruft den Wert der snjobid-Eigenschaft ab.
     * 
     */
    public long getSnjobid() {
        return snjobid;
    }

    /**
     * Legt den Wert der snjobid-Eigenschaft fest.
     * 
     */
    public void setSnjobid(long value) {
        this.snjobid = value;
    }

    /**
     * Ruft den Wert der eventname-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventname() {
        return eventname;
    }

    /**
     * Legt den Wert der eventname-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventname(String value) {
        this.eventname = value;
    }

    /**
     * Ruft den Wert der herkunft-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHerkunft() {
        return herkunft;
    }

    /**
     * Legt den Wert der herkunft-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHerkunft(String value) {
        this.herkunft = value;
    }

    /**
     * Ruft den Wert der fremdid-Eigenschaft ab.
     * 
     */
    public long getFremdid() {
        return fremdid;
    }

    /**
     * Legt den Wert der fremdid-Eigenschaft fest.
     * 
     */
    public void setFremdid(long value) {
        this.fremdid = value;
    }

    /**
     * Ruft den Wert der jobtyp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobtyp() {
        return jobtyp;
    }

    /**
     * Legt den Wert der jobtyp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobtyp(String value) {
        this.jobtyp = value;
    }

    /**
     * Ruft den Wert der sau-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSau() {
        return sau;
    }

    /**
     * Legt den Wert der sau-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSau(String value) {
        this.sau = value;
    }

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
     * Ruft den Wert der scu-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScu() {
        return scu;
    }

    /**
     * Legt den Wert der scu-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScu(String value) {
        this.scu = value;
    }

    /**
     * Ruft den Wert der scdt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScdt() {
        return scdt;
    }

    /**
     * Legt den Wert der scdt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScdt(String value) {
        this.scdt = value;
    }

}
