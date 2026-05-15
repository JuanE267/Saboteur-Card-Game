package Vista;

import Controlador.ControladorJuego;
import Modelo.IJugador;
import Modelo.Tablero;
import Vista.VistaJuego.VentanaJuego;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;


public class VistaGrafica implements IVistaGrafica {
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJuego ventanaJuego;
    private JFrame ventanaEspera;
    private ControladorJuego controlador;


    public VistaGrafica(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.ventanaInicioSesion = new VentanaInicioSesion();

        // agrego jugadores
        this.ventanaInicioSesion.onClickEntrar(e -> {
            String nombre = ventanaInicioSesion.getNombreJugador();
            int edad = ventanaInicioSesion.getEdadJugador();
            if(nombre == null || nombre.isEmpty() || edad <= 0){
                JOptionPane.showMessageDialog(null,"Por favor, ingrese un nombre/edad valido", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            IJugador jugador = controlador.conectarUsuario(nombre, edad);
            controlador.setJugadorCliente(jugador);
            ocultarInicioSesion();
            if(controlador.esHost()){
                VentanaServidor ventanaServidor = new VentanaServidor(controlador);
                controlador.setVistaServidor(ventanaServidor);
                ventanaServidor.iniciar();
                try {
                    ventanaServidor.actualizarListaJugadores(controlador.getJugadores());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }else{
                mostrarPantallaEspera();
            }

        });


    }

    public void iniciar() {
        this.mostrarInicioSesion();
    }


    private void mostrarInicioSesion() {
        this.ventanaInicioSesion.setVisible(true);
    }

    private void ocultarInicioSesion() {
        this.ventanaInicioSesion.setVisible(false);
    }

    public void mostrarPantallaEspera() {
        ventanaEspera = new JFrame("Saboteur");
        ventanaEspera.setSize(400, 200);
        ventanaEspera.setLocationRelativeTo(null);
        ventanaEspera.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        ventanaEspera.setLayout(new BorderLayout());

        JLabel mensaje = new JLabel(
                "Esperando que el host inicie la partida...",
                SwingConstants.CENTER
        );
        mensaje.setFont(new Font("Arial", Font.BOLD, 16));

        JProgressBar barra = new JProgressBar();
        barra.setIndeterminate(true); // animación de carga

        ventanaEspera.add(mensaje, BorderLayout.CENTER);
        ventanaEspera.add(barra, BorderLayout.SOUTH);
        ventanaEspera.setVisible(true);
    }


    @Override
    public void mostrarPartida() throws RemoteException {
        // mostrar todos los elementos de la partida
        if(ventanaEspera != null){
            ventanaEspera.dispose();
            ventanaEspera = null;
        }
        iniciarVentanaJuego();
    }

    @Override
    public void ocultarPartida() {
        ventanaJuego.setVisible(false);
    }


    public void actualizar(Tablero tablero, IJugador[] jugadores) throws RemoteException {
        IJugador actualizado = controlador.getJugadorActualizado();
        if (actualizado != null) {
            ventanaJuego.getPanelJugador().actualizar(actualizado);
            ventanaJuego.getPanelHerramientas().actualizar(actualizado);
            ventanaJuego.getPanelPuntosYAcciones().actualizar(actualizado);
            ventanaJuego.getPanelTablaJugadores().actualizar(jugadores, actualizado);
            ventanaJuego.getPanelTablero().actualizar(tablero, actualizado);
            ventanaJuego.getPanelTomarCarta().actualizar();
            ventanaJuego.actualizarVentana();

        }
    }

    private VentanaJuego iniciarVentanaJuego() throws RemoteException {
        return ventanaJuego = new VentanaJuego(controlador);
    }
}
