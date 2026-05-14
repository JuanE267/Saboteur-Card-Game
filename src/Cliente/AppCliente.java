package Cliente;

import Controlador.ControladorJuego;
import Vista.PantallaBienvenida;
import Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppCliente {
    public static void main(String[] args) throws RemoteException {
    SwingUtilities.invokeLater(() ->{
            ControladorJuego controlador = new ControladorJuego();
            new PantallaBienvenida(controlador);
    });
    }

}
