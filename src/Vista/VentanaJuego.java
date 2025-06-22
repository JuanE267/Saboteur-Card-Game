package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.Jugador;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class VentanaJuego extends JFrame {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelJugador panelJugador;
    PanelTablaJugadores panelTablaJugadores;
    PanelTomarCarta panelTomarCarta;
    PanelPuntosYAcciones panelPuntosYAcciones;
    PanelHerramientas panelHerramientas;
    JPanel contenedorJugador;
    JLabel turnoActual;
    String nombreJugadorActual = "";

    public VentanaJuego(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
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
        turnoActual.setText("Es el Turno de: " + nombreJugadorActual);
        add(turnoActual, BorderLayout.NORTH);

        inicializarVentana();
    }


    public void inicializarVentana() throws RemoteException {


        // contiene las cartas, puntos y las herramientas del jugador
        contenedorJugador = new JPanel();
        contenedorJugador.setLayout(new BoxLayout(contenedorJugador, BoxLayout.LINE_AXIS));
        add(contenedorJugador, BorderLayout.SOUTH);


        panelJugador = new PanelJugador(controlador);

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

    public void actualizarTurno() throws RemoteException {
        Jugador jugadorActual = controlador.getJugadorActual();
        turnoActual.setText("Es el Turno de: " + jugadorActual.getNombre());
        panelHerramientas.dibujarHerramientas();
    }

    public void actualizarVentana() throws RemoteException {
        inicializarVentana();
    }

    public PanelTablero getPanelTablero() {
        return panelTablero;
    }

    public void setPanelTablero(PanelTablero panelTablero) {
        this.panelTablero = panelTablero;
    }

    public PanelJugador getPanelJugador() {
        return panelJugador;
    }

    public void setPanelJugador(PanelJugador panelJugador) {
        this.panelJugador = panelJugador;
    }

    public PanelTablaJugadores getPanelTablaJugadores() {
        return panelTablaJugadores;
    }

    public void setPanelTablaJugadores(PanelTablaJugadores panelTablaJugadores) {
        this.panelTablaJugadores = panelTablaJugadores;
    }

    public PanelTomarCarta getPanelTomarCarta() {
        return panelTomarCarta;
    }

    public void setPanelTomarCarta(PanelTomarCarta panelTomarCarta) {
        this.panelTomarCarta = panelTomarCarta;
    }

    public PanelPuntosYAcciones getPanelPuntosYAcciones() {
        return panelPuntosYAcciones;
    }

    public void setPanelPuntosYAcciones(PanelPuntosYAcciones panelPuntosYAcciones) {
        this.panelPuntosYAcciones = panelPuntosYAcciones;
    }

    public PanelHerramientas getPanelHerramientas() {
        return panelHerramientas;
    }

    public void setPanelHerramientas(PanelHerramientas panelHerramientas) {
        this.panelHerramientas = panelHerramientas;
    }

    public void mostrarGanador() throws RemoteException {
        JOptionPane.showMessageDialog(this, "El ganador es " + controlador.getGanador());
    }
}
