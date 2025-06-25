package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.TipoAccion;
import Modelo.IJugador;
import Modelo.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class PanelTablero extends JPanel {

    private ControladorJuego controlador;
    private PanelJugador panelJugador;
    private Tablero tablero;
    List<Casillero> listaCasilleros = new ArrayList<>();
    private final int ANCHO_CASILLERO = 65;
    private final int ALTO_CASILLERO = 100;
    private IJugador jugadorCliente;

    public PanelTablero(PanelJugador jugador, ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.panelJugador = jugador;
        this.jugadorCliente = controlador.getJugadorActualizado();
        this.tablero = controlador.getTablero();

        setBackground(Color.decode("#4b3e2c"));
        setLayout(new GridLayout(tablero.getAlto(), tablero.getAncho()));
        setPreferredSize(new Dimension(tablero.getAncho(), tablero.getAlto()));

        dibujarTablero(tablero);
    }

    public void dibujarTablero(Tablero tablero) {
        this.removeAll();
        setLayout(new GridLayout(tablero.getAlto(), tablero.getAncho()));
        for (int i = 0; i < tablero.getAlto(); i++) {
            for (int j = 0; j < tablero.getAncho(); j++) {

                Casillero casillero = new Casillero(i, j);
                casillero.setPreferredSize(new Dimension(ANCHO_CASILLERO, ALTO_CASILLERO));
                casillero.setBorder(BorderFactory.createDashedBorder(new Color(212, 210, 210, 128)));
                setImagenCasillero(casillero);
                listaCasilleros.add(casillero);
                add(casillero);
            }
        }

        for (Casillero c : listaCasilleros) {
            c.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (controlador.esTurnoDe(jugadorCliente)) {
                            super.mouseClicked(e);
                            try {
                                casilleroEsPresionado(c);
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            mensajeNoEsTuTurno();
                        }
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }

        revalidate();
        repaint();
    }

    private void casilleroEsPresionado(Casillero casillero) throws RemoteException {

        int cartaSeleccionada = panelJugador.getCartaSeleccionada();
        Boolean pudoSerJugado = false;


        // si es una carta dentro del mazo
        if (cartaSeleccionada != -1 && cartaSeleccionada >= 0) {
            Carta cartaAJugar = jugadorCliente.elegirCarta(cartaSeleccionada);
            //si es de tipo tunel
            if (cartaAJugar instanceof CartaTunel) {
                pudoSerJugado = controlador.jugarUnaCarta(casillero.posX(), casillero.posY(), cartaSeleccionada, null);
                if (!pudoSerJugado) {
                    if (jugadorCliente.puedeConstruir()) {
                        JOptionPane.showMessageDialog(this, "La carta no coincide con ninguno de los tuneles!!");
                    }else{
                        JOptionPane.showMessageDialog(this, "Tenes una herramienta rota, NO podes construir!!");
                    }
                }
            }
            //si es de tipo accion
            else if (cartaAJugar instanceof CartaAccion) {
                // juego la carta si es derrumbre o mapa

                if (((CartaAccion) cartaAJugar).getTipoAccion().getFirst() == TipoAccion.MAPA || ((CartaAccion) cartaAJugar).getTipoAccion().getFirst() == TipoAccion.DERRUMBAR) {
                    pudoSerJugado = controlador.jugarUnaCarta(casillero.posX(), casillero.posY(), cartaSeleccionada, null);
                    if (!pudoSerJugado) {
                        JOptionPane.showMessageDialog(this, "No puede jugar sobre esta carta!");
                    }
                    // si es el mapa muestro la carta destino dada vuelta
                    if (((CartaAccion) cartaAJugar).getTipoAccion().getFirst() == TipoAccion.MAPA && pudoSerJugado) {
                        Carta destino = tablero.getCarta(casillero.posX(), casillero.posY());
                        if (destino instanceof CartaDestino) {
                            //intento de girar (no funciona)
                            /*
                            javax.swing.Timer timer = new javax.swing.Timer(3000, e -> {
                                ((CartaDestino) destino).girar();
                                setImagenCasillero(casillero);
                                try {
                                    controlador.pasarTurno();
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }

                            });
                            paseTurnoDespuesDeGirar = true;
                            timer.setRepeats(false);
                            timer.start();
                            */
                            // guardo la imagen de la cara del destino
                            ImageIcon icono = new ImageIcon(destino.getClass().getResource("/" + ((CartaDestino) destino).getCara()));
                            // la escalo al tamaño de un casiller
                            icono = new ImageIcon(icono.getImage().getScaledInstance(ANCHO_CASILLERO, ALTO_CASILLERO, Image.SCALE_SMOOTH));
                            // la muestro
                            JOptionPane.showMessageDialog(this, new JLabel(icono));

                        }
                    }
                }


            }

        }
        // reseteo todo despues de jugar la carta
        panelJugador.resetCartaSeleccionada();

        // despues de actualizar todo en el jugador, paso el turno
        if (pudoSerJugado) {
            controlador.verificarSiTerminoLaRonda();
                controlador.pasarTurno();

        }
    }

    public void setImagenCasillero(Casillero casillero) {

        Carta carta = tablero.getCarta(casillero.posX(), casillero.posY());

        // si la carta no esta vacia (inicio o destinos)
        if (carta != null) {
            // cargo y escalo la imagen
            ImageIcon icono = new ImageIcon(carta.getClass().getResource("/" + carta.getImg()));
            casillero.setIcon(new ImageIcon(icono.getImage().getScaledInstance(ANCHO_CASILLERO, ALTO_CASILLERO, Image.SCALE_SMOOTH)));
        } else {
            casillero.setIcon(null);
        }

    }

    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(getRootPane(), "No es tu turno!");
    }

    public void actualizar(Tablero tablero, IJugador jugadorCliente) {
        this.jugadorCliente = jugadorCliente;
        this.tablero = tablero;
        dibujarTablero(tablero);
        revalidate();
        repaint();
    }
}