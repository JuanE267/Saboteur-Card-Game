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

    private List<TipoAccion> tipo;

    public List<TipoAccion> getTipoAccion() {
        return tipo;
    }

    public void setTipo(List<TipoAccion> tipos) {
        this.tipo = tipos;
    }

    public CartaAccion(int id, TipoCarta tipo, String img) {
        super(id, tipo, img);
    }

    // Acción para romper/reparar herramienta a un jugador
    public void jugarCarta(Jugador afectado) {
        for (TipoAccion tipoAccion : tipo) {
            switch (tipoAccion) {
                case ROMPERLINTERNA, ROMPERPICO, ROMPERVAGONETA -> {
                    romperHerramienta(afectado, tipoAccion);
                }
                case REPARARLINTERNA, REPARARVAGONETA, REPARARPICO -> {
                    repararHerramienta(afectado, tipoAccion);
                }
            }
        }
    }

    // Acción para ver un destino del tablero o derrumbar
    public void jugarCarta(int x, int y, Tablero tablero) {
        if (tipo.getFirst() == TipoAccion.MAPA) {
            verDestino(x, y, tablero);
        } else if (tipo.getFirst() == TipoAccion.DERRUMBAR) {
            DerrumbarTunel(x, y, tablero);
        }
    }

    private void DerrumbarTunel(int x, int y, Tablero tablero) {
        Carta cartaADerrumbar = tablero.getCarta(x, y);
        if (cartaADerrumbar instanceof CartaTunel) {
            if (!(((CartaTunel) cartaADerrumbar).getEsInicio())) {
                ((CartaTunel) cartaADerrumbar).setEstaDerrumbada(true);
                tablero.getCuadricula()[x][y] = null;
            }
        }
    }

    // romper herramienta
    private void romperHerramienta(Jugador afectado, TipoAccion tipoAccion) {

        Herramienta herramienta = null;

        switch (tipoAccion) {
            case ROMPERPICO -> herramienta = Herramienta.PICO;
            case ROMPERVAGONETA -> herramienta = Herramienta.VAGONETA;
            case ROMPERLINTERNA -> herramienta = Herramienta.LINTERNA;
        }

        boolean yaEstaRota = false;
        List<Herramienta> herramientasRotas = afectado.getHerramientasRotas();
        for (Herramienta h : herramientasRotas) {
            if (h == herramienta) yaEstaRota = true;
        }
        if (!yaEstaRota) {
            herramientasRotas.add(herramienta);
        } else {
            System.out.println("la herramienta del jugador " + afectado.getNombre() + "ya esta rota");
        }
    }

    //reparar herramienta
    private void repararHerramienta(Jugador afectado, TipoAccion tipoAccion) {
        Herramienta herramienta = null;

        switch (tipoAccion) {
            case REPARARPICO -> herramienta = Herramienta.PICO;
            case REPARARVAGONETA -> herramienta = Herramienta.VAGONETA;
            case REPARARLINTERNA -> herramienta = Herramienta.LINTERNA;
        }

        boolean yaEstaSana = true;
        List<Herramienta> herramientasRotas = afectado.getHerramientasRotas();
        for (Herramienta h : herramientasRotas) {
            if (h == herramienta) yaEstaSana = false;
        }
        if (!yaEstaSana) {
            herramientasRotas.remove(herramienta);
        } else {
            System.out.println("la herramienta del jugador " + afectado.getNombre() + "ya esta sana");
        }
    }

    public void verDestino(int x, int y, Tablero tablero) {
        Carta destino = tablero.getCarta(x, y);

        if ((this.getTipoAccion().getFirst() == TipoAccion.MAPA) && destino.getTipo() == TipoCarta.DESTINO) {

            ((CartaDestino) destino).girar();
        }
    }

}
