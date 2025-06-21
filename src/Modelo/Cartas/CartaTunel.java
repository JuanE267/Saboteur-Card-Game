package Modelo.Cartas;

import Modelo.Enums.Direccion;
import Modelo.Enums.TipoCarta;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CartaTunel extends Carta {

    private Map<Direccion, Boolean> caminos = new HashMap<>();
    private boolean estaDerrumbada;
    private boolean esInicio;

    public CartaTunel(int id, TipoCarta tipo, String img, boolean esInicio) {
        super(id, tipo, img);
        this.esInicio = esInicio;
    }

    public Map<Direccion, Boolean> getCaminos() {
        return caminos;
    }

    // controla si es posible conectar la carta con los demas tuneles
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

    public void setEstaDerrumbada(Boolean derrumbada){
        this.estaDerrumbada = derrumbada;
    }
    public void setCaminos(boolean arriba, boolean abajo,
                           boolean izquierda, boolean derecha) {
        this.caminos.put(Direccion.ARRIBA, arriba);
        this.caminos.put(Direccion.ABAJO, abajo);
        this.caminos.put(Direccion.IZQUIERDA, izquierda);
        this.caminos.put(Direccion.DERECHA, derecha);
    }

    public boolean getEsInicio(){
        return esInicio;
    }

}
