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
    private int puntaje;

    public Jugador(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    // jugar tunel
    public boolean jugarCarta(Tablero tablero,int x, int y, Carta carta) {
        if (puedeConstruir()) {
            return tablero.colocarCarta((CartaTunel) carta, x, y);
        }
        return false;
    }


    // jugar reparar o romper
    public void jugarCarta(Jugador afectado, Carta carta) {
        ((CartaAccion) carta).jugarCarta(afectado);
    }

    // jugar mapa o derrumbe
    public Boolean jugarCartaMapaDerrumbe(Tablero tablero, int x, int y, Carta carta) {
        return ((CartaAccion) carta).jugarCarta(x, y, tablero);
    }

    public Carta elegirCarta(int posCarta) {
        return manoCartas.get(posCarta);
    }

    // descartar carta\
    public Boolean descartarCarta(Carta carta){
        return manoCartas.remove(carta);
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

    public List<Carta>  getManoCartas() {
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

    public void reiniciarEstado(){
        manoCartas.clear();
        herramientasRotas.clear();
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

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }
}



