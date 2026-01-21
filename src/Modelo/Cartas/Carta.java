package Modelo.Cartas;

import Modelo.Enums.TipoCarta;
import Modelo.Jugador;

import java.io.Serializable;

public abstract class Carta implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private TipoCarta tipo;
    private String img;
    private boolean esDestino = false;

    public Carta(int id, TipoCarta tipo, String img) {
        this.id = id;
        this.tipo = tipo;
        this.img = img;
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

    public String getImg(){
      return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
