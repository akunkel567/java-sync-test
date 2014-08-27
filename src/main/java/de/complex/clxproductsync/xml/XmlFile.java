/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.complex.clxproductsync.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFile {

	@XmlAttribute(name = "filename")
	private String filename = null;
	@XmlValue
	private String filedata = null;

	public XmlFile() {
	}

	public XmlFile(String filename, String filedata) {
		this.filename = filename;
		this.filedata = filedata;
	}

	public String getFiledata() {
		return filedata;
	}

	public void setFiledata(String filedata) {
		this.filedata = filedata;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
