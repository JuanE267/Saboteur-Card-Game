package Modelo.Cartas;

import Modelo.Enums.TipoCarta;
import Modelo.Jugador;

public abstract class Carta {

    private int id;
    private TipoCarta tipo;
    private String img;

    public Carta(int id, TipoCarta tipo, String img) {
        this.id = id;
        this.tipo = tipo;
        this.img = img;
    }

    public TipoCarta getTipo() {
        return tipo;
    }

    public String getImg(){
      return img;
    }


}
