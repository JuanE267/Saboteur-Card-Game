package Modelo;

import Modelo.Cartas.Carta;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public interface IJuego extends IObservableRemoto {
     void iniciarPartida() throws RemoteException;

     Jugador agregarJugador(String nombre, int edad) throws RemoteException;

     void asignoPrimerTurno(int ronda) throws RemoteException;

     Tablero getTablero()throws RemoteException;

     void asignarRoles()throws RemoteException;

     void pasarTurno() throws RemoteException;

     void finalizarRonda(boolean ganaronLosMineros) throws RemoteException;

     Boolean jugarCarta(int x, int y, int posCarta, Jugador objetivo) throws RemoteException;

     void jugarHerramienta(Jugador objetivo, Carta carta) throws RemoteException;

     void tomarCartaDeMazo() throws RemoteException;

     Mazo getMazo()throws RemoteException;

     Jugador[] getJugadores()throws RemoteException;

     Jugador getJugadorActual()throws RemoteException;

     void reiniciarRonda(int ronda)throws RemoteException;

     boolean hayCaminoHastaOro()throws RemoteException;

     boolean noHayCartas()throws RemoteException;

     void verificarSiTerminoLaRonda() throws RemoteException;

     void descartarCarta(Carta carta) throws RemoteException;

     String getGanador()throws RemoteException;

}
