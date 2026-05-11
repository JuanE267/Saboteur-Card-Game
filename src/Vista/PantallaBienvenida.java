package Vista;

import Controlador.ControladorJuego;
import Modelo.IJuego;
import Modelo.Juego;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class PantallaBienvenida extends JFrame {
    private static final int PORTSERVIDOR = 8888;
    ArrayList<String> ips = Util.getIpDisponibles();

    private Image fondo;


    public PantallaBienvenida() {
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

        btnCrear.addActionListener(e -> crearPartida());
        btnUnirse.addActionListener(e -> {
            try {
                unirsePartida();
            } catch (RMIMVCException | RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error al unirse a la partida"
                        + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel);
        setVisible(true);

    }

    private void crearPartida() {

        // Seleccion de ipServidor servidor

        String ipServidor = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchara peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );

        try {
            Registry registry = LocateRegistry.getRegistry(ipServidor, PORTSERVIDOR);
            String[] servicios = registry.list(); // lanza excepción si no hay nada
            if (servicios != null && servicios.length > 0) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe una partida en esa IP. Usá 'Unirse a partida'.",
                        "Partida existente", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception e) {
            // No hay registro RMI en esa IP/puerto → no existe servidor → continuar
        }

        // Creo el servidor

        IJuego modelo = new Juego();

        // Seleccion puerto cliente
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        Servidor servidor = new Servidor(ipServidor, PORTSERVIDOR);


        try {
            servidor.iniciar(modelo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Servidor corriendo en ipServidor: " + ipServidor + " port: " + port);

        // Creo el cliente y lo conecto al servidor

        ControladorJuego controlador = new ControladorJuego();
        Cliente c = new Cliente(ipServidor, Integer.parseInt(port), ipServidor, PORTSERVIDOR);

        try {
            c.iniciar(controlador);
            controlador.setEsHost();
            VistaGrafica vistaGrafica = new VistaGrafica(controlador);
            vistaGrafica.iniciar();
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
    setVisible(false);
    }

    private void unirsePartida() throws RMIMVCException, RemoteException {

        // Seleccion de ipServidor servidor
        String ipServidor = JOptionPane.showInputDialog(
                null,
                "Ingrese la IP del servidor (PC donde se creó la partida)",
                "192.168.1.33"  // valor por defecto como sugerencia
        );

        // IP del CLIENTE (la propia máquina Linux que se está conectando)
        String ipCliente = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione su propia IP (la de esta máquina)", "IP del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),   // muestra las IPs disponibles en esta máquina
                null
        );

        // Seleccion puerto cliente
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        ControladorJuego controlador = new ControladorJuego();
        Cliente c = new Cliente(ipCliente, Integer.parseInt(port), ipServidor, PORTSERVIDOR);

        try {
            c.iniciar(controlador);
            VistaGrafica vistaGrafica = new VistaGrafica(controlador);
            vistaGrafica.iniciar();
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
        setVisible(false);
    }


}
