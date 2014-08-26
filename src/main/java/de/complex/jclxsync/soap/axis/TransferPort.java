/**
 * TransferPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.jclxsync.soap.axis;

public interface TransferPort extends java.rmi.Remote {
    public de.complex.jclxsync.soap.axis.SoapAnswer[] setDataFromXml(java.lang.String login, java.lang.String password, de.complex.jclxsync.soap.axis.SnjobIn snjobIn, java.lang.String xml) throws java.rmi.RemoteException;
    public de.complex.jclxsync.soap.axis.Snjobweb[] getListFromSnjobweb(java.lang.String login, java.lang.String password, int limit) throws java.rmi.RemoteException;
    public de.complex.jclxsync.soap.axis.XmlOut getXmlFromSnjobweb(java.lang.String login, java.lang.String password, long snjobwebid) throws java.rmi.RemoteException;
    public de.complex.jclxsync.soap.axis.SoapAnswer setSnjobwebOk(java.lang.String login, java.lang.String password, long snjobwebid) throws java.rmi.RemoteException;
    public de.complex.jclxsync.soap.axis.SoapAnswer setFile(java.lang.String login, java.lang.String password, de.complex.jclxsync.soap.axis.FiledataIn filedataIn) throws java.rmi.RemoteException;
    public de.complex.jclxsync.soap.axis.SoapAnswer deleteFile(java.lang.String login, java.lang.String password, de.complex.jclxsync.soap.axis.FiledataIn filedataIn) throws java.rmi.RemoteException;
}
