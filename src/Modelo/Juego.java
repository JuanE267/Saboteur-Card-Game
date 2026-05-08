package Modelo;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Evento;
import Modelo.Enums.Rol;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Juego extends ObservableRemoto implements IJuego {

    private HashMap<Integer, IJugador> jugadores;
    private HashMap<Integer, IJugador> jugadoresCargados = new HashMap<>();
    private Mazo mazo;
    private Tablero tablero;
    private List<Rol> roles;
    private int turnoInicial;
    private int ronda;
    private List<Integer> ordenTurnos = new ArrayList<>();
    private int turno;
    private IJugador ganador;
    private IJugador ganadorRonda;

    public Juego() {
        this.tablero = new Tablero();
        this.mazo = new Mazo();
        this.jugadores = new HashMap<Integer, IJugador>();
        ronda = 1;
        // asigno los roles y reparto las cartas

    }

    public void iniciarPartida() throws RemoteException {

        Jugador.resetID();

        int cantJugadores = getJugadores().length;
        if (cantJugadores < 2 || cantJugadores > 10) {
            throw new IllegalStateException(
                    "No se puede iniciar la partida con " + cantJugadores + " jugadores."
            );
        }

        for (IJugador j : getJugadores()) {
            j.setManoCartas(repartirCartas());
        }
        ordenTurnos.clear();
        ordenTurnos.addAll(jugadores.keySet());
        Collections.sort(ordenTurnos);
        turno = 0;
        asignoPrimerTurno(ronda);
        asignarRoles();
        notificarObservadores(Evento.INICIAR_PARTIDA);
    }

    public void iniciarPartidaCargadaDesdeServidor() throws RemoteException {
        notificarObservadores(Evento.SERVIDOR_NOTIFICA_CLIENTE);
    }

    public void iniciarPartidaCargadaDesdeCliente(String nombreCliente) throws RemoteException {
        for (IJugador jc : getJugadoresCargados()) {
            if (jc.getNombre().equals(nombreCliente)) {
                for (IJugador j : getJugadores()) {
                    if (j.getNombre().equals(nombreCliente)) {
                        j.setPuntaje(jc.getPuntaje());
                        j.setManoCartas(jc.getManoCartas());
                        j.setHerramientasRotas(jc.getHerramientasRotas());
                        j.setRol(jc.getRol());
                        j.setId(jc.getId());
                        j.setEdad(jc.getEdad());
                    }
                }
            }
        }
    }

    public IJugador agregarJugador(String nombre, int edad) throws RemoteException {
        IJugador jugador = new Jugador(nombre, edad);
        this.jugadores.put(jugador.getId(), jugador);
        this.notificarObservadores(Evento.NUEVO_USUARIO);
        return jugador;
    }

    public void asignoPrimerTurno(int ronda) {
        if (ronda == 1) {
            // el jugador en empezar es el de mayor edad
            IJugador mayorEdad = jugadores.values().iterator().next();
            for (IJugador j : jugadores.values()) {
                if (j.getEdad() > mayorEdad.getEdad()) {
                    mayorEdad = j;
                }
            }
            turnoInicial = ordenTurnos.indexOf(mayorEdad.getId());

            this.turno = turnoInicial;
        }
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void asignarRoles() {


        // elimino los roles anteriores
        jugadores.forEach((id, j) -> {
            j.setRol(null);
        });

        // genero los roles dependiendo la cantidad de jugadores
        // asigno los  roles a cada uno
        switch (jugadores.size()) {
            case 1, 2, 3 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 4 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 5 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 6 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 7 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 8 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 9 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
            case 10 -> {
                roles = new ArrayList<>();
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.MINERO);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                roles.add(Rol.SABOTEADOR);
                Collections.shuffle(roles);

                jugadores.forEach((id, j) -> {
                    j.setRol(roles.removeFirst());
                });
            }
        }
    }

    public void pasarTurno() throws RemoteException {
        turno = (turno + 1) % ordenTurnos.size();
        notificarObservadores(Evento.PASAR_TURNO);
    }


    public void finalizarRonda(boolean ganaronLosMineros) throws RemoteException {

        //String mensajeGanador;

        if (ganaronLosMineros) {
            //mensajeGanador = "GANARON LOS MINEROS";

            jugadores.forEach((id, j) -> {
                if (j.getRol() == Rol.MINERO) {
                    j.sumarPuntos(4);
                } else {
                    j.sumarPuntos(3);
                }
            });
        } else {
            // mensajeGanador = "GANARON LOS SABOTEADORES";

            jugadores.forEach((id, j) -> {
                if (j.getRol() == Rol.SABOTEADOR) {
                    j.sumarPuntos(4);
                } else {
                    j.sumarPuntos(3);
                }
            });
        }

        //System.out.println(mensajeGanador);
        //System.out.println("Se revelan los roles..");
        //jugadores.forEach((id, j) -> {
        //    System.out.println(j.getNombre() + " -> " + j.getRol());
        //});

        if (ronda <= 2) {
            // reinicio el estado logico
            reiniciarRonda(ronda);
            //reinicio la vista dependiendo el ganador
            if (ganaronLosMineros) {
                notificarObservadores(Evento.NUEVA_RONDA_GANADOR_MINEROS);
                ronda++;

            } else {
                notificarObservadores(Evento.NUEVA_RONDA_GANADOR_SABOTEADORES);
                ronda++;
            }
        } else {

            if(jugadores.isEmpty()) return;

            IJugador mayorPuntaje = jugadores.values().iterator().next();
            for (IJugador j : jugadores.values()) {
                if (j.getPuntaje() > mayorPuntaje.getPuntaje()) {
                    mayorPuntaje = j;
                }
            }
            ganador = mayorPuntaje;
            if ((ganaronLosMineros)) {
                notificarObservadores(Evento.FINALIZAR_PARTIDA_MINEROS);
            } else {
                notificarObservadores(Evento.FINALIZAR_PARTIDA_SABOTEADORES);
            }
            reiniciarPartida();
        }
    }

    @Override
    public int getRonda() throws RemoteException {
        return ronda;
    }


    public Boolean jugarCarta(int x, int y, int posCarta, IJugador objetivo) throws RemoteException {

        IJugador actual = getJugadorActual();
        Carta carta = actual.elegirCarta(posCarta);
        Boolean pudoSerJugado = false;

        // dependiendo el tipo de la carta juego de cierta manera
        if (carta instanceof CartaTunel) {

            // despues de jugar elimino la carta de la mano, si es que pudo ser jugada
            if (actual.getHerramientasRotas().isEmpty()) {
                if (actual.jugarCarta(tablero, x, y, carta)) {
                    actual.getManoCartas().remove(posCarta);
                    robarCartaAuto(actual);
                    pudoSerJugado = true;
                }
            }

        } else if (carta instanceof CartaAccion) {
            if (((CartaAccion) carta).getTipoAccion().size() == 1) {
                if (actual.jugarCartaMapaDerrumbe(tablero, x, y, carta)) {
                    // despues de jugar elimino la carta de la mano
                    actual.getManoCartas().remove(posCarta);
                    robarCartaAuto(actual);
                    pudoSerJugado = true;
                }
            }

        }
        notificarObservadores(Evento.JUGAR_CARTA_TABLERO);
        return pudoSerJugado;
    }

    public boolean jugarHerramienta(IJugador objetivo, int posCarta) throws RemoteException {

        IJugador actual = getJugadorActual();
        Carta carta = actual.elegirCarta(posCarta);
        objetivo = getJugadorPorId(objetivo.getId());
        boolean pudoSerJugada = actual.jugarCarta(objetivo, carta);
        if (pudoSerJugada) {
            // despues de jugar elimino la carta de la mano
            actual.getManoCartas().remove(carta);
            robarCartaAuto(actual);
        }

        notificarObservadores(Evento.ACTUALIZAR_HERRAMIENTAS);
        return pudoSerJugada;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public IJugador[] getJugadores() {
        return this.jugadores.values().toArray(new IJugador[0]);
    }

    public IJugador[] getJugadoresCargados() {
        return this.jugadoresCargados.values().toArray(new IJugador[0]);
    }

    public IJugador getJugadorActual() {
        if (ordenTurnos.isEmpty()) return null;
        // tomo el id que corresponde al jugador de este turno
        int idActual = ordenTurnos.get(turno);
        // devuelvo el jugador en base al id
        return jugadores.get(idActual);
    }

    public int getTurnoActual() {
        return turno;
    }

    public IJugador getJugadorPorId(int id) throws RemoteException {
        for (int i = 0; i < jugadores.size(); i++) {
            if (getJugadores()[i].getId() == id) {
                return getJugadores()[i];
            }
        }
        return null;
    }


    public void reiniciarRonda(int ronda) {

        Jugador.resetID();

        // reinicio el mazo
        mazo = new Mazo();
        mazo.barajarMazo();
        // reinicio el tablero
        tablero = new Tablero();
        // reinicio jugadores
        for (IJugador j : jugadores.values()) {
            j.reiniciarEstado();
        }

        asignoPrimerTurno(ronda);
        //asigno roles de nuevo
        asignarRoles();
        for (IJugador j : getJugadores()) {
            j.setManoCartas(repartirCartas());
        }

    }

    public void reiniciarPartida() {
        Jugador.resetID();

        // reinicio el mazo
        mazo = new Mazo();
        mazo.barajarMazo();
        // reinicio el tablero
        tablero = new Tablero();
        // reinicio jugadores
        for (IJugador j : jugadores.values()) {
            j.reiniciarEstado();
            j.setPuntaje(0);
        }
        ronda = 1;
        asignoPrimerTurno(ronda);
        //asigno roles de nuevo
        asignarRoles();
        for (IJugador j : getJugadores()) {
            j.setManoCartas(repartirCartas());
        }
    }

    public List<Carta> repartirCartas() {

        //por cada jugador genero una mano y se la doy
        int cantJugadores = getJugadores().length;
        switch (cantJugadores) {
            case 2, 3, 4, 5 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    if(mazo.noHayCartas()) break;
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            case 6, 7 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    if(mazo.noHayCartas()) break;
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            case 8, 9, 10 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if(mazo.noHayCartas()) break;
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            default ->  throw new IllegalStateException(
                    "Cantidad de jugadores inválida: " + cantJugadores +
                            ". Debe ser entre 2 y 10."
            );
        }
    }

    public boolean hayCaminoHastaOro() {
        Boolean hayCamino = tablero.hayCaminoHastaOro();
        if(hayCamino){ ganadorRonda = getJugadorActual(); }
        return hayCamino;
    }


    public IJugador getGanadorRonda(){
        return ganadorRonda;
    }

    public boolean noHayCartas() {
        return mazo.noHayCartas();
    }

    public void verificarSiTerminoLaRonda() throws RemoteException {
        if (hayCaminoHastaOro()) {
            finalizarRonda(true);
        } else if (noHayCartas()) {
            finalizarRonda(false);
        }
    }

    public void descartarCarta(int posCarta) throws RemoteException {
        getJugadorActual().descartarCarta(posCarta);
        robarCartaAuto(getJugadorActual());
        notificarObservadores(Evento.DESCARTAR_CARTA);
    }

    // metodo para automatizar el robo de carta
    private void robarCartaAuto(IJugador jugador){
        if(!mazo.noHayCartas()){
            jugador.getManoCartas().add(mazo.tomarCarta());
        }
    }

    public IJugador getGanador() {
        return this.ganador;
    }

    // PERSISTENCIA DE PARTIDAS

    @Override
    public boolean cargarPartida() throws RemoteException {
        try {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/jugadores.dat"))) {
                this.jugadoresCargados = (HashMap<Integer, IJugador>) ois.readObject();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/mazo.dat"))) {
                this.mazo = (Mazo) ois.readObject();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/tablero.dat"))) {
                this.tablero = (Tablero) ois.readObject();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/roles.dat"))) {
                this.roles = (List<Rol>) ois.readObject();
            }

            try (DataInputStream dis = new DataInputStream(
                    new FileInputStream("Data/turnoInicial.dat"))) {
                this.turnoInicial = dis.readInt();
            }

            try (DataInputStream dis = new DataInputStream(
                    new FileInputStream("Data/ronda.dat"))) {
                this.ronda = dis.readInt();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/ordenTurnos.dat"))) {
                this.ordenTurnos = (List<Integer>) ois.readObject();
            }

            try (DataInputStream dis = new DataInputStream(
                    new FileInputStream("Data/turno.dat"))) {
                this.turno = dis.readInt();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/ganador.dat"))) {
                this.ganador = (IJugador) ois.readObject();
            }

            this.notificarObservadores(Evento.CARGAR_PARTIDA);
            return true;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean guardarPartida() throws RemoteException {
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/jugadores.dat"))) {
                oos.writeObject(this.jugadores);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/tablero.dat"))) {
                oos.writeObject(this.tablero);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/roles.dat"))) {
                oos.writeObject(this.roles);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/mazo.dat"))) {
                oos.writeObject(this.mazo);
            }

            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream("Data/turnoInicial.dat"))) {
                this.turnoInicial = ordenTurnos.indexOf(getJugadorActual().getId());
                dos.writeInt(this.turnoInicial);
            }

            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream("Data/turno.dat"))) {
                dos.writeInt(this.turno);
            }

            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream("Data/ronda.dat"))) {
                dos.writeInt(this.ronda);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/ganador.dat"))) {
                oos.writeObject(this.ganador);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/ordenTurnos.dat"))) {
                oos.writeObject(this.ordenTurnos);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

