package Controlador;

import Modelo.Enums.Evento;
import Modelo.IJuego;
import Modelo.IJugador;
import Vista.IVistaServidor;
import Vista.VentanaServidor;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import java.rmi.RemoteException;

public class ControladorServer implements IControladorRemoto {

    private IJuego juego;
    private IVistaServidor vistaServidor;

    public <T extends IObservableRemoto> ControladorServer(T juego) {
            try {
                this.setModeloRemoto(juego);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    public ControladorServer() {
    }

    public void setVistaServidor(VentanaServidor vistaServidor){
        this.vistaServidor = vistaServidor;
        }

    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o == Evento.NUEVO_USUARIO) {
            IJugador[] jugadores = this.juego.getJugadores();
            this.vistaServidor.actualizarListaJugadores(jugadores);
        }
    }

    @Override
    public <T extends IObservableRemoto > void setModeloRemoto(T modelo) throws RemoteException {
        this.juego = (IJuego) modelo;
    }

    public void iniciarPartida() throws RemoteException {
        this.juego.iniciarPartida();
    }
}
