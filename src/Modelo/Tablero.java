package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaDestino;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Direccion;
import Modelo.Enums.TipoCarta;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tablero {

    private final Carta[][] cuadricula = new Carta[17][17];
    private Map<String, Jugador> jugadores;
    private final Integer alto = cuadricula.length;
    private final Integer ancho = cuadricula[0].length;
    private int xInicio;
    private int yInicio;
    private int xOro;
    private int yOro;

    public void inicarTablero() {

        // Creo las cartas iniciales
        CartaTunel inicio = new CartaTunel(0, TipoCarta.TUNEL, "", true);
        Map<Direccion, Boolean> caminosInicio = new HashMap<>();
        caminosInicio.put(Direccion.ARRIBA, true);
        caminosInicio.put(Direccion.ABAJO, true);
        caminosInicio.put(Direccion.IZQUIERDA, true);
        caminosInicio.put(Direccion.DERECHA, true);
        inicio.setCaminos(true, true, true, true);

        CartaDestino oro = new CartaDestino(1, TipoCarta.DESTINO, "", true);
        CartaDestino primerCarbon = new CartaDestino(2, TipoCarta.DESTINO, "", false);
        CartaDestino segundoCarbon = new CartaDestino(3, TipoCarta.DESTINO, "", false);

        // coloco inicio en el centro del tablero
        xInicio = (int) Math.floor((double) alto / 2);
        yInicio = (int) Math.floor((double) ancho / 2);
        colocarCarta(inicio, xInicio, yInicio);

        // genero una posicion aleatoria donde coloco cada destino
        List<CartaDestino> destinos = new ArrayList<>();
        destinos.add(oro);
        destinos.add(primerCarbon);
        destinos.add(segundoCarbon);

        List<int[]> posicionesDestino = new ArrayList<>();
        posicionesDestino.add(new int[]{alto / 2, ancho - 1});
        posicionesDestino.add(new int[]{(alto / 2) - 2, ancho - 1});
        posicionesDestino.add(new int[]{(alto / 2) + 2, ancho - 1});

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
            System.out.println("Posición fuera del tablero");
            return false;
        }
        // si la posicion esta ocupada
        if (cuadricula[x][y] != null) return false;

        // comprobar si la carta puede conectarse (el vecino es tunel y los tuneles coinciden)
        Carta vecinoDerecha = getCarta(x, y + 1);

        boolean puedeConectarDerecha = (vecinoDerecha instanceof CartaTunel)
                && carta.puedeConectar((CartaTunel) vecinoDerecha, Direccion.DERECHA);
        Carta vecinoIzquierda = getCarta(x, y - 1);
        boolean puedeConectarIzquierda = (vecinoIzquierda instanceof CartaTunel)
                && carta.puedeConectar((CartaTunel) vecinoIzquierda, Direccion.IZQUIERDA);
        Carta vecinoAbajo = getCarta(x + 1, y);
        boolean puedeConectarAbajo = (vecinoAbajo instanceof CartaTunel)
                && carta.puedeConectar((CartaTunel) vecinoAbajo, Direccion.ABAJO);
        Carta vecinoArriba = getCarta(x - 1, y);
        boolean puedeConectarArriba = (vecinoArriba instanceof CartaTunel)
                && carta.puedeConectar((CartaTunel) vecinoArriba, Direccion.ARRIBA);

        if (puedeConectarDerecha || puedeConectarIzquierda || puedeConectarAbajo || puedeConectarArriba) {
            cuadricula[x][y] = carta;
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
        if(visitado[x][y]) return false;

        //si la posicion no es una carta, salgo de la busqueda
        Carta cartaActual = getCarta(x,y);
        if(!((cartaActual instanceof CartaTunel) || (cartaActual instanceof CartaDestino))) return false;

        // marco la posicion como visitada
        visitado[x][y] = true;

        // me fijo si la carta es el oro
        if(x == xOro && y == yOro) return true;

        // por cada direccion
        for(Direccion direccion: Direccion.values()){
            int nuevoX = x;
            int nuevoY = y;

            // dependiendo la direccion modifico la coordenada
            switch (direccion){
                case ARRIBA -> nuevoX--;
                case ABAJO -> nuevoX++;
                case IZQUIERDA -> nuevoY--;
                case DERECHA -> nuevoY++;
            }

            // compruebo si la carta vecina en cierta direccion esta conectada con la actual
            CartaTunel cartaVecina = (CartaTunel) getCarta(nuevoX, nuevoY);
            if(((CartaTunel) cartaActual).puedeConectar(cartaVecina, direccion)){
                // paso a recorrer la vecina
                if (buscarOro(nuevoX,nuevoY, visitado)) return true;
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

    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }

    public Carta[][] getCuadricula() {
        return cuadricula;
    }

    public Carta getCarta(int x, int y) {
        // compruebo que este dentro de los limites del tablero
        if (x < 0 || x >= alto || y < 0 || y >= ancho) {
            System.out.println("Posición fuera del tablero");
            return null;
        }
        return cuadricula[x][y];
    }

    public void imprimirTablero() {
        System.out.println("Tablero:");
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                Carta carta = cuadricula[i][j];
                if (i == xInicio && j == yInicio) {
                    System.out.print(" I "); // Carta de inicio
                } else if (carta instanceof CartaDestino) {
                    System.out.print(" D "); // Destino (oro o carbón)
                } else if (carta instanceof CartaTunel) {
                    System.out.print(" C "); // Carta túnel común
                } else {
                    System.out.print(" . "); // Vacío
                }
            }
            System.out.println();
        }
    }


}
