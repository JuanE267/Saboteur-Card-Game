package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Rol;
import Modelo.Juego;
import Modelo.Jugador;
import Modelo.Tablero;
import Observer.Observer;
import Vista.BotonCarta;
import Vista.Ventana;

import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {

    private Juego juego;
    private Jugador ganador;

    public ControladorJuego(Juego juego) {
        this.juego = juego;
    }

    public void pasarTurno() {
       juego.pasarTurno();
    }

    public void verificarSiTerminoLaRonda() {
         juego.verificarSiTerminoLaRonda();
    }

    public Boolean jugarUnaCarta(int x, int y, int posCarta, Jugador objetivo) {
        return juego.jugarCarta(x, y, posCarta, objetivo);
    }

    public void jugarHerramienta(int posCarta, Jugador objetivo) {
        juego.jugarHerramienta(posCarta, objetivo);
    }

    public Jugador getJugadorActual() {
        return juego.getJugadorActual();
    }

    public void descartarCarta(Carta carta) {
        juego.descartarCarta(carta);
    }

    public void tomarCartaDeMazo() {
        juego.tomarCartaDeMazo();
    }

    public Boolean esTurnoDe(Jugador jugador){
        return jugador.equals(getJugadorActual());
    }

    public Juego getJuego() {
        return juego;
    }

    public Tablero getTablero() {
        return juego.getTablero();
    }

    public Jugador getGanador(){
        return ganador;
    }

    public void agregarObserver(Observer observer) {
        juego.agregarObserver(observer);
    }

    public List<Jugador> getJugadores() {
        return juego.getJugadores();
    }
}