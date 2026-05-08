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

        if(!controladorJuego.esJuegoCreado()) panelPrincipal.add(botonCrear);

        JButton botonUnirse = new JButton("Unirse a partida");
        botonUnirse.setPreferredSize(new Dimension(300, 80));

        botonUnirse.addActionListener(e -> {
            System.out.println("espere que el creador inicie la partida");
            dispose();
        });

        if(controladorJuego.esJuegoCreado()) panelPrincipal.add(botonUnirse);


        add(panelPrincipal);
        setVisible(false);
    }

    public void iniciar(){
        setVisible(true);
    }


}
