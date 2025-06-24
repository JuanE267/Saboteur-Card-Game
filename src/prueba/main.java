package prueba;

import Controlador.ControladorJuego;
import Modelo.Juego;
import Vista.VistaGrafica;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import Cliente.AppServidorCliente;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;


public class main {
    public static void main(String[] args) throws RMIMVCException, RemoteException {

        String ip = "192.168.1.33";
        String port = "8888";

        Juego modelo = new Juego();
        Servidor servidor = new Servidor(ip, Integer.parseInt(port));

        try {
            servidor.iniciar(modelo);

            AppServidorCliente appServidorCliente = new AppServidorCliente();
            appServidorCliente.iniciar();

            System.out.println("Servidor corriendo en ip: " + ip + " port: " + port);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        conectarCliente(ip, "1111", ip, port);
        conectarCliente(ip, "2222", ip, port);
        conectarCliente(ip, "3333", ip, port);

    }

    private static void conectarCliente(String ipServer, String portCliente, String ipCliente, String portServer) throws RemoteException {
        ControladorJuego controlador = new ControladorJuego();
        VistaGrafica vistaGrafica = new VistaGrafica(controlador);
        Cliente c = new Cliente(ipCliente, Integer.parseInt(portCliente), ipServer, Integer.parseInt(portServer));
        try {
            c.iniciar(controlador);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        vistaGrafica.iniciar();
    }
}
