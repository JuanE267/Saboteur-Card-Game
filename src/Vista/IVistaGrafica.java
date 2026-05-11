package Vista;

import Modelo.Enums.Evento;
import Modelo.IJugador;
import Modelo.Tablero;
import Vista.VistaJuego.VentanaJuego;

import java.rmi.RemoteException;

public interface IVistaGrafica {

    void actualizar(Tablero tablero, IJugador[] jugadores) throws RemoteException;

    VentanaJuego getVentanaJuego();

    VentanaJuego iniciarVentanaJuego() throws RemoteException;

    void mostrarPartida() throws RemoteException;

    void ocultarPartida();
}
