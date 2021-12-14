/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

import de.complex.util.lang.StringTool;

/**
 *
 * @author akunkel
 */
public class Zulauf {

    private String status;
    private int menge;
    private int kalenderwoche;
    private int jahr;
    private boolean bestellt;

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

    public int getKalenderwoche() {
        return kalenderwoche;
    }

    public void setKalenderwoche(int kalenderwoche) {
        this.kalenderwoche = kalenderwoche;
    }

    public int getJahr() {
        return jahr;
    }

    public void setJahr(int jahr) {
        this.jahr = jahr;
    }

    public boolean isBestellt() {
        return bestellt;
    }

    public void setBestellt(boolean bestellt) {
        this.bestellt = bestellt;
    }

    public boolean isStatusIndispatch() {
        return "INDISPATCH".equalsIgnoreCase(StringTool.getNotNullTrim(getStatus()));
    }

    public boolean isStatusOrdered() {
        return isBestellt();
    }

    @Override
    public String toString() {
        return "Zulauf{" +
                "status='" + status + '\'' +
                ", menge=" + menge +
                ", kalenderwoche=" + kalenderwoche +
                ", jahr=" + jahr +
                ", bestellt=" + bestellt +
                '}';
    }
}
