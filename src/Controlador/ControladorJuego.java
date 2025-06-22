package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.Juego;
import Modelo.Jugador;
import Modelo.Tablero;
import Observer.Observer;
import Vista.Ventana;

import java.util.List;

public class ControladorJuego implements Observer {

    private Juego juego;
    private Jugador ganador;
    private Ventana ventana;

    public ControladorJuego(Juego juego) {
        this.juego = juego;
    }

    public void setVentana(Ventana ventana) {
        this.ventana = ventana;
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

    public TipoAccion jugarHerramienta(int posCarta, Jugador objetivo) {

        // valido desde el controlador si puedo usar la carta, despues la uso desde el modelo Juego
        Carta carta = getJugadorActual().elegirCarta(posCarta);
        Jugador actual = getJugadorActual();

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
                }else{
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

    public void descartarCarta(Carta carta) {
        juego.descartarCarta(carta);
    }

    public void tomarCartaDeMazo() {
        if (getJugadorActual().getManoCartas().size() < 8) {
            juego.tomarCartaDeMazo();
        } else {
            System.out.println("ya tienes el maximo (8) de cartas");
        }
    }

    public Boolean esTurnoDe(Jugador jugador) {
        return jugador.equals(getJugadorActual());
    }

    public Juego getJuego() {
        return juego;
    }

    public Tablero getTablero() {
        return juego.getTablero();
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void agregarObserver(Observer observer) {
        juego.agregarObserver(observer);
    }

    public List<Jugador> getJugadores() {
        return juego.getJugadores();
    }


    @Override
    public void actualizar(Evento evento) {
        ventana.actualizar(evento);
    }
}