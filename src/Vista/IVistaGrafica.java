package Vista;

import Modelo.IJugador;
import Modelo.Tablero;

import java.rmi.RemoteException;

public interface IVistaGrafica {

    void actualizar(Tablero tablero, IJugador[] jugadores) throws RemoteException;

    void iniciar();

    VentanaJuego getVentanaJuego();

    VentanaJuego iniciarVentanaJuego() throws RemoteException;

    void mostrarPartida() throws RemoteException;

}
