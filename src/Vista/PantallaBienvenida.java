package Vista;

import Controlador.Controlador;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class PantallaBienvenida extends JFrame {
    private final Controlador controlador;
    ArrayList<String> ips = Util.getIpDisponibles();
    private final Image fondo;


    public PantallaBienvenida(Controlador controlador) {

        this.controlador = controlador;

        setTitle("Saboteur");
        setSize(1792, 1024);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImageIcon iconoFondo = new ImageIcon(
                getClass().getResource("/INICIO/fondo-inicio.png")
        );
        fondo = iconoFondo.getImage();

        JPanel panel = new JPanel(new GridBagLayout()) {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                g.drawImage(
                        fondo,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JButton btnCrear = new JButton("Crear partida");
        btnCrear.setPreferredSize(new Dimension(160, 50));
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(btnCrear, gbc);

        JButton btnUnirse = new JButton("Unirse a partida");
        btnUnirse.setPreferredSize(new Dimension(160, 50));
        gbc.gridy = 1;
        panel.add(btnUnirse, gbc);

        JButton btnReglas = new JButton("Reglas");
        btnReglas.setPreferredSize(new Dimension(160, 50));
        gbc.gridy = 2;
        panel.add(btnReglas, gbc);

        btnCrear.addActionListener(e -> {
            try {
                pedirDatosParaCrear();
            } catch (RMIMVCException ex) {
                throw new RuntimeException(ex);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnUnirse.addActionListener(e -> {
            try {
                pedirDatosParaUnirse();
            } catch (RMIMVCException | RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error al unirse a la partida"
                        + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReglas.addActionListener(e -> {
            URL url = getClass().getResource("/Reglas/reglas-saboteur.pdf");
            if (url != null) {
                try {
                    Desktop.getDesktop().open(new File(url.toURI()));
                } catch (IOException | URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el archivo de reglas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
        setVisible(true);

    }

    private void pedirDatosParaCrear() throws RMIMVCException, RemoteException {

        // Seleccion de ipServidor servidor

        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchara peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );

        if (ipServidor == null || ipServidor.isBlank()) return;


        String puertoCliente = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        if (puertoCliente == null || puertoCliente.isBlank()) return;


        try {
            setVisible(false);
            controlador.crearPartida(ipServidor, Integer.parseInt(puertoCliente));

            VistaGrafica vista = new VistaGrafica(controlador);
            controlador.setVistaGrafica(vista);
            vista.iniciar();
        } catch (RemoteException | RMIMVCException ex) {
            setVisible(true);
            JOptionPane.showMessageDialog(this,
                    "Error al crear la partida:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void pedirDatosParaUnirse() throws RMIMVCException, RemoteException {

        // Seleccion de ipServidor
        String ipServidor = JOptionPane.showInputDialog(
                null,
                "Ingrese la IP del servidor (PC donde se creó la partida)",
                "127.0.0.1"
        );

        if (ipServidor == null || ipServidor.isBlank()) return;

        // Seleccion ip cliente
        String ipCliente = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione su propia IP (la de esta máquina)", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),   // muestra las IPs disponibles en esta máquina
                null
        );

        if (ipCliente == null || ipCliente.isBlank()) return;

        // Seleccion puerto cliente
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        if (port == null || port.isBlank()) return;

        try {
            setVisible(false);
            controlador.unirseAPartida(ipServidor, ipCliente, Integer.parseInt(port));

            VistaGrafica vista = new VistaGrafica(controlador);
            controlador.setVistaGrafica(vista);
            vista.iniciar();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de conexión con el servidor.\nVerificá que el servidor esté corriendo y la IP sea correcta.\n\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (RMIMVCException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al iniciar el cliente.\n\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


}
