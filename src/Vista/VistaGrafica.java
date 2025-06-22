package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.Jugador;

import javax.swing.*;
import java.rmi.RemoteException;


public class VistaGrafica {
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJuego ventanaJuego;
    private ControladorJuego controlador;
    private Jugador jugadorCliente;

    public VistaGrafica(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.controlador.setVistaGrafica(this);
        this.ventanaInicioSesion = new VentanaInicioSesion();

        // agrego jugadores
        this.ventanaInicioSesion.onClickEntrar(e ->
        {
            this.jugadorCliente = controlador.conectarUsuario(ventanaInicioSesion.getNombreJugador(), ventanaInicioSesion.getEdadJugador());
            ocultarInicioSesion();
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

    public VentanaJuego getVentanaJuego(){
        return this.ventanaJuego;
    }

    public void iniciarVentanaJuego() throws RemoteException {
        this.ventanaJuego = new VentanaJuego(controlador, jugadorCliente);
    }
}
