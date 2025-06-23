package Vista;

import java.rmi.RemoteException;

public interface IVistaGrafica {
     void iniciar();
     VentanaJuego getVentanaJuego();

     void iniciarVentanaJuego() throws RemoteException;

}
