package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaTunel;
import Modelo.IJugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PanelJugador extends JPanel {
    private IJugador jugadorCliente;
    private ControladorJuego controlador;
    private boolean cartaRotada = false;

    List<BotonCarta> vistaManoActual = new ArrayList<>();
    private final int TAM_CARTA = 75;
    private int cartaSeleccionada = -1; // ninguna carta es seleccionada (si selcciono cambio el valor a la pos en la mano)

    public PanelJugador(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.jugadorCliente = controlador.getJugadorActualizado();
        setBackground(Color.decode("#4b3e2c"));
        setLayout(new FlowLayout());
        dibujarManoDeCartas();
    }

    private void cartaEnMazoPresionada(BotonCarta boton) throws RemoteException {
        if (controlador.esTurnoDe(jugadorCliente)) {
            Carta cartaPresionada = boton.getCartaAsociada();
            cartaSeleccionada = jugadorCliente.getManoCartas().indexOf(cartaPresionada);
        } else {
            mensajeNoEsTuTurno();
        }
    }

    public void resetCartaSeleccionada() {
        cartaSeleccionada = -1;
        cartaRotada = false;
    }

    public void dibujarManoDeCartas() throws RemoteException {
        // elimino lo existente

        removeAll();
        vistaManoActual.clear();
        List<Carta> mano = jugadorCliente.getManoCartas();
        // por cada carta creo un JButton
        for (int i = 0; i < mano.size(); i++) {

            Carta cartaActual = mano.get(i);
            BotonCarta botonCarta = new BotonCarta(cartaActual);
            vistaManoActual.add(botonCarta);
            URL url = cartaActual.getClass().getResource("/" + cartaActual.getImg());

            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                BufferedImage bi = new BufferedImage(TAM_CARTA, TAM_CARTA + 30, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bi.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                if (cartaActual.isRotada()) {
                    g2.rotate(Math.PI, TAM_CARTA / 2.0, (TAM_CARTA + 30) / 2.0);
                }

                g2.drawImage(icono.getImage(), 0, 0, TAM_CARTA, TAM_CARTA + 30, null);
                g2.dispose();
                botonCarta.setIcon(new ImageIcon(bi));
            } else {
                System.err.println("Imagen no encontrada: " + cartaActual.getImg());
            }
            botonCarta.setPreferredSize(new Dimension(TAM_CARTA, TAM_CARTA + 30));

            add(botonCarta);
        }

        // implemento actionListener para cada boton en el mazo actual
        for (BotonCarta carta : vistaManoActual) {
            carta.addActionListener(e -> {
                try {
                    cartaEnMazoPresionada(carta);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            });
        }

        for (BotonCarta boton : vistaManoActual) {
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            // Click derecho: rotar la carta
                            rotarCartaSeleccionada(boton);
                        } else {
                            // Click izquierdo: seleccionar
                            cartaEnMazoPresionada(boton);
                        }
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        revalidate();
        repaint();

    }

    private void rotarCartaSeleccionada(BotonCarta boton) throws RemoteException {
        if(!controlador.esTurnoDe(jugadorCliente)){
            mensajeNoEsTuTurno();
            return;
        }

        Carta carta = boton.getCartaAsociada();

        if(carta instanceof CartaTunel tunel){
            tunel.rotar();
            carta.setRotada(!carta.isRotada());
            actualizarIconoBoton(boton, carta);
            cartaSeleccionada = jugadorCliente.getManoCartas().indexOf(carta);
            cartaRotada = carta.isRotada();
        }

    }


    private void actualizarIconoBoton(BotonCarta boton, Carta carta) {
        URL url = carta.getClass().getResource("/" + carta.getImg());
        if (url != null) {
            ImageIcon icono = new ImageIcon(url);

            BufferedImage bi = new BufferedImage(TAM_CARTA, TAM_CARTA + 30, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (carta.isRotada()) {
                g2.rotate(Math.PI, TAM_CARTA / 2.0, (TAM_CARTA + 30) / 2.0);
            }

            g2.drawImage(icono.getImage(), 0, 0, TAM_CARTA, TAM_CARTA + 30, null);
            g2.dispose();

            boton.setIcon(new ImageIcon(bi));
        }
    }

    public boolean isCartaRotada() {
        return cartaRotada;
    }

    public int getCartaSeleccionada() {
        return cartaSeleccionada;
    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(getRootPane(), "No es tu turno!");
    }

    public void actualizar(IJugador jugador) throws RemoteException {
        this.jugadorCliente = jugador;
        removeAll();
        if (jugador.getManoCartas() != null) {
            dibujarManoDeCartas();
        }
        revalidate();
        repaint();
    }
}
