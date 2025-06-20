package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Observer.Observable;
import Observer.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.net.URL;

public class PanelHerramientas extends JPanel implements Observer {

    private PanelJugador panelJugador;
    private ControladorJuego controlador;

    public PanelHerramientas(PanelJugador panelJugador, ControladorJuego controlador) {
        this.panelJugador = panelJugador;
        this.controlador = controlador;

        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(10, 0, 0, 0));
        dibujarHerramientas();
    }

    public void dibujarHerramientas() {

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

        List<Herramienta> herramientasRotas = panelJugador.getJugador().getHerramientasRotas();


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
                if(controlador.esTurnoDe(panelJugador.getJugador())) {
                    super.mouseClicked(e);
                    herramientaEsPresionada(pico);
                }else{
                    mensajeNoEsTuTurno();
                }
            }
        });

        vagoneta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(controlador.esTurnoDe(panelJugador.getJugador())) {
                    super.mouseClicked(e);
                    herramientaEsPresionada(vagoneta);
                }else{
                    mensajeNoEsTuTurno();
                }
            }
        });

        linterna.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(controlador.esTurnoDe(panelJugador.getJugador())) {
                    super.mouseClicked(e);
                    herramientaEsPresionada(linterna);
                }else{
                    mensajeNoEsTuTurno();
                }
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
        Carta cartaHerramienta = panelJugador.getJugador().elegirCarta(posCarta);
        List<TipoAccion> tipos = ((CartaAccion)cartaHerramienta).getTipoAccion();

        switch (tipos.getFirst()){
            case REPARARPICO, REPARARLINTERNA, REPARARVAGONETA -> {
                controlador.jugarHerramienta(posCarta, controlador.getJugadorActual());

                panelJugador.resetCartaSeleccionada();
                panelJugador.revalidate();
                panelJugador.repaint();
                controlador.verificarSiTerminoLaRonda();
                controlador.pasarTurno();
            }
            default -> JOptionPane.showMessageDialog(this , "No podes romper tu propia herramienta!");

        }


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


    public void mensajeNoEsTuTurno(){
        JOptionPane.showMessageDialog(this,"No es tu turno!");
    }

    @Override
    public void actualizar() {
        dibujarHerramientas();

    }
}
