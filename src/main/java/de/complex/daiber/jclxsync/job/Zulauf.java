/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

/**
 *
 * @author akunkel
 */
public class Zulauf {

    public final static String STATUS_ORDERED = "ordered";
    public final static String STATUS_INDISPATCH = "indispatch";
    
    private String status;
    private int menge;
    private String kalenderwoche;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMenge() {
        return menge;
    }

    public void setMenge(int menge) {
        this.menge = menge;
    }

    public String getKalenderwoche() {
        return kalenderwoche;
    }

    public void setKalenderwoche(String kalenderwoche) {
        this.kalenderwoche = kalenderwoche;
    }
    
    public boolean isStatusIndispatch(){
        return STATUS_INDISPATCH.equalsIgnoreCase(status);
    }
}
