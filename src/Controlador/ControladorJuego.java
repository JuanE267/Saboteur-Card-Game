package Controlador;

import Modelo.*;
import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.TipoAccion;
import Vista.*;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

import javax.swing.*;
import java.rmi.RemoteException;

public class ControladorJuego implements IControladorRemoto {
    private IJuego juego;
    private boolean esPartidaCargada = false;
    private VentanaGanador ventanaGanador;

    private IVistaServidor vistaServidor;
    private IVistaGrafica vista;
    private IJugador jugadorCliente;
    private boolean esHost = false;

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

    // indica si el juego ya ha sido creado
    public boolean esJuegoCreado() throws RemoteException {
        return (juego.getJugadores().length != 0);
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
        if(jugadorCliente == null) return;
        for (IJugador j : juego.getJugadores()) {
            if (j.getId() == jugadorCliente.getId()) {
                jugadorCliente = j;
                return;
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

    public boolean verificarSiTerminoLaRonda() throws RemoteException {
        return juego.verificarSiTerminoLaRonda();
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
        jugadorCliente = getJugadorActualizado();
        Carta carta = jugadorCliente.elegirCarta(posCarta);
        boolean pudoSerJugada = false;


        IJugador objetivo = getJugadorPorId(idObjetivo);

        // DEVUELVO SOLO UN TIPOACCION PORQUE SOLO ME INTERESA COMO INICIA
        if (carta instanceof CartaAccion) {

            pudoSerJugada = juego.jugarHerramienta(objetivo, posCarta);

            if (!pudoSerJugada) {
                if (objetivo.getId() != jugadorCliente.getId()) {
                    if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                        return TipoAccion.OBJETIVO_REPARAR;
                    } else {
                        return TipoAccion.OBJETIVO_ROMPER;
                    }
                } else {
                    if (((CartaAccion) carta).getTipoAccion().getFirst().toString().startsWith("REPARAR")) {
                        return TipoAccion.REPARARPICO;
                    } else {
                        return TipoAccion.ROMPERPICO;
                    }
                }

            }

            return TipoAccion.JUGADA;
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

    public Boolean esTurnoDe(IJugador jugador) throws RemoteException {
        if (jugador == null) return false;
        IJugador jugadorActual = juego.getJugadorActual();
        return jugadorActual.getId() == jugador.getId();
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
                    SwingUtilities.invokeLater(() -> {
                        try {
                            iniciarVistaGrafica();
                            vista.mostrarPartida();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case SERVIDOR_NOTIFICA_CLIENTE -> {
                    juego.iniciarPartidaCargadaDesdeCliente(getJugadorActualizado().getNombre());
                    actualizarJugador();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            iniciarVistaGrafica();
                            vista.mostrarPartida();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case CARGAR_PARTIDA -> {
                    juego.iniciarPartidaCargadaDesdeCliente(getJugadorActualizado().getNombre());
                    actualizarJugador();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            if (vista == null) {
                                iniciarVistaGrafica();
                                vista.mostrarPartida();
                            } else {
                                vista.actualizar(getTablero(), juego.getJugadores());
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case PASAR_TURNO, JUGAR_CARTA_TABLERO, ACTUALIZAR_HERRAMIENTAS, TOMAR_CARTA, DESCARTAR_CARTA -> {
                    actualizarJugador();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vista.actualizar(getTablero(), juego.getJugadores());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case NUEVA_RONDA_GANADOR_MINEROS, NUEVA_RONDA_GANADOR_SABOTEADORES -> {
                    actualizarJugador();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vista.ocultarPartida();
                            avisarGanadores(evento, juego.getGanadorRonda());
                            vista.actualizar(getTablero(), juego.getJugadores());
                            new Timer(10000, e -> {
                                ((Timer) e.getSource()).stop();
                                try {
                                    ventanaGanador.setVisible(false);
                                    vista.mostrarPartida();
                                } catch (RemoteException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }).start();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case FINALIZAR_PARTIDA_SABOTEADORES, FINALIZAR_PARTIDA_MINEROS -> {
                    actualizarJugador();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vista.ocultarPartida();
                            avisarGanadores(evento, juego.getGanadorRonda());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case NUEVO_USUARIO -> {
                    IJugador[] jugadores = this.juego.getJugadores();
                    SwingUtilities.invokeLater(() -> {
                        if (vistaServidor != null) {
                            vistaServidor.actualizarListaJugadores(jugadores);
                        }
                    });
                }
            }
        }
    }

    public Boolean iniciarPartida() throws RemoteException {
        int cantJugadores = juego.getJugadores().length;

        // ✅ validación una sola vez
        if (cantJugadores < 2 || cantJugadores > 10) {
            JOptionPane.showMessageDialog(null,
                    "Se necesitan entre 2 y 10 jugadores para iniciar.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // ✅ lógica separada y clara
        if (esPartidaCargada) {
            this.juego.iniciarPartidaCargadaDesdeServidor();
        } else {
            this.juego.iniciarPartida();
        }

        return true;
    }

    public void avisarGanadores(Evento evento, IJugador ganadorRonda) throws RemoteException {

        if (ventanaGanador == null) {
            ventanaGanador = new VentanaGanador(this);
        }

        IJugador ganadorActualizado = null;
        int maxPuntos = -1;
        for (IJugador j : juego.getJugadores()) {
            if (j.getPuntaje() > maxPuntos) {
                maxPuntos = j.getPuntaje();
                ganadorActualizado = j;
            }
        }
        ventanaGanador.mostrarVentana(evento, ganadorActualizado, ganadorRonda);
    }

    public IJugador getGanadorRonda() throws RemoteException {
        return juego.getGanadorRonda();
    }


    public void setVistaServidor(VentanaServidor vistaServidor) {
        this.vistaServidor = vistaServidor;
    }

    public void guardarPartida() throws RemoteException {
        this.juego.guardarPartida();
    }

    public void cargarPartida() throws RemoteException {
        this.esPartidaCargada = true;
        this.juego.cargarPartida();
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

    public void setEsHost() {
        esHost = true;
    }
}