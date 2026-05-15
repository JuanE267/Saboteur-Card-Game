package Modelo.Cartas;

import Modelo.Enums.Direccion;
import Modelo.Enums.TipoCarta;

import java.util.HashMap;
import java.util.Map;

// Clase que representa una carta de tunel en el juego
// cada una tiene 4 o menos conexiones y pueden rotarse
public class CartaTunel extends Carta {

    private final Map<Direccion, Boolean> caminos = new HashMap<>();
    private final boolean esInicio;
    private int rotacion;


    public CartaTunel(int id, TipoCarta tipo, String img, boolean esInicio) {
        super(id, tipo, img);
        this.esInicio = esInicio;
    }

    public Map<Direccion, Boolean> getCaminos() {
        return caminos;
    }

    // controla si es posible conectar la carta con los demas tuneles
    // puedeConectar se llama en el tablero cada vez que se intenta colocar una carta
    // pasandole la carta vecina y la direccion
    public Boolean puedeConectar(CartaTunel vecino, Direccion dir) {

        // si no hay carta en la posicion vecina retorno true
        if (vecino == null)
            return (this.esInicio);


        switch (dir) {

            case ABAJO -> {

                // devuelve si los tuneles de cada carta conectan
                return (vecino.getCaminos().get(Direccion.ARRIBA) && this.caminos.get(Direccion.ABAJO));
            }
            case ARRIBA -> {

                return (vecino.getCaminos().get(Direccion.ABAJO) && this.caminos.get(Direccion.ARRIBA));
            }
            case IZQUIERDA -> {

                return (vecino.getCaminos().get(Direccion.DERECHA) && this.caminos.get(Direccion.IZQUIERDA));
            }
            case DERECHA -> {

                return (vecino.getCaminos().get(Direccion.IZQUIERDA) && this.caminos.get(Direccion.DERECHA));
            }
            default -> {
                System.out.println("la direccion no es valida");
                return false;
            }
        }
    }

    public void setCaminos(boolean arriba, boolean abajo,
                           boolean izquierda, boolean derecha) {

        this.caminos.put(Direccion.ARRIBA, arriba);
        this.caminos.put(Direccion.ABAJO, abajo);
        this.caminos.put(Direccion.IZQUIERDA, izquierda);
        this.caminos.put(Direccion.DERECHA, derecha);

        rotacion = 0;
    }

    // rota la carta 180 grados
    public void rotar() {
        boolean arriba = caminos.get(Direccion.ARRIBA);
        boolean abajo = caminos.get(Direccion.ABAJO);
        boolean izquierda = caminos.get(Direccion.IZQUIERDA);
        boolean derecha = caminos.get(Direccion.DERECHA);

        setCaminos(abajo, arriba, derecha, izquierda);

        rotacion = (rotacion + 180) % 360;
    }

    public boolean getEsInicio() {
        return esInicio;
    }

}
