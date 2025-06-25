package Modelo;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public interface IJuego extends IObservableRemoto {
    void iniciarPartida() throws RemoteException;
    void iniciarPartidaCargadaDesdeServidor() throws RemoteException;
    void iniciarPartidaCargadaDesdeCliente(String nombreCliente) throws RemoteException;

        IJugador agregarJugador(String nombre, int edad) throws RemoteException;

    void asignoPrimerTurno(int ronda) throws RemoteException;

    Tablero getTablero() throws RemoteException;

    void asignarRoles() throws RemoteException;

    void pasarTurno() throws RemoteException;

    void finalizarRonda(boolean ganaronLosMineros) throws RemoteException;

    Boolean jugarCarta(int x, int y, int posCarta, IJugador objetivo) throws RemoteException;

    boolean jugarHerramienta(IJugador objetivo, int posCarta) throws RemoteException;

    void tomarCartaDeMazo() throws RemoteException;

    Mazo getMazo() throws RemoteException;

    IJugador[] getJugadores() throws RemoteException;

    IJugador getJugadorActual() throws RemoteException;

    void reiniciarRonda(int ronda) throws RemoteException;

    boolean hayCaminoHastaOro() throws RemoteException;

     int getTurnoActual() throws RemoteException;

    boolean noHayCartas() throws RemoteException;

    void verificarSiTerminoLaRonda() throws RemoteException;

    void descartarCarta(int posCarta) throws RemoteException;

    IJugador getGanador() throws RemoteException;

    IJugador getJugadorPorId(int id) throws RemoteException;

    int getRonda() throws RemoteException;

    // PERSISTENCIA DE PARTIDAS

    boolean cargarPartida() throws RemoteException;

    boolean guardarPartida() throws RemoteException;
}
