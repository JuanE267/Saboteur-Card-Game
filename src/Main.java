import Controlador.ControladorJuego;
import Modelo.Juego;
import Vista.Ventana;

public class Main {
    public static void main(String[] args) {

        Juego juego = new Juego();
        ControladorJuego controlador = new ControladorJuego(juego);
        juego.agregarObserver(controlador);
        Ventana ventana = new Ventana(controlador);
    }
}
