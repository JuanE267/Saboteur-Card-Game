package Modelo.Cartas;

import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.Enums.TipoCarta;
import Modelo.Jugador;
import Modelo.Tablero;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class CartaAccion extends Carta {

    private TipoAccion tipo;

    public TipoAccion getTipoAccion() {
        return tipo;
    }

    public void setTipo(TipoAccion tipo) {
        this.tipo = tipo;
    }

    public CartaAccion(int id, TipoCarta tipo, String img) {
        super(id, tipo, img);
    }

    // Acción para romper/reparar herramienta a un jugador
    public void jugarCarta(Jugador afectado) {
        switch (this.tipo){
            case ROMPERLINTERNA, ROMPERPICO, ROMPERVAGONETA -> {
                romperHerramienta(afectado);
            }
            case REPARARLINTERNA, REPARARVAGONETA, REPARARPICO -> {
                repararHerramienta(afectado);
            }
        }
    }

    // Acción para ver un destino del tablero
    public void jugarCarta(int x, int y, Tablero tablero) {
        if (tipo == TipoAccion.MAPA) {
            verDestino(x, y, tablero);
        }
    }

    private void romperHerramienta(Jugador afectado){

        Herramienta herramienta = null;

        switch (this.tipo){
            case ROMPERPICO -> herramienta = Herramienta.PICO;
            case ROMPERVAGONETA -> herramienta = Herramienta.VAGONETA;
            case ROMPERLINTERNA -> herramienta = Herramienta.LINTERNA;
        }

        boolean yaEstaRota = false;
        List<Herramienta> herramientasRotas = afectado.getHerramientasRotas();
        for(Herramienta h: herramientasRotas){
            if(h == herramienta) yaEstaRota = true;
        }
        if(!yaEstaRota){
            herramientasRotas.add(herramienta);
        }else{
            System.out.println("la herramienta del jugador " + afectado.getNombre() + "ya esta rota");
        }
    }

    private void repararHerramienta(Jugador afectado){
        Herramienta herramienta = null;

        switch (this.tipo){
            case REPARARPICO -> herramienta = Herramienta.PICO;
            case REPARARVAGONETA -> herramienta = Herramienta.VAGONETA;
            case REPARARLINTERNA -> herramienta = Herramienta.LINTERNA;
        }

        boolean yaEstaSana = true;
        List<Herramienta> herramientasRotas = afectado.getHerramientasRotas();
        for(Herramienta h: herramientasRotas){
            if(h == herramienta) yaEstaSana = false;
        }
        if(!yaEstaSana){
            herramientasRotas.remove(herramienta);
        }else{
            System.out.println("la herramienta del jugador " + afectado.getNombre() + "ya esta sana");
        }
    }

    public void verDestino( int x, int y, Tablero tablero){
        Carta destino = tablero.getCuadricula()[x][y];

        if((this.getTipoAccion() == TipoAccion.MAPA) && destino.getTipo() == TipoCarta.DESTINO){
            if(((CartaDestino)destino).getEsOro()){
                System.out.println("Es oroooooo");
            }else{
                System.out.println("Es carbon :c");
            }
        }
    }

}
