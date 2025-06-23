package Vista;

import Controlador.ControladorJuego;
import Modelo.Jugador;

import java.rmi.RemoteException;


public class VistaGrafica implements IVistaGrafica{
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJuego ventanaJuego;
    private ControladorJuego controlador;

    public VistaGrafica(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.controlador.setVistaGrafica(this);
        this.ventanaInicioSesion = new VentanaInicioSesion();

        // agrego jugadores
        this.ventanaInicioSesion.onClickEntrar(e ->
        {
            controlador.conectarUsuario(ventanaInicioSesion.getNombreJugador(), ventanaInicioSesion.getEdadJugador());
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
        this.ventanaJuego = new VentanaJuego(controlador);
    }
}
