package Vista;

import Controlador.ControladorJuego;
import Modelo.Juego;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelJugador panelJugador;

    public Ventana(Juego juego) {
        this.controlador = new ControladorJuego(juego);
        setTitle("Saboteur - Juan Espinosa");
        setSize(1800, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // contiene las cartas, puntos y las herramientas del jugador
        JPanel contenedorJugador = new JPanel();
        contenedorJugador.setLayout(new BoxLayout(contenedorJugador, BoxLayout.LINE_AXIS));
        add(contenedorJugador, BorderLayout.SOUTH);


        PanelJugador panelJugador = new PanelJugador(controlador.getJugadorActual());

        PanelTomarCarta panelTomarCarta = new PanelTomarCarta(panelJugador, controlador);


        PanelPuntosYAcciones puntosYAcciones = new PanelPuntosYAcciones(panelJugador, controlador);

        PanelHerramientas herramientas = new PanelHerramientas(panelJugador, controlador);

        contenedorJugador.add(puntosYAcciones);
        contenedorJugador.add(panelJugador);
        contenedorJugador.add(panelTomarCarta);
        contenedorJugador.add(herramientas);

        // contiene el mapa con las cartas
        PanelTablero panelTablero = new PanelTablero(panelJugador, controlador);
        add(panelTablero, BorderLayout.CENTER);

        // contiene el listado de jugadores
        PanelTablaJugadores panelTablaJugadores = new PanelTablaJugadores(panelJugador, controlador);
        add(panelTablaJugadores, BorderLayout.WEST);


        setVisible(true);
    }

}
