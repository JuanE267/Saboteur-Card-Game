package Modelo;

import java.io.Serializable;

public class Posicion implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int x;
    private final int y;

    public Posicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Posicion posicion = (Posicion) obj;

        boolean ret =  x == posicion.x &&  y == posicion.y;

        return ret;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
