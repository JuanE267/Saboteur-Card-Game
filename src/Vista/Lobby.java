package Vista;
import Controlador.ControladorJuego;
import Modelo.Enums.Evento;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;


public class Lobby extends JFrame {
    private ControladorJuego controladorJuego;
    public Lobby(ControladorJuego controladorJuego) throws RemoteException {
        this.controladorJuego = controladorJuego;

        setTitle("Saboteur");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());

        JButton botonCrear = new JButton("Crear partida");
        botonCrear.setPreferredSize(new Dimension(300, 80));

        botonCrear.addActionListener(e -> {
            VentanaServidor ventanaServidor = new VentanaServidor(controladorJuego);
            controladorJuego.setVistaServidor(ventanaServidor);
            controladorJuego.setEsHost();
            ventanaServidor.iniciar();
            try {
                ventanaServidor.actualizarListaJugadores(controladorJuego.getJugadores());
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        panelPrincipal.add(botonCrear);
        JButton botonUnirse = new JButton("Unirse a partida");
        botonUnirse.setPreferredSize(new Dimension(300, 80));

        botonUnirse.addActionListener(e -> {
            dispose();
            mostrarPantallaEspera();
        });
        panelPrincipal.add(botonUnirse);

        add(panelPrincipal);
        setVisible(false);
    }

    private void mostrarPantallaEspera() {
        JFrame espera = new JFrame("Saboteur");
        espera.setSize(400, 200);
        espera.setLocationRelativeTo(null);
        espera.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        espera.setLayout(new BorderLayout());

        JLabel mensaje = new JLabel(
                "Esperando que el host inicie la partida...",
                SwingConstants.CENTER
        );
        mensaje.setFont(new Font("Arial", Font.BOLD, 16));

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true); // animación de carga

        espera.add(mensaje, BorderLayout.CENTER);
        espera.add(barra, BorderLayout.SOUTH);
        espera.setVisible(true);
    }

    public void iniciar(){
        setVisible(true);
    }


}
