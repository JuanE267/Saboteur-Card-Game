package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.Jugador;

import java.rmi.RemoteException;


public class VistaGrafica {
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJuego ventanaJuego;
    private ControladorJuego controlador;

    public VistaGrafica(ControladorJuego controlador) {
        this.controlador = controlador;
        this.controlador.setVistaGrafica(this);
        this.ventanaInicioSesion = new VentanaInicioSesion();


        // agrego jugadores
        this.ventanaInicioSesion.onClickEntrar(e ->
        {
            controlador.conectarUsuario(ventanaInicioSesion.getNombreJugador(), ventanaInicioSesion.getEdadJugador());
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

    private void mostrarPartida() {
        this.ventanaJuego.setVisible(true);
        this.ventanaInicioSesion.setVisible(false);
    }

    public void actualizar(Evento evento) throws RemoteException {
        switch (evento) {
            case NUEVA_RONDA -> ventanaJuego.actualizarVentana();
            case PASAR_TURNO -> ventanaJuego.actualizarTurno();
            case TOMAR_CARTA, DESCARTAR_CARTA -> ventanaJuego.getPanelJugador().actualizar();
            case ACTUALIZAR_HERRAMIENTAS -> {
                ventanaJuego.getPanelHerramientas().actualizar();
                ventanaJuego.getPanelTablaJugadores().actualizar();
            }
            case JUGAR_CARTA_TABLERO -> {
                ventanaJuego.getPanelTablero().actualizar();
                ventanaJuego.getPanelJugador().actualizar();
            }
            case INICIAR_PARTIDA -> {
                this.ventanaJuego = new VentanaJuego(controlador);
                mostrarPartida();
            }
            case FINALIZAR_PARTIDA -> ventanaJuego.mostrarGanador();
        }
    }
}
