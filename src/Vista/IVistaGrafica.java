package Vista;

import Controlador.ControladorJuego;

import java.rmi.RemoteException;

public interface IVistaGrafica {

    void actualizar() throws RemoteException;

    void iniciar();

    VentanaJuego getVentanaJuego();

    VentanaJuego iniciarVentanaJuego() throws RemoteException;

    void mostrarPartida() throws RemoteException;

}
