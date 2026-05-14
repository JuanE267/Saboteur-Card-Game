package Vista;

import Controlador.ControladorJuego;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class PantallaBienvenida extends JFrame {
    private ControladorJuego controlador;
    ArrayList<String> ips = Util.getIpDisponibles();
    private Image fondo;


    public PantallaBienvenida(ControladorJuego controlador) {

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
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(btnCrear, gbc);

        JButton btnUnirse = new JButton("Unirse a partida");
        btnUnirse.setPreferredSize(new Dimension(160, 50));
        gbc.gridx = 1;
        panel.add(btnUnirse, gbc);

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

        if(ipServidor == null || ipServidor.isBlank()) return;


        String puertoCliente = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        if(puertoCliente == null || puertoCliente.isBlank()) return;


//        try {
//            Registry registry = LocateRegistry.getRegistry(ipServidor, PORTSERVIDOR);
//            String[] servicios = registry.list(); // lanza excepción si no hay nada
//            if (servicios != null && servicios.length > 0) {
//                JOptionPane.showMessageDialog(this,
//                        "Ya existe una partida en esa IP. Usá 'Unirse a partida'.",
//                        "Partida existente", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//        } catch (Exception e) {
//            // No hay registro RMI en esa IP/puerto → no existe servidor → continuar
//        }

        try {
            setVisible(false);
            controlador.crearPartida(ipServidor, Integer.parseInt(puertoCliente));

            VistaGrafica vista = new VistaGrafica(controlador);
            controlador.setVistaGrafica(vista);
            vista.iniciar();
        }catch (RemoteException | RMIMVCException ex) {
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

        if(ipServidor == null || ipServidor.isBlank()) return;

        // Seleccion ip cliente
        String ipCliente = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione su propia IP (la de esta máquina)", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),   // muestra las IPs disponibles en esta máquina
                null
        );

        if(ipCliente == null || ipCliente.isBlank()) return;

        // Seleccion puerto cliente
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        if(port == null || port.isBlank()) return;

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
