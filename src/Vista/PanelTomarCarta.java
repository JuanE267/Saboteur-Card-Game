package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.CartaTunel;
import Observer.Observable;
import Observer.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class PanelTomarCarta extends JPanel implements Observer {


    private PanelJugador panelJugador;
    private ControladorJuego controlador;
    private final int TAM_CARTA = 75;

    public PanelTomarCarta(PanelJugador panelJugador, ControladorJuego controlador) {
        this.panelJugador = panelJugador;
        this.controlador = controlador;

        dibujarPanel();
    }

    public void dibujarPanel() {
        removeAll();

        JButton tomarCarta = new JButton();

        String rutaImagen = "";
        if (controlador.getJuego().getMazo().getPrimerCarta() instanceof CartaTunel) {
            rutaImagen = "dorsos/dorso tunel.png";

        } else {
            rutaImagen = "dorsos/dorso accion.png";
        }

        URL url = getClass().getClassLoader().getResource(rutaImagen);

        if (url != null) {

            ImageIcon icono = new ImageIcon(url);
            Image imagen = icono.getImage().getScaledInstance(TAM_CARTA, TAM_CARTA + 30, Image.SCALE_SMOOTH);
            tomarCarta.setIcon(new ImageIcon(imagen));
        } else {
            System.err.println("Imagen no encontrada: " + url);
        }

        tomarCarta.setPreferredSize(new Dimension(TAM_CARTA, TAM_CARTA + 30));
        comportamientoTomarCarta(tomarCarta);
        add(tomarCarta);
    }

    private void comportamientoTomarCarta(JButton tomarCarta) {
        tomarCarta.addActionListener(e -> {
            if (controlador.esTurnoDe(panelJugador.getJugador())) {
                controlador.tomarCartaDeMazo();
                panelJugador.revalidate();
                panelJugador.repaint();
                panelJugador.dibujarManoDeCartas();
                dibujarPanel();
                controlador.pasarTurno();
            } else {
                mensajeNoEsTuTurno();
            }
        });
    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(this, "No es tu turno!");
    }

    @Override
    public void actualizar() {
        dibujarPanel();
    }
}
