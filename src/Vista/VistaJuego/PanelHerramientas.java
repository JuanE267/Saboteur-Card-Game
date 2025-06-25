package Vista.VistaJuego;

import Controlador.ControladorJuego;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.IJugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;
import java.net.URL;

public class PanelHerramientas extends JPanel {

    private PanelJugador panelJugador;
    private ControladorJuego controlador;
    private IJugador jugadorCliente;

    public PanelHerramientas(PanelJugador panelJugador, ControladorJuego controlador) throws RemoteException {
        this.panelJugador = panelJugador;
        this.controlador = controlador;
        this.jugadorCliente = controlador.getJugadorActualizado();

        setBackground(Color.decode("#4b3e2c"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        dibujarHerramientas(jugadorCliente);
    }

    public void dibujarHerramientas(IJugador jugadorCliente) {
        removeAll();

        JLabel pico = new JLabel();
        JLabel vagoneta = new JLabel();
        JLabel linterna = new JLabel();

        pico.setSize(new Dimension(70, 70));
        vagoneta.setSize(new Dimension(70, 70));
        linterna.setSize(new Dimension(70, 70));

        String picoSano = "herramientas/PICO SANO.png";
        String vagonetaSana = "herramientas/VAGONETA SANA.png";
        String linternaSana = "herramientas/LINTERNA SANA.png";

        String picoRoto = "herramientas/PICO ROTO.png";
        String vagonetaRota = "herramientas/VAGONETA ROTA.png";
        String linternaRota = "herramientas/LINTERNA ROTA.png";

        List<Herramienta> herramientasRotas = jugadorCliente.getHerramientasRotas();


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

        add(pico);
        add(vagoneta);
        add(linterna);
    }


    private void herramientaEsPresionada(JLabel herr) throws RemoteException {

        int posCarta = panelJugador.getCartaSeleccionada();
        if (posCarta != -1) {
            TipoAccion tipoAccion = controlador.jugarHerramienta(posCarta, jugadorCliente.getId());

            if (tipoAccion != null) {
                if (tipoAccion.toString().startsWith("ROMPER")) {
                    JOptionPane.showMessageDialog(this, "No podes romper tu propia herramienta!");
                } else if (controlador.jugarHerramienta(posCarta, jugadorCliente.getId()).toString().startsWith("REPARAR")) {
                    JOptionPane.showMessageDialog(this, "La herramienta ya esta sana!");
                }
            } else if (tipoAccion == null){
                JOptionPane.showMessageDialog(this, "No podes arreglar esta herramienta con tu carta");
            }else{
                panelJugador.resetCartaSeleccionada();
                panelJugador.revalidate();
                panelJugador.repaint();
                controlador.pasarTurno();

            }
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


    public void mensajeNoEsTuTurno() {
        JOptionPane.showMessageDialog(getRootPane(), "No es tu turno!");
    }

    public void actualizar(IJugador jugadorCliente) throws RemoteException {
        this.jugadorCliente = jugadorCliente;
        dibujarHerramientas(jugadorCliente);
        revalidate();
        repaint();
    }
}
