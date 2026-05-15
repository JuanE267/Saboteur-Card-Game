package Modelo;

import java.io.Serializable;

public record Posicion(int x, int y) implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Posicion posicion = (Posicion) obj;

        boolean ret = x == posicion.x && y == posicion.y;

        return ret;
    }

}
