package Vista;

import Modelo.Jugador;

import javax.swing.*;

public class HerramientaTabla extends JLabel {
    private Jugador dueño;
    public HerramientaTabla(Jugador jugador){
        dueño = jugador;
    }

    public Jugador getDueño(){
        return dueño;
    }
}
