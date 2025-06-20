package Vista;

import Controlador.ControladorJuego;
import Modelo.Juego;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame{
   private ControladorJuego controlador;
   private PanelTablero tablero;
   private PanelJugador jugador;

   public Ventana(Juego juego){
      this.controlador = new ControladorJuego(juego);
      setTitle("Saboteur - Juan Espinosa");
      setSize(1800,1000);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setResizable(false);
      setLayout(new BorderLayout());

      PanelTablero tablero = new PanelTablero(juego.getTablero(), jugador);
      add(tablero, BorderLayout.CENTER);

      PanelJugador jugador = new PanelJugador(controlador.getJugadorActual(), tablero);
      add(jugador, BorderLayout.SOUTH);



      setVisible(true);
   }

}
