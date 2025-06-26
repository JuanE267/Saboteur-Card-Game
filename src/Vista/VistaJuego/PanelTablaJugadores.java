package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Cartas.Carta;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.Enums.TipoCarta;
import Modelo.IJugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

public class PanelTablaJugadores extends JPanel {

    private IJugador jugadorCliente;
    private IJugador[] jugadores;
    private ControladorJuego controlador;
    private PanelJugador panelJugador;

    public PanelTablaJugadores(PanelJugador panelJugador, ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.panelJugador = panelJugador;
        this.jugadores = controlador.getJugadores();
        this.jugadorCliente = controlador.getJugadorActualizado();

        setBackground(Color.decode("#4b3e2c"));
        setLayout(new GridLayout(jugadores.length, 1));
        setBorder(new EmptyBorder(200, 0, 200, 0));
        dibujarListaJugadores(jugadores, jugadorCliente);
    }

    private void dibujarListaJugadores(IJugador[] jugadores, IJugador jugadorCliente) throws RemoteException {

        removeAll();
        jugadores = controlador.getJugadores();
        for (IJugador j : jugadores) {
            if (j.getId() != jugadorCliente.getId()) {
                JPanel jugadorTabla = new JPanel();
                jugadorTabla.setLayout(new BoxLayout(jugadorTabla, BoxLayout.Y_AXIS));
                jugadorTabla.setBackground(Color.decode("#4b3e2c"));

                // nombre
                JLabel nombre = new JLabel(j.getNombre());
                nombre.setFont(new Font("Arial", Font.BOLD, 24));
                nombre.setAlignmentX(Component.CENTER_ALIGNMENT);
                nombre.setForeground(Color.white);

                // listado de herramientas
                JPanel herramientas = new JPanel();
                herramientas.setLayout(new FlowLayout());
                herramientas.setBackground(Color.decode("#4b3e2c"));

                // label para cada herr
                HerramientaTabla pico = new HerramientaTabla(j);
                HerramientaTabla vagoneta = new HerramientaTabla(j);
                HerramientaTabla linterna = new HerramientaTabla(j);

                pico.setSize(new Dimension(100, 100));
                vagoneta.setSize(new Dimension(100, 100));
                linterna.setSize(new Dimension(100, 100));

                String picoSano = "herramientas/PICO SANO.png";
                String vagonetaSana = "herramientas/VAGONETA SANA.png";
                String linternaSana = "herramientas/LINTERNA SANA.png";

                String picoRoto = "herramientas/PICO ROTO.png";
                String vagonetaRota = "herramientas/VAGONETA ROTA.png";
                String linternaRota = "herramientas/LINTERNA ROTA.png";

                List<Herramienta> herramientasRotas = j.getHerramientasRotas();

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
                        try {
                            if (controlador.esTurnoDe(jugadorCliente)) {
                                super.mouseClicked(e);
                                try {
                                    herramientaEsPresionada(pico);
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

                vagoneta.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        try {
                            if (controlador.esTurnoDe(jugadorCliente)) {
                                super.mouseClicked(e);
                                try {
                                    herramientaEsPresionada(vagoneta);
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

                linterna.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        try {
                            if (controlador.esTurnoDe(jugadorCliente)) {
                                super.mouseClicked(e);
                                try {
                                    herramientaEsPresionada(linterna);
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

                setImagen(pico, urlPico);
                setImagen(vagoneta, urlVagoneta);
                setImagen(linterna, urlLinterna);

                herramientas.add(pico);
                herramientas.add(vagoneta);
                herramientas.add(linterna);

                //----
                jugadorTabla.add(nombre);
                jugadorTabla.add(herramientas);
                add(jugadorTabla);
            }
        }
    }


    private void herramientaEsPresionada(HerramientaTabla herr) throws RemoteException {

        int posCarta = panelJugador.getCartaSeleccionada();
        if (posCarta != -1) {

            jugadorCliente = controlador.getJugadorActualizado();
            Carta carta = jugadorCliente.elegirCarta(posCarta);
            if (carta.getTipo() == TipoCarta.ACCION) {
                TipoAccion tipoAccion = controlador.jugarHerramienta(posCarta, herr.getDueño().getId());

                if (tipoAccion != null) {
                    if (tipoAccion.toString().startsWith("OBJETIVO_ROMPER")) {
                        JOptionPane.showMessageDialog(this, "La herramienta ya esta rota!");
                    } else if (tipoAccion.toString().startsWith("OBJETIVO_REPARAR")) {
                        JOptionPane.showMessageDialog(this, "La herramienta ya esta sana!");
                    } else {
                        panelJugador.resetCartaSeleccionada();
                        panelJugador.revalidate();
                        panelJugador.repaint();
                        controlador.pasarTurno();
                    }
                } else if (tipoAccion == null) {
                    JOptionPane.showMessageDialog(this, "No podes arreglar esta herramienta con tu carta");
                }
            }

        }
    }


    private void setImagen(HerramientaTabla herr, URL url) {
        if (url != null) {
            ImageIcon icono = new ImageIcon(url);
            Image imagen = icono.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            herr.setIcon(new ImageIcon(imagen));
        } else {
            System.err.println("Imagen no encontrada: " + url);
        }
    }


    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(getRootPane(), "No es tu turno!");
    }

    public void actualizar(IJugador[] jugadores, IJugador jugadorCliente) throws RemoteException {
        this.jugadores = jugadores;
        this.jugadorCliente = jugadorCliente;
        dibujarListaJugadores(jugadores, jugadorCliente);
        revalidate();
        repaint();
    }

}


