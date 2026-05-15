package Modelo.Cartas;

import Modelo.Enums.TipoCarta;

import java.io.Serializable;

public abstract class Carta implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final TipoCarta tipo;
    private String img;
    private boolean esDestino = false;
    private boolean rotada = false;

    public Carta(int id, TipoCarta tipo, String img) {
        this.id = id;
        this.tipo = tipo;
        this.img = img;
    }

    public boolean isRotada() {
        return rotada;
    }

    public void setRotada(boolean rotada) {
        this.rotada = rotada;
    }

    public boolean esDestino() {
        return esDestino;
    }

    public void setEsDestino(boolean esDestino) {
        this.esDestino = esDestino;
    }

    public TipoCarta getTipo() {
        return tipo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
