package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Direccion;
import Modelo.Enums.TipoCarta;

import java.io.Serializable;
import java.util.*;

public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<Posicion, Carta> tablero;

    private int xInicio;
    private int yInicio;
    private int xOro = -1;
    private int yOro = -1;

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
        xInicio = 0;
        yInicio = 0;

        //prueba para ver si gana la partida
        //xInicio = 0;
        //yInicio = 5;

        colocarCarta(inicio, xInicio, yInicio);

        // genero una posicion aleatoria donde coloco cada destino
        List<CartaDestino> destinos = new ArrayList<>();
        destinos.add(oro);
        destinos.add(primerCarbon);
        destinos.add(segundoCarbon);

        List<Posicion> posicionesDestino = new ArrayList<>();
        posicionesDestino.add(new Posicion(0, 8));
        posicionesDestino.add(new Posicion(-4, 8));
        posicionesDestino.add(new Posicion(4, 8));

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

        // si hay vecino, y no se puede conectar, retorno false
        // tiene que conectar con todos
        if (vecinoArriba != null  && !puedeConectar(vecinoArriba, carta, Direccion.ARRIBA)) {
            return false;
        }
        if (vecinoAbajo != null && !puedeConectar(vecinoAbajo, carta, Direccion.ABAJO)) {
            return false;
        }
        if (vecinoIzquierda != null &&  !puedeConectar(vecinoIzquierda, carta, Direccion.IZQUIERDA)) {
            return false;
        }
        if (vecinoDerecha != null && !puedeConectar(vecinoDerecha, carta, Direccion.DERECHA)) {
            return false;
        }

        // aca compruebo que al menos tenga un vecino
        boolean tieneVecino = (vecinoArriba != null) || (vecinoAbajo != null) || (vecinoDerecha != null) || (vecinoIzquierda != null);
        if (!tieneVecino) return false;

        // si se cumple toddo
        // coloco la carta
        setCarta(carta, x, y);

        Set<Posicion> visitado = new HashSet<>();
        boolean conectaInicio = buscarPos(this.xInicio, this.yInicio, visitado, x, y);

        if (!conectaInicio){
            tablero.remove(new Posicion(x, y));
            return false;
        }

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
            if (c != null && !c.esDestino()) {
                existeVecino = true;
                break;
            }
        }

        if (existeVecino) {

            // ver si conecta al inicio
            Set<Posicion> visitado = new HashSet<>();
            if (buscarPos(this.xInicio, this.yInicio, visitado, x, y)) {
                CartaDestino destino = (CartaDestino) vecino;
                destino.girar();
            }

        }
    }

    private Boolean puedeConectar(Carta vecino, CartaTunel carta, Direccion direccion) {
        if (vecino instanceof CartaTunel cartaTunel) {
            return carta.puedeConectar(cartaTunel, direccion);
        } else if (vecino instanceof CartaDestino cartaDestino) {
            return carta.puedeConectar(cartaDestino, direccion);
        }
        return false;
    }

    public boolean hayCaminoHastaOro() {

        if (xOro == -1 && yOro == -1) return false;

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
