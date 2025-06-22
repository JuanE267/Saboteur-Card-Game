package Vista;

import Modelo.Cartas.Carta;

import javax.swing.*;

public class BotonCarta extends JButton {
    private Carta cartaAsociada;

    public BotonCarta(Carta carta){
        super();
        cartaAsociada = carta;
    }

    public Carta getCartaAsociada() {
        return cartaAsociada;
    }
}
