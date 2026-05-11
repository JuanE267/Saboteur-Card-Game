package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.IJugador;
import Modelo.Tablero;
import Vista.VistaJuego.VentanaJuego;

import javax.swing.*;
import java.rmi.RemoteException;


public class VistaGrafica implements IVistaGrafica {
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJuego ventanaJuego;
    private ControladorJuego controlador;
    private Lobby lobby;


    public VistaGrafica(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.controlador.setVistaGrafica(this);
        this.ventanaInicioSesion = new VentanaInicioSesion();
        this.lobby = new Lobby(controlador);

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
            lobby.iniciar();

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

    public VentanaJuego getVentanaJuego() {
        return this.ventanaJuego;
    }

    @Override
    public void mostrarPartida() throws RemoteException {
        // mostrar todos los elementos de la partida
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

    public VentanaJuego iniciarVentanaJuego() throws RemoteException {
        return ventanaJuego = new VentanaJuego(controlador);
    }
}
