package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.Juego;
import Modelo.Jugador;
import Observer.Observer;
import jdk.jfr.Event;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelJugador panelJugador;
    PanelTablaJugadores panelTablaJugadores;
    PanelTomarCarta panelTomarCarta;
    PanelPuntosYAcciones panelPuntosYAcciones;
    PanelHerramientas panelHerramientas;
    JPanel contenedorJugador;
    JLabel turnoActual;

    public Ventana(ControladorJuego controlador) {
        this.controlador = controlador;
        controlador.setVentana(this);
        setTitle("Saboteur - Juan Espinosa");
        setSize(1800, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // label que muestra el turno actual
        turnoActual = new JLabel();
        turnoActual.setFont(new Font("Arial", Font.BOLD, 18));
        turnoActual.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        turnoActual.setText("Es el Turno de: " + controlador.getJugadorActual().getNombre());
        add(turnoActual, BorderLayout.NORTH);

        inicializarVentana();
    }


    public void inicializarVentana() {


        // contiene las cartas, puntos y las herramientas del jugador
        contenedorJugador = new JPanel();
        contenedorJugador.setLayout(new BoxLayout(contenedorJugador, BoxLayout.LINE_AXIS));
        add(contenedorJugador, BorderLayout.SOUTH);


        panelJugador = new PanelJugador(controlador.getJugadorActual(), controlador);

        panelTomarCarta = new PanelTomarCarta(panelJugador, controlador);


        panelPuntosYAcciones = new PanelPuntosYAcciones(panelJugador, controlador);

        panelHerramientas = new PanelHerramientas(panelJugador, controlador);

        contenedorJugador.add(panelPuntosYAcciones);
        contenedorJugador.add(panelJugador);
        contenedorJugador.add(panelTomarCarta);
        contenedorJugador.add(panelHerramientas);



        // contiene el mapa con las cartas
        panelTablero = new PanelTablero(panelJugador, controlador);
        add(panelTablero, BorderLayout.CENTER);

        // contiene el listado de jugadores
        panelTablaJugadores = new PanelTablaJugadores(panelJugador, controlador);
        add(panelTablaJugadores, BorderLayout.WEST);

        setVisible(true);
    }

    public void actualizarTurno() {
        Jugador jugadorActual = controlador.getJugadorActual();
        turnoActual.setText("Es el Turno de: " + jugadorActual.getNombre());
        panelHerramientas.dibujarHerramientas();
    }

    public void actualizarVentana(){
        inicializarVentana();
    }

    public void actualizar(Evento evento){
        switch (evento){
            case NUEVA_RONDA -> actualizarVentana();
            case PASAR_TURNO -> actualizarTurno();
            case TOMAR_CARTA, DESCARTAR_CARTA -> panelJugador.actualizar();
            case ACTUALIZAR_HERRAMIENTAS -> {
                panelHerramientas.actualizar();
                panelTablaJugadores.actualizar();
            }
            case JUGAR_CARTA_TABLERO -> {
                panelTablero.actualizar();
                panelJugador.actualizar();
            }
        }
    }

}
