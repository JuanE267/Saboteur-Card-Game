package Modelo.Cartas;

import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.Enums.TipoCarta;
import Modelo.IJugador;
import Modelo.Jugador;
import Modelo.Tablero;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class CartaAccion extends Carta {

    private static final long serialVersionUID = 1L;
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
    public void jugarCarta(IJugador afectado) {
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
    public Boolean jugarCarta(int x, int y, Tablero tablero) {
        Boolean pudoSerJugada = false;
        if (tipo.getFirst() == TipoAccion.MAPA) {
            pudoSerJugada = verDestino(x, y, tablero);
        } else if (tipo.getFirst() == TipoAccion.DERRUMBAR) {
            pudoSerJugada = DerrumbarTunel(x, y, tablero);
        }
        return pudoSerJugada;
    }

    private Boolean DerrumbarTunel(int x, int y, Tablero tablero) {
        Carta cartaADerrumbar = tablero.getCarta(x, y);
        if (cartaADerrumbar instanceof CartaTunel) {
            if (!(((CartaTunel) cartaADerrumbar).getEsInicio())) {
                tablero.getCuadricula()[x][y] = null;
                return true;
            }
        }
        return false;
    }

    // romper herramienta
    private void romperHerramienta(IJugador afectado, TipoAccion tipoAccion) {

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
    private Boolean repararHerramienta(IJugador afectado, TipoAccion tipoAccion) {
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
            return true;
        } else {
            System.out.println("la herramienta del jugador " + afectado.getNombre() + " ya esta sana");
        }
        return false;
    }

    public Boolean verDestino(int x, int y, Tablero tablero) {
        Carta destino = tablero.getCarta(x, y);

        if (destino != null) {
            if ((this.getTipoAccion().getFirst() == TipoAccion.MAPA) && destino.getTipo() == TipoCarta.DESTINO) {

                ((CartaDestino) destino).girar();
                return true;
            }
        }

        return false;
    }

}
