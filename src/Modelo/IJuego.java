package Modelo;

import Modelo.Cartas.Carta;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.List;

public interface IJuego extends IObservableRemoto {
     void iniciarPartida() throws RemoteException;

     IJugador agregarJugador(String nombre, int edad) throws RemoteException;

     void asignoPrimerTurno(int ronda) throws RemoteException;

     Tablero getTablero()throws RemoteException;

     void asignarRoles()throws RemoteException;

     void pasarTurno() throws RemoteException;

     void finalizarRonda(boolean ganaronLosMineros) throws RemoteException;

     Boolean jugarCarta(int x, int y, int posCarta, IJugador objetivo) throws RemoteException;

     void jugarHerramienta(IJugador objetivo, Carta carta) throws RemoteException;

     void tomarCartaDeMazo() throws RemoteException;

     Mazo getMazo()throws RemoteException;

     IJugador[] getJugadores()throws RemoteException;

     IJugador getJugadorActual()throws RemoteException;

     void reiniciarRonda(int ronda)throws RemoteException;

     boolean hayCaminoHastaOro()throws RemoteException;

     boolean noHayCartas()throws RemoteException;

     void verificarSiTerminoLaRonda() throws RemoteException;

     void descartarCarta(Carta carta) throws RemoteException;

     String getGanador()throws RemoteException;

}
