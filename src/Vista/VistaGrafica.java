package Vista;

import Controlador.ControladorJuego;
import Modelo.IJugador;
import Modelo.Jugador;

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

    public void actualizar() throws RemoteException {
        IJugador actualizado = controlador.getJugadorActualizado();
        if (actualizado != null) {
            VentanaJuego ventana = iniciarVentanaJuego();
            ventana.getPanelJugador().actualizar(actualizado);
            ventana.getPanelHerramientas().actualizar(actualizado);
            ventana.getPanelPuntosYAcciones().actualizar(actualizado);
            ventana.getPanelTablaJugadores().actualizar(controlador.getJugadores());
            ventana.getPanelTablero().actualizar(controlador.getTablero());
            ventana.actualizarTurno();

        }
    }

    public VentanaJuego iniciarVentanaJuego() throws RemoteException {
        return ventanaJuego = new VentanaJuego(controlador);
    }
}
