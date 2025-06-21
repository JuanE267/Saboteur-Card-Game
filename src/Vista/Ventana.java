package Vista;

import Controlador.ControladorJuego;
import Modelo.Juego;
import Modelo.Jugador;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelJugador panelJugador;
    PanelTablaJugadores panelTablaJugadores;
    PanelTomarCarta panelTomarCarta;
    PanelPuntosYAcciones puntosYAcciones;
    PanelHerramientas herramientas;
    JPanel contenedorJugador;
    JLabel turnoActual;

    public Ventana(Juego juego) {
        this.controlador = new ControladorJuego(juego, this);
        setTitle("Saboteur - Juan Espinosa");
        setSize(1800, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        inicializarVentana();
    }

    public void reiniciarVista() {
        removeAll();
        if (controlador.getGanador() != null) {
            JOptionPane.showMessageDialog(this, "El ganador es: " + controlador.getGanador().getNombre() +
                                                                        "\n con un puntaje de: " + controlador.getGanador().getPuntaje());
            System.exit(0);
        } else {
            inicializarVentana();
        }

    }

    public void inicializarVentana() {

        // contiene las cartas, puntos y las herramientas del jugador
        contenedorJugador = new JPanel();
        contenedorJugador.setLayout(new BoxLayout(contenedorJugador, BoxLayout.LINE_AXIS));
        add(contenedorJugador, BorderLayout.SOUTH);


        panelJugador = new PanelJugador(controlador.getJugadorActual(), controlador);

        panelTomarCarta = new PanelTomarCarta(panelJugador, controlador);


        puntosYAcciones = new PanelPuntosYAcciones(panelJugador, controlador);

        herramientas = new PanelHerramientas(panelJugador, controlador);

        contenedorJugador.add(puntosYAcciones);
        contenedorJugador.add(panelJugador);
        contenedorJugador.add(panelTomarCarta);
        contenedorJugador.add(herramientas);

        // label que muestra el turno actual
        turnoActual = new JLabel();
        turnoActual.setFont(new Font("Arial", Font.BOLD, 18));
        turnoActual.setHorizontalAlignment(SwingConstants.HORIZONTAL);
        turnoActual.setText("Es el Turno de: " + controlador.getJugadorActual().getNombre());
        add(turnoActual, BorderLayout.NORTH);

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
        herramientas.dibujarHerramientas();
    }

}
