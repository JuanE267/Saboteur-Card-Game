package Cliente;

import Controlador.ControladorJuego;
import Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppCliente {
    public static void main(String[] args) throws RemoteException {
        ArrayList<String> ips = Util.getIpDisponibles();

//        String ip = (String) JOptionPane.showInputDialog(
//                null,
//                "Seleccione la IP en la que escuchara peticiones el cliente", "IP del cliente",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                ips.toArray(),
//                null
//        );
//        String port = (String) JOptionPane.showInputDialog(
//                null,
//                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                null,
//                9999
//        );
//        String ipServidor = (String) JOptionPane.showInputDialog(
//                null,
//                "Seleccione la IP en la corre el servidor", "IP del servidor",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                null,
//                null
//        );
//        String portServidor = (String) JOptionPane.showInputDialog(
//                null,
//                "Seleccione el puerto en el que corre el servidor", "Puerto del servidor",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                null,
//                8888
//        );

        String ip = "127.0.0.1";

        String port = (String) JOptionPane.showInputDialog(
               null,
                "Seleccione el puerto en el que escuchara peticiones el cliente", "Puerto del cliente",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                9999
        );

        String ipServidor = "127.0.0.1";
        String portServidor = "8888";

        ControladorJuego controlador = new ControladorJuego();
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));



        try {
            c.iniciar(controlador);
            VistaGrafica vistaGrafica = new VistaGrafica(controlador);
            vistaGrafica.iniciar();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "Error de conexión con el servidor.\nVerificá que el servidor esté corriendo y la IP sea correcta.\n\n" + e.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (RMIMVCException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al iniciar el cliente.\n\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }



    }

}
