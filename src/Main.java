import Controlador.ControladorJuego;
import Modelo.Juego;
import Vista.Ventana;

public class Main {
    public static void main(String[] args) {

        Juego juego = new Juego();
        Ventana ventana = new Ventana(new ControladorJuego(juego));
        juego.agregarObserver(ventana);
    }
}
