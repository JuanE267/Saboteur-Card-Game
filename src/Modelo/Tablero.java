package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Direccion;
import Modelo.Enums.Rol;
import Modelo.Enums.TipoCarta;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tablero {

    private int ancho = 15;
    private int alto = 9;
    private final Carta[][] cuadricula = new Carta[alto][ancho];
    private Map<String, Jugador> jugadores;
    private int xInicio;
    private int yInicio;
    private int xOro;
    private int yOro;

    public Tablero() {
        inicializarTablero();
    }

    public void inicializarTablero() {

        // Creo las cartas iniciales
        CartaTunel inicio = new CartaTunel(0, TipoCarta.TUNEL, "tuneles/INICIO.png", true);
        Map<Direccion, Boolean> caminosInicio = new HashMap<>();
        caminosInicio.put(Direccion.ARRIBA, true);
        caminosInicio.put(Direccion.ABAJO, true);
        caminosInicio.put(Direccion.IZQUIERDA, true);
        caminosInicio.put(Direccion.DERECHA, true);
        inicio.setCaminos(true, true, true, true);

        CartaDestino oro = new CartaDestino(1, TipoCarta.DESTINO, "tuneles/DESTINO.png", "tuneles/DORSO TUNELES.png", true);
        CartaDestino primerCarbon = new CartaDestino(2, TipoCarta.DESTINO, "tuneles/carbon2.png","tuneles/DORSO TUNELES.png", false);
        CartaDestino segundoCarbon = new CartaDestino(3, TipoCarta.DESTINO, "tuneles/carbon.png","tuneles/DORSO TUNELES.png", false);

        // coloco inicio en el centro del tablero
        xInicio = (int) Math.floor((double) alto / 2);
        yInicio = 0;
        colocarCarta(inicio, xInicio, yInicio);

        // genero una posicion aleatoria donde coloco cada destino
        List<CartaDestino> destinos = new ArrayList<>();
        destinos.add(oro);
        destinos.add(primerCarbon);
        destinos.add(segundoCarbon);

        List<int[]> posicionesDestino = new ArrayList<>();
        posicionesDestino.add(new int[]{(int) Math.floor((double) alto / 2), ancho - 1});
        posicionesDestino.add(new int[]{((int) Math.floor((double) alto / 2) - 2), ancho - 1});
        posicionesDestino.add(new int[]{(int) Math.floor((double) alto / 2) + 2, ancho - 1});

        // mientras que haya cartas destino y posiciones las coloco aleatoriamente
        while (!(destinos.isEmpty()) && !(posicionesDestino.isEmpty())) {
            int indicePos = (int) (Math.random() * posicionesDestino.size());
            int indiceDestinos = (int) (Math.random() * destinos.size());

            CartaDestino destino = destinos.get(indiceDestinos);
            cuadricula[posicionesDestino.get(indicePos)[0]][posicionesDestino.get(indicePos)[1]] = destino;
            if (destino.getEsOro()) {
                destino.setOroX(posicionesDestino.get(indicePos)[0]);
                destino.setOroX(posicionesDestino.get(indicePos)[1]);

                this.xOro = destino.getOroX();
                this.yOro = destino.getOroY();
            }
            destinos.remove(indiceDestinos);
            posicionesDestino.remove(indicePos);
        }

    }


    public boolean colocarCarta(CartaTunel carta, int x, int y) {
        // compruebo que este dentro de los limites del tablero
        if (x < 0 || x >= alto || y < 0 || y >= ancho) {
            System.out.println("Posicion fuera del tablero");
            return false;
        }
        // si la posicion esta ocupada
        if (cuadricula[x][y] != null) return false;

        // comprobar si la carta puede conectarse (el vecino es tunel y los tuneles coinciden)

        Boolean puedeConectarDerecha = false;
        Boolean puedeConectarIzquierda = false;
        Boolean puedeConectarAbajo = false;
        Boolean puedeConectarArriba= false;
        if (y < ancho - 1) {
            Carta vecinoDerecha = getCarta(x, y + 1);
             puedeConectarDerecha = (vecinoDerecha instanceof CartaTunel)
                    && carta.puedeConectar((CartaTunel) vecinoDerecha, Direccion.DERECHA);
        }
        if (y > 0) {
            Carta vecinoIzquierda = getCarta(x, y - 1);
             puedeConectarIzquierda = (vecinoIzquierda instanceof CartaTunel)
                    && carta.puedeConectar((CartaTunel) vecinoIzquierda, Direccion.IZQUIERDA);
        }
        if (x < alto - 1) {
            Carta vecinoAbajo = getCarta(x + 1, y);
             puedeConectarAbajo = (vecinoAbajo instanceof CartaTunel)
                    && carta.puedeConectar((CartaTunel) vecinoAbajo, Direccion.ABAJO);
        }
        if (x > 0) {
            Carta vecinoArriba = getCarta(x - 1, y);
             puedeConectarArriba = (vecinoArriba instanceof CartaTunel)
                    && carta.puedeConectar((CartaTunel) vecinoArriba, Direccion.ARRIBA);
        }
        if (carta.getEsInicio() || puedeConectarDerecha || puedeConectarIzquierda || puedeConectarAbajo || puedeConectarArriba) {
            cuadricula[x][y] = carta;  // coloco la carta en la posicion
            return true;
        }
        System.out.println("la carta no coincide con ningun vecino");
        return false;
    }

    public boolean hayCaminoHastaOro() {

        // llevo una lista de las posiciones del tablero que ya visite
        boolean[][] visitado = new boolean[alto][ancho];

        return buscarOro(this.xInicio, this.yInicio, visitado);

    }

    private boolean buscarOro(int x, int y, boolean[][] visitado) {

        // si la posicion no esta dentro del tablero
        if (x < 0 || x >= alto || y < 0 || y >= ancho) return false;

        //si la posicion ya la visitamos, salimos
        if (visitado[x][y]) return false;

        //si la posicion no es una carta, salgo de la busqueda
        Carta cartaActual = getCarta(x, y);
        if (!((cartaActual instanceof CartaTunel) || (cartaActual instanceof CartaDestino))) return false;

        // marco la posicion como visitada
        visitado[x][y] = true;

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
            CartaTunel cartaVecina = (CartaTunel) getCarta(nuevoX, nuevoY);
            if (((CartaTunel) cartaActual).puedeConectar(cartaVecina, direccion)) {
                // paso a recorrer la vecina
                if (buscarOro(nuevoX, nuevoY, visitado)) return true;
            }
        }

        return false;

    }


    public int getxInicio() {
        return xInicio;
    }

    public int getyInicio() {
        return yInicio;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }

    public Carta[][] getCuadricula() {
        return cuadricula;
    }

    public Carta getCarta(int x, int y) {
        // compruebo que este dentro de los limites del tablero
        if (x < 0 || x >= alto || y < 0 || y >= ancho) {
            System.out.println("Posicion[" + x + "][" + y + "] fuera del tablero");
            return null;
        }
        return cuadricula[x][y];
    }

}
