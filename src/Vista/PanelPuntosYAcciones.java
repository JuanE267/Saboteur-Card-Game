package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Observer.Observable;
import Observer.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelPuntosYAcciones extends JPanel  implements Observer {

    private PanelJugador panelJugador;
    private ControladorJuego controlador;

    public PanelPuntosYAcciones(PanelJugador panelJugador, ControladorJuego controlador) {
        this.panelJugador = panelJugador;
        this.controlador = controlador;

        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(10, 0, 0, 0));

        dibujarPanel();
    }

    private void dibujarPanel() {

        removeAll();

        JLabel puntaje = new JLabel();
        Font letra = new Font("Arial", Font.PLAIN, 27);
        puntaje.setFont(letra);
        puntaje.setText("PUNTAJE: "+ panelJugador.getJugador().getPuntaje());

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
            if(controlador.esTurnoDe(panelJugador.getJugador())) {
                panelJugador.resetCartaSeleccionada();
                panelJugador.revalidate();
                panelJugador.repaint();
                panelJugador.dibujarManoDeCartas();
                controlador.verificarSiTerminoLaRonda();
                controlador.pasarTurno();
            }else{
                mensajeNoEsTuTurno();
            }

        });
    }

    public void comportamientoDescarte(JButton descartar){

        descartar.addActionListener(e -> {
            // mediante el controlador tomo el jugador de esta ronda y elijo la carta seleccionada en el panel de la interfaz
            if(controlador.esTurnoDe(panelJugador.getJugador())) {
                Carta cartaADescartar = controlador.getJugadorActual().elegirCarta(panelJugador.getCartaSeleccionada());
                controlador.descartarCarta(cartaADescartar);

                // reseteo todo despues de descartar la carta
                panelJugador.resetCartaSeleccionada();
                panelJugador.revalidate();
                panelJugador.repaint();
                controlador.verificarSiTerminoLaRonda();
                controlador.pasarTurno();
            }else{
                mensajeNoEsTuTurno();
            }
            });
    }

    public void mensajeNoEsTuTurno(){
        JOptionPane.showMessageDialog(this,"No es tu turno!");
    }

    @Override
    public void actualizar() {
        dibujarPanel();
    }
}
