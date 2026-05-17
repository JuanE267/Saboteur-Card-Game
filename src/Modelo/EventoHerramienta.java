package Modelo;

import Modelo.Enums.Evento;
import Modelo.Enums.Herramienta;
import java.io.Serializable;

public class EventoHerramienta implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Evento tipo;           // HERRAMIENTA_ROTA o HERRAMIENTA_REPARADA
    private final String nombreAfectado; // nombre del jugador cuya herramienta cambió
    private final String nombreActor;    // nombre del jugador que jugó la carta
    private final Herramienta herramienta;

    public EventoHerramienta(Evento tipo, String nombreActor,
                             String nombreAfectado, Herramienta herramienta) {
        this.tipo = tipo;
        this.nombreActor = nombreActor;
        this.nombreAfectado = nombreAfectado;
        this.herramienta = herramienta;
    }

    public Evento getTipo() { return tipo; }
    public String getNombreAfectado() { return nombreAfectado; }
    public String getNombreActor() { return nombreActor; }
    public Herramienta getHerramienta() { return herramienta; }
}