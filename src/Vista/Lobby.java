package Vista;
import Controlador.ControladorJuego;
import Modelo.Enums.Evento;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;


public class Lobby extends JFrame {
    private ControladorJuego controladorJuego;
    public Lobby(ControladorJuego controladorJuego) throws RemoteException {
        this.controladorJuego = controladorJuego;
    }




}
