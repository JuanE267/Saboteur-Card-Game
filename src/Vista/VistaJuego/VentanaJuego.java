package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
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
        setSize(1220, 960);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setBackground(Color.decode("#4b3e2c"));
        setLayout(new BorderLayout());

        // label que muestra el turno actual
        turnoActual = new JLabel();
        turnoActual.setForeground(Color.decode("#4b3e2c"));
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
        contenedorJugador.setBackground(Color.decode("#4b3e2c"));
        add(contenedorJugador, BorderLayout.SOUTH);


        panelJugador = new PanelJugador(controlador);

        panelTomarCarta = new PanelTomarCarta(panelJugador, controlador);


        panelPuntosYAcciones = new PanelPuntosYAcciones(panelJugador, controlador);

        panelHerramientas = new PanelHerramientas(panelJugador, controlador);
        JPanel contenedorHerramientas = new JPanel();
        contenedorHerramientas.setLayout(new BoxLayout(contenedorHerramientas, BoxLayout.Y_AXIS));
        contenedorHerramientas.setBackground(Color.decode("#4b3e2c"));

        contenedorHerramientas.add(Box.createVerticalGlue());
        contenedorHerramientas.add(panelHerramientas);
        contenedorHerramientas.add(Box.createVerticalGlue());

        contenedorJugador.add(panelPuntosYAcciones);
        contenedorJugador.add(panelJugador);
        contenedorJugador.add(panelTomarCarta);
        add(contenedorHerramientas, BorderLayout.EAST);
        contenedorHerramientas.add(panelHerramientas);


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


    public void avisarGanadores(IJugador[] jugadores, Evento evento, IJugador ganador, int ronda) throws RemoteException {
        setVisible(false);
        StringBuilder sb = new StringBuilder("Se revelan los roles...\n\n");
        for (IJugador j : jugadores) {
            sb.append(j.getNombre()).append(" ---> ").append(j.getRol()).append("\n");
        }

        final String mensaje = sb.toString();
        switch (evento) {
            case NUEVA_RONDA_GANADOR_SABOTEADORES -> {
                    mostrarAviso("No hay mas cartas para jugar!!!" , 5000, () -> mostrarAviso("GANARON LOS SABOTEADORES!!", 5000, () ->
                            mostrarAviso(mensaje, 5000, () ->
                                    mostrarAviso("Iniciando nueva ronda...", 5000, null)
                            )));

            }
            case NUEVA_RONDA_GANADOR_MINEROS -> {
                mostrarAviso(controlador.getJugadorActual().getNombre() + " encontro el oro!!", 5000, () -> mostrarAviso("GANARON LOS MINEROS!!", 5000, () ->
                        mostrarAviso(mensaje, 5000, () ->
                                mostrarAviso("Iniciando nueva ronda...", 5000, null)
                        )));
            }
            case FINALIZAR_PARTIDA_SABOTEADORES -> {
                mostrarAviso("GANARON LOS SABOTEADORES!!", 5000, () ->
                        mostrarAviso(mensaje, 5000, () ->
                                mostrarAviso("Partida Terminada!!\n El ganador es " + ganador.getNombre()
                                        + " con " + ganador.getPuntaje() + " Pepitas", 5000, () ->
                                        mostrarAviso("Reiniciando partida...", 3000, null))));
            }
            case FINALIZAR_PARTIDA_MINEROS -> {
                mostrarAviso("GANARON LOS MINEROS!!", 5000, () ->
                        mostrarAviso(mensaje, 5000, () ->
                                mostrarAviso("Partida Terminada!!\n El ganador es " + ganador.getNombre()
                                        + " con " + ganador.getPuntaje() + " Pepitas", 5000, () ->
                                        mostrarAviso("Reiniciando partida...", 3000, null))));
            }
        }


    }


    public void mostrarAviso(String mensaje, int tiempo, Runnable siguiente) {

        // inicio la plantilla (no tiene bordes)
        JDialog plantilla = new JDialog(this, false);
        plantilla.setUndecorated(true);

        // agrego el texto
        JTextArea aviso = new JTextArea(mensaje);
        aviso.setFont(new Font("Arial", Font.BOLD, 24));

        aviso.setForeground(Color.WHITE);
        aviso.setBackground(Color.BLACK);
        aviso.setOpaque(true);
        aviso.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        aviso.setEnabled(false);
        plantilla.getContentPane().add(aviso);
        plantilla.getRootPane().setBorder(BorderFactory.createLineBorder(Color.yellow));
        plantilla.pack();
        plantilla.setLocationRelativeTo(this);
        plantilla.setVisible(true);

        // cada 5 seg la muestro y al terminar muestro la siguiente, si no hay siguiente muestro la ventana
        new Timer(tiempo, e -> {
            plantilla.dispose();
            ((Timer) e.getSource()).stop();
            if (siguiente != null) {
                siguiente.run();
            }
            if (siguiente == null) {
                setVisible(true);
            }
        }).start();
    }

}
