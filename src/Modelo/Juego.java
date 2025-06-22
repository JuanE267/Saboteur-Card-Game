package Modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Evento;
import Modelo.Enums.Rol;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Juego extends ObservableRemoto {

    private HashMap<Integer, Jugador> jugadores;
    private Mazo mazo;
    private Tablero tablero;
    private List<Rol> roles;
    private int turnoInicial;
    private int ronda;
    private int turno;
    private Jugador ganador;

    public Juego() {
        this.tablero = new Tablero();
        this.mazo = new Mazo();
        ronda = 1;
        // asigno los roles y reparto las cartas

    }

    public void iniciarPartida(){
        asignarRoles();
        mazo.repartirCartas(jugadores);
    }

    public Jugador agregarJugador(String nombre, int edad) throws RemoteException {
        Jugador jugador = new Jugador(nombre, edad);
        this.jugadores.put(jugador.getId(), jugador);
        this.notificarObservadores(Evento.NUEVO_USUARIO);
        return jugador;
    }

    private void asignoPrimerTurno(int ronda) {
        if (ronda == 0) {
            // el jugador en empezar es el de mayor edad
            Jugador mayorEdad = jugadores.get(0);
            for (Jugador j : jugadores.values()) {
                if (j.getEdad() > mayorEdad.getEdad()) {
                    mayorEdad = j;
                }
            }
            turnoInicial = jugadores.get(mayorEdad.getId()).getId();
        } else {
            turnoInicial++;
        }

    }

    public Tablero getTablero() {
        return tablero;
    }

    public void asignarRoles() {


        // elimino los roles anteriores
        jugadores.forEach((id, j) -> {
            j.setRol(null);
        });

        // genero los roles dependiendo la cantidad de jugadores
        // asigno los  roles a cada uno
        switch (jugadores.size()) {
            case 1, 3 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                        j.setRol(roles.removeFirst());
                });
            }
            case 4 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 5 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
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

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 7 -> {
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

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 8 -> {
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

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 9 -> {
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

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 10 -> {
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

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
        }
    }

    public void pasarTurno() throws RemoteException {
        if (this.turno == jugadores.size() - 1) this.turno = 0;
        else this.turno++;
        notificarObservadores(Evento.PASAR_TURNO);
    }


    public Jugador finalizarRonda(boolean ganaronLosMineros) throws RemoteException {

        String mensajeGanador;

        if (ganaronLosMineros) {
            mensajeGanador = "GANARON LOS MINEROS";

            jugadores.forEach((id,j)->{
                if (j.getRol() == Rol.MINERO) {
                    j.sumarPuntos(4);
                } else {
                    j.sumarPuntos(3);
                }
            });
        } else {
            mensajeGanador = "GANARON LOS SABOTEADORES";

            jugadores.forEach((id, j)->{
                if (j.getRol() == Rol.SABOTEADOR) {
                    j.sumarPuntos(4);
                } else {
                    j.sumarPuntos(3);
                }
            });
        }

        System.out.println(mensajeGanador);
        System.out.println("Se revelan los roles..");
        jugadores.forEach((id,j) ->  {
            System.out.println(j.getNombre() + " -> " + j.getRol());
        });

        if (ronda <= 3) {
            // reinicio el estado logico
            reiniciarRonda(ronda);
            //reinicio la vista
            notificarObservadores(Evento.NUEVA_RONDA);
            pasarRonda();
        } else {

            Jugador mayorPuntaje = jugadores.get(0);
            for (Jugador j : jugadores.values()) {
                if (j.getPuntaje() > mayorPuntaje.getPuntaje()) {
                    mayorPuntaje = j;
                }
            }
            ganador = mayorPuntaje;
        }
        return ganador;
    }


    public Boolean jugarCarta(int x, int y, int posCarta, Jugador objetivo) throws RemoteException {

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
        notificarObservadores(Evento.JUGAR_CARTA_TABLERO);
        return pudoSerJugado;
    }

    public void jugarHerramienta(Jugador objetivo, Carta carta) throws RemoteException {

        getJugadorActual().jugarCarta(objetivo, carta);

        // despues de jugar elimino la carta de la mano
        getJugadorActual().getManoCartas().remove(carta);
        // tomo una nueva si el mazo no esta vacio
        if (!mazo.noHayCartas()) {
            Carta nuevaCarta = mazo.tomarCarta();
            getJugadorActual().getManoCartas().add(nuevaCarta);
        }
        notificarObservadores(Evento.ACTUALIZAR_HERRAMIENTAS);
    }

    public void tomarCartaDeMazo() throws RemoteException {
        Carta nuevaCarta = mazo.tomarCarta();
        getJugadorActual().getManoCartas().add(nuevaCarta);
        notificarObservadores(Evento.TOMAR_CARTA);
    }

    public int getTurnoInicial() {
        return turnoInicial;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public HashMap<Integer, Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(this.turno);
    }


    public void reiniciarRonda(int ronda) {

        // reinicio el mazo
        mazo = new Mazo();
        mazo.barajarMazo();
        // reinicio el tablero
        tablero = new Tablero();
        // reinicio jugadores
        for (Jugador j : jugadores.values()) {
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

    public void verificarSiTerminoLaRonda() throws RemoteException {
        if (hayCaminoHastaOro()) {
            finalizarRonda(true);
        } else if (noHayCartas()) {
            finalizarRonda(false);
        }
    }

    public void descartarCarta(Carta carta) throws RemoteException {
        getJugadorActual().descartarCarta(carta);
        notificarObservadores(Evento.DESCARTAR_CARTA);
    }


}

