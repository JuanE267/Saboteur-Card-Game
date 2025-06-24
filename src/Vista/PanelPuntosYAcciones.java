package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class PanelPuntosYAcciones extends JPanel {

    private PanelJugador panelJugador;
    private ControladorJuego controlador;
    private IJugador jugadorCliente;

    public PanelPuntosYAcciones(PanelJugador panelJugador, ControladorJuego controlador) throws RemoteException {
        this.panelJugador = panelJugador;
        this.controlador = controlador;
        this.jugadorCliente = controlador.getJugadorActualizado();

        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(10, 0, 0, 0));

        dibujarPanel(jugadorCliente);
    }

    private void dibujarPanel(IJugador jugadorCliente) {

        removeAll();

        JLabel puntaje = new JLabel();
        Font letra = new Font("Arial", Font.PLAIN, 27);
        puntaje.setFont(letra);
        if (jugadorCliente != null) {
            puntaje.setText("PUNTAJE: " + jugadorCliente.getPuntaje());
        }
        JPanel contenedorBotones = new JPanel();
        contenedorBotones.setLayout(new FlowLayout());
        contenedorBotones.setBorder(new EmptyBorder(10, 100, 0, 0));

        JButton descartar = new JButton();
        descartar.setPreferredSize(new Dimension(110, 40));
        comportamientoDescarte(descartar);
        JButton pasarTurno = new JButton();
        pasarTurno.setPreferredSize(new Dimension(130, 40));
        comportamientoPasarTurno(pasarTurno);
        descartar.setText("DESCARTAR");
        pasarTurno.setText("PASAR TURNO");


        add(puntaje);
        contenedorBotones.add(descartar);
        contenedorBotones.add(pasarTurno);
        add(contenedorBotones);

    }

    private void comportamientoPasarTurno(JButton pasarTurno) {
        pasarTurno.addActionListener(e -> {

            //si es el turno del jugador, paso el turno y vuelvo a dibujar la mano por si acaso
            try {
                if (controlador.esTurnoDe(jugadorCliente)) {
                    panelJugador.resetCartaSeleccionada();
                    panelJugador.revalidate();
                    panelJugador.repaint();
                    panelJugador.dibujarManoDeCartas(controlador.getJugadorActualizado().getManoCartas());
                    try {
                        controlador.verificarSiTerminoLaRonda();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        controlador.pasarTurno();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mensajeNoEsTuTurno();
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

        });
    }

    public void comportamientoDescarte(JButton descartar) {

        descartar.addActionListener(e -> {
            // mediante el controlador tomo el jugador de esta ronda y elijo la carta seleccionada en el panel de la interfaz
            try {
                if (controlador.esTurnoDe(jugadorCliente)) {
                    try {
                        controlador.descartarCarta(panelJugador.getCartaSeleccionada());
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }

                    // reseteo todo despues de descartar la carta
                    panelJugador.resetCartaSeleccionada();
                    panelJugador.revalidate();
                    panelJugador.repaint();
                    try {
                        controlador.verificarSiTerminoLaRonda();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        controlador.pasarTurno();
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    mensajeNoEsTuTurno();
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(getRootPane(), "No es tu turno!");
    }

    public void actualizar(IJugador jugadorCliente) throws RemoteException {
        this.jugadorCliente = jugadorCliente;
        dibujarPanel(jugadorCliente);
        revalidate();
        repaint();
    }
}
