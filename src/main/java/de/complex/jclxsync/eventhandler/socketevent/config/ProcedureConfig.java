/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.eventhandler.socketevent.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "procedure")
public class ProcedureConfig {
	@XmlAttribute(name="name")
	private String procedureName = "";
   @XmlAttribute(name="tablefield")
	private String tableFieldName = "";

	public ProcedureConfig() {
	}

	public ProcedureConfig(String procedureName) {
		this.procedureName = procedureName;
	}

	public ProcedureConfig(String procedureName, String tableFieldName) {
		this.procedureName = procedureName;
		this.tableFieldName = tableFieldName;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getTableFieldName() {
		return tableFieldName;
	}

	public void setTableFieldName(String tableFieldName) {
		this.tableFieldName = tableFieldName;
	}

}
