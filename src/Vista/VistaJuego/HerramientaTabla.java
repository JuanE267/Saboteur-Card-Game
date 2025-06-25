package Vista.VistaJuego;

import Modelo.IJugador;

import javax.swing.*;

public class HerramientaTabla extends JLabel {
    private IJugador dueño;
    public HerramientaTabla(IJugador jugador){
        dueño = jugador;
    }

    public IJugador getDueño(){
        return dueño;
    }
}
