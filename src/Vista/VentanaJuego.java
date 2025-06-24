package Vista;

import Controlador.ControladorJuego;
import Modelo.IJugador;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class VentanaJuego extends JFrame {
    private ControladorJuego controlador;
    private PanelTablero panelTablero;
    private PanelJugador panelJugador;

    private PanelTablaJugadores panelTablaJugadores;
    private PanelTomarCarta panelTomarCarta;
    private PanelPuntosYAcciones panelPuntosYAcciones;
    private PanelHerramientas panelHerramientas;
    private JPanel contenedorJugador;
    private JLabel turnoActual;

    public VentanaJuego(ControladorJuego controlador) throws RemoteException {

        this.controlador = controlador;

        if (controlador.getJugadorActualizado() != null) {
            setTitle("Saboteur - Juan Espinosa (cliente " + controlador.getJugadorActualizado().getNombre() + ")");
        }
        setSize(1800, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setBackground(Color.decode("#736d62"));
        setLayout(new BorderLayout());

        // label que muestra el turno actual
        turnoActual = new JLabel();
        turnoActual.setForeground(Color.decode("#736d62"));
        // jugador del turno
        IJugador jugadorActual = controlador.getJugadorActual();
        turnoActual.setText("Es el Turno de: " + jugadorActual.getNombre());

        turnoActual.setFont(new Font("Arial", Font.BOLD, 18));
        turnoActual.setHorizontalAlignment(SwingConstants.CENTER);
        add(turnoActual, BorderLayout.NORTH);

        inicializarVentana();
        setVisible(true);

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


        // contiene el mapa
        panelTablero = new PanelTablero(panelJugador, controlador);
        add(panelTablero, BorderLayout.CENTER);

        // contiene el listado de jugadores
        panelTablaJugadores = new PanelTablaJugadores(panelJugador, controlador);
        add(panelTablaJugadores, BorderLayout.WEST);
        setVisible(true);
        revalidate();
        repaint();
    }


    public void actualizarVentana() throws RemoteException {
        IJugador jugadorActual = controlador.getJugadorActual();
        turnoActual.setText("Es el Turno de: " + jugadorActual.getNombre());
        revalidate();
        repaint();
    }

    public PanelTablero getPanelTablero() {
        return panelTablero;
    }

    public PanelJugador getPanelJugador() {
        return panelJugador;
    }

    public PanelTablaJugadores getPanelTablaJugadores() {
        return panelTablaJugadores;
    }


    public PanelTomarCarta getPanelTomarCarta() {
        return panelTomarCarta;
    }

    public PanelPuntosYAcciones getPanelPuntosYAcciones() {
        return panelPuntosYAcciones;
    }

    public PanelHerramientas getPanelHerramientas() {
        return panelHerramientas;
    }


    public void mostrarGanador(String ganador) throws RemoteException {
        JOptionPane.showMessageDialog(this, "El ganador es " + ganador);
    }
}
