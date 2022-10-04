package de.complex.clxproductsync.soap;

public enum PortPath {

    DATACHECK, TRANSFER;

    public String getUrlPath(){
        return this.name().toLowerCase();
    }
}
