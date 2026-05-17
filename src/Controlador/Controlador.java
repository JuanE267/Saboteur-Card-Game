package Controlador;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Enums.Evento;
import Modelo.Enums.Herramienta;
import Modelo.Enums.TipoAccion;
import Modelo.*;
import Vista.IVistaGrafica;
import Vista.IVistaServidor;
import Vista.VentanaGanador;
import Vista.VentanaServidor;
import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import ar.edu.unlu.rmimvc.servidor.Servidor;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

// Clase que actua como intermediario entre el modelo y la vista
public class Controlador implements IControladorRemoto {
    private IJuego juego;
    private boolean esPartidaCargada = false;
    private VentanaGanador ventanaGanador;

    private IVistaServidor vistaServidor;
    private IVistaGrafica vista;
    private IJugador jugadorCliente;

    // indica si es el creador de la partida
    private boolean esHost = false;

    public <T extends IObservableRemoto> Controlador(T juego) {
        try {
            this.setModeloRemoto(juego);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Controlador() {

    }

    // indica si el juego ya ha sido creado
    public boolean esJuegoCreado() throws RemoteException {
        return (juego.getJugadores().length != 0);
    }

    // conecta usuario con nombre y edad
    public IJugador conectarUsuario(String nombre, int edad) {
        try {
            return this.juego.agregarJugador(nombre, edad);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    // obtiene el jugador por su id
    public IJugador getJugadorActualizado() throws RemoteException {
        for (IJugador j : juego.getJugadores()) {
            if (j.getId() == jugadorCliente.getId()) {
                return j;
            }
        }
        return null;
    }

    // actualiza el jugador cliente con los datos obtenidos del modelo remoto
    public void actualizarJugador() throws RemoteException {
        if (jugadorCliente == null) return;
        for (IJugador j : juego.getJugadores()) {
            if (j.getId() == jugadorCliente.getId()) {
                jugadorCliente = j;
                return;
            }
        }
    }

    // actualizo el jugador por nombre para cuando cargo la partida
    public void actualizarJugadorPorNombre() throws RemoteException {
        if (jugadorCliente == null) return;
        for (IJugador j : juego.getJugadores()) {
            if (j.getNombre().equals(jugadorCliente.getNombre())) {
                jugadorCliente = j;
                return;
            }
        }
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

    // juega una carta
    // creo que el objetivo es innecesario, se podria armar mas simple
    public Boolean jugarUnaCarta(int x, int y, int posCarta, IJugador objetivo, boolean rotada) throws RemoteException {

        // tomo el jugador objetivo actualizado
        if (objetivo != null) {
            for (IJugador j : getJugadores()) {
                if (j.getId() == objetivo.getId()) {
                    objetivo = j;
                }
            }
        }
        return juego.jugarCarta(x, y, posCarta, objetivo, rotada);
    }

    public IJugador getJugadorPorId(int id) throws RemoteException {
        return juego.getJugadorPorId(id);
    }

    // juega la herramienta, y le avisa a la vista para modificar los iconos
    public TipoAccion jugarHerramienta(int posCarta, int idObjetivo, Herramienta herramientaPresionada) throws RemoteException {

        // valido desde el controlador si puedo usar la carta, despues la uso desde el modelo Juego
        jugadorCliente = getJugadorActualizado();
        Carta carta = jugadorCliente.elegirCarta(posCarta);
        boolean pudoSerJugada = false;


        IJugador objetivo = getJugadorPorId(idObjetivo);

        // DEVUELVO SOLO UN TIPOACCION PORQUE SOLO ME INTERESA COMO INICIA
        if (carta instanceof CartaAccion cartaAccion) {

            pudoSerJugada = juego.jugarHerramienta(objetivo, posCarta, herramientaPresionada);

            if (!pudoSerJugada) {
                if (objetivo.getId() != jugadorCliente.getId()) {
                    if (cartaAccion.esReparar()) {
                        return TipoAccion.OBJETIVO_REPARAR;
                    } else {
                        return TipoAccion.OBJETIVO_ROMPER;
                    }
                } else {
                    if (cartaAccion.esReparar()) {
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

        // --- NUEVO: manejar EventoHerramienta ---
        if (o instanceof EventoHerramienta eh) {
            actualizarJugador();
            SwingUtilities.invokeLater(() -> {
                try {
                    // Primero actualizá la vista normalmente
                    vista.actualizar(getTablero(), juego.getJugadores());

                    // Luego mostrá el mensaje
                    String herr = eh.getHerramienta().toString(); // PICO, LINTERNA, VAGONETA
                    if (eh.getTipo() == Evento.HERRAMIENTA_ROTA) {
                        JOptionPane.showMessageDialog(
                                null,
                                eh.getNombreActor() + " le rompió el/la " + herr
                                        + " a " + eh.getNombreAfectado() + "!",
                                "Herramienta rota 🔨",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                eh.getNombreActor() + " le reparó el/la " + herr
                                        + " a " + eh.getNombreAfectado() + "!",
                                "Herramienta reparada ✅",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            return; // importante: no caer al bloque de Evento
        }

        if (o instanceof Evento evento) {
            switch (evento) {
                case INICIAR_PARTIDA -> {
                    actualizarJugador();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vista.mostrarPartida();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case SERVIDOR_NOTIFICA_CLIENTE -> {
                    juego.iniciarPartidaCargadaDesdeCliente(getJugadorActualizado().getNombre());
                    actualizarJugadorPorNombre();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            vista.mostrarPartida();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                }
                case CARGAR_PARTIDA -> {
                    juego.iniciarPartidaCargadaDesdeCliente(getJugadorActualizado().getNombre());
                    actualizarJugadorPorNombre();
                    SwingUtilities.invokeLater(() -> {
                        try {
                            if (vista != null) {
                                vista.mostrarPartida();
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

                            // timer para que empiece la siguiente ronda

                            final int[] segundos = {10};
                            Timer timer = new Timer(1000, null);
                            timer.addActionListener(e -> {
                                segundos[0]--;
                                ventanaGanador.actualizarCuentaRegresiva(segundos[0]);
                                if (segundos[0] <= 0) {
                                    timer.stop();
                                    try {
                                        if (esHost()) {
                                            juego.reiniciarRonda(juego.getRonda() + 1);
                                        }
                                        ventanaGanador.setVisible(false);
                                        vista.mostrarPartida();
                                    } catch (RemoteException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                            timer.start();
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

        if (cantJugadores < 3 || cantJugadores > 10) {
            throw new IllegalStateException("La partida debe tener entre 3 y 10 jugadores para iniciar.");
        }

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

    public void setVistaServidor(VentanaServidor vistaServidor) {
        this.vistaServidor = vistaServidor;
    }

    public void crearPartida(String ipServidor, int puertoCliente) throws RMIMVCException, RemoteException {
        Servidor servidor = new Servidor(ipServidor, 8888);
        servidor.iniciar(new Juego());
        Cliente cliente = new Cliente(ipServidor, puertoCliente, ipServidor, 8888);
        cliente.iniciar(this);
        this.setEsHost();
    }

    public void unirseAPartida(String ipServidor, String ipCliente, int puertoCliente) throws RMIMVCException, RemoteException {
        Cliente cliente = new Cliente(ipServidor, puertoCliente, ipCliente, 8888);
        cliente.iniciar(this);
    }

    public void guardarPartida() throws RemoteException {
        this.juego.guardarPartida();
    }

    public void cargarPartida() throws RemoteException {
        this.esPartidaCargada = true;

        try {
            this.juego.cargarPartida();
        } catch (RemoteException e) {
            this.esPartidaCargada = false;
            throw e;
        }
    }


    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T modelo) throws RemoteException {
        this.juego = (IJuego) modelo;
    }


    public Mazo getMazo() throws RemoteException {
        return juego.getMazo();
    }

    public void setJugadorCliente(IJugador jugador) {
        this.jugadorCliente = jugador;
    }

    public void setEsHost() {
        esHost = true;
    }

    public boolean esHost() {
        return esHost;
    }

    public List<String> obtenerRanking() {
        return Ranking.obtenerRanking();
    }
}