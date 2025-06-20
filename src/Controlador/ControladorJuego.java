package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Juego;
import Modelo.Jugador;

public class ControladorJuego {

    private Juego juego;
    private int turno;

    public ControladorJuego(Juego juego) {

        this.juego = juego;
        
        Jugador mayorEdad = juego.getJugadores().removeFirst();
        for (Jugador j : juego.getJugadores()) {
            if (j.getEdad() > mayorEdad.getEdad()) {
                mayorEdad = j;
            }
        }
        this.turno = mayorEdad.getEdad(); 
        this.juego.asignarRoles();
    }

    public Jugador getJugadorActual() {
        return juego.getJugadores().get(this.turno);
    }

    public void pasarTurno() {
        if (this.turno == juego.getJugadores().size() - 1) this.turno = 0;
        else this.turno++;
    }

    public boolean TerminarRonda() {
        return juego.getTablero().hayCaminoHastaOro() || jugadoresSinCartas();
    }

    public void jugarCarta(int x, int y, int posCarta, Jugador objetivo) {

        Carta carta = getJugadorActual().elegirCarta(posCarta);
        Jugador actual = getJugadorActual();

        // dependiendo el tipo de la carta juego de cierta manera
        if (carta instanceof CartaTunel) actual.jugarCarta(juego.getTablero(), x, y, carta);
        else if (carta instanceof CartaAccion) {
            if (((CartaAccion) carta).getTipoAccion().size() == 1) {
                actual.jugarCartaMapa(juego.getTablero(), x, y, carta);
            } else {
                actual.jugarCarta(objetivo, carta);
            }
        }
        // despues de jugar elimino la carta de la mano
        actual.getManoCartas().remove(posCarta);

        // tomo una nueva si el mazo no esta vacio
        if(!juego.getMazo().noHayCartas()) {
            Carta nuevaCarta = juego.getMazo().tomarCarta();
            actual.getManoCartas().add(nuevaCarta);
        }
    }

    private boolean jugadoresSinCartas() {
        for (Jugador j : juego.getJugadores()) {
            if(!(j.getManoCartas().isEmpty())) return false;
        }
        return true;
    }
}