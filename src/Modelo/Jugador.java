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
    private Rol rol;
    private List<Carta> manoCartas;
    private List<Herramienta> herramientasRotas = new ArrayList<>();
    private Tablero tablero;

    public Jugador(String nombre, Tablero tablero) {
        this.nombre = nombre;
        this.tablero = tablero;
    }

    // jugar tunel
    public void JugarCarta(Tablero tablero) {
        Carta carta = elegirCarta();
        System.out.println("Elija la posicion de la carta");
        Scanner sc = new Scanner(System.in);
        tablero.colocarCarta((CartaTunel) carta, sc.nextInt(), sc.nextInt());
    }

    // jugar reparar o romper
    public void jugarCarta(Jugador afectado) {
        Carta carta = elegirCarta();
        ((CartaAccion) carta).jugarCarta(afectado);
    }

    // jugar mapa
    public void jugarCarta(Tablero tablero) {
        Carta carta = elegirCarta();
        System.out.println("Ingrese la pos de la carta de destino");
        Scanner sc = new Scanner(System.in);
        int x = sc.nextInt();
        int y = sc.nextInt();
        ((CartaAccion) carta).jugarCarta(x,y,tablero);
    }

    public Carta elegirCarta() {
        System.out.println("Elige una carta de tu mazo");
        Scanner sc = new Scanner(System.in);
        int posCarta = sc.nextInt();
        return manoCartas.get(posCarta);
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


}



