package Cliente;

import Controlador.ControladorJuego;
import Vista.VentanaServidor;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.Util;
import ar.edu.unlu.rmimvc.cliente.Cliente;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppServidorCliente {
    public void iniciar(){
        ArrayList<String> ips = Util.getIpDisponibles();
        String ip = "192.168.1.33";
        String port = "1";
        String ipServidor = "192.168.1.33";
        String portServidor = "8888";


        ControladorJuego controlador = new ControladorJuego();
        VentanaServidor ventanaServidor = new VentanaServidor(controlador);
        Cliente c = new Cliente(ip, Integer.parseInt(port), ipServidor, Integer.parseInt(portServidor));
        ventanaServidor.iniciar();

        try {
            c.iniciar(controlador);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RMIMVCException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
