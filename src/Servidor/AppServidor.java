package Servidor;

import Cliente.AppServidorCliente;
import Controlador.ControladorJuego;
import Modelo.Juego;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppServidor {
    public static void main(String[] args) {
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione la IP en la que escuchar� peticiones el servidor", "IP del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                ips.toArray(),
                null
        );
        String port = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el puerto en el que escuchar� peticiones el servidor", "Puerto del servidor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                8888
        );

        Juego modelo = new Juego();
        ControladorJuego controladorJuego = new ControladorJuego();
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));
        AppServidorCliente serverCliente = new AppServidorCliente();
        try {
            servidor.iniciar(modelo);
            serverCliente.iniciar();
            System.out.println("Servidor corriendo en ip: " + ip + " port: " + port);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
