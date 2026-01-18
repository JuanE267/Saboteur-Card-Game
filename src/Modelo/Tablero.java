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

        // coloco inicio en el centro del tablero
//        xInicio = (int) Math.floor((double) alto / 2);
//        yInicio = 0;

        //prueba para ver si gana la partida
        xInicio = 0;
        yInicio = 0;
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
            setCarta( destino, posicionesDestino.get(indicePos).getX(), posicionesDestino.get(indicePos).getY());
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


    public boolean colocarCarta(CartaTunel carta, int x, int y) {

        // si la posicion esta ocupada
        if (getCarta(x, y) != null) return false;

        // comprobar si la carta puede conectarse (el vecino es tunel y los tuneles coinciden)
        Boolean puedeConectarDerecha = false;
        Boolean puedeConectarIzquierda = false;
        Boolean puedeConectarAbajo = false;
        Boolean puedeConectarArriba = false;
        // DERECHA
        Carta vecinoDerecha = getCarta(x, y + 1);
        puedeConectarDerecha = this.puedeConectar(vecinoDerecha, carta, Direccion.DERECHA);
        if (vecinoDerecha instanceof CartaDestino) {
            ((CartaDestino) vecinoDerecha).girar();
            ((CartaDestino) vecinoDerecha).setDorso(vecinoDerecha.getImg());
            //cuadricula[x][y + 1] = vecinoDerecha;
            setCarta(vecinoDerecha, x, y + 1);
        }
        // IZQUIERDA
        Carta vecinoIzquierda = getCarta(x, y - 1);
        puedeConectarIzquierda = this.puedeConectar(vecinoIzquierda, carta, Direccion.IZQUIERDA);
        if (vecinoIzquierda instanceof CartaDestino) {
            ((CartaDestino) vecinoIzquierda).girar();
            ((CartaDestino) vecinoIzquierda).setDorso(vecinoIzquierda.getImg());
            //cuadricula[x][y - 1] = vecinoIzquierda;
            setCarta(vecinoIzquierda, x, y - 1);
        }
        // ABAJO
        Carta vecinoAbajo = getCarta(x + 1, y);
        puedeConectarAbajo = this.puedeConectar(vecinoAbajo, carta, Direccion.ABAJO);
        if (vecinoAbajo instanceof CartaDestino) {
            ((CartaDestino) vecinoAbajo).girar();
            ((CartaDestino) vecinoAbajo).setDorso(vecinoAbajo.getImg());
            //cuadricula[x + 1][y] = vecinoAbajo;
            setCarta(vecinoAbajo, x + 1, y);
        }
        // ARRIBA
        Carta vecinoArriba = getCarta(x - 1, y);
        puedeConectarArriba = this.puedeConectar(vecinoArriba, carta, Direccion.ARRIBA);
        if (vecinoArriba instanceof CartaDestino) {
            ((CartaDestino) vecinoArriba).girar();
            ((CartaDestino) vecinoArriba).setDorso(vecinoArriba.getImg());
            //cuadricula[x - 1][y] = vecinoArriba;
            setCarta(vecinoArriba, x - 1, y);
        }
        if (carta.getEsInicio() || puedeConectarDerecha || puedeConectarIzquierda || puedeConectarAbajo || puedeConectarArriba) {
            setCarta(carta, x, y);// coloco la carta en la posicion
            return true;
        }
        return false;
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

        return buscarOro(this.xInicio, this.yInicio, visitado);

    }

    private boolean buscarOro(int x, int y, Set<Posicion> visitado) {

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
        if (x == xOro && y == yOro) return true;

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
                    if (buscarOro(nuevoX, nuevoY, visitado)) return true;
                }
            } else if (cartaVecina instanceof CartaTunel vecinoTunel) {
                if (((CartaTunel) cartaActual).puedeConectar(vecinoTunel, direccion)) {
                    // paso a recorrer la vecina
                    if (buscarOro(nuevoX, nuevoY, visitado)) return true;
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

    public Map<Posicion, Carta> getCartas(){ return tablero;}
}
