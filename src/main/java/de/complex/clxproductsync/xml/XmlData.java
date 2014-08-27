/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.complex.clxproductsync.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

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
