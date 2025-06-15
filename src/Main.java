import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Direccion;
import Modelo.Enums.TipoCarta;
import Modelo.Tablero;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Tablero tablero = new Tablero();
        tablero.inicarTablero();

        // Crear una carta túnel simple que conecta ARRIBA y ABAJO
        CartaTunel nuevaCarta = new CartaTunel(10, TipoCarta.TUNEL, "", false, false);
        Map<Direccion, Boolean> caminos = new HashMap<>();
        caminos.put(Direccion.ARRIBA, true);
        caminos.put(Direccion.ABAJO, true);
        caminos.put(Direccion.IZQUIERDA, false);
        caminos.put(Direccion.DERECHA, false);
        nuevaCarta.setCaminos(caminos);

        // Colocarla justo debajo del inicio
        int x = tablero.getxInicio() + 1;
        int y = tablero.getyInicio();

        boolean colocada = tablero.colocarCarta(nuevaCarta, x, y);
        System.out.println("¿Se colocó la carta?: " + colocada);

         x = tablero.getxInicio();
         y = tablero.getyInicio() + 1;

        colocada = tablero.colocarCarta(nuevaCarta, x, y);
        System.out.println("¿Se colocó la carta?: " + colocada);


        // Imprimir el tablero
        tablero.imprimirTablero();

    }
}
