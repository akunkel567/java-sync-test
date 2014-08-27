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

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlRow {

	@XmlElementWrapper(name = "cols")
	@XmlElement(name = "col")
	private List<XmlCol> cols = new ArrayList<XmlCol>();
	@XmlElementWrapper(name = "subtables")
	@XmlElement(name = "table")
	private List<XmlTable> subTables = new ArrayList<XmlTable>();
	@XmlAttribute(name = "pk")
	private String pkValue;
	@XmlElementWrapper(name = "files")
	@XmlElement(name = "file")
	private List<XmlFile> files = new ArrayList<XmlFile>();

	public XmlRow() {
	}

	public XmlRow(String pkValue) {
		this.pkValue = pkValue;
	}

	/**
	 * Get the value of pk
	 *
	 * @return the value of pk
	 */
	public String getPkValue() {
		return pkValue;
	}

	/**
	 * Set the value of pk
	 *
	 * @param pk new value of pk
	 */
	public void setPkValue(String pkValue) {
		this.pkValue = pkValue;
	}

	public List<XmlCol> getCol() {
		return cols;
	}

	public void setCol(List<XmlCol> cols) {
		this.cols = cols;
	}

	public void addCol(XmlCol col) {
		cols.add(col);
	}

	public List<XmlTable> getSubTables() {
		return subTables;
	}

	public void setSubTables(List<XmlTable> subTables) {
		this.subTables = subTables;
	}

	public void addSubTable(XmlTable table) {
		this.subTables.add(table);
	}

	public List<XmlFile> getFiles() {
		return files;
	}

	public void setFiles(List<XmlFile> files) {
		this.files = files;
	}

	public void addFile(XmlFile file) {
		this.files.add(file);
	}
}
