package Vista;

import Controlador.ControladorJuego;
import Modelo.Juego;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame{
   private ControladorJuego controlador;
   private PanelTablero panelTablero;
   private PanelJugador panelJugador;

   public Ventana(Juego juego){
      this.controlador = new ControladorJuego(juego);
      setTitle("Saboteur - Juan Espinosa");
      setSize(1800,1000);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setResizable(false);
      setLayout(new BorderLayout());

      JPanel contenedorJugador = new JPanel();
      contenedorJugador.setLayout(new BoxLayout(contenedorJugador, BoxLayout.LINE_AXIS));
      add(contenedorJugador, BorderLayout.SOUTH);

      PanelPuntos puntos = new PanelPuntos();
      contenedorJugador.add(puntos);

      PanelJugador panelJugador = new PanelJugador(controlador.getJugadorActual());
      contenedorJugador.add(panelJugador);

      PanelHerramientas herramientas = new PanelHerramientas();
      contenedorJugador.add(herramientas);

      PanelTablero panelTablero = new PanelTablero(panelJugador, controlador);
      add(panelTablero, BorderLayout.CENTER);

      PanelJugadores panelJugadores = new PanelJugadores(panelJugador, controlador);
      add(panelJugadores, BorderLayout.WEST);





      setVisible(true);
   }

}
