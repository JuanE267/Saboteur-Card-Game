package Vista.VistaJuego;

import Controlador.ControladorJuego;
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

        setBackground(Color.decode("#4b3e2c"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 0, 0, 0));
        dibujarPanel(jugadorCliente);
    }

    private void dibujarPanel(IJugador jugadorCliente) {

        removeAll();

        JLabel puntaje = new JLabel();
        Font letra = new Font("Arial", Font.BOLD, 27);
        puntaje.setFont(letra);
        puntaje.setForeground(Color.decode("#bba00b"));
        if (jugadorCliente != null) {
            puntaje.setText("PEPITAS: " + jugadorCliente.getPuntaje());
        }
        puntaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel contenedorBotones = new JPanel();
        contenedorBotones.setBackground(Color.decode("#4b3e2c"));

        contenedorBotones.setLayout(new FlowLayout());
        contenedorBotones.setBorder(new EmptyBorder(10, 30, 0, 0));

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

                    controlador.pasarTurno();

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

                    if (panelJugador.getCartaSeleccionada() != -1) {
                        controlador.descartarCarta(panelJugador.getCartaSeleccionada());

                        // reseteo  despues de descartar la carta
                        panelJugador.resetCartaSeleccionada();
                        panelJugador.revalidate();
                        panelJugador.repaint();
                        controlador.pasarTurno();
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
