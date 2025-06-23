package Controlador;

import Modelo.*;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.TipoAccion;
import Vista.*;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import java.util.List;
import java.awt.*;
import java.rmi.RemoteException;

public class ControladorJuego implements IControladorRemoto {
    private IJuego juego;

    private IVistaGrafica vista;
    private IVistaServidor vistaServidor;
    private IJugador jugadorCliente;

    public <T extends IObservableRemoto> ControladorJuego(T juego) {
        try {
            this.setModeloRemoto(juego);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ControladorJuego() {

    }

    public IJugador conectarUsuario(String nombre, int edad) {
        try {
            jugadorCliente = this.juego.agregarJugador(nombre, edad);
            return this.jugadorCliente;
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public IJugador getJugadorActualizado() throws RemoteException {
        for (IJugador j : juego.getJugadores()) {
            if (j.getId() == jugadorCliente.getId()) {
                return j;
            }
        }
        return null;
    }

        public IJugador getJugadorCliente () throws RemoteException {
            return jugadorCliente;
        }


        public void setVistaGrafica (IVistaGrafica vista){
            this.vista = vista;
        }

        public Boolean iniciarPartida () throws RemoteException {
            if (getJugadores().length <= 10 && getJugadores().length >= 1) {
                 juego.iniciarPartida();
                return true;
            } else {
                System.out.println("no hay jugadores suficientes");
                return false;
            }

        }

        public void pasarTurno () throws RemoteException {
            juego.pasarTurno();
        }

        public void verificarSiTerminoLaRonda () throws RemoteException {
            juego.verificarSiTerminoLaRonda();
        }

        public Boolean jugarUnaCarta ( int x, int y, int posCarta, IJugador objetivo) throws RemoteException {
            return juego.jugarCarta(x, y, posCarta, objetivo);
        }

        public TipoAccion jugarHerramienta ( int posCarta, IJugador objetivo) throws RemoteException {

            // valido desde el controlador si puedo usar la carta, despues la uso desde el modelo Juego
            Carta carta = jugadorCliente.elegirCarta(posCarta);

            if (carta instanceof CartaAccion) {
                // si el objetivo no es el mismo jugador
                if (objetivo != jugadorCliente) {
                    juego.jugarHerramienta(objetivo, carta);
                }
                // es el mismo jugador pero la carta es de reparar
                else if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                    // reviso si tiene cartas rotas
                    if (!(jugadorCliente.getHerramientasRotas().isEmpty())) {
                        juego.jugarHerramienta(objetivo, carta);
                    } else {
                        return TipoAccion.REPARARPICO;
                    }
                } else {
                    return TipoAccion.ROMPERPICO;
                }
            }
            return null;
        }

        public IJugador getJugadorActual () throws RemoteException {
            return juego.getJugadorActual();
        }

        public void descartarCarta (Carta carta) throws RemoteException {
            juego.descartarCarta(carta);
        }

        public void tomarCartaDeMazo () throws RemoteException {
            if (jugadorCliente.getManoCartas().size() < 8) {
                juego.tomarCartaDeMazo();
            } else {
                System.out.println("ya tienes el maximo (8) de cartas");
            }
        }

        public Boolean esTurnoDe (IJugador jugador) throws RemoteException {
            if (jugador == null) return false;
            return jugador.equals(jugadorCliente);
        }

        public Tablero getTablero () throws RemoteException {
            return juego.getTablero();
        }


        public IJugador[] getJugadores () throws RemoteException {
            return juego.getJugadores();
        }


    @Override
    public void actualizar (IObservableRemoto iObservableRemoto, Object o)  throws RemoteException {
        if (o instanceof Evento evento) {
            switch (evento) {
                case INICIAR_PARTIDA -> {
                    iniciarVistaGrafica();
                    vista.mostrarPartida();
                }
                case PASAR_TURNO, JUGAR_CARTA_TABLERO, ACTUALIZAR_HERRAMIENTAS, TOMAR_CARTA, DESCARTAR_CARTA, NUEVA_RONDA -> {
                    vista.actualizar();
                }
                case FINALIZAR_PARTIDA -> {
                    vista.getVentanaJuego().mostrarGanador();
                }
            }
        }
    }

    private void iniciarVistaGrafica() throws RemoteException {
        this.vista = new VistaGrafica(this);
    }


    private IJugador getJugadorClienteActualizado() throws RemoteException {
        for (IJugador j : juego.getJugadores()) {
            if (j.getId() == jugadorCliente.getId()) {
                return j;
            }
        }
        return null;
    }

    @Override
        public <T extends IObservableRemoto > void setModeloRemoto (T modelo) throws RemoteException {
            this.juego = (IJuego) modelo;
        }


        public Mazo getMazo () throws RemoteException {
            return juego.getMazo();
        }

        public String getGanador () throws RemoteException {
            return juego.getGanador();
        }

    public void setJugadorCliente(IJugador jugador) {
        this.jugadorCliente = jugador;
    }
}