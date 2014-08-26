/**
 * Snjobweb.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.jclxsync.soap.axis;

public class Snjobweb  implements java.io.Serializable {
    private int snjobwebid;

    private java.lang.String eventname;

    private java.lang.String herkunft;

    private int fremdid;

    private java.lang.String jobtyp;

    private int done;

    private int selfdone;

    private java.lang.String create_user;

    private java.lang.String create_date;

    private java.lang.String update_user;

    private java.lang.String update_date;

    public Snjobweb() {
    }

    public Snjobweb(
           int snjobwebid,
           java.lang.String eventname,
           java.lang.String herkunft,
           int fremdid,
           java.lang.String jobtyp,
           int done,
           int selfdone,
           java.lang.String create_user,
           java.lang.String create_date,
           java.lang.String update_user,
           java.lang.String update_date) {
           this.snjobwebid = snjobwebid;
           this.eventname = eventname;
           this.herkunft = herkunft;
           this.fremdid = fremdid;
           this.jobtyp = jobtyp;
           this.done = done;
           this.selfdone = selfdone;
           this.create_user = create_user;
           this.create_date = create_date;
           this.update_user = update_user;
           this.update_date = update_date;
    }


    /**
     * Gets the snjobwebid value for this Snjobweb.
     * 
     * @return snjobwebid
     */
    public int getSnjobwebid() {
        return snjobwebid;
    }


    /**
     * Sets the snjobwebid value for this Snjobweb.
     * 
     * @param snjobwebid
     */
    public void setSnjobwebid(int snjobwebid) {
        this.snjobwebid = snjobwebid;
    }


    /**
     * Gets the eventname value for this Snjobweb.
     * 
     * @return eventname
     */
    public java.lang.String getEventname() {
        return eventname;
    }


    /**
     * Sets the eventname value for this Snjobweb.
     * 
     * @param eventname
     */
    public void setEventname(java.lang.String eventname) {
        this.eventname = eventname;
    }


    /**
     * Gets the herkunft value for this Snjobweb.
     * 
     * @return herkunft
     */
    public java.lang.String getHerkunft() {
        return herkunft;
    }


    /**
     * Sets the herkunft value for this Snjobweb.
     * 
     * @param herkunft
     */
    public void setHerkunft(java.lang.String herkunft) {
        this.herkunft = herkunft;
    }


    /**
     * Gets the fremdid value for this Snjobweb.
     * 
     * @return fremdid
     */
    public int getFremdid() {
        return fremdid;
    }


    /**
     * Sets the fremdid value for this Snjobweb.
     * 
     * @param fremdid
     */
    public void setFremdid(int fremdid) {
        this.fremdid = fremdid;
    }


    /**
     * Gets the jobtyp value for this Snjobweb.
     * 
     * @return jobtyp
     */
    public java.lang.String getJobtyp() {
        return jobtyp;
    }


    /**
     * Sets the jobtyp value for this Snjobweb.
     * 
     * @param jobtyp
     */
    public void setJobtyp(java.lang.String jobtyp) {
        this.jobtyp = jobtyp;
    }


    /**
     * Gets the done value for this Snjobweb.
     * 
     * @return done
     */
    public int getDone() {
        return done;
    }


    /**
     * Sets the done value for this Snjobweb.
     * 
     * @param done
     */
    public void setDone(int done) {
        this.done = done;
    }


    /**
     * Gets the selfdone value for this Snjobweb.
     * 
     * @return selfdone
     */
    public int getSelfdone() {
        return selfdone;
    }


    /**
     * Sets the selfdone value for this Snjobweb.
     * 
     * @param selfdone
     */
    public void setSelfdone(int selfdone) {
        this.selfdone = selfdone;
    }


    /**
     * Gets the create_user value for this Snjobweb.
     * 
     * @return create_user
     */
    public java.lang.String getCreate_user() {
        return create_user;
    }


    /**
     * Sets the create_user value for this Snjobweb.
     * 
     * @param create_user
     */
    public void setCreate_user(java.lang.String create_user) {
        this.create_user = create_user;
    }


    /**
     * Gets the create_date value for this Snjobweb.
     * 
     * @return create_date
     */
    public java.lang.String getCreate_date() {
        return create_date;
    }


    /**
     * Sets the create_date value for this Snjobweb.
     * 
     * @param create_date
     */
    public void setCreate_date(java.lang.String create_date) {
        this.create_date = create_date;
    }


    /**
     * Gets the update_user value for this Snjobweb.
     * 
     * @return update_user
     */
    public java.lang.String getUpdate_user() {
        return update_user;
    }


    /**
     * Sets the update_user value for this Snjobweb.
     * 
     * @param update_user
     */
    public void setUpdate_user(java.lang.String update_user) {
        this.update_user = update_user;
    }


    /**
     * Gets the update_date value for this Snjobweb.
     * 
     * @return update_date
     */
    public java.lang.String getUpdate_date() {
        return update_date;
    }


    /**
     * Sets the update_date value for this Snjobweb.
     * 
     * @param update_date
     */
    public void setUpdate_date(java.lang.String update_date) {
        this.update_date = update_date;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Snjobweb)) return false;
        Snjobweb other = (Snjobweb) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.snjobwebid == other.getSnjobwebid() &&
            ((this.eventname==null && other.getEventname()==null) || 
             (this.eventname!=null &&
              this.eventname.equals(other.getEventname()))) &&
            ((this.herkunft==null && other.getHerkunft()==null) || 
             (this.herkunft!=null &&
              this.herkunft.equals(other.getHerkunft()))) &&
            this.fremdid == other.getFremdid() &&
            ((this.jobtyp==null && other.getJobtyp()==null) || 
             (this.jobtyp!=null &&
              this.jobtyp.equals(other.getJobtyp()))) &&
            this.done == other.getDone() &&
            this.selfdone == other.getSelfdone() &&
            ((this.create_user==null && other.getCreate_user()==null) || 
             (this.create_user!=null &&
              this.create_user.equals(other.getCreate_user()))) &&
            ((this.create_date==null && other.getCreate_date()==null) || 
             (this.create_date!=null &&
              this.create_date.equals(other.getCreate_date()))) &&
            ((this.update_user==null && other.getUpdate_user()==null) || 
             (this.update_user!=null &&
              this.update_user.equals(other.getUpdate_user()))) &&
            ((this.update_date==null && other.getUpdate_date()==null) || 
             (this.update_date!=null &&
              this.update_date.equals(other.getUpdate_date())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getSnjobwebid();
        if (getEventname() != null) {
            _hashCode += getEventname().hashCode();
        }
        if (getHerkunft() != null) {
            _hashCode += getHerkunft().hashCode();
        }
        _hashCode += getFremdid();
        if (getJobtyp() != null) {
            _hashCode += getJobtyp().hashCode();
        }
        _hashCode += getDone();
        _hashCode += getSelfdone();
        if (getCreate_user() != null) {
            _hashCode += getCreate_user().hashCode();
        }
        if (getCreate_date() != null) {
            _hashCode += getCreate_date().hashCode();
        }
        if (getUpdate_user() != null) {
            _hashCode += getUpdate_user().hashCode();
        }
        if (getUpdate_date() != null) {
            _hashCode += getUpdate_date().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Snjobweb.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Transfer", "Snjobweb"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("snjobwebid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "snjobwebid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventname");
        elemField.setXmlName(new javax.xml.namespace.QName("", "eventname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("herkunft");
        elemField.setXmlName(new javax.xml.namespace.QName("", "herkunft"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fremdid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fremdid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobtyp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jobtyp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("done");
        elemField.setXmlName(new javax.xml.namespace.QName("", "done"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selfdone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "selfdone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("create_user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "create_user"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("create_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "create_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("update_user");
        elemField.setXmlName(new javax.xml.namespace.QName("", "update_user"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("update_date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "update_date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

	@Override
	public String toString() {
		return "Snjobweb{" + "snjobwebid=" + snjobwebid + ", eventname=" + eventname + ", herkunft=" + herkunft + ", fremdid=" + fremdid + ", jobtyp=" + jobtyp + '}';
	}
}
