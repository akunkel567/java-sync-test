/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.clxproductsync.soap.axis.TransferServiceLocator;

/**
 *
 * @author kunkel
 */
public class TransTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {

            TransferServiceLocator service = new TransferServiceLocator();

        } catch (Exception ex) {
            System.out.println(ex);

        } finally {
            System.out.println("");

        }

    }
}
