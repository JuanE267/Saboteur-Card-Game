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
    private HashMap<Integer, IJugador> jugadoresCargados;
    private Mazo mazo;
    private Tablero tablero;
    private List<Rol> roles;
    private int turnoInicial;
    private int ronda;
    private List<Integer> ordenTurnos = new ArrayList<>();
    private int turno;
    private IJugador ganador;

    public Juego() {
        this.tablero = new Tablero();
        this.mazo = new Mazo();
        this.jugadores = new HashMap<Integer, IJugador>();
        ronda = 1;
        // asigno los roles y reparto las cartas

    }

    public void iniciarPartida() throws RemoteException {
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
        notificarObservadores(Evento.INICIAR_PARTIDA);
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

            IJugador mayorPuntaje = jugadores.get(0);
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
                    pudoSerJugado = true;
                    // tomo una nueva si el mazo no esta vacio
                    if (!mazo.noHayCartas()) {
                        Carta nuevaCarta = mazo.tomarCarta();
                        actual.getManoCartas().add(nuevaCarta);
                    }
                }
            }

        } else if (carta instanceof CartaAccion) {
            if (((CartaAccion) carta).getTipoAccion().size() == 1) {
                if (actual.jugarCartaMapaDerrumbe(tablero, x, y, carta)) {
                    // despues de jugar elimino la carta de la mano
                    actual.getManoCartas().remove(posCarta);
                    pudoSerJugado = true;
                    // tomo una nueva si el mazo no esta vacio
                    if (!mazo.noHayCartas()) {
                        Carta nuevaCarta = mazo.tomarCarta();
                        actual.getManoCartas().add(nuevaCarta);
                    }
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
            // tomo una nueva si el mazo no esta vacio
            if (!mazo.noHayCartas()) {
                Carta nuevaCarta = mazo.tomarCarta();
                getJugadorActual().getManoCartas().add(nuevaCarta);
            }
        }

        notificarObservadores(Evento.ACTUALIZAR_HERRAMIENTAS);
        return pudoSerJugada;
    }

    public void tomarCartaDeMazo() throws RemoteException {
        Carta nuevaCarta = mazo.tomarCarta();
        getJugadorActual().getManoCartas().add(nuevaCarta);
        notificarObservadores(Evento.TOMAR_CARTA);
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
        switch (getJugadores().length) {
            case 2, 3, 4, 5 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            case 6, 7 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            case 8, 9, 10 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            default -> System.out.println("Minimo 3 jugadores y Maximo 10");
        }
        return null;
    }

    public boolean hayCaminoHastaOro() {
        return tablero.hayCaminoHastaOro();
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
        notificarObservadores(Evento.DESCARTAR_CARTA);
    }

    public IJugador getGanador() {
        return this.ganador;
    }

    // PERSISTENCIA DE PARTIDAS

    @Override
    public boolean cargarPartida() throws RemoteException {
        try {
            FileInputStream fis = new FileInputStream("Data/jugadores.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.jugadoresCargados = (HashMap<Integer, IJugador>) ois.readObject();

            fis = new FileInputStream("Data/mazo.dat");
            ois = new ObjectInputStream(fis);
            this.mazo = (Mazo) ois.readObject();


            fis = new FileInputStream("Data/tablero.dat");
            ois = new ObjectInputStream(fis);
            this.tablero = (Tablero) ois.readObject();

            fis = new FileInputStream("Data/roles.dat");
            ois = new ObjectInputStream(fis);
            this.roles = (List<Rol>) ois.readObject();


            fis = new FileInputStream("Data/turnoInicial.dat");
            DataInputStream dis = new DataInputStream(fis);
            this.turnoInicial = dis.readInt();


            fis = new FileInputStream("Data/ronda.dat");
            dis = new DataInputStream(fis);
            this.ronda = dis.readInt();

            fis = new FileInputStream("Data/ordenTurnos.dat");
            ois = new ObjectInputStream(fis);
            this.ordenTurnos = (List<Integer>) ois.readObject();

            fis = new FileInputStream("Data/turno.dat");
            dis = new DataInputStream(fis);
            this.turno = dis.readInt();

            fis = new FileInputStream("Data/ganador.dat");
            ois = new ObjectInputStream(fis);
            this.ganador = (IJugador) ois.readObject();

            this.notificarObservadores(Evento.CARGAR_PARTIDA);
            ois.close();
            fis.close();

            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean guardarPartida() throws RemoteException {
        try {
            FileOutputStream fos = new FileOutputStream("Data/jugadores.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.jugadores);

            fos = new FileOutputStream("Data/tablero.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.tablero);

            fos = new FileOutputStream("Data/roles.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.roles);

            fos = new FileOutputStream("Data/mazo.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.mazo);


            fos = new FileOutputStream("Data/turnoInicial.dat");
            DataOutputStream dos = new DataOutputStream(fos);
            this.turnoInicial = ordenTurnos.indexOf(getJugadorActual().getId());
            dos.writeInt(this.turnoInicial);

            fos = new FileOutputStream("Data/turno.dat");
            dos = new DataOutputStream(fos);
            dos.writeInt(this.turno);

            fos = new FileOutputStream("Data/ronda.dat");
            dos = new DataOutputStream(fos);
            dos.writeInt(this.ronda);

            fos = new FileOutputStream("Data/ganador.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.ganador);

            fos = new FileOutputStream("Data/ordenTurnos.dat");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.ordenTurnos);
            oos.close();
            dos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}

