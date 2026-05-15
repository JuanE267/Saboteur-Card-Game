import Controlador.Controlador;
import Vista.PantallaBienvenida;

import javax.swing.*;
import java.rmi.RemoteException;

public class Saboteur {
    public static void main(String[] args) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            Controlador controlador = new Controlador();
            new PantallaBienvenida(controlador);
        });
    }

}
