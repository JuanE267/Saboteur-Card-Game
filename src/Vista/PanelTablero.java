package Vista;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.TipoAccion;
import Modelo.Tablero;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PanelTablero extends JPanel {

    private ControladorJuego controlador;
    private PanelJugador panelJugador;
    private Tablero tablero;
    private final int ANCHO_CASILLERO = 65;
    private final int ALTO_CASILLERO = 100;

    public PanelTablero(PanelJugador jugador, ControladorJuego controlador) {
        this.controlador = controlador;
        this.panelJugador = jugador;
        tablero = controlador.getTablero();

        setLayout(new GridLayout(tablero.getAlto(), tablero.getAncho()));
        setPreferredSize(new Dimension(tablero.getAncho(), tablero.getAlto()));
        setBorder(new EmptyBorder(0, 200, 0, 400));

        if (controlador.terminarRonda()) {
            JOptionPane.showMessageDialog(null, "¡LLegaste al oro!");
        }
        dibujarTablero();


    }

    private void casilleroEsPresionado(Casillero casillero) {
        int cartaSeleccionada = panelJugador.getCartaSeleccionada();
        Carta cartaAJugar = controlador.getJugadorActual().elegirCarta(cartaSeleccionada);

        // si es una carta dentro del mazo
        if (cartaSeleccionada != -1 && cartaSeleccionada >= 0) {

            //si es de tipo tunel
            if (cartaAJugar instanceof CartaTunel) {
                controlador.jugarCarta(casillero.posX(), casillero.posY(), cartaSeleccionada, null);
            }
            //si es de tipo accion
            else if (cartaAJugar instanceof CartaAccion) {
                // juego la carta si es derrumbre o mapa

                if (((CartaAccion) cartaAJugar).getTipoAccion().getFirst() == TipoAccion.MAPA || ((CartaAccion) cartaAJugar).getTipoAccion().getFirst() == TipoAccion.DERRUMBAR) {
                    controlador.jugarCarta(casillero.posX(), casillero.posY(), cartaSeleccionada, null);
                    // si es el mapa necesito que despues de 3 segundo se de vuelta el destino nuevamente
                    if (((CartaAccion) cartaAJugar).getTipoAccion().getFirst() == TipoAccion.MAPA) {
                        Carta destino = tablero.getCarta(casillero.posX(), casillero.posY());
                        if (destino instanceof CartaDestino) {
                            javax.swing.Timer timer = new javax.swing.Timer(3000, e -> {
                                ((CartaDestino) destino).girar();
                                dibujarTablero();
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }
                    }
                }


            }

        }
        // reseteo todo despues de jugar la carta
        panelJugador.resetCartaSeleccionada();
        panelJugador.revalidate();
        panelJugador.repaint();
        panelJugador.dibujarManoDeCartas();
        dibujarTablero();

        // despues de actualizar todo en el jugador, paso el turno
        controlador.pasarTurno();
    }

    public void dibujarTablero() {

        // elimino lo existente
        removeAll();

        List<Casillero> listaCasilleros = new ArrayList<>();
        for (int i = 0; i < tablero.getAlto(); i++) {
            for (int j = 0; j < tablero.getAncho(); j++) {

                // recorro mi tablero inicializado
                Carta carta = tablero.getCarta(i, j);

                // creo un jlabel por cada carta
                Casillero casillero = new Casillero(i, j);
                listaCasilleros.add(casillero);
                casillero.setPreferredSize(new Dimension(ANCHO_CASILLERO, ALTO_CASILLERO));
                casillero.setBorder(BorderFactory.createDashedBorder(Color.BLACK));

                // si la carta no esta vacia (inicio o destinos)
                if (carta != null) {
                    // cargo y escalo la imagen
                    if (carta instanceof CartaDestino) {
                        ImageIcon icono = new ImageIcon(carta.getClass().getResource("/" + ((CartaDestino) carta).getImg()));
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

        for (Casillero c : listaCasilleros) {
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    casilleroEsPresionado(c);
                }
            });
        }
    }

}