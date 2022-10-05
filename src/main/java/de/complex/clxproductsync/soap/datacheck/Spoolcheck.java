
package de.complex.clxproductsync.soap.datacheck;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Spoolcheck complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Spoolcheck"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="anzahl" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="sadt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="now" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="idsum" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Spoolcheck", propOrder = {

})
public class Spoolcheck {

    @XmlElement(required = true)
    protected String name;
    protected int anzahl;
    @XmlElement(required = true)
    protected String sadt;
    @XmlElement(required = true)
    protected String now;
    protected long idsum;
    protected int id;

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der anzahl-Eigenschaft ab.
     * 
     */
    public int getAnzahl() {
        return anzahl;
    }

    /**
     * Legt den Wert der anzahl-Eigenschaft fest.
     * 
     */
    public void setAnzahl(int value) {
        this.anzahl = value;
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
     * Ruft den Wert der idsum-Eigenschaft ab.
     * 
     */
    public long getIdsum() {
        return idsum;
    }

    /**
     * Legt den Wert der idsum-Eigenschaft fest.
     * 
     */
    public void setIdsum(long value) {
        this.idsum = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

}
