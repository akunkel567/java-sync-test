
package de.complex.clxproductsync.soap.transfer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SoapAnswer complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SoapAnswer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="msg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rowsok" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Returnnok" type="{urn:Transfer}ReturnnokList"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoapAnswer", propOrder = {

})
public class SoapAnswer {

    protected int status;
    @XmlElement(required = true)
    protected String msg;
    protected int rowsok;
    @XmlElement(name = "Returnnok", required = true)
    protected ReturnnokList returnnok;

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * Ruft den Wert der msg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Legt den Wert der msg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
    }

    /**
     * Ruft den Wert der rowsok-Eigenschaft ab.
     * 
     */
    public int getRowsok() {
        return rowsok;
    }

    /**
     * Legt den Wert der rowsok-Eigenschaft fest.
     * 
     */
    public void setRowsok(int value) {
        this.rowsok = value;
    }

    /**
     * Ruft den Wert der returnnok-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ReturnnokList }
     *     
     */
    public ReturnnokList getReturnnok() {
        return returnnok;
    }

    /**
     * Legt den Wert der returnnok-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ReturnnokList }
     *     
     */
    public void setReturnnok(ReturnnokList value) {
        this.returnnok = value;
    }

}
