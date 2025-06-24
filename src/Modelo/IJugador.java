package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Herramienta;
import Modelo.Enums.Rol;

import java.util.List;

public interface IJugador {


    boolean jugarCarta(Tablero tablero, int x, int y, Carta carta);

    boolean jugarCarta(IJugador afectado, Carta carta);

    Boolean jugarCartaMapaDerrumbe(Tablero tablero, int x, int y, Carta carta);

    Carta elegirCarta(int posCarta);

    Boolean descartarCarta(int posCarta);

    String getNombre();

    Rol getRol();

    void setRol(Rol rol);

    List<Carta> getManoCartas();

    void setManoCartas(List<Modelo.Cartas.Carta> manoCartas);

    List<Herramienta> getHerramientasRotas();

    void setHerramientasRotas(List<Herramienta> herramientasRotas);

    void reiniciarEstado();

    int getEdad();

    void setEdad(int edad);

    void setNombre(String nombre);

    int getPuntaje();

    void setPuntaje(int puntaje);

    void sumarPuntos(int puntos);

    int getId();

    void setId(int id);
}