package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.TipoAccion;
import Modelo.Juego;
import Modelo.Jugador;
import Modelo.Mazo;
import Modelo.Tablero;
import Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;
import java.util.HashMap;

public class ControladorJuego implements IControladorRemoto {

    private Juego juego;
    private VistaGrafica vista;

    public <T extends IObservableRemoto> ControladorJuego(Juego juego) {
        this.juego = juego;
    }

    public ControladorJuego() {

    }

    public void conectarUsuario(String nombre, int edad) {
        try {
            this.juego.agregarJugador(nombre, edad);
            this.vista.mostrarListaJugadores(this.juego.getJugadores());
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setVista(VistaGrafica vista) {
        this.vista = vista;
    }

    public void iniciarPartida() throws RemoteException {
        if (getJugadores().length <= 10 && getJugadores().length >= 3){
            juego.iniciarPartida();
    }else{
            System.out.println("no hay jugadores suficientes");
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

public Jugador getJugadorActual() {
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

public Boolean esTurnoDe(Jugador jugador) {
    return jugador.equals(getJugadorActual());
}

public Tablero getTablero() {
    return juego.getTablero();
}


public Jugador[] getJugadores() {
    return juego.getJugadores();
}

@Override
public <T extends IObservableRemoto> void setModeloRemoto(T t) throws RemoteException {
    this.juego = (Juego) t;
}

@Override
public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
    if (o instanceof Evento) {
        vista.actualizar((Evento) o);
    }
}

public Mazo getMazo() {
    return juego.getMazo();
}
}