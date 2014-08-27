/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

/**
 *
 * @author akunkel
 */
public class SpLeck {

    int i = 0;

    public SpLeck() {
    }

    public SpLeck(int i) {
        this.i = i;
    }

    public int getI() {
        try {
            return i;
        } finally {
            System.out.println("na aber hallo");
            i = 0;
        }
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "SpLeck value: " + i;
    }
}


