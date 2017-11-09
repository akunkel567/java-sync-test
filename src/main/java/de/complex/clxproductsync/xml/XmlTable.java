/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "table")
public class XmlTable {

    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "pkname")
    private String pkName;
    @XmlElementWrapper(name = "rows")
    @XmlElement(name = "row")
    private List<XmlRow> rows = new ArrayList<XmlRow>();

    public XmlTable() {
    }

    public XmlTable(String name, String pkName) {
        this.name = name;
        this.pkName = pkName;
    }

    public List<XmlRow> getRows() {
        return this.rows;
    }

    public void setRows(List<XmlRow> rows) {
        this.rows = rows;
    }

    public void addRow(XmlRow row) {
        this.rows.add(row);
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }
}
