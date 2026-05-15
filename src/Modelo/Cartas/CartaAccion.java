package Modelo.Cartas;

import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.Enums.TipoCarta;
import Modelo.IJugador;
import Modelo.Tablero;

import java.util.List;

// Clase que representa una carta de accion en el juego
// puede romper/reparar herramientas, ver un destino o derrumbar un tunel
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

    // Accion para romper/reparar herramienta a un jugador
    // herramientaPresionada es la herramienta que se selecciona en la vista
    // utilizo polimorfismo para sobreescribir el metodo jugarCarta dependiendo de la accion
    public Boolean jugarCarta(IJugador afectado, Herramienta herramientaPresionada) {

        boolean sePudoArreglar = false;

        // por cada tipo accion de la carta
        for (TipoAccion tipoAccion : tipo) {
            // dependiendo del tipo accion rompo o arreglo
            switch (tipoAccion) {
                case ROMPERLINTERNA, ROMPERPICO, ROMPERVAGONETA -> {
                    if (!sePudoArreglar) sePudoArreglar = romperHerramienta(afectado, tipoAccion);
                    else romperHerramienta(afectado, tipoAccion);
                }
                case REPARARLINTERNA, REPARARVAGONETA, REPARARPICO -> {
                    //arreglar solamente si la herramienta coincide
                    Herramienta herramientaTipoAccion = TipoAccionAHerramienta(tipoAccion);
                    if (herramientaPresionada == null || herramientaPresionada == herramientaTipoAccion) {
                        if (!sePudoArreglar) sePudoArreglar = repararHerramienta(afectado, tipoAccion);
                    }
                }
            }
        }
        return sePudoArreglar;
    }
    // metodo para convertir un tipo accion en una herramienta
    private Herramienta TipoAccionAHerramienta(TipoAccion tipoAccion) {
        Herramienta herramienta = null;
        switch (tipoAccion) {
            case ROMPERPICO, REPARARPICO -> herramienta = Herramienta.PICO;
            case ROMPERVAGONETA, REPARARVAGONETA -> herramienta = Herramienta.VAGONETA;
            case ROMPERLINTERNA, REPARARLINTERNA -> herramienta = Herramienta.LINTERNA;
        }
        return herramienta;
    }

    // Accion para ver un destino del tablero o derrumbar
    public Boolean jugarCarta(int x, int y, Tablero tablero) {
        Boolean pudoSerJugada = false;

        // si es de tipo mapa
        if (tipo.getFirst() == TipoAccion.MAPA) {
            pudoSerJugada = verDestino(x, y, tablero);

        }
        // si es de tipo derrumbar
        else if (tipo.getFirst() == TipoAccion.DERRUMBAR) {
            pudoSerJugada = DerrumbarTunel(x, y, tablero);
        }
        return pudoSerJugada;
    }

    // metodo para derrumbar una carta tunel del tablero
    private Boolean DerrumbarTunel(int x, int y, Tablero tablero) {
        // obtengo la carta a derrumbar
        Carta cartaADerrumbar = tablero.getCarta(x, y);

        // si la carta es un tunel
        if (cartaADerrumbar instanceof CartaTunel) {
            // si el tunel no es el inicio ni un destino, lo derrumbo
            if (!(((CartaTunel) cartaADerrumbar).getEsInicio()) && !(cartaADerrumbar.esDestino())) {
                // para derrumbar elimino la carta del tablero
                tablero.setCarta(null, x, y);
                return true;
            }
        }
        return false;
    }

    // metodo para romper una herramienta
    private Boolean romperHerramienta(IJugador afectado, TipoAccion tipoAccion) {

        Herramienta herramienta = null;

        // dependiendo el tipo elijo la heramienta
        switch (tipoAccion) {
            case ROMPERPICO -> herramienta = Herramienta.PICO;
            case ROMPERVAGONETA -> herramienta = Herramienta.VAGONETA;
            case ROMPERLINTERNA -> herramienta = Herramienta.LINTERNA;
        }

        // obtengo las herramientas rotas del jugador afectado
        boolean yaEstaRota = false;
        List<Herramienta> herramientasRotas = afectado.getHerramientasRotas();
        // verifico si ya esta rota
        for (Herramienta h : herramientasRotas) {
            if (h == herramienta) {
                yaEstaRota = true;
                break;
            }
        }
        // si no esta rota la rompo (agrego a herramientas rotas del afectado)
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

        // dependiendo el tipo elijo la heramienta
        switch (tipoAccion) {
            case REPARARPICO -> herramienta = Herramienta.PICO;
            case REPARARVAGONETA -> herramienta = Herramienta.VAGONETA;
            case REPARARLINTERNA -> herramienta = Herramienta.LINTERNA;
        }

        //obtengo las herramientas rotas del jugador afectado
        boolean yaEstaSana = true;
        List<Herramienta> herramientasRotas = afectado.getHerramientasRotas();
        // verifico si ya esta sana
        for (Herramienta h : herramientasRotas) {
            if (h == herramienta) {
                yaEstaSana = false;
                break;
            }
        }
        // si no esta sana la reparo (la elimino de herramientas rotas del afectado)
        if (!yaEstaSana) {
            herramientasRotas.remove(herramienta);
            return true;
        }
        return false;
    }

    // Esto se puede achicar pasandolo directamente al metodo jugarCarta creo
    // no lo modifico ahora para no romper nada
    // metodo para ver un destino al utilizar una carta mapa
    public Boolean verDestino(int x, int y, Tablero tablero) {
        // obtengo la carta en la posicion presionada
        Carta destino = tablero.getCarta(x, y);

        // si es un destino,
        if (destino != null) {
            //((CartaDestino) destino).girar();

            // devuelvo si es un destino y la carta accion de tipo mapa
            return (this.getTipoAccion().getFirst() == TipoAccion.MAPA) && destino.getTipo() == TipoCarta.DESTINO;
        }

        return false;
    }

    // metodo para saber si una carta accion es de tipo reparar
    public boolean esReparar() {
        return getTipoAccion().getFirst().toString().startsWith("REPARAR");
    }
}
