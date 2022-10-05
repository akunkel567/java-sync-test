
package de.complex.clxproductsync.soap.transfer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Snjobweb complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Snjobweb"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="snjobwebid" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="eventname" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="herkunft" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fremdid" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="jobtyp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="done" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="selfdone" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="create_user" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="update_user" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="update_date" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Snjobweb", propOrder = {

})
public class Snjobweb {

    protected int snjobwebid;
    @XmlElement(required = true)
    protected String eventname;
    @XmlElement(required = true)
    protected String herkunft;
    protected int fremdid;
    @XmlElement(required = true)
    protected String jobtyp;
    protected int done;
    protected int selfdone;
    @XmlElement(name = "create_user", required = true)
    protected String createUser;
    @XmlElement(name = "create_date", required = true)
    protected String createDate;
    @XmlElement(name = "update_user", required = true)
    protected String updateUser;
    @XmlElement(name = "update_date", required = true)
    protected String updateDate;

    /**
     * Ruft den Wert der snjobwebid-Eigenschaft ab.
     * 
     */
    public int getSnjobwebid() {
        return snjobwebid;
    }

    /**
     * Legt den Wert der snjobwebid-Eigenschaft fest.
     * 
     */
    public void setSnjobwebid(int value) {
        this.snjobwebid = value;
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
    public int getFremdid() {
        return fremdid;
    }

    /**
     * Legt den Wert der fremdid-Eigenschaft fest.
     * 
     */
    public void setFremdid(int value) {
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
     * Ruft den Wert der done-Eigenschaft ab.
     * 
     */
    public int getDone() {
        return done;
    }

    /**
     * Legt den Wert der done-Eigenschaft fest.
     * 
     */
    public void setDone(int value) {
        this.done = value;
    }

    /**
     * Ruft den Wert der selfdone-Eigenschaft ab.
     * 
     */
    public int getSelfdone() {
        return selfdone;
    }

    /**
     * Legt den Wert der selfdone-Eigenschaft fest.
     * 
     */
    public void setSelfdone(int value) {
        this.selfdone = value;
    }

    /**
     * Ruft den Wert der createUser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * Legt den Wert der createUser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateUser(String value) {
        this.createUser = value;
    }

    /**
     * Ruft den Wert der createDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Legt den Wert der createDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateDate(String value) {
        this.createDate = value;
    }

    /**
     * Ruft den Wert der updateUser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Legt den Wert der updateUser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateUser(String value) {
        this.updateUser = value;
    }

    /**
     * Ruft den Wert der updateDate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Legt den Wert der updateDate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateDate(String value) {
        this.updateDate = value;
    }

    @Override
    public String toString() {
        return "Snjobweb{" +
                "snjobwebid=" + snjobwebid +
                ", eventname='" + eventname + '\'' +
                ", herkunft='" + herkunft + '\'' +
                ", fremdid=" + fremdid +
                ", jobtyp='" + jobtyp + '\'' +
                ", done=" + done +
                ", selfdone=" + selfdone +
                ", createUser='" + createUser + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateUser='" + updateUser + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
