package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.IJugador;
import Modelo.Tablero;
import Vista.VistaJuego.VentanaJuego;

import java.rmi.RemoteException;


public class VistaGrafica implements IVistaGrafica {
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJuego ventanaJuego;
    private ControladorJuego controlador;

    public VistaGrafica(ControladorJuego controlador) throws RemoteException {
        this.controlador = controlador;
        this.controlador.setVistaGrafica(this);
        this.ventanaInicioSesion = new VentanaInicioSesion();

        // agrego jugadores
        this.ventanaInicioSesion.onClickEntrar(e -> {
            IJugador jugador = controlador.conectarUsuario(ventanaInicioSesion.getNombreJugador(), ventanaInicioSesion.getEdadJugador());
            controlador.setJugadorCliente(jugador);
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

    public VentanaJuego getVentanaJuego() {
        return this.ventanaJuego;
    }

    @Override
    public void mostrarPartida() throws RemoteException {
        // mostrar todos los elementos de la partida
        iniciarVentanaJuego();
    }


    @Override
    public void avisarGanadores(IJugador[] jugadores, Evento evento, IJugador ganador, int ronda) throws RemoteException {
        ventanaJuego.avisarGanadores(jugadores, evento, ganador, ronda);
    }


    public void actualizar(Tablero tablero, IJugador[] jugadores) throws RemoteException {
        IJugador actualizado = controlador.getJugadorActualizado();
        if (actualizado != null) {
            ventanaJuego.getPanelJugador().actualizar(actualizado);
            ventanaJuego.getPanelHerramientas().actualizar(actualizado);
            ventanaJuego.getPanelPuntosYAcciones().actualizar(actualizado);
            ventanaJuego.getPanelTablaJugadores().actualizar(jugadores, actualizado);
            ventanaJuego.getPanelTablero().actualizar(tablero, actualizado);
            ventanaJuego.actualizarVentana();

        }
    }

    public VentanaJuego iniciarVentanaJuego() throws RemoteException {
        return ventanaJuego = new VentanaJuego(controlador);
    }
}
