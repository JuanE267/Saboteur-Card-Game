package Modelo;

import java.io.Serializable;

// record es una forma mas simple de crear una clase, sin codigo repetitivo
public record Posicion(int x, int y) implements Serializable {

    private static final long serialVersionUID = 1L;

    // sobreescribo la comparacion para poder comparar contenido y no referencias en memoria, si es que lo necesito
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Posicion posicion = (Posicion) obj;

        boolean ret = x == posicion.x && y == posicion.y;

        return ret;
    }

}
