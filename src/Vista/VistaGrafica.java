package Vista;

import Controlador.ControladorJuego;
import Modelo.Enums.Evento;
import Modelo.Jugador;
import jdk.jfr.Event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


public class VistaGrafica {
    private VentanaInicioSesion ventanaInicioSesion;
    private VentanaJugadores ventanaJugadores;
    private VentanaJuego ventanaJuego;
    private ControladorJuego controlador;

    public VistaGrafica(ControladorJuego controlador) {
        this.controlador = controlador;
        this.controlador.setVista(this);
        this.ventanaInicioSesion = new VentanaInicioSesion();
        this.ventanaJugadores = new VentanaJugadores();


        // agrego jugadores
        this.ventanaInicioSesion.onClickEntrar(e ->
                controlador.conectarUsuario(ventanaInicioSesion.getNombreJugador(), ventanaInicioSesion.getEdadJugador()));

        // inicio la partida
        this.ventanaJugadores.onClickIniciarPartida(e -> {
            try {
                controlador.iniciarPartida();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });


    }

    public void iniciar() {
        this.mostrarInicioSesion();
    }


    private void mostrarInicioSesion() {
        this.ventanaInicioSesion.setVisible(true);
        this.ventanaJugadores.setVisible(true);
    }

    private void mostrarPartida() {
        this.ventanaJuego.setVisible(true);
        this.ventanaInicioSesion.setVisible(false);
        this.ventanaJugadores.setVisible(false);
    }

    public void mostrarListaJugadores(Jugador[] jugadores) {
        this.ventanaJugadores.actualizarListaJugadores(jugadores);
    }

    public void actualizar(Evento evento) {
        switch (evento) {
            case NUEVA_RONDA -> ventanaJuego.actualizarVentana();
            case PASAR_TURNO -> {
                ventanaJuego.actualizarTurno();
            }
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
                this.ventanaJuego = ventanaJuego.inicializarVentana();
                mostrarPartida();
            }
        }
    }

}
