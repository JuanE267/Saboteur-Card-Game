package Vista;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaDestino;
import Modelo.Tablero;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PanelTablero extends JPanel {

    private Tablero tablero;
    private final int ANCHO_CASILLERO = 65;
    private final int ALTO_CASILLERO = 100;

    public PanelTablero(Tablero tablero, PanelJugador jugador) {
        this.tablero = tablero;
        setLayout(new GridLayout(tablero.getAlto(), tablero.getAncho()));
        setPreferredSize(new Dimension(tablero.getAncho(), tablero.getAlto()));
        setBorder(new EmptyBorder(0, 400, 0, 400));

        List<Casillero> casilleros = dibujarTablero();

        for(Casillero c : casilleros){
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    devolverPosicionAlClickear(c);
                }
            });
        }
    }

    private int[] devolverPosicionAlClickear(Casillero c) {
        return new int[]{c.getX(), c.getY()};
    }

    public List<Casillero> dibujarTablero() {

        // elimino lo existente
        removeAll();

        List<Casillero> listaCasilleros = new ArrayList<>();
        for (int i = 0; i < tablero.getAlto(); i++) {
            for (int j = 0; j < tablero.getAncho(); j++) {

                // recorro mi tablero inicializado
                Carta carta = tablero.getCarta(i, j);

                // creo un jlabel por cada carta
                Casillero casillero = new Casillero(i,j);
                listaCasilleros.add(casillero);
                casillero.setPreferredSize(new Dimension(ANCHO_CASILLERO, ALTO_CASILLERO));
                casillero.setBorder(BorderFactory.createDashedBorder(Color.BLACK));

                // si la carta no esta vacia (inicio o destinos)
                if (carta != null) {
                    // cargo y escalo la imagen
                    if (carta instanceof CartaDestino) {
                        ImageIcon icono = new ImageIcon(carta.getClass().getResource("/" + ((CartaDestino) carta).getDorso()));
                        Image imagen = icono.getImage().getScaledInstance(ANCHO_CASILLERO, ALTO_CASILLERO, Image.SCALE_SMOOTH);
                        casillero.setIcon(new ImageIcon(imagen));
                    } else {
                        ImageIcon icono = new ImageIcon(carta.getClass().getResource("/" + carta.getImg()));
                        Image imagen = icono.getImage().getScaledInstance(ANCHO_CASILLERO, ALTO_CASILLERO, Image.SCALE_SMOOTH);
                        casillero.setIcon(new ImageIcon(imagen));

                    }
                }

                add(casillero);
            }
        }
        revalidate();
        repaint();
        return listaCasilleros;
    }

}