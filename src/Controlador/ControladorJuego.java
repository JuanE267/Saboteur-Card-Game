package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Rol;
import Modelo.Juego;
import Modelo.Jugador;
import Modelo.Tablero;
import Vista.BotonCarta;
import Vista.Ventana;

import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {

    private Juego juego;
    private Ventana ventana;
    private Jugador ganador;
    private int turno;

    public ControladorJuego(Juego juego, Ventana ventana) {
        this.juego = juego;
        this.turno = juego.getTurnoInicial();
        this.ventana = ventana;
    }


    public Jugador getJugadorActual() {
        return juego.getJugadores().get(this.turno);
    }

    public void pasarTurno() {
        if (this.turno == juego.getJugadores().size() - 1) this.turno = 0;
        else this.turno++;
        ventana.actualizarTurno();
    }

    public void verificarSiTerminoLaRonda() {
         if(juego.getTablero().hayCaminoHastaOro()){
            finalizarRonda(true);
         }else if(juego.getMazo().noHayCartas()){
             finalizarRonda(false);
         }
    }

    private void finalizarRonda(boolean ganaronLosMineros) {

        String mensajeGanador;

        if(ganaronLosMineros){
            mensajeGanador = "GANARON LOS MINEROS";

            for(Jugador j : juego.getJugadores()){
                if(j.getRol() == Rol.MINERO){
                    j.sumarPuntos(4);
                }else{
                    j.sumarPuntos(3);
                }
            }
        }else{
            mensajeGanador = "GANARON LOS SABOTEADORES";

            for(Jugador j : juego.getJugadores()){
                if(j.getRol() == Rol.SABOTEADOR){
                    j.sumarPuntos(4);
                }else{
                    j.sumarPuntos(3);
                }
            }
        }


        System.out.println(mensajeGanador);
        System.out.println("Se revelan los roles..");
        for(Jugador j : juego.getJugadores()){
            System.out.println(j.getNombre() +" -> "+ j.getRol());
        }

        if(juego.getRondaActual() <= 3) {
            // reinicio el estado logico
            juego.reiniciarRonda(juego.getRondaActual());
            //reinicio la vista
            ventana.reiniciarVista();
            juego.pasarRonda();
        }else {

            Jugador mayorPuntaje = juego.getJugadores().getFirst();
            for(Jugador j : juego.getJugadores()){
                    if(j.getPuntaje() > mayorPuntaje.getPuntaje()){
                        mayorPuntaje = j;
                    }
            }
            ganador = mayorPuntaje;
        }
    }


    public Boolean jugarCarta(int x, int y, int posCarta, Jugador objetivo) {

        Carta carta = getJugadorActual().elegirCarta(posCarta);
        Jugador actual = getJugadorActual();
        Boolean pudoSerJugado = false;

        // dependiendo el tipo de la carta juego de cierta manera
        if (carta instanceof CartaTunel) {
            pudoSerJugado = actual.jugarCarta(juego.getTablero(), x, y, carta);

            // despues de jugar elimino la carta de la mano, si es que pudo ser jugada
            if (pudoSerJugado) {
                actual.getManoCartas().remove(posCarta);
                // tomo una nueva si el mazo no esta vacio
                if (!juego.getMazo().noHayCartas()) {
                    Carta nuevaCarta = juego.getMazo().tomarCarta();
                    actual.getManoCartas().add(nuevaCarta);
                }
            }

        } else if (carta instanceof CartaAccion) {
            if (((CartaAccion) carta).getTipoAccion().size() == 1) {
                pudoSerJugado = actual.jugarCartaMapaDerrumbe(juego.getTablero(), x, y, carta);

                if (pudoSerJugado) {

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
        return pudoSerJugado;
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
}