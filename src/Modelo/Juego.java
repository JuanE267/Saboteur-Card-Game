package Modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Modelo.Enums.Rol;

public class Juego {

    private List<Jugador> jugadores;
    private Mazo mazo;
    private Tablero tablero;
    private List<Rol> roles;
    private int turnoInicial;
    private int ronda;

    public Juego(){
        this.tablero = new Tablero();
        this.mazo = new Mazo();
        ronda = 1;

        //HARDCODEEEEEE
        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(new Jugador("JUAN", 1));
        jugadores.add(new Jugador("PEPITO", 2));
        jugadores.add(new Jugador("MARIA", 3));
        jugadores.add(new Jugador("FRANCIA", 4));
        setJugadores(jugadores);


        asignoPrimerTurno(1);
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

    public int getTurnoInicial() {
        return turnoInicial;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
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
}
