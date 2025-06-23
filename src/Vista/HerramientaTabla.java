package Vista;

import Modelo.IJugador;
import Modelo.Jugador;

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
