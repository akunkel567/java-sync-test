/**
 * SnjobIn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.jclxsync.soap.axis;

public class SnjobIn  implements java.io.Serializable {
    private long snjobid;

    private java.lang.String eventname;

    private java.lang.String herkunft;

    private long fremdid;

    private java.lang.String jobtyp;

    private java.lang.String sau;

    private java.lang.String sadt;

    private java.lang.String scu;

    private java.lang.String scdt;

    public SnjobIn() {
    }

    public SnjobIn(
           long snjobid,
           java.lang.String eventname,
           java.lang.String herkunft,
           long fremdid,
           java.lang.String jobtyp,
           java.lang.String sau,
           java.lang.String sadt,
           java.lang.String scu,
           java.lang.String scdt) {
           this.snjobid = snjobid;
           this.eventname = eventname;
           this.herkunft = herkunft;
           this.fremdid = fremdid;
           this.jobtyp = jobtyp;
           this.sau = sau;
           this.sadt = sadt;
           this.scu = scu;
           this.scdt = scdt;
    }


    /**
     * Gets the snjobid value for this SnjobIn.
     * 
     * @return snjobid
     */
    public long getSnjobid() {
        return snjobid;
    }


    /**
     * Sets the snjobid value for this SnjobIn.
     * 
     * @param snjobid
     */
    public void setSnjobid(long snjobid) {
        this.snjobid = snjobid;
    }


    /**
     * Gets the eventname value for this SnjobIn.
     * 
     * @return eventname
     */
    public java.lang.String getEventname() {
        return eventname;
    }


    /**
     * Sets the eventname value for this SnjobIn.
     * 
     * @param eventname
     */
    public void setEventname(java.lang.String eventname) {
        this.eventname = eventname;
    }


    /**
     * Gets the herkunft value for this SnjobIn.
     * 
     * @return herkunft
     */
    public java.lang.String getHerkunft() {
        return herkunft;
    }


    /**
     * Sets the herkunft value for this SnjobIn.
     * 
     * @param herkunft
     */
    public void setHerkunft(java.lang.String herkunft) {
        this.herkunft = herkunft;
    }


    /**
     * Gets the fremdid value for this SnjobIn.
     * 
     * @return fremdid
     */
    public long getFremdid() {
        return fremdid;
    }


    /**
     * Sets the fremdid value for this SnjobIn.
     * 
     * @param fremdid
     */
    public void setFremdid(long fremdid) {
        this.fremdid = fremdid;
    }


    /**
     * Gets the jobtyp value for this SnjobIn.
     * 
     * @return jobtyp
     */
    public java.lang.String getJobtyp() {
        return jobtyp;
    }


    /**
     * Sets the jobtyp value for this SnjobIn.
     * 
     * @param jobtyp
     */
    public void setJobtyp(java.lang.String jobtyp) {
        this.jobtyp = jobtyp;
    }


    /**
     * Gets the sau value for this SnjobIn.
     * 
     * @return sau
     */
    public java.lang.String getSau() {
        return sau;
    }


    /**
     * Sets the sau value for this SnjobIn.
     * 
     * @param sau
     */
    public void setSau(java.lang.String sau) {
        this.sau = sau;
    }


    /**
     * Gets the sadt value for this SnjobIn.
     * 
     * @return sadt
     */
    public java.lang.String getSadt() {
        return sadt;
    }


    /**
     * Sets the sadt value for this SnjobIn.
     * 
     * @param sadt
     */
    public void setSadt(java.lang.String sadt) {
        this.sadt = sadt;
    }


    /**
     * Gets the scu value for this SnjobIn.
     * 
     * @return scu
     */
    public java.lang.String getScu() {
        return scu;
    }


    /**
     * Sets the scu value for this SnjobIn.
     * 
     * @param scu
     */
    public void setScu(java.lang.String scu) {
        this.scu = scu;
    }


    /**
     * Gets the scdt value for this SnjobIn.
     * 
     * @return scdt
     */
    public java.lang.String getScdt() {
        return scdt;
    }


    /**
     * Sets the scdt value for this SnjobIn.
     * 
     * @param scdt
     */
    public void setScdt(java.lang.String scdt) {
        this.scdt = scdt;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SnjobIn)) return false;
        SnjobIn other = (SnjobIn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.snjobid == other.getSnjobid() &&
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
            ((this.sau==null && other.getSau()==null) || 
             (this.sau!=null &&
              this.sau.equals(other.getSau()))) &&
            ((this.sadt==null && other.getSadt()==null) || 
             (this.sadt!=null &&
              this.sadt.equals(other.getSadt()))) &&
            ((this.scu==null && other.getScu()==null) || 
             (this.scu!=null &&
              this.scu.equals(other.getScu()))) &&
            ((this.scdt==null && other.getScdt()==null) || 
             (this.scdt!=null &&
              this.scdt.equals(other.getScdt())));
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
        _hashCode += new Long(getSnjobid()).hashCode();
        if (getEventname() != null) {
            _hashCode += getEventname().hashCode();
        }
        if (getHerkunft() != null) {
            _hashCode += getHerkunft().hashCode();
        }
        _hashCode += new Long(getFremdid()).hashCode();
        if (getJobtyp() != null) {
            _hashCode += getJobtyp().hashCode();
        }
        if (getSau() != null) {
            _hashCode += getSau().hashCode();
        }
        if (getSadt() != null) {
            _hashCode += getSadt().hashCode();
        }
        if (getScu() != null) {
            _hashCode += getScu().hashCode();
        }
        if (getScdt() != null) {
            _hashCode += getScdt().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SnjobIn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Transfer", "SnjobIn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("snjobid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "snjobid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobtyp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jobtyp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sau");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sau"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sadt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sadt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scu");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scu"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scdt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "scdt"));
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

}
