package Vista;

import Modelo.IJugador;
import Modelo.Tablero;

import java.rmi.RemoteException;

public interface IVistaGrafica {

    void actualizar(Tablero tablero, IJugador[] jugadores) throws RemoteException;

    void iniciar();

    void mostrarPartida() throws RemoteException;

    void ocultarPartida();
}
