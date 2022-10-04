/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent.config;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "table")
public class TableConfig {

    @XmlAttribute(name = "name")
    private String tableName = "";
    @XmlElement(name = "procedure")
    private List<ProcedureConfig> procedureConfigs = new ArrayList<ProcedureConfig>();

    public TableConfig() {
    }

    public TableConfig(String tableName) {
        this.tableName = tableName;
    }

    public List<ProcedureConfig> getProcedureConfigs() {
        return procedureConfigs;
    }

    public void setProcedureConfigs(List<ProcedureConfig> procedureConfigs) {
        this.procedureConfigs = procedureConfigs;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TableConfig other = (TableConfig) obj;
        if ((this.tableName == null) ? (other.tableName != null) : !this.tableName.equals(other.tableName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + (this.tableName != null ? this.tableName.hashCode() : 0);
        return hash;
    }
}
