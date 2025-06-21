package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Juego;
import Modelo.Jugador;
import Modelo.Tablero;

import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {

    private Juego juego;
    private int turno;

    public ControladorJuego(Juego juego) {

        this.juego = juego;
        this.turno = juego.getTurnoInicial();
    }


    public Jugador getJugadorActual() {
        return juego.getJugadores().get(this.turno);
    }

    public void pasarTurno() {
        if (this.turno == juego.getJugadores().size() - 1) this.turno = 0;
        else this.turno++;
        System.out.println("es turno de -> " + juego.getJugadores().get(turno).getNombre());
    }

    public boolean terminarRonda() {
        return juego.getTablero().hayCaminoHastaOro() || juego.getMazo().noHayCartas();
    }

    public void jugarCarta(int x, int y, int posCarta, Jugador objetivo) {

        Carta carta = getJugadorActual().elegirCarta(posCarta);
        Jugador actual = getJugadorActual();
        Boolean tunelPudoSerJugado = false;

        // dependiendo el tipo de la carta juego de cierta manera
        if (carta instanceof CartaTunel) {
            tunelPudoSerJugado = actual.jugarCarta(juego.getTablero(), x, y, carta);

            // despues de jugar elimino la carta de la mano, si es que pudo ser jugada
            if (tunelPudoSerJugado) {
                actual.getManoCartas().remove(posCarta);
                // tomo una nueva si el mazo no esta vacio
                if (!juego.getMazo().noHayCartas()) {
                    Carta nuevaCarta = juego.getMazo().tomarCarta();
                    actual.getManoCartas().add(nuevaCarta);
                }
            }

        } else if (carta instanceof CartaAccion) {
            if (((CartaAccion) carta).getTipoAccion().size() == 1) {
                actual.jugarCartaMapaDerrumbe(juego.getTablero(), x, y, carta);

                // despues de jugar elimino la carta de la mano
                actual.getManoCartas().remove(posCarta);

                // tomo una nueva si el mazo no esta vacio
                if (!juego.getMazo().noHayCartas()) {
                    Carta nuevaCarta = juego.getMazo().tomarCarta();
                    actual.getManoCartas().add(nuevaCarta);
                }
            }

        }
    }

    public void jugarHerramienta(int posCarta, Jugador objetivo) {

        Carta carta = getJugadorActual().elegirCarta(posCarta);
        Jugador actual = getJugadorActual();

        if (carta instanceof CartaAccion) {
            actual.jugarCarta(objetivo, carta);

            // despues de jugar elimino la carta de la mano
            actual.getManoCartas().remove(posCarta);

            // tomo una nueva si el mazo no esta vacio
            if (!juego.getMazo().noHayCartas()) {
                Carta nuevaCarta = juego.getMazo().tomarCarta();
                actual.getManoCartas().add(nuevaCarta);
            }
        }
    }

    public void descartarCarta(Carta carta) {
        getJugadorActual().descartarCarta(carta);
    }

    public void tomarCartaDeMazo() {
        if (getJugadorActual().getManoCartas().size() < 8) {
            Carta nuevaCarta = juego.getMazo().tomarCarta();
            getJugadorActual().getManoCartas().add(nuevaCarta);
        } else {
            System.out.println("ya tienes el maximo (8) de cartas");
        }
    }

    public Juego getJuego() {
        return juego;
    }

    public Tablero getTablero() {
        return juego.getTablero();
    }
}