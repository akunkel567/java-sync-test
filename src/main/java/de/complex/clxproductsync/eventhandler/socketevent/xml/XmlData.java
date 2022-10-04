/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xmldata")
public class XmlData {

    private XmlTable table = null;

    public XmlData() {
    }

    public XmlTable getTable() {
        return table;
    }

    public void setTable(XmlTable table) {
        this.table = table;
    }
}
