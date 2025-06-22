package prueba;

import Controlador.ControladorJuego;
import Modelo.Juego;
import Vista.VistaGrafica;

public class Prueba {
    public static void main(String[] args) {
        Juego modelo = new Juego();
        ControladorJuego controlador = new ControladorJuego(modelo);
        VistaGrafica vista = new VistaGrafica(controlador);
        vista.iniciar();
    }
}
