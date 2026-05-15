package Vista.VistaJuego;

import Modelo.Enums.Herramienta;
import Modelo.IJugador;

import javax.swing.*;

public class HerramientaTabla extends JLabel {
    private final IJugador dueño;
    private final Herramienta herramienta;

    public HerramientaTabla(IJugador jugador, Herramienta herramienta) {
        this.dueño = jugador;
        this.herramienta = herramienta;
    }

    public IJugador getDueño() {
        return dueño;
    }

    public Herramienta getHerramienta() {
        return herramienta;
    }

}
