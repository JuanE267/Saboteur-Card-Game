package Modelo;

import java.io.Serializable;

// record es una forma mas simple de crear una clase, sin codigo repetitivo
public record Posicion(int x, int y) implements Serializable {

    private static final long serialVersionUID = 1L;

}
