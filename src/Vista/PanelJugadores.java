package Vista;

import Controlador.ControladorJuego;
import Modelo.Juego;
import Modelo.Jugador;
import Modelo.Tablero;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelJugadores extends JPanel {

    private ControladorJuego controlador;
    private Juego juego;
    private PanelJugador panelJugador;
    private int cantidadJugadores;

    public PanelJugadores(PanelJugador jugador, ControladorJuego controlador) {
        this.controlador = controlador;
        this.panelJugador = jugador;

        juego = controlador.getJuego();
        cantidadJugadores = controlador.getJuego().getJugadores().size();
        setLayout(new GridLayout(cantidadJugadores, 1));
        setBorder(new EmptyBorder(300, 100, 300, 0));
        dibujarListaJugadores();
    }

    private void dibujarListaJugadores() {

        removeAll();

        for (Jugador j : juego.getJugadores()) {

            JPanel panelJugador = new JPanel();
            panelJugador.setLayout(new BoxLayout(panelJugador, BoxLayout.Y_AXIS));
            panelJugador.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY),
                    new EmptyBorder(10, 10, 10, 10)
            ));
            panelJugador.setBackground(Color.WHITE);

            // Nombre del jugador
            JLabel nombre = new JLabel(j.getNombre());
            nombre.setFont(new Font("Arial", Font.BOLD, 14));
            nombre.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Ejemplo de estado
            JLabel estado = new JLabel("Herramientas: " + ((j.getHerramientasRotas().size() != 0) ? "Rotas" : "Sanas"));
            estado.setFont(new Font("Arial", Font.PLAIN, 12));
            estado.setAlignmentX(Component.CENTER_ALIGNMENT);

            panelJugador.add(nombre);
            panelJugador.add(estado);
            panelJugador.add(Box.createVerticalStrut(5));
            add(panelJugador);
        }

    }
}
