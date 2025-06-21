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
    private Jugador ganador;

    public ControladorJuego(Juego juego) {
        this.juego = juego;
    }

    public void pasarTurno() {
       juego.pasarTurno();
    }

    public void verificarSiTerminoLaRonda() {
         if(juego.hayCaminoHastaOro()){
            finalizarRonda(true);
         }else if(juego.noHayCartas()){
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
            juego.notificarObservers();
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
        getJugadorActual().descartarCarta(carta);
        juego.notificarObservers();
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

    public void agregarObserver(Ventana ventana) {
        juego.agregarObserver(ventana);
    }

    public List<Jugador> getJugadores() {
        return juego.getJugadores();
    }
}