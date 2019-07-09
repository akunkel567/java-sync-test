/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kunkel
 */
public class SKU {

    private int artgroesseid;
    private int cdhid;
    private int artgroesseAuslauf;
    private int artgroesseWebinaktiv;
    private int artfarbeId;
    private int artfarbeAuslauf;
    private int artfarbeWebinaktiv;
    private boolean artfarbeFirstviewfarbe;
    private boolean artfarbeSeohauptfarbe;
    private int artid;
    private int artAuslauf;
    private int artAktiv;

    public int getArtgroesseid() {
        return artgroesseid;
    }

    public void setArtgroesseid(int artgroesseid) {
        this.artgroesseid = artgroesseid;
    }

    public int getCdhid() {
        return cdhid;
    }

    public void setCdhid(int cdhid) {
        this.cdhid = cdhid;
    }

    public int getArtgroesseAuslauf() {
        return artgroesseAuslauf;
    }

    public void setArtgroesseAuslauf(int artgroesseAuslauf) {
        this.artgroesseAuslauf = artgroesseAuslauf;
    }

    public int getArtgroesseWebinaktiv() {
        return artgroesseWebinaktiv;
    }

    public void setArtgroesseWebinaktiv(int artgroesseWebinaktiv) {
        this.artgroesseWebinaktiv = artgroesseWebinaktiv;
    }

    public int getArtfarbeId() {
        return artfarbeId;
    }

    public void setArtfarbeId(int artfarbeId) {
        this.artfarbeId = artfarbeId;
    }

    public int getArtfarbeAuslauf() {
        return artfarbeAuslauf;
    }

    public void setArtfarbeAuslauf(int artfarbeAuslauf) {
        this.artfarbeAuslauf = artfarbeAuslauf;
    }

    public int getArtfarbeWebinaktiv() {
        return artfarbeWebinaktiv;
    }

    public void setArtfarbeWebinaktiv(int artfarbeWebinaktiv) {
        this.artfarbeWebinaktiv = artfarbeWebinaktiv;
    }

    public int getArtid() {
        return artid;
    }

    public void setArtid(int artid) {
        this.artid = artid;
    }

    public int getArtAuslauf() {
        return artAuslauf;
    }

    public void setArtAuslauf(int artAuslauf) {
        this.artAuslauf = artAuslauf;
    }

    public int getArtAktiv() {
        return artAktiv;
    }

    public void setArtAktiv(int artAktiv) {
        this.artAktiv = artAktiv;
    }

    public boolean isArtfarbeFirstviewfarbe() {
        return artfarbeFirstviewfarbe;
    }

    public void setArtfarbeFirstviewfarbe(boolean artfarbeFirstviewfarbe) {
        this.artfarbeFirstviewfarbe = artfarbeFirstviewfarbe;
    }

    public boolean isArtfarbeSeohauptfarbe() {
        return artfarbeSeohauptfarbe;
    }

    public void setArtfarbeSeohauptfarbe(boolean artfarbeSeohauptfarbe) {
        this.artfarbeSeohauptfarbe = artfarbeSeohauptfarbe;
    }

    public static SKU createSKU(ResultSet rs) throws SQLException {
        SKU sku = new SKU();
        sku.setArtgroesseid(rs.getInt("ARTGROESSEID"));
        sku.setCdhid(rs.getInt("FREMDID"));
        sku.setArtgroesseAuslauf(rs.getInt("ARTGROESSE_AUSLAUF"));
        sku.setArtgroesseWebinaktiv(rs.getInt("ARTGROESSE_WEBINAKTIV"));
        sku.setArtfarbeId(rs.getInt("ARTFARBE_ARTFARBEID"));
        sku.setArtfarbeAuslauf(rs.getInt("ARTFARBE_AUSLAUF"));
        sku.setArtfarbeWebinaktiv(rs.getInt("ARTFARBE_WEBINAKTIV"));
        sku.setArtid(rs.getInt("ART_ARTID"));
        sku.setArtAktiv(rs.getInt("ART_AKTIV"));
        sku.setArtAuslauf(rs.getInt("ART_AUSLAUF"));
        sku.setArtfarbeFirstviewfarbe(rs.getInt("ARTFARBE_FIRSTVIEWFARBE") != 0);
        sku.setArtfarbeSeohauptfarbe(rs.getInt("ARTFARBE_SEOHAUPTFARBE") != 0);

        return sku;
    }
}
