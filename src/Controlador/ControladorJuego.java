package Controlador;

import Modelo.*;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Vista.*;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

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
            return this.juego.agregarJugador(nombre, edad);
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

    public void actualizarJugador() throws RemoteException {
        for (IJugador j : juego.getJugadores()) {
            if (j.getId() == jugadorCliente.getId()) {
                jugadorCliente = j;
            }
        }
    }

    public IJugador getJugadorCliente() throws RemoteException {
        return jugadorCliente;
    }


    public void setVistaGrafica(IVistaGrafica vista) {
        this.vista = vista;
    }


    public void pasarTurno() throws RemoteException {
        juego.pasarTurno();
    }

    public void verificarSiTerminoLaRonda() throws RemoteException {
        juego.verificarSiTerminoLaRonda();
    }

    public Boolean jugarUnaCarta(int x, int y, int posCarta, IJugador objetivo) throws RemoteException {

        // tomo el jugador objetivo actualizado
        if (objetivo != null) {
            for (IJugador j : getJugadores()) {
                if (j.getId() == objetivo.getId()) {
                    objetivo = j;
                }
            }
        }
        return juego.jugarCarta(x, y, posCarta, objetivo);
    }

    public IJugador getJugadorPorId(int id) throws RemoteException {
        return juego.getJugadorPorId(id);
    }

    public TipoAccion jugarHerramienta(int posCarta, int idObjetivo) throws RemoteException {

        // valido desde el controlador si puedo usar la carta, despues la uso desde el modelo Juego
        Carta carta = jugadorCliente.elegirCarta(posCarta);


        IJugador objetivo = getJugadorPorId(idObjetivo);

        if (carta instanceof CartaAccion) {
            // si el objetivo no es el mismo jugador
            if (objetivo.getId() != jugadorCliente.getId()) {

                if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                    // reviso si tiene cartas rotas
                    if (!(objetivo.getHerramientasRotas().isEmpty())) {
                        juego.jugarHerramienta(objetivo, posCarta);
                    } else {
                        return TipoAccion.OBJETIVO_REPARAR_PICO;
                    }
                } else {
                    for(Herramienta herr : objetivo.getHerramientasRotas()){
                        if(((CartaAccion) carta).getTipoAccion().toString().endsWith(herr.toString())){
                            return TipoAccion.OBJETIVO_ROMPER_PICO;
                        }
                    }
                    juego.jugarHerramienta(objetivo,posCarta);
                }
            }
            // es el mismo jugador pero la carta es de reparar
            else if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                // reviso si tiene cartas rotas
                if (!(jugadorCliente.getHerramientasRotas().isEmpty())) {
                    juego.jugarHerramienta(objetivo, posCarta);
                } else {
                    return TipoAccion.REPARARPICO;
                }
            } else {
                return TipoAccion.ROMPERPICO;
            }

            return ((CartaAccion) carta).getTipoAccion().getFirst();
        }
        return null;
    }

    public IJugador getJugadorActual() throws RemoteException {
        return juego.getJugadorActual();
    }

    public int getTurnoActual() throws RemoteException {
        return juego.getTurnoActual();
    }

    public void descartarCarta(int posCarta) throws RemoteException {
        if (posCarta != -1) {
            juego.descartarCarta(posCarta);
        }
    }

    public Boolean tomarCartaDeMazo() throws RemoteException {
        if (jugadorCliente.getManoCartas().size() < 8) {
            juego.tomarCartaDeMazo();
            return true;
        } else {
            return false;
        }
    }

    public Boolean esTurnoDe(IJugador jugador) throws RemoteException {
        if (jugador == null) return false;
        juego.getJugadorActual();
        return getTurnoActual() == jugador.getId();
    }

    public Tablero getTablero() throws RemoteException {
        return juego.getTablero();
    }


    public IJugador[] getJugadores() throws RemoteException {
        return juego.getJugadores();
    }


    @Override
    public void actualizar(IObservableRemoto iObservableRemoto, Object o) throws RemoteException {
        if (o instanceof Evento evento) {

            switch (evento) {
                case INICIAR_PARTIDA -> {
                    actualizarJugador();
                    iniciarVistaGrafica();
                    vista.mostrarPartida();
                }
                case SERVIDOR_NOTIFICA_CLIENTE -> juego.iniciarPartidaCargadaDesdeCliente(getJugadorActualizado().getNombre(), getJugadorActualizado().getId());
                case PASAR_TURNO, JUGAR_CARTA_TABLERO, ACTUALIZAR_HERRAMIENTAS, TOMAR_CARTA, DESCARTAR_CARTA-> {
                    actualizarJugador();
                    vista.actualizar(getTablero(), juego.getJugadores());
                }

                case NUEVA_RONDA_GANADOR_MINEROS, NUEVA_RONDA_GANADOR_SABOTEADORES -> {
                    actualizarJugador();
                    vista.avisarGanadores(juego.getJugadores(), evento, null, getRonda());
                    vista.actualizar(getTablero(), juego.getJugadores());
                }
                case FINALIZAR_PARTIDA_SABOTEADORES, FINALIZAR_PARTIDA_MINEROS -> {
                    actualizarJugador();
                    vista.avisarGanadores(getJugadores(), evento, getGanador(), getRonda());
                }
            }
        }
    }

    private int getRonda() throws RemoteException {
        return juego.getRonda();
    }

    private void iniciarVistaGrafica() throws RemoteException {
        this.vista = new VistaGrafica(this);
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modelo) throws RemoteException {
        this.juego = (IJuego) modelo;
    }


    public Mazo getMazo() throws RemoteException {
        return juego.getMazo();
    }

    public IJugador getGanador() throws RemoteException {
        return juego.getGanador();
    }

    public void setJugadorCliente(IJugador jugador) {
        this.jugadorCliente = jugador;
    }
}