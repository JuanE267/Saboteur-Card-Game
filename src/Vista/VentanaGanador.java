package Vista;

import Controlador.Controlador;
import Modelo.Enums.Evento;
import Modelo.Enums.Rol;
import Modelo.IJugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;

public class VentanaGanador extends JFrame {

    private static final Color FONDO = new Color(30, 20, 10);
    private static final Color ORO = new Color(212, 160, 50);
    private static final Color TEXTO = new Color(240, 220, 180);
    private static final Color TARJETA = new Color(50, 35, 15);

    private final JLabel labelMensaje;
    private final JLabel labelTitulo;
    private final JLabel labelCuenta;
    private final JPanel panelJugadores;

    private final Controlador controladorJuego;

    public VentanaGanador(Controlador controladorJuego) throws RemoteException {

        this.controladorJuego = controladorJuego;

        setTitle("");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getContentPane().setBackground(FONDO);


        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(FONDO);

        labelMensaje = new JLabel("", SwingConstants.CENTER);
        labelMensaje.setForeground(TEXTO);
        labelMensaje.setFont(new Font("Serif", Font.BOLD, 28));
        labelMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelTitulo = new JLabel("Revelación de roles:", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Serif", Font.ITALIC | Font.BOLD, 24));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitulo.setForeground(TEXTO);

        panelSuperior.add(Box.createVerticalStrut(20));
        panelSuperior.add(labelMensaje);
        panelSuperior.add(Box.createVerticalStrut(20));
        panelSuperior.add(labelTitulo);

        add(panelSuperior, BorderLayout.NORTH);

        panelJugadores = new JPanel();
        panelJugadores.setBackground(FONDO);
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(panelJugadores, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(FONDO);

        labelCuenta = new JLabel("La siguiente ronda empezará en 10 segundos");
        labelCuenta.setForeground(TEXTO);
        labelCuenta.setFont(new Font("Serif", Font.BOLD, 18));

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
                        "<html><div style='text-align:center'>" + nombre + " encontro el oro!!<br>GANARON LOS MINEROS!</html>"
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
                            "<html>¡No hay mas cartas!<br>GANARON LOS SABOTEADORES!</html>"
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
            jugadorPanel.setLayout(new BorderLayout(0, 8));
            jugadorPanel.setBackground(TARJETA);
            jugadorPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ORO, 1),
                    BorderFactory.createEmptyBorder(2, 10, 2, 10)
            ));

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
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imagen.setIcon(new ImageIcon(img));
            }

            JLabel nombre = new JLabel(j.getNombre(), SwingConstants.CENTER);
            nombre.setForeground(TEXTO);
            nombre.setFont(new Font("Serif", Font.BOLD, 32));

            jugadorPanel.add(imagen, BorderLayout.CENTER);
            jugadorPanel.add(nombre, BorderLayout.SOUTH);

            panelJugadores.add(jugadorPanel);
        }

        panelJugadores.revalidate();
        panelJugadores.repaint();
    }

    public void actualizarCuentaRegresiva(int segundos) {
        if (segundos > 0) {
            labelCuenta.setText("La siguiente ronda empezará en " + segundos + " segundos");
        } else {
            labelCuenta.setText("¡Empezando nueva ronda!");
        }
    }
}