/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.jclxsync.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author kunkel
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EventConfig {

	@XmlAttribute(name = "name")
	private String eventName = "";
	@XmlValue
	private String actionName = "";

	public EventConfig() {
	}

	public EventConfig(String eventName) {
		this.eventName = eventName;
	}

	public EventConfig(String eventName, String actionName) {
		this.eventName = eventName;
		this.actionName = actionName;
	}

	public String getActionName() {

		if ((actionName == null) || (actionName.equalsIgnoreCase(""))) {
			return this.eventName.toLowerCase();
		}

		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EventConfig other = (EventConfig) obj;
		if ((this.eventName == null) ? (other.eventName != null) : !this.eventName.equals(other.eventName)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + (this.eventName != null ? this.eventName.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "eventName: " + this.getEventName() + " actionName: " + this.getActionName();
	}
}
