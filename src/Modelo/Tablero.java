package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Direccion;
import Modelo.Enums.Rol;
import Modelo.Enums.TipoCarta;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Posicion, Carta> tablero;

    private int xInicio;
    private int yInicio;
    private int xOro;
    private int yOro;

    public Tablero() {
        tablero = new HashMap<>();
        inicializarTablero();
    }

    public void inicializarTablero() {

        // Creo las cartas iniciales
        CartaTunel inicio = new CartaTunel(0, TipoCarta.TUNEL, "tuneles/INICIO.png", true);
        inicio.setCaminos(true, true, true, true);

        CartaDestino oro = new CartaDestino(1, TipoCarta.DESTINO, "tuneles/DESTINO.png", "tuneles/DORSO TUNELES.png", true);
        CartaDestino primerCarbon = new CartaDestino(2, TipoCarta.DESTINO, "tuneles/carbon.png", "tuneles/DORSO TUNELES.png", false);
        CartaDestino segundoCarbon = new CartaDestino(3, TipoCarta.DESTINO, "tuneles/carbon.png", "tuneles/DORSO TUNELES.png", false);

        oro.setEsDestino(true);
        primerCarbon.setEsDestino(true);
        segundoCarbon.setEsDestino(true);

        // coloco inicio en el centro del tablero
//        xInicio = (int) Math.floor((double) alto / 2);
//        yInicio = 0;

        //prueba para ver si gana la partida
        xInicio = 0;
        yInicio = 5;
        colocarCarta(inicio, xInicio, yInicio);

        // genero una posicion aleatoria donde coloco cada destino
        List<CartaDestino> destinos = new ArrayList<>();
        destinos.add(oro);
        destinos.add(primerCarbon);
        destinos.add(segundoCarbon);

        List<Posicion> posicionesDestino = new ArrayList<>();
        posicionesDestino.add(new Posicion(0, 7));
        posicionesDestino.add(new Posicion(-2, 7));
        posicionesDestino.add(new Posicion(2, 7));

        // mientras que haya cartas destino y posiciones las coloco aleatoriamente
        while (!(destinos.isEmpty()) && !(posicionesDestino.isEmpty())) {
            int indicePos = (int) (Math.random() * posicionesDestino.size());
            int indiceDestinos = (int) (Math.random() * destinos.size());

            CartaDestino destino = destinos.get(indiceDestinos);
            setCarta(destino, posicionesDestino.get(indicePos).getX(), posicionesDestino.get(indicePos).getY());
            if (destino.getEsOro()) {
                destino.setOroX(posicionesDestino.get(indicePos).getX());
                destino.setOroY(posicionesDestino.get(indicePos).getY());
                System.out.println(destino.getOroX());
                this.xOro = destino.getOroX();
                this.yOro = destino.getOroY();
            }
            destinos.remove(indiceDestinos);
            posicionesDestino.remove(indicePos);
        }

    }

//colocar carta viejo
//    public boolean colocarCarta(CartaTunel carta, int x, int y) {
//
//        // si la posicion esta ocupada
//        if (getCarta(x, y) != null) return false;
//
//        // comprobar si la carta puede conectarse (el vecino es tunel y los tuneles coinciden)
//        Boolean puedeConectarDerecha = false;
//        Boolean puedeConectarIzquierda = false;
//        Boolean puedeConectarAbajo = false;
//        Boolean puedeConectarArriba = false;
//
//        // VECINOS
//        Carta vecinoAbajo = getCarta(x + 1, y);
//        Carta vecinoArriba = getCarta(x - 1, y);
//        Carta vecinoDerecha = getCarta(x, y + 1);
//        Carta vecinoIzquierda = getCarta(x, y - 1);
//
//        boolean fueColocada = false;
//
//        // DERECHA
//        puedeConectarDerecha = this.puedeConectar(vecinoDerecha, carta, Direccion.DERECHA);
//        if (vecinoDerecha instanceof CartaDestino && puedeConectarDerecha) {
//            if ((vecinoAbajo != null && !vecinoAbajo.esDestino()) || (vecinoArriba != null && !vecinoArriba.esDestino())
//                    || (vecinoDerecha != null && !vecinoDerecha.esDestino()) || (vecinoIzquierda != null && !vecinoIzquierda.esDestino())) {
//
//                if (((carta.getEsInicio()) || (puedeConectarDerecha && !(vecinoDerecha.esDestino()))
//                        || (puedeConectarIzquierda && !(vecinoIzquierda.esDestino()))
//                        || (puedeConectarAbajo && !(vecinoAbajo.esDestino()))
//                        || (puedeConectarArriba && !(vecinoArriba.esDestino()))) && !fueColocada) {
//                    ((CartaDestino) vecinoDerecha).girar();
//                    ((CartaDestino) vecinoDerecha).setDorso(vecinoDerecha.getImg());
//                    //cuadricula[x][y + 1] = vecinoDerecha;
//                    setCarta(vecinoDerecha, x, y + 1);
//                    fueColocada = true;
//                }
//            }
//        }
//        // IZQUIERDA
//        puedeConectarIzquierda = this.puedeConectar(vecinoIzquierda, carta, Direccion.IZQUIERDA);
//        if (vecinoIzquierda instanceof CartaDestino && puedeConectarIzquierda) {
//            if ((vecinoAbajo != null && !vecinoAbajo.esDestino()) || (vecinoArriba != null && !vecinoArriba.esDestino())
//                    || (vecinoDerecha != null && !vecinoDerecha.esDestino()) || (vecinoIzquierda != null && !vecinoIzquierda.esDestino())) {
//
//                if (((carta.getEsInicio()) || (puedeConectarDerecha && !(vecinoDerecha.esDestino()))
//                        || (puedeConectarIzquierda && !(vecinoIzquierda.esDestino()))
//                        || (puedeConectarAbajo && !(vecinoAbajo.esDestino()))
//                        || (puedeConectarArriba && !(vecinoArriba.esDestino()))) && !fueColocada){
//                    ((CartaDestino) vecinoIzquierda).girar();
//                    ((CartaDestino) vecinoIzquierda).setDorso(vecinoIzquierda.getImg());
//                    //cuadricula[x][y - 1] = vecinoIzquierda;
//                    setCarta(vecinoIzquierda, x, y - 1);
//                    fueColocada = true;
//                }
//            }
//        }
//        // ABAJO
//        puedeConectarAbajo = this.puedeConectar(vecinoAbajo, carta, Direccion.ABAJO);
//        if (vecinoAbajo instanceof CartaDestino && puedeConectarAbajo) {
//            if ((vecinoAbajo != null && !vecinoAbajo.esDestino()) || (vecinoArriba != null && !vecinoArriba.esDestino())
//                    || (vecinoDerecha != null && !vecinoDerecha.esDestino()) || (vecinoIzquierda != null && !vecinoIzquierda.esDestino())) {
//
//                if (((carta.getEsInicio()) || (puedeConectarDerecha && !(vecinoDerecha.esDestino()))
//                        || (puedeConectarIzquierda && !(vecinoIzquierda.esDestino()))
//                        || (puedeConectarAbajo && !(vecinoAbajo.esDestino()))
//                        || (puedeConectarArriba && !(vecinoArriba.esDestino()))) && !fueColocada) {
//                    ((CartaDestino) vecinoAbajo).girar();
//                    ((CartaDestino) vecinoAbajo).setDorso(vecinoAbajo.getImg());
//                    //cuadricula[x + 1][y] = vecinoAbajo;
//                    setCarta(vecinoAbajo, x + 1, y);
//                    fueColocada = true;
//                }
//            }
//        }
//        // ARRIBA
//        puedeConectarArriba = this.puedeConectar(vecinoArriba, carta, Direccion.ARRIBA);
//        if (vecinoArriba instanceof CartaDestino && puedeConectarArriba) {
//            if ((vecinoAbajo != null && !vecinoAbajo.esDestino()) ||
//                    (vecinoArriba != null && !vecinoArriba.esDestino())
//                    || (vecinoDerecha != null && !vecinoDerecha.esDestino())
//                    || (vecinoIzquierda != null && !vecinoIzquierda.esDestino())) {
//
//                if (((carta.getEsInicio()) || (puedeConectarDerecha && !(vecinoDerecha.esDestino()))
//                        || (puedeConectarIzquierda && !(vecinoIzquierda.esDestino()))
//                        || (puedeConectarAbajo && !(vecinoAbajo.esDestino()))
//                        || (puedeConectarArriba && !(vecinoArriba.esDestino()))) && !fueColocada) {
//                    ((CartaDestino) vecinoArriba).girar();
//                    ((CartaDestino) vecinoArriba).setDorso(vecinoArriba.getImg());
//                    //cuadricula[x - 1][y] = vecinoArriba;
//                    setCarta(vecinoArriba, x - 1, y);
//                    fueColocada = true;
//                }
//            }
//        }
//
//        if (((carta.getEsInicio()) || (puedeConectarDerecha && !(vecinoDerecha.esDestino()))
//                || (puedeConectarIzquierda && !(vecinoIzquierda.esDestino()))
//                || (puedeConectarAbajo && !(vecinoAbajo.esDestino()))
//                || (puedeConectarArriba && !(vecinoArriba.esDestino()))) && !fueColocada) {
//            setCarta(carta, x, y);// coloco la carta en la posicion
//            fueColocada = true;
//        }
//        return fueColocada;
//    }

    public boolean colocarCarta(CartaTunel carta, int x, int y) {

        // si ya hay una carta en la posicion
        if (getCarta(x, y) != null) return false;

        // si es carta de inicio no necesita comprobaciones
        if (carta.getEsInicio()) {
            setCarta(carta, x, y);
            return true;
        }

        // obtengo los vecinos
        Carta vecinoAbajo = getCarta(x + 1, y);
        Carta vecinoArriba = getCarta(x - 1, y);
        Carta vecinoDerecha = getCarta(x, y + 1);
        Carta vecinoIzquierda = getCarta(x, y - 1);

        // ahora necesito ver si conecta con un vecino que no es destino
        boolean puedeColocar = false;

        // si tiene el vecino, no es destino y puede colocar
        if (vecinoArriba != null && !vecinoArriba.esDestino() && puedeConectar(vecinoArriba, carta, Direccion.ARRIBA)) {
            puedeColocar = true;
        }
        if (vecinoAbajo != null && !vecinoAbajo.esDestino() && puedeConectar(vecinoAbajo, carta, Direccion.ABAJO)) {
            puedeColocar = true;
        }
        if (vecinoIzquierda != null && !vecinoIzquierda.esDestino() && puedeConectar(vecinoIzquierda, carta, Direccion.IZQUIERDA)) {
            puedeColocar = true;
        }
        if (vecinoDerecha != null && !vecinoDerecha.esDestino() && puedeConectar(vecinoDerecha, carta, Direccion.DERECHA)) {
            puedeColocar = true;
        }

        // si no puede colocar, rechazo
        if (!puedeColocar) return false;

        // coloco la carta
        setCarta(carta, x, y);

        // giro las carta destino si hay que hacerlo
        girarDestino(vecinoArriba, carta, x - 1, y, Direccion.ARRIBA);
        girarDestino(vecinoAbajo, carta, x + 1, y, Direccion.ABAJO);
        girarDestino(vecinoIzquierda, carta, x, y - 1, Direccion.IZQUIERDA);
        girarDestino(vecinoDerecha, carta, x, y + 1, Direccion.DERECHA);

        return true;
    }

    private void girarDestino(Carta vecino, CartaTunel carta, int x, int y, Direccion direccion) {
        // si no es destino, salgo
        if (!(vecino instanceof CartaDestino)) return;

        // si no se puede conectar, salgo
        if (!puedeConectar(vecino, carta, direccion)) return;

        // guardo los vecinos
        Carta[] vecinos = {
                getCarta(x + 1, y),
                getCarta(x - 1, y),
                getCarta(x, y + 1),
                getCarta(x, y - 1)
        };

        // me fijo si no conecta solo al vecino
        boolean existeVecino = false;

        for (Carta c : vecinos) {
            if (c != null && !vecino.esDestino()) {
                existeVecino = true;
                break;
            }
        }

        if (existeVecino) {

            // ver si conecta al inicio
            Set<Posicion> visitado = new HashSet<>();
            if (buscarPos(((CartaDestino) vecino).getOroX(), ((CartaDestino) vecino).getOroY(),
                    visitado, this.xInicio, this.yInicio)){

                CartaDestino destino = (CartaDestino) vecino;
                destino.girar();
                destino.setDorso(vecino.getImg());
            }

        }
    }

    private Boolean puedeConectar(Carta vecino, CartaTunel carta, Direccion direccion) {
        if (vecino instanceof CartaTunel) {
            return carta.puedeConectar((CartaTunel) vecino, direccion);
        } else if (vecino instanceof CartaDestino) {
            return carta.puedeConectar((CartaDestino) vecino, direccion);
        }
        return false;
    }

    public boolean hayCaminoHastaOro() {

        // llevo una lista de las posiciones del tablero que ya visite
        Set<Posicion> visitado = new HashSet<>();

        // busco si conecta el inicio con la posicion del oro
        return buscarPos(this.xInicio, this.yInicio, visitado, this.xOro, this.yOro);

    }

    private boolean buscarPos(int x, int y, Set<Posicion> visitado, int finx, int finy) {

        // posicion actual
        Posicion actual = new Posicion(x, y);

        //si la posicion ya la visitamos, salimos
        if (visitado.contains(actual)) return false;

        //si la posicion no es una carta, salgo de la busqueda
        Carta cartaActual = getCarta(x, y);
        if (!((cartaActual instanceof CartaTunel) || (cartaActual instanceof CartaDestino))) return false;

        // marco la posicion como visitada
        visitado.add(actual);

        // me fijo si la carta es el oro
        if (x == finx && y == finy) return true;

        // por cada direccion
        for (Direccion direccion : Direccion.values()) {
            int nuevoX = x;
            int nuevoY = y;

            // dependiendo la direccion modifico la coordenada
            switch (direccion) {
                case ARRIBA -> nuevoX--;
                case ABAJO -> nuevoX++;
                case IZQUIERDA -> nuevoY--;
                case DERECHA -> nuevoY++;

            }

            // compruebo si la carta vecina en cierta direccion esta conectada con la actual
            Carta cartaVecina = getCarta(nuevoX, nuevoY);
            if (cartaVecina instanceof CartaDestino vecinoDestino) {
                if (((CartaTunel) cartaActual).puedeConectar(vecinoDestino, direccion)) {
                    // paso a recorrer la vecina
                    if (buscarPos(nuevoX, nuevoY, visitado, finx, finy)) return true;
                }
            } else if (cartaVecina instanceof CartaTunel vecinoTunel) {
                if (((CartaTunel) cartaActual).puedeConectar(vecinoTunel, direccion)) {
                    // paso a recorrer la vecina
                    if (buscarPos(nuevoX, nuevoY, visitado, finx, finy)) return true;
                }
            }
        }

        return false;

    }

    public Carta getCarta(int x, int y) {
        return tablero.get(new Posicion(x, y));
    }

    public void setCarta(Carta c, int x, int y) {
        tablero.put(new Posicion(x, y), c);
    }

    public Map<Posicion, Carta> getCartas() {
        return tablero;
    }
}
