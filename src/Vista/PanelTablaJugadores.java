package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Herramienta;
import Modelo.Juego;
import Modelo.Jugador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

public class PanelTablaJugadores extends JPanel  {

    private ControladorJuego controlador;
    private PanelJugador panelJugador;
    private Juego juego;
    private int cantidadJugadores;

    public PanelTablaJugadores(PanelJugador panelJugador, ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.panelJugador = panelJugador;
        setLayout(new GridLayout(cantidadJugadores, 1));
        setBorder(new EmptyBorder(200, 100, 200, 0));
        dibujarListaJugadores();
    }

    private void dibujarListaJugadores() throws RemoteException {

        removeAll();
        cantidadJugadores = controlador.getJugadores().length;
        for (Jugador j : controlador.getJugadores()) {
            if(j != panelJugador.getJugadorCliente()) {
                JPanel jugadorTabla = new JPanel();
                jugadorTabla.setLayout(new GridLayout(2, 1));
                jugadorTabla.setBackground(Color.WHITE);

                // nombre
                JLabel nombre = new JLabel(j.getNombre());
                nombre.setFont(new Font("Arial", Font.BOLD, 20));
                nombre.setAlignmentX(Component.CENTER_ALIGNMENT);

                // listado de herramientas
                JPanel herramientas = new JPanel();
                herramientas.setLayout(new FlowLayout());

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
                            if (controlador.esTurnoDe(panelJugador.getJugadorCliente())) {
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
                            if (controlador.esTurnoDe(panelJugador.getJugadorCliente())) {
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
                            if (controlador.esTurnoDe(panelJugador.getJugadorCliente())) {
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
        controlador.jugarHerramienta(posCarta, herr.getDueño());

        panelJugador.resetCartaSeleccionada();
        panelJugador.revalidate();
        panelJugador.repaint();
        controlador.verificarSiTerminoLaRonda();
        controlador.pasarTurno();
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
        JOptionPane.showMessageDialog(this, "No es tu turno!");
    }

    public void actualizar() throws RemoteException {
        dibujarListaJugadores();
        revalidate();
        repaint();
    }
}
