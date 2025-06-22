package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Rol;
import Modelo.Enums.TipoAccion;
import Observer.Observable;

public class Juego extends Observable {

    private List<Jugador> jugadores;
    private Mazo mazo;
    private Tablero tablero;
    private List<Rol> roles;
    private int turnoInicial;
    private int ronda;
    private int turno;
    private Jugador ganador;

    public Juego(){
        this.tablero = new Tablero();
        this.mazo = new Mazo();
        ronda = 1;

        //HARDCODEEEEEE
        this.jugadores = new ArrayList<>();
        jugadores.add(new Jugador("JUAN", 1));
        jugadores.add(new Jugador("PEPITO", 2));
        jugadores.add(new Jugador("MARIA", 3));
        jugadores.add(new Jugador("FRANCIA", 4));
        asignoPrimerTurno(1);
        this.turno = turnoInicial;

        // asigno los roles y reparto las cartas
        asignarRoles();
        mazo.repartirCartas(jugadores);
    }

    private void asignoPrimerTurno(int ronda) {
        if (ronda == 0) {
            // el jugador en empezar es el de mayor edad
            Jugador mayorEdad = getJugadores().getFirst();
            for (Jugador j : getJugadores()) {
                if (j.getEdad() > mayorEdad.getEdad()) {
                    mayorEdad = j;
                }
            }
            turnoInicial = jugadores.indexOf(mayorEdad);
        }else{
            turnoInicial++;
        }

    }

    public Tablero getTablero() {
        return tablero;
    }
    
    public void asignarRoles() {


        // elimino los roles anteriores
        for(Jugador j: jugadores){
            j.setRol(null);
        }
        // genero los roles dependiendo la cantidad de jugadores
        // asigno los  roles a cada uno
        switch (jugadores.size()) {
            case 1,3 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
               }
            case 4-> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
            case 5-> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
            case 6 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
            case 7-> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
            case 8-> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
            case 9-> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
            case 10-> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                for (Jugador j : jugadores){
                    j.setRol(roles.removeFirst());
                }
            }
    }
}

    public void pasarTurno(){
        if (this.turno == jugadores.size() - 1) this.turno = 0;
        else this.turno++;
        notificarObservers();
    }


    public Jugador finalizarRonda(boolean ganaronLosMineros) {

        String mensajeGanador;

        if(ganaronLosMineros){
            mensajeGanador = "GANARON LOS MINEROS";

            for(Jugador j : jugadores){
                if(j.getRol() == Rol.MINERO){
                    j.sumarPuntos(4);
                }else{
                    j.sumarPuntos(3);
                }
            }
        }else{
            mensajeGanador = "GANARON LOS SABOTEADORES";

            for(Jugador j : jugadores){
                if(j.getRol() == Rol.SABOTEADOR){
                    j.sumarPuntos(4);
                }else{
                    j.sumarPuntos(3);
                }
            }
        }


        System.out.println(mensajeGanador);
        System.out.println("Se revelan los roles..");
        for(Jugador j : jugadores){
            System.out.println(j.getNombre() +" -> "+ j.getRol());
        }

        if(ronda <= 3) {
            // reinicio el estado logico
            reiniciarRonda(ronda);
            //reinicio la vista
            notificarObservers();
            pasarRonda();
        }else {

            Jugador mayorPuntaje = jugadores.getFirst();
            for(Jugador j : jugadores){
                if(j.getPuntaje() > mayorPuntaje.getPuntaje()){
                    mayorPuntaje = j;
                }
            }
            ganador = mayorPuntaje;
        }
        return ganador;
    }


    public Boolean jugarCarta(int x, int y, int posCarta, Jugador objetivo){

        Carta carta = getJugadorActual().elegirCarta(posCarta);
        Jugador actual = getJugadorActual();
        Boolean pudoSerJugado = false;

        // dependiendo el tipo de la carta juego de cierta manera
        if (carta instanceof CartaTunel) {
            pudoSerJugado = actual.jugarCarta(tablero, x, y, carta);

            // despues de jugar elimino la carta de la mano, si es que pudo ser jugada
            if (pudoSerJugado) {
                actual.getManoCartas().remove(posCarta);
                // tomo una nueva si el mazo no esta vacio
                if (!mazo.noHayCartas()) {
                    Carta nuevaCarta = mazo.tomarCarta();
                    actual.getManoCartas().add(nuevaCarta);
                }
            }

        } else if (carta instanceof CartaAccion) {
            if (((CartaAccion) carta).getTipoAccion().size() == 1) {
                pudoSerJugado = actual.jugarCartaMapaDerrumbe(tablero, x, y, carta);

                if (pudoSerJugado) {

                    // despues de jugar elimino la carta de la mano
                    actual.getManoCartas().remove(posCarta);

                    // tomo una nueva si el mazo no esta vacio
                    if (!mazo.noHayCartas()) {
                        Carta nuevaCarta = mazo.tomarCarta();
                        actual.getManoCartas().add(nuevaCarta);
                    }
                }

            }

        }
        notificarObservers();
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
            if (!mazo.noHayCartas()) {
                Carta nuevaCarta = mazo.tomarCarta();
                actual.getManoCartas().add(nuevaCarta);
            }
        }
        notificarObservers();
    }

    public void tomarCartaDeMazo() {
        if (getJugadorActual().getManoCartas().size() < 8) {
            Carta nuevaCarta = mazo.tomarCarta();
            getJugadorActual().getManoCartas().add(nuevaCarta);
        } else {
            System.out.println("ya tienes el maximo (8) de cartas");
        }
        notificarObservers();
    }

    public int getTurnoInicial() {
        return turnoInicial;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(this.turno);
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public void reiniciarRonda(int ronda) {

        // reinicio el mazo
        mazo = new Mazo();
        mazo.barajarMazo();
        // reinicio el tablero
        tablero = new Tablero();
        // reinicio jugadores
        for(Jugador j : jugadores){
            j.reiniciarEstado();
        }

        asignoPrimerTurno(ronda);
        //asigno roles de nuevo
        asignarRoles();
        mazo.repartirCartas(jugadores);

    }

    public int getRondaActual() {
        return ronda;
    }

    public void pasarRonda() {
        ronda += 1;
    }

    public boolean hayCaminoHastaOro() {
        return tablero.hayCaminoHastaOro();
    }

    public boolean noHayCartas() {
        return mazo.noHayCartas();
    }

    public void verificarSiTerminoLaRonda() {
        if(hayCaminoHastaOro()){
            finalizarRonda(true);
        }else if(noHayCartas()){
            finalizarRonda(false);
        }
    }

    public void descartarCarta(Carta carta) {
        getJugadorActual().descartarCarta(carta);
        notificarObservers();
    }
}
