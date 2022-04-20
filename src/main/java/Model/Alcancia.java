/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author dell
 */
public class Alcancia {
    private int nMonedas;
    private Moneda m = new Moneda();

    public Alcancia() {
        nMonedas=0;
    }

    public Alcancia(int nMonedas,Moneda moneda) {
        this.nMonedas = nMonedas;
        this.m = moneda;
    }

    /**
     * @return the nMonedas
     */
    public int getnMonedas() {
        return nMonedas;
    }

    /**
     * @param nMonedas the nMonedas to set
     */
    public void setnMonedas(int nMonedas) {
        this.nMonedas = nMonedas;
    }

    /**
     * @return the m
     */
    public Moneda getM() {
        return m;
    }

    /**
     * @param m the m to set
     */
    public void setM(Moneda m) {
        this.m = m;
    }
    
}
