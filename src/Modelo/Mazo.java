package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Rol;
import Modelo.Enums.TipoAccion;
import Modelo.Enums.TipoCarta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Mazo {

    private List<Carta> cartas;

    public Mazo() {
        inicializarCartas();
        barajarMazo();
    }

    private void inicializarCartas() {

        // creo las cartas y las guardo en el mazo

        int cantCartas = 4; //porque ya tengo la de inicio y los destinos

        // CARTAS TUNEL
        // tunel vertical
        int i;
        for (i = 0; i < 4; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(true, true, false, false);
            agregarCartaAlMazo(carta);
        }

        // tunel cruz
        for (i = 0; i < 6; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(true, true, true, true);
            agregarCartaAlMazo(carta);
        }

        // tunel vertical y derecha
        for (i = 0; i < 6; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(true, true, false, true);
            agregarCartaAlMazo(carta);
        }

        // tunel izquierda y abajo
        for (i = 0; i < 5; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(false, true, true, false);
            agregarCartaAlMazo(carta);
        }

        // tunel abajo, izquierda y derecha
        for (i = 0; i < 6; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(false, true, true, true);
            agregarCartaAlMazo(carta);
        }


        // tunel derecha y abajo
        for (i = 0; i < 7; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(false, true, false, true);
            agregarCartaAlMazo(carta);
        }

        // tunel horizontal
        for (i = 0; i < 5; i++) {
            cantCartas++;
            CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
            carta.setCaminos(false, false, true, true);
            agregarCartaAlMazo(carta);
        }

        // tunel abajo
        cantCartas++;
        CartaTunel carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
        carta.setCaminos(false, true, false, false);
        agregarCartaAlMazo(carta);

        //tunel derecha
        cantCartas++;
        carta = new CartaTunel(cantCartas, TipoCarta.TUNEL, "", false);
        carta.setCaminos(false, false, true, false);
        agregarCartaAlMazo(carta);

        // CARTAS ACCION

        // romper linterna
        for (int j = 0; j < 3; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.ROMPERLINTERNA);
            cartaAccion.setTipo(tipos);
            agregarCartaAlMazo(cartaAccion);
        }

        // romper pico
        for (int j = 0; j < 3; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.ROMPERPICO);
            cartaAccion.setTipo(tipos);
            agregarCartaAlMazo(cartaAccion);
        }

        //romper vagoneta
        for (int j = 0; j < 3; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.ROMPERVAGONETA);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // derrumbar
        for (int j = 0; j < 3; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.DERRUMBAR);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // reparar pico
        for (int j = 0; j < 2; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.REPARARPICO);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // reparar linterna
        for (int j = 0; j < 2; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.REPARARLINTERNA);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // reparar vagoneta
        for (int j = 0; j < 2; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.REPARARVAGONETA);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // reparar pico y linterna
        for (int j = 0; j < 2; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.REPARARLINTERNA);
            tipos.add(TipoAccion.REPARARPICO);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // reparar pico y vagoneta
        for (int j = 0; j < 2; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.REPARARVAGONETA);
            tipos.add(TipoAccion.REPARARPICO);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // reparar linterna y vagoneta
        for (int j = 0; j < 2; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.REPARARVAGONETA);
            tipos.add(TipoAccion.REPARARLINTERNA);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

        // mapa
        for (int j = 0; j < 6; j++) {
            cantCartas++;
            CartaAccion cartaAccion = new CartaAccion(cantCartas, TipoCarta.ACCION, "");
            List<TipoAccion> tipos = new ArrayList<>();
            tipos.add(TipoAccion.MAPA);
            agregarCartaAlMazo(cartaAccion);
            cartaAccion.setTipo(tipos);
        }

    }

    public void barajarMazo() {
        Collections.shuffle(cartas);
    }

    public void repartirCartas(List<Jugador> jugadores) {

        //por cada jugador genero una mano y se la doy
        for (Jugador j : jugadores) {
            switch (jugadores.size()) {
                case 3, 4, 5 -> {
                    List<Carta> mano = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        mano.add(tomarCarta());
                    }
                    j.setManoCartas(mano);
                }
                case 6, 7 -> {
                    List<Carta> mano = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        mano.add(tomarCarta());
                    }
                    j.setManoCartas(mano);
                }
                case 8, 9, 10 -> {
                    List<Carta> mano = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        mano.add(tomarCarta());
                    }
                    j.setManoCartas(mano);
                }
            }
        }
    }

    public void asignarRoles(List<Jugador> jugadores) {

            // genero los roles dependiendo la cantidad de jugadores
            // asigno los  roles a cada uno
            switch (jugadores.size()) {
                case 3 -> {
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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
                    List<Rol> roles = new ArrayList<>();
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

    public void agregarCartaAlMazo(Carta carta) {
        this.cartas.add(carta);
    }

    public Carta tomarCarta() {
        Carta carta = cartas.removeFirst();
        return carta;
    }

    public boolean noHayCartas() {
        return cartas.isEmpty();
    }


}
