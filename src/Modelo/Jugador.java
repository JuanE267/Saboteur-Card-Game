package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Herramienta;
import Modelo.Enums.Rol;
import Modelo.Enums.TipoAccion;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Jugador {

    private String nombre;
    private int edad;
    private Rol rol;
    private List<Carta> manoCartas;
    private List<Herramienta> herramientasRotas = new ArrayList<>();

    public Jugador(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // jugar tunel
    public boolean JugarCarta(Tablero tablero,int x, int y, int posCarta) {
        Carta carta = elegirCarta(posCarta);
        if (puedeConstruir()) {
            return tablero.colocarCarta((CartaTunel) carta, x, y);
        }
        return false;
    }

    // jugar reparar o romper
    public void jugarCarta(Jugador afectado, int posCarta) {
        Carta carta = elegirCarta(posCarta);
        ((CartaAccion) carta).jugarCarta(afectado);
    }

    // jugar mapa
    public void jugarCarta(Tablero tablero, int x, int y, int posCarta) {
        Carta carta = elegirCarta(posCarta);
        ((CartaAccion) carta).jugarCarta(x, y, tablero);
    }

    private Carta elegirCarta(int posCarta) {
        return manoCartas.get(posCarta);
    }

    private Boolean puedeConstruir() {
        return (herramientasRotas.isEmpty());
    }

    public String getNombre() {
        return nombre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Carta> getManoCartas() {
        return manoCartas;
    }

    public void setManoCartas(List<Carta> manoCartas) {
        this.manoCartas = manoCartas;
    }

    public List<Herramienta> getHerramientasRotas() {
        return herramientasRotas;
    }

    public void setHerramientasRotas(List<Herramienta> herramientasRotas) {
        this.herramientasRotas = herramientasRotas;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}



