package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.Enums.Rol;
import Modelo.IJugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;

public class VentanaGanador extends JFrame {

    private JLabel labelMensaje;
    private JLabel labelTitulo;
    private JLabel labelCuenta;
    private JPanel panelJugadores;

    private ControladorJuego controladorJuego;

    public VentanaGanador(ControladorJuego controladorJuego) throws RemoteException {

        this.controladorJuego = controladorJuego;

        setTitle("");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        labelMensaje = new JLabel("", SwingConstants.CENTER);
        labelMensaje.setFont(new Font("Arial", Font.BOLD, 18));
        labelMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelTitulo = new JLabel("Revelación de roles:", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelSuperior.add(Box.createVerticalStrut(20));
        panelSuperior.add(labelMensaje);
        panelSuperior.add(Box.createVerticalStrut(20));
        panelSuperior.add(labelTitulo);

        add(panelSuperior, BorderLayout.NORTH);

        panelJugadores = new JPanel();
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(panelJugadores, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();

        labelCuenta = new JLabel("La siguiente ronda empezará en 10 segundos");
        labelCuenta.setFont(new Font("Arial", Font.BOLD, 14));

        panelInferior.add(labelCuenta);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public void mostrarVentana(Evento evento, IJugador ganador, IJugador ganadorRonda) throws RemoteException {
        switch (evento) {
            case NUEVA_RONDA_GANADOR_SABOTEADORES -> {
                labelMensaje.setText("<html>No hay mas cartas para jugar!<br>GANARON LOS SABOTEADORES!</html>");
                labelCuenta.setText("La siguiente ronda empezará en 10 segundos");
            }
            case NUEVA_RONDA_GANADOR_MINEROS -> {
                String nombre = ganadorRonda != null ? ganadorRonda.getNombre() : "Alguien";
                labelMensaje.setText(
                        "<html>" + nombre + " encontró el oro!!<br>GANARON LOS MINEROS!</html>"
                );
                labelCuenta.setText("La siguiente ronda empezará en 10 segundos");
            }
            case FINALIZAR_PARTIDA_SABOTEADORES -> {
                if (ganadorRonda != null) {
                    labelMensaje.setText(
                            "<html>" + ganadorRonda.getNombre() + " encontró el oro...<br>" +
                                    "¡Pero era una trampa! GANARON LOS SABOTEADORES!</html>"
                    );
                } else {
                    labelMensaje.setText(
                            "<html>¡No hay más cartas!<br>GANARON LOS SABOTEADORES!</html>"
                    );
                }
                labelCuenta.setText("Partida terminada");
            }
            case FINALIZAR_PARTIDA_MINEROS -> {
                String nombreGanador = ganador != null ? ganador.getNombre() : "?";
                int puntaje = ganador != null ? ganador.getPuntaje() : 0;
                labelMensaje.setText(
                        "<html>GANARON LOS MINEROS!!<br>Partida Terminada!<br>" +
                                "El ganador es " + nombreGanador + " con " + puntaje + " Pepitas</html>"
                );
                labelCuenta.setText("Partida terminada");
            }
        }

        actualizarPanelRoles();
        setVisible(true);
    }

    private void actualizarPanelRoles() throws RemoteException {
        panelJugadores.removeAll();

        IJugador[] jugadores = controladorJuego.getJugadores();
        panelJugadores.setLayout(new GridLayout(1, jugadores.length, 20, 20));

        for (IJugador j : jugadores) {
            JPanel jugadorPanel = new JPanel();
            jugadorPanel.setLayout(new BorderLayout());

            JLabel imagen = new JLabel();
            imagen.setHorizontalAlignment(SwingConstants.CENTER);

            String ruta = "";
            if (j.getRol() == Rol.SABOTEADOR) {
                ruta = "ROL/Saboteador.png";
            } else if (j.getRol() == Rol.MINERO) {
                ruta = "ROL/Minero.png";
            }

            URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                imagen.setIcon(new ImageIcon(img));
            }

            JLabel nombre = new JLabel(j.getNombre(), SwingConstants.CENTER);

            jugadorPanel.add(imagen, BorderLayout.CENTER);
            jugadorPanel.add(nombre, BorderLayout.SOUTH);

            panelJugadores.add(jugadorPanel);
        }

        panelJugadores.revalidate();
        panelJugadores.repaint();
    }
}