/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.eventhandler.socketevent.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "socketconfig")
public class SocketConfig {
   @XmlElement(name="table")
	private List<TableConfig> tableConfigs = new ArrayList<TableConfig>();

	public SocketConfig() {
	}

	public List<TableConfig> getTableConfigs() {
		return tableConfigs;
	}

	public void setTableConfigs(List<TableConfig> tableConfigs) {
		this.tableConfigs = tableConfigs;
	}
}
