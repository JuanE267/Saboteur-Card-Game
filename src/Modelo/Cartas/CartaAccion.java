package Modelo.Cartas;

import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.Enums.TipoCarta;
import Modelo.IJugador;
import Modelo.Tablero;

import java.util.List;

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
    public Boolean jugarCarta(IJugador afectado, Herramienta herramientaPresionada) {

        boolean sePudoArreglar = false;

        for (TipoAccion tipoAccion : tipo) {
            switch (tipoAccion) {
                case ROMPERLINTERNA, ROMPERPICO, ROMPERVAGONETA -> {
                    if(!sePudoArreglar) sePudoArreglar = romperHerramienta(afectado, tipoAccion);
                    else romperHerramienta(afectado, tipoAccion);
                }
                case REPARARLINTERNA, REPARARVAGONETA, REPARARPICO -> {
                    //arreglar solamente si la herramienta coincide
                    Herramienta herramientaTipoAccion = TipoAccionAHerramienta(tipoAccion);
                    if(herramientaPresionada == null || herramientaPresionada == herramientaTipoAccion) {
                        if (!sePudoArreglar) sePudoArreglar = repararHerramienta(afectado, tipoAccion);
                    }
                }
            }
        }
        return sePudoArreglar;
    }

    private Herramienta TipoAccionAHerramienta(TipoAccion tipoAccion) {
        Herramienta herramienta = null;
        switch (tipoAccion) {
            case ROMPERPICO, REPARARPICO -> herramienta = Herramienta.PICO;
            case ROMPERVAGONETA, REPARARVAGONETA -> herramienta = Herramienta.VAGONETA;
            case ROMPERLINTERNA, REPARARLINTERNA -> herramienta = Herramienta.LINTERNA;
        }
        return herramienta;
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
            if (!(((CartaTunel) cartaADerrumbar).getEsInicio()) && !(cartaADerrumbar.esDestino())) {
                tablero.setCarta(null, x, y);
                return true;
            }
        }
        return false;
    }

    // romper herramienta
    private Boolean romperHerramienta(IJugador afectado, TipoAccion tipoAccion) {

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
                return true;
            } else {
                return false;
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
        }
        return false;
    }

    public Boolean verDestino(int x, int y, Tablero tablero) {
        Carta destino = tablero.getCarta(x, y);

        if (destino != null) {
            if ((this.getTipoAccion().getFirst() == TipoAccion.MAPA) && destino.getTipo() == TipoCarta.DESTINO) {
                //((CartaDestino) destino).girar();
                return true;
            }
        }

        return false;
    }

    public boolean esReparar(){
        return getTipoAccion().getFirst().toString().startsWith("REPARAR");
    }
}
