package Vista;

import Modelo.Cartas.Carta;
import Modelo.Jugador;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PanelJugador extends JPanel {
    private Jugador jugador;
    private final int TAM_CARTA = 75;

    public PanelJugador(Jugador jugador, PanelTablero tablero) {
        this.jugador = jugador;
        setLayout(new FlowLayout());

        List<BotonCarta> botonesCartas = dibujarManoDeCartas();

        // implemento actionListener para cada boton en el mazo actual
        for(BotonCarta carta : botonesCartas){
            carta.addActionListener(e -> cartaEnMazoPresionada(carta, tablero));
        }

    }

    private void cartaEnMazoPresionada(BotonCarta boton, PanelTablero tablero) {
        System.out.println("apretaste una carta en tu mazo");
        Carta cartaPresionada = boton.getCartaAsociada();

    }

    public List<BotonCarta> dibujarManoDeCartas() {
        // elimino lo existente
        removeAll();

        // tomo la mano del jugador
        List<Carta> mano = jugador.getManoCartas();
        List<BotonCarta> vistaManoActual = new ArrayList<>();
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
        return vistaManoActual;
    }
}
