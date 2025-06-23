package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.IJugador;
import Modelo.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PanelJugador extends JPanel {
    private IJugador jugadorCliente;
    private ControladorJuego controlador;
    List<BotonCarta> vistaManoActual = new ArrayList<>();
    private final int TAM_CARTA = 75;
    private int cartaSeleccionada = -1; // ninguna carta es seleccionada (si selcciono cambio el valor a la pos en la mano)

    public PanelJugador(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.jugadorCliente = controlador.getJugadorActualizado();
        setLayout(new FlowLayout());
        dibujarManoDeCartas(jugadorCliente.getManoCartas());
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
    }

    public void dibujarManoDeCartas(List<Carta> mano) throws RemoteException {
        // elimino lo existente

        removeAll();
        vistaManoActual.clear();
        mano = jugadorCliente.getManoCartas();
        // por cada carta creo un JButton
        for (int i = 0; i < mano.size(); i++) {

            Carta cartaActual = mano.get(i);
            BotonCarta botonCarta = new BotonCarta(cartaActual);
            vistaManoActual.add(botonCarta);
            URL url = cartaActual.getClass().getResource("/" + cartaActual.getImg());

            if (url != null) {
                ImageIcon icono = new ImageIcon(url);
                Image imagen = icono.getImage().getScaledInstance(TAM_CARTA, TAM_CARTA + 30, Image.SCALE_SMOOTH);
                botonCarta.setIcon(new ImageIcon(imagen));
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
        revalidate();
        repaint();

    }


    public IJugador getJugadorCliente() {
        return jugadorCliente;
    }

    public int getCartaSeleccionada() {
        return cartaSeleccionada;
    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(this, "No es tu turno!");
    }

    public void actualizar(IJugador jugador) throws RemoteException {
        removeAll();
        if (jugador.getManoCartas() != null) {
            dibujarManoDeCartas(jugador.getManoCartas());
        }
        revalidate();
        repaint();
    }
}
