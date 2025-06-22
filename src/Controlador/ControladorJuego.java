package Controlador;

import Modelo.*;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.TipoAccion;
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

    public void conectarUsuario(String nombre, int edad) {
        try {
            this.juego.agregarJugador(nombre, edad);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        Carta carta = getJugadorActual().elegirCarta(posCarta);

        if (carta instanceof CartaAccion) {
            // si el objetivo no es el mismo jugador
            if (objetivo != getJugadorActual()) {
                juego.jugarHerramienta(objetivo, carta);
            }
            // es el mismo jugador pero la carta es de reparar
            else if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                // reviso si tiene cartas rotas
                if (!(getJugadorActual().getHerramientasRotas().isEmpty())) {
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
        if (getJugadorActual().getManoCartas().size() < 8) {
            juego.tomarCartaDeMazo();
        } else {
            System.out.println("ya tienes el maximo (8) de cartas");
        }
    }

    public Boolean esTurnoDe(Jugador jugador) throws RemoteException {
        return jugador.equals(getJugadorActual());
    }

    public Tablero getTablero() throws RemoteException {
        return juego.getTablero();
    }


    public Jugador[] getJugadores() throws RemoteException {
        return juego.getJugadores();
    }


    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof Evento) {
            if (o == Evento.NUEVO_USUARIO && vistaServidor != null) {
                Jugador[] jugadores = this.juego.getJugadores();
                    this.vistaServidor.actualizarListaJugadores(jugadores);
            } else {
                if(vista != null) {
                    vista.actualizar((Evento) o);
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