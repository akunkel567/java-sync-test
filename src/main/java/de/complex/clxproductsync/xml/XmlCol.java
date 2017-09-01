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
public class XmlCol {

	@XmlAttribute(name = "name")
	private String name;
	@XmlValue
	protected String value;
	@XmlAttribute
	protected boolean base64;

        @XmlAttribute
	protected boolean valueIsNull;

	public XmlCol() {
	}

	public XmlCol(String name) {
		this.name = name;
	}

	public XmlCol(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public XmlCol(String name, String value, boolean base64) {
		this.name = name;
		this.value = value;
		this.base64 = base64;
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

	/**
	 * Get the value of value
	 *
	 * @return the value of value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value of value
	 *
	 * @param value new value of value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isIsBase64() {
		return base64;
	}

	public void setIsBase64(boolean base64) {
		this.base64 = base64;
	}

        public boolean isValueIsNull() {
            return valueIsNull;
        }

        public void setValueIsNull(boolean valueIsNull) {
            this.valueIsNull = valueIsNull;
        }

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final XmlCol other = (XmlCol) obj;
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}

}
