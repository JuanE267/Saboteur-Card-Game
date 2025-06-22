package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Jugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PanelJugador extends JPanel {
    private Jugador jugador;
    private ControladorJuego controlador;
    List<BotonCarta> vistaManoActual = new ArrayList<>();
    private final int TAM_CARTA = 75;
    private int cartaSeleccionada = -1; // ninguna carta es seleccionada (si selcciono cambio el valor a la pos en la mano)

    public PanelJugador(ControladorJuego controlador) {
        this.controlador = controlador;
        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        dibujarManoDeCartas();
    }

    private void cartaEnMazoPresionada(BotonCarta boton) throws RemoteException {
        jugador = controlador.getJugadorActual();
        if (controlador.esTurnoDe(jugador)) {
            Carta cartaPresionada = boton.getCartaAsociada();
            cartaSeleccionada = jugador.getManoCartas().indexOf(cartaPresionada);
        } else {
            mensajeNoEsTuTurno();
        }
    }

    public void resetCartaSeleccionada() {
        cartaSeleccionada = -1;
    }

    public void dibujarManoDeCartas() {
        // elimino lo existente
        removeAll();
        if(jugador != null) {
            // tomo la mano del jugador
            List<Carta> mano = jugador.getManoCartas();
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
        }

    }

    public Jugador getJugador() {
        return jugador;
    }

    public int getCartaSeleccionada() {
        return cartaSeleccionada;
    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(this, "No es tu turno!");
    }

    public void actualizar() {
        dibujarManoDeCartas();
    }
}
