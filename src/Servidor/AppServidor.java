package Servidor;

import Controlador.ControladorJuego;
import Modelo.IJuego;
import Modelo.Juego;
import Vista.VentanaServidor;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppServidor {
    public static void main(String[] args) {
        ArrayList<String> ips = Util.getIpDisponibles();

        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

//        String ip = (String) JOptionPane.showInputDialog(
//                null,
//                "Seleccione la IP en la que escuchara peticiones el servidor", "IP del servidor",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                ips.toArray(),
//                null
//        );
//        String port = (String) JOptionPane.showInputDialog(
//                null,
//                "Seleccione el puerto en el que escuchar� peticiones el servidor", "Puerto del servidor",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                null,
//                8888
//        );

        String ip = "127.0.0.1";
        String port = "8888";

        IJuego modelo = new Juego();
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));

        try {
            servidor.iniciar(modelo);

            //AppServidorCliente appServidorCliente = new AppServidorCliente();
            //appServidorCliente.iniciar();


        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Servidor corriendo en ip: " + ip + " port: " + port);

    }

}
