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

    private ControladorJuego controladorJuego;

    public VentanaGanador(ControladorJuego controladorJuego) throws RemoteException {

        this.controladorJuego = controladorJuego;

        setTitle("");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setLayout(new BorderLayout());

        // PANEL SUPERIOR
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

        // PANEL CENTRAL (jugadores)
        JPanel panelJugadores = new JPanel();
        panelJugadores.setLayout(new GridLayout(1, 4, 20, 20));
        panelJugadores.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        IJugador[] jugadores = controladorJuego.getJugadores();

        for (int i = 0; i < jugadores.length; i++) {

            JPanel jugadorPanel = new JPanel();
            jugadorPanel.setLayout(new BorderLayout());

            JLabel imagen = new JLabel();
            imagen.setHorizontalAlignment(SwingConstants.CENTER);
            String ruta = "";
            if(jugadores[i].getRol() == Rol.SABOTEADOR){
                ruta = "ROL/Saboteador.png";
            }else if(jugadores[i].getRol() == Rol.MINERO){
                ruta = "ROL/Minero.png";
            }
            System.out.println(ruta);
            URL url = getClass().getClassLoader().getResource(ruta);
            System.out.println(url);
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(img));

            JLabel nombre = new JLabel(jugadores[i].getNombre(), SwingConstants.CENTER);

            jugadorPanel.add(imagen, BorderLayout.CENTER);
            jugadorPanel.add(nombre, BorderLayout.SOUTH);

            panelJugadores.add(jugadorPanel);
        }

        add(panelJugadores, BorderLayout.CENTER);

        // PANEL INFERIOR
        JPanel panelInferior = new JPanel();

        labelCuenta = new JLabel("La siguiente ronda empezará en 5 segundos");
        labelCuenta.setFont(new Font("Arial", Font.BOLD, 14));

        panelInferior.add(labelCuenta);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public void mostrarVentana(Evento evento, IJugador ganador, IJugador ganadorRonda) throws RemoteException {
        switch (evento) {
            case NUEVA_RONDA_GANADOR_SABOTEADORES -> {
                labelMensaje.setText("No hay mas cartas para jugar!\n GANARON LOS SABOTEDORES!");
            }
            case NUEVA_RONDA_GANADOR_MINEROS -> {
                System.out.println(controladorJuego.getGanadorRonda());
                    labelMensaje.setText(
                            ganadorRonda.getNombre() + " encontró el oro!!\nGANARON LOS MINEROS!"
                    );

            }
            case FINALIZAR_PARTIDA_SABOTEADORES -> {
                    labelMensaje.setText(
                            ganadorRonda.getNombre() + " encontró el oro!!\nGANARON LOS SABOTEADORES!"
                    );

            }
            case FINALIZAR_PARTIDA_MINEROS -> {
                labelMensaje.setText("GANARON LOS MINEROS!! \n Partida Terminada! \n El ganador es " + ganador.getNombre() + " con " + ganador.getPuntaje() + " Pepitas");
            }


        }
        setVisible(true);
    }

}