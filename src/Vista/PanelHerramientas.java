package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Herramienta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.net.URL;

public class PanelHerramientas extends JPanel {

    private PanelJugador panelJugador;
    private ControladorJuego controlador;

    public PanelHerramientas(PanelJugador panelJugador, ControladorJuego controlador) {
        this.panelJugador = panelJugador;
        this.controlador = controlador;

        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(10, 0, 0, 0));
        dibujarHerramientas();
    }

    private void dibujarHerramientas() {

        removeAll();

        JLabel pico = new JLabel();
        JLabel vagoneta = new JLabel();
        JLabel linterna = new JLabel();

        pico.setSize(new Dimension(100, 100));
        vagoneta.setSize(new Dimension(100, 100));
        linterna.setSize(new Dimension(100, 100));

        String picoSano = "herramientas/PICO SANO.png";
        String vagonetaSana = "herramientas/VAGONETA SANA.png";
        String linternaSana = "herramientas/LINTERNA SANA.png";

        String picoRoto = "herramientas/PICO ROTO.png";
        String vagonetaRota = "herramientas/VAGONETA ROTA.png";
        String linternaRota = "herramientas/LINTERNA ROTA.png";

        List<Herramienta> herramientasRotas = controlador.getJugadorActual().getHerramientasRotas();


        URL urlPico = getClass().getClassLoader().getResource(picoSano);
        URL urlVagoneta = getClass().getClassLoader().getResource(vagonetaSana);
        URL urlLinterna = getClass().getClassLoader().getResource(linternaSana);

        for (Herramienta herr : herramientasRotas) {
            if (herr == Herramienta.PICO) {
                urlPico = getClass().getClassLoader().getResource(picoRoto);
            }

            if (herr == Herramienta.VAGONETA) {
                urlVagoneta = getClass().getClassLoader().getResource(vagonetaRota);
            }

            if (herr == Herramienta.LINTERNA) {
                urlLinterna = getClass().getClassLoader().getResource(linternaRota);
            }
        }

        pico.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                herramientaEsPresionada(pico);
            }
        });

        vagoneta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                herramientaEsPresionada(vagoneta);
            }
        });

        linterna.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                herramientaEsPresionada(linterna);
            }
        });

        setImagen(pico, urlPico);
        setImagen(vagoneta, urlVagoneta);
        setImagen(linterna, urlLinterna);

        add(pico);
        add(vagoneta);
        add(linterna);
    }

    private void herramientaEsPresionada(JLabel herr) {

        int posCarta = panelJugador.getCartaSeleccionada();
        controlador.jugarHerramienta(posCarta, controlador.getJugadorActual());

        panelJugador.resetCartaSeleccionada();
        panelJugador.revalidate();
        panelJugador.repaint();
        panelJugador.dibujarManoDeCartas();
        dibujarHerramientas();
        controlador.pasarTurno();
    }

    private void setImagen(JLabel herr, URL url) {
        if (url != null) {
            ImageIcon icono = new ImageIcon(url);
            Image imagen = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            herr.setIcon(new ImageIcon(imagen));
        } else {
            System.err.println("Imagen no encontrada: " + url);
        }
    }
}
