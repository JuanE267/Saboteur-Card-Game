package Modelo;

import Modelo.Cartas.Carta;
import Modelo.Cartas.CartaAccion;
import Modelo.Cartas.CartaTunel;
import Modelo.Enums.Evento;
import Modelo.Enums.Herramienta;
import Modelo.Enums.Rol;
import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// Clase que gestiona la logica del juego
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

    // metodo que inicializa la nueva partida
    public void iniciarPartida() throws RemoteException {

        // reseteo los ID de los jugadores
        Jugador.resetID();

        // pongo todos los puntajes en 0
        for (IJugador j : jugadores.values()) {
            j.setPuntaje(0);
        }

        // compruebo la cantidad de jugadores
        int cantJugadores = getJugadores().length;
        if (cantJugadores < 3 || cantJugadores > 10) {
            throw new IllegalStateException(
                    "No se puede iniciar la partida con " + cantJugadores + " jugadores."
            );
        }

        // reparto las cartas a los jugadores
        for (IJugador j : getJugadores()) {
            j.setManoCartas(repartirCartas());
        }

        // limpio el orden de los turnos, y los gestiono
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

        // reparto los datos guardados entre los jugadores
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

    // meotodo para agregar un nuevo jugador
    public IJugador agregarJugador(String nombre, int edad) throws RemoteException {
        IJugador jugador = new Jugador(nombre, edad);
        this.jugadores.put(jugador.getId(), jugador);
        this.notificarObservadores(Evento.NUEVO_USUARIO);
        return jugador;
    }

    // metodo calcular quien es el primer turno
    public void asignoPrimerTurno(int ronda) {
        if (ronda == 1) {
            // el jugador en empezar es el de menor edad
            IJugador menorEdad = jugadores.values().iterator().next();
            for (IJugador j : jugadores.values()) {
                if (j.getEdad() < menorEdad.getEdad()) {
                    menorEdad = j;
                }
            }
            turnoInicial = ordenTurnos.indexOf(menorEdad.getId());

            this.turno = turnoInicial;
        } else {
            // si no es la ronda 1, inicia el jugador a la izquierda
            turnoInicial = (turnoInicial + 1) % ordenTurnos.size();
        }
        this.turno = turnoInicial;
    }

    public Tablero getTablero() {
        return tablero;
    }

    // metodo para repartir roles
    public void asignarRoles() {

        // elimino los roles anteriores
        jugadores.forEach((id, j) -> {
            j.setRol(null);
        });

        // genero los roles dependiendo la cantidad de jugadores
        // asigno los  roles a cada uno
        switch (jugadores.size()) {
            case 3 -> {
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

    // metodo que finaliza la ronda, reparte las pepitas y finaliza la partida
    public void finalizarRonda(boolean ganaronLosMineros) throws RemoteException {


        if (ganaronLosMineros) {
            // obtengo las pepitas de los mineors
            List<Integer> pepitas = pepitasMineros();

            // le doy la primera pepita al que encontro el oro
            if (ganadorRonda != null && ganadorRonda.getRol() == Rol.MINERO) {
                ganadorRonda.sumarPuntos(pepitas.removeFirst());
            }

            // el resto recibe las pepitas en orden x turno
            for (int idTurno : ordenTurnos) {
                IJugador j = jugadores.get(idTurno);
                if (j != null && j.getRol() == Rol.MINERO
                        && (ganadorRonda == null || j.getId() != ganadorRonda.getId())) {
                    if (!pepitas.isEmpty()) {
                        j.sumarPuntos(pepitas.removeFirst());
                    }
                }
            }
        } else {
            // si ganan los saboteadores
            // obtengo las pepitas
            int pepitas = pepitasSaboteadores();

            // y se las reparto a cada saboteador
            for (IJugador j : jugadores.values()) {
                if (j != null && j.getRol() == Rol.SABOTEADOR) {
                    j.sumarPuntos(pepitas);
                }
            }
        }

        if (ronda <= 2) {
            //reinicio la vista dependiendo el ganador
            if (ganaronLosMineros) {
                notificarObservadores(Evento.NUEVA_RONDA_GANADOR_MINEROS);

            } else {
                notificarObservadores(Evento.NUEVA_RONDA_GANADOR_SABOTEADORES);
            }
        } else {
            // finalizar partida
            if (jugadores.isEmpty()) return;

            IJugador mayorPuntaje = jugadores.values().iterator().next();
            for (IJugador j : jugadores.values()) {
                if (j.getPuntaje() > mayorPuntaje.getPuntaje()) {
                    mayorPuntaje = j;
                }
            }
            ganador = mayorPuntaje;

            // para el ranking
            try {
                Ranking.guardarRanking(ganador.getNombre(), ganador.getPuntaje());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if ((ganaronLosMineros)) {
                notificarObservadores(Evento.FINALIZAR_PARTIDA_MINEROS);
            } else {
                notificarObservadores(Evento.FINALIZAR_PARTIDA_SABOTEADORES);
            }
            reiniciarPartida();
        }
    }

    // metodo que devuelve la cantidad de pepitas a repartir entre los mineros
    private List<Integer> pepitasMineros() {
        int cantMineros = 0;
        for (IJugador j : jugadores.values()) {
            if (j.getRol() == Rol.MINERO) cantMineros++;
        }

        switch (cantMineros) {
            case 1 -> {
                return new ArrayList<>(List.of(4));
            }
            case 2 -> {
                return new ArrayList<>(List.of(4, 3));
            }
            case 3 -> {
                return new ArrayList<>(List.of(4, 3, 2));
            }
            case 4 -> {
                return new ArrayList<>(List.of(4, 3, 2, 1));
            }
            case 5 -> {
                return new ArrayList<>(List.of(4, 3, 2, 1, 1));
            }
            case 6 -> {
                return new ArrayList<>(List.of(4, 3, 2, 1, 1, 1));
            }
            case 7 -> {
                return new ArrayList<>(List.of(4, 3, 2, 1, 1, 1, 1));
            }
            default -> {
                return new ArrayList<>(List.of(4));
            }
        }
    }

    private int pepitasSaboteadores() {
        int cantSaboteadores = 0;
        for (IJugador j : jugadores.values()) {
            if (j.getRol() == Rol.SABOTEADOR) cantSaboteadores++;
        }

        switch (cantSaboteadores) {
            case 1 -> {
                return 4;
            }
            case 2, 3 -> {
                return 3;
            }
            case 4 -> {
                return 2;
            }
            default -> {
                return 4;
            }
        }
    }

    @Override
    public int getRonda() throws RemoteException {
        return ronda;
    }

    // metodo que gestiona las cartas jugadas (sin contar herramientas)
    // funciona como intermediario entre el controlador y el modelo
    public Boolean jugarCarta(int x, int y, int posCarta, IJugador objetivo, boolean rotada) throws RemoteException {

        IJugador actual = getJugadorActual();
        Carta carta = actual.elegirCarta(posCarta);
        Boolean pudoSerJugado = false;

        // dependiendo el tipo de la carta juego de cierta manera
        if (carta instanceof CartaTunel cartaTunel) {

            // despues de jugar elimino la carta de la mano, si es que pudo ser jugada
            if (actual.getHerramientasRotas().isEmpty()) {
                if (rotada) {
                    cartaTunel.rotar();
                    carta.setRotada(true);
                }
                if (actual.jugarCarta(tablero, x, y, carta)) {
                    actual.getManoCartas().remove(posCarta);
                    robarCartaAuto(actual);
                    pudoSerJugado = true;
                }
            }

        } else if (carta instanceof CartaAccion cartaAccion) {
            if (cartaAccion.getTipoAccion().size() == 1) {
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

    // metodo para jugar una herramienta
    public boolean jugarHerramienta(IJugador objetivo, int posCarta, Herramienta herramienta) throws RemoteException {

        IJugador actual = getJugadorActual();
        Carta carta = actual.elegirCarta(posCarta);
        objetivo = getJugadorPorId(objetivo.getId());
        boolean pudoSerJugada = actual.jugarCarta(objetivo, carta, herramienta);
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
        return jugadores.get(id);
    }


    public void reiniciarRonda(int ronda) {
        this.ronda = ronda;

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
                    if (mazo.noHayCartas()) break;
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            case 6, 7 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    if (mazo.noHayCartas()) break;
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            case 8, 9, 10 -> {
                List<Carta> mano = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    if (mazo.noHayCartas()) break;
                    mano.add(mazo.tomarCarta());
                }
                return mano;
            }
            default -> throw new IllegalStateException(
                    "Cantidad de jugadores invalida: " + cantJugadores +
                            ". Debe ser entre 2 y 10."
            );
        }
    }

    public boolean hayCaminoHastaOro() {
        return tablero.hayCaminoHastaOro();
    }


    public IJugador getGanadorRonda() {
        return ganadorRonda;
    }

    public boolean noHayCartas() {
        return mazo.noHayCartas();
    }

    public boolean verificarSiTerminoLaRonda() throws RemoteException {
        if (hayCaminoHastaOro()) {
            ganadorRonda = getJugadorActual();
            finalizarRonda(true);
            return true;
        } else if (noHayCartas() && todosLosJugadoresSinCartasTunel()) {
            finalizarRonda(false);
            return true;
        }
        return false;
    }

    private boolean todosLosJugadoresSinCartasTunel() {
        for (IJugador j : jugadores.values()) {
           for(Carta carta : j.getManoCartas()){
               if (carta instanceof CartaTunel) return false;
           }
        }
        return true;
    }

    public void descartarCarta(int posCarta) throws RemoteException {
        getJugadorActual().descartarCarta(posCarta);
        robarCartaAuto(getJugadorActual());
        notificarObservadores(Evento.DESCARTAR_CARTA);
    }

    // metodo para automatizar el robo de carta
    private void robarCartaAuto(IJugador jugador) {
        if (!mazo.noHayCartas()) {
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
            validarCarga();
        } catch (IllegalStateException e) {
            throw new RemoteException(e.getMessage());
        }

        try {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/jugadores.dat"))) {
                this.jugadoresCargados = (HashMap<Integer, IJugador>) ois.readObject();
                this.jugadores = new HashMap<>(this.jugadoresCargados);
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/mazo.dat"))) {
                this.mazo = (Mazo) ois.readObject();
            }

            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/tablero.dat"))) {
                this.tablero = (Tablero) ois.readObject();
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


            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("Data/ganadorRonda.dat"))) {
                this.ganadorRonda = (IJugador) ois.readObject();
            }

            this.notificarObservadores(Evento.CARGAR_PARTIDA);
            return true;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validarCarga() {

        // cargo los jugadoresCargados temporalmente para validar

        HashMap<Integer, IJugador> jugadoresCargadosValidar;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("Data/jugadores.dat"))) {
            jugadoresCargadosValidar = (HashMap<Integer, IJugador>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(
                    "No se pudo cargar la partida. Asegurese de que el archivo de la partida guardada exista y sea valido."
            );
        }

        // validar si la cantidad de jugadores es correcta antes de cargar la partida
        if (jugadores.size() != jugadoresCargadosValidar.size()) {
            throw new IllegalStateException(
                    "La partida guardada tiene una cantidad de jugadores diferente a la actual. No se puede cargar la partida."
            );
        }

        // validar nombres y edades
        for (IJugador cargado : jugadores.values()) {
            boolean coincide = false;
            for (IJugador guardado : jugadoresCargadosValidar.values()) {
                if (guardado.getNombre().equals(cargado.getNombre()) && guardado.getEdad() == cargado.getEdad()) {
                    coincide = true;
                    break;
                }
            }
            if (!coincide) {
                throw new IllegalStateException(
                        "No se pudo cargar la partida. Asegurese de que los jugadores de la partida guardada coincidan con los jugadores actuales."
                );
            }
        }
        return true;
    }

    @Override
    public boolean guardarPartida() throws RemoteException {
        new File("Data").mkdirs();
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
                    new FileOutputStream("Data/mazo.dat"))) {
                oos.writeObject(this.mazo);
            }

            try (DataOutputStream dos = new DataOutputStream(
                    new FileOutputStream("Data/turnoInicial.dat"))) {
                //  this.turnoInicial = ordenTurnos.indexOf(getJugadorActual().getId());
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

            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream("Data/ganadorRonda.dat"))) {
                oos.writeObject(this.ganadorRonda);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}

