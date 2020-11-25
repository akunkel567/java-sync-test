/**
 * TransferPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package de.complex.clxproductsync.soap.axis;

public interface TransferPort extends java.rmi.Remote {

    public de.complex.clxproductsync.soap.axis.SoapAnswer[] setDataFromXml(java.lang.String login, java.lang.String password, de.complex.clxproductsync.soap.axis.SnjobIn snjobIn, java.lang.String xml) throws java.rmi.RemoteException;

    public de.complex.clxproductsync.soap.axis.Snjobweb[] getListFromSnjobweb(java.lang.String login, java.lang.String password, int limit) throws java.rmi.RemoteException;

    public de.complex.clxproductsync.soap.axis.XmlOut getXmlFromSnjobweb(java.lang.String login, java.lang.String password, long snjobwebid) throws java.rmi.RemoteException;

    public de.complex.clxproductsync.soap.axis.SoapAnswer setSnjobwebOk(java.lang.String login, java.lang.String password, long snjobwebid) throws java.rmi.RemoteException;

    public de.complex.clxproductsync.soap.axis.SoapAnswer setFile(java.lang.String login, java.lang.String password, de.complex.clxproductsync.soap.axis.FiledataIn filedataIn) throws java.rmi.RemoteException;

    public de.complex.clxproductsync.soap.axis.SoapAnswer deleteFile(java.lang.String login, java.lang.String password, de.complex.clxproductsync.soap.axis.FiledataIn filedataIn) throws java.rmi.RemoteException;
}
