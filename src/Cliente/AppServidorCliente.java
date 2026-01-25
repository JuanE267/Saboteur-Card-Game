package Cliente;

import Controlador.ControladorJuego;
import Controlador.ControladorServer;
import Vista.VentanaServidor;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppServidorCliente {
    public void iniciar(){
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = "192.168.1.44";
        String port = "1099";
        String ipServidor = "192.168.1.44";
        String portServidor = "8888";


        ControladorServer controladorServer = new ControladorServer();
        VentanaServidor ventanaServidor = new VentanaServidor(controladorServer);
        controladorServer.setVistaServidor(ventanaServidor);
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));

        try {
            c.iniciar(controladorServer);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ventanaServidor.iniciar();



    }
}
