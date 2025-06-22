package Controlador;

import Modelo.*;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.TipoAccion;
import Vista.VentanaJuego;
import Vista.VentanaServidor;
import Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.awt.*;
import java.rmi.RemoteException;
public class ControladorJuego implements IControladorRemoto {

    private IJuego juego;
    private VistaGrafica vista;
    private VentanaServidor vistaServidor;
    private Jugador jugadorCliente;

    public <T extends IObservableRemoto> ControladorJuego(T juego) {
        try {
            this.setModeloRemoto(juego);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ControladorJuego() {

    }

    public Jugador conectarUsuario(String nombre, int edad) {
        try {
            this.jugadorCliente = this.juego.agregarJugador(nombre, edad);
            return jugadorCliente;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void setVistaGrafica(VistaGrafica vista) {
        this.vista = vista;
    }


    public void setVistaServidor(VentanaServidor vista) {
        this.vistaServidor = vista;
    }

    public Boolean iniciarPartida() throws RemoteException {
        if (getJugadores().length <= 10 && getJugadores().length >= 3) {
            juego.iniciarPartida();
            return true;
        } else {
            System.out.println("no hay jugadores suficientes");
            return false;
        }

    }

    public void pasarTurno() throws RemoteException {
        juego.pasarTurno();
    }

    public void verificarSiTerminoLaRonda() throws RemoteException {
        juego.verificarSiTerminoLaRonda();
    }

    public Boolean jugarUnaCarta(int x, int y, int posCarta, Jugador objetivo) throws RemoteException {
        return juego.jugarCarta(x, y, posCarta, objetivo);
    }

    public TipoAccion jugarHerramienta(int posCarta, Jugador objetivo) throws RemoteException {

        // valido desde el controlador si puedo usar la carta, despues la uso desde el modelo Juego
        Carta carta = jugadorCliente.elegirCarta(posCarta);

        if (carta instanceof CartaAccion) {
            // si el objetivo no es el mismo jugador
            if (objetivo != jugadorCliente) {
                juego.jugarHerramienta(objetivo, carta);
            }
            // es el mismo jugador pero la carta es de reparar
            else if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                // reviso si tiene cartas rotas
                if (!(jugadorCliente.getHerramientasRotas().isEmpty())) {
                    juego.jugarHerramienta(objetivo, carta);
                } else {
                    return TipoAccion.REPARARPICO;
                }
            } else {
                return TipoAccion.ROMPERPICO;
            }
        }
        return null;
    }

    public Jugador getJugadorActual() throws RemoteException {
        return juego.getJugadorActual();
    }

    public void descartarCarta(Carta carta) throws RemoteException {
        juego.descartarCarta(carta);
    }

    public void tomarCartaDeMazo() throws RemoteException {
        if (jugadorCliente.getManoCartas().size() < 8) {
            juego.tomarCartaDeMazo();
        } else {
            System.out.println("ya tienes el maximo (8) de cartas");
        }
    }

    public Boolean esTurnoDe(Jugador jugador) throws RemoteException {
        if (jugador == null) return false;
        return jugador.equals(jugadorCliente);
    }

    public Tablero getTablero() throws RemoteException {
        return juego.getTablero();
    }


    public Jugador[] getJugadores() throws RemoteException {
        return juego.getJugadores();
    }


    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof Evento evento) {
            if (o == Evento.NUEVO_USUARIO && vistaServidor != null) {
                Jugador[] jugadores = this.juego.getJugadores();
                    this.vistaServidor.actualizarListaJugadores(jugadores);
            } else {
                if(vista != null) {
                    switch (evento) {
                        case NUEVA_RONDA -> vista.getVentanaJuego().actualizarVentana();
                        case PASAR_TURNO -> vista.getVentanaJuego().actualizarTurno();
                        case TOMAR_CARTA, DESCARTAR_CARTA -> {
                            vista.getVentanaJuego().getPanelJugador().actualizar();
                            vista.getVentanaJuego().getPanelTomarCarta().actualizar();
                        }
                        case ACTUALIZAR_HERRAMIENTAS -> {
                            vista.getVentanaJuego().getPanelHerramientas().actualizar();
                            vista.getVentanaJuego().getPanelTablaJugadores().actualizar();
                            vista.getVentanaJuego().getPanelJugador().actualizar();
                        }
                        case JUGAR_CARTA_TABLERO -> {
                            vista.getVentanaJuego().getPanelTablero().actualizar();
                            vista.getVentanaJuego().getPanelJugador().actualizar();
                        }
                        case INICIAR_PARTIDA -> {
                            this.vista.iniciarVentanaJuego();
                            vista.getVentanaJuego().inicializarVentana();
                            vista.getVentanaJuego().setVisible(true);
                            vista.getVentanaJuego().actualizarTurno();
                        }
                        case FINALIZAR_PARTIDA -> vista.getVentanaJuego().mostrarGanador();
                    }
                }
            }
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modelo) throws RemoteException {
        this.juego = (IJuego) modelo;
    }


    public Mazo getMazo() throws RemoteException {
        return juego.getMazo();
    }

    public String getGanador() throws RemoteException {
        return juego.getGanador();
    }
}