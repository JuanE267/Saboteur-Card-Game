package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaTunel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.rmi.RemoteException;

import static javax.swing.text.StyleConstants.setBackground;

public class PanelTomarCarta extends JPanel {

    private ControladorJuego controlador;
    private final int TAM_CARTA = 75;

    public PanelTomarCarta(ControladorJuego controlador) {
        this.controlador = controlador;
        setBackground(Color.decode("#4b3e2c"));
        dibujarPanel();
    }

    public void dibujarPanel() {
        removeAll();

        JLabel cartaVisual = new JLabel(); // ← JLabel en lugar de JButton

        String rutaImagen;
        try {
            Carta primera = controlador.getMazo().getPrimerCarta();
            if (primera == null) {
                // mazo vacío — mostrar texto discreto
                JLabel vacio = new JLabel("Sin cartas");
                vacio.setForeground(Color.WHITE);
                add(vacio);
                revalidate();
                repaint();
                return;
            }
            rutaImagen = primera instanceof CartaTunel
                    ? "dorsos/dorso tunel.png"
                    : "dorsos/dorso accion.png";
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        URL url = getClass().getClassLoader().getResource(rutaImagen);
        if (url != null) {
            ImageIcon icono = new ImageIcon(url);
            Image imagen = icono.getImage().getScaledInstance(
                    TAM_CARTA, TAM_CARTA + 30, Image.SCALE_SMOOTH
            );
            cartaVisual.setIcon(new ImageIcon(imagen));
        }

        cartaVisual.setPreferredSize(new Dimension(TAM_CARTA, TAM_CARTA + 30));
        add(cartaVisual);
        revalidate();
        repaint();
    }

    public void actualizar() {
        dibujarPanel();
        revalidate();
        repaint();
    }
}