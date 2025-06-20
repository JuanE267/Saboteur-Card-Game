package Modelo.Cartas;

import Modelo.Enums.TipoCarta;

public class CartaDestino extends Carta {

    private boolean esOro;
    private int oroX;
    private int oroY;
    private String dorso;


    public CartaDestino(int id, TipoCarta tipo, String img, String dorso, boolean esOro) {
        super(id, tipo, img);
        this.esOro = esOro;
        this.dorso = dorso;
    }

    public boolean getEsOro() {
        return esOro;
    }

    public int getOroX() {
        return oroX;
    }

    public int getOroY() {
        return oroY;
    }

    public void setOroX(int oroX) {
        this.oroX = oroX;
    }

    public void setOroY(int oroY) {
        this.oroY = oroY;
    }

    public String getDorso() {
        return dorso;
    }
}
