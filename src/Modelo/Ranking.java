package Modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Clase que gestiona el ranking
public class Ranking {
    private static final String  ARCHIVO_RANKING = "Data/ranking.dat";
    private static final int MAX_RANKING = 5;

    public static List<String> obtenerRanking(){

        // si el archivo no existe, retorno una lista vacia
        if(!new File(ARCHIVO_RANKING).exists()) {
            return new ArrayList<>();
        }

        // abrimos el archivo
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_RANKING))) {
            return (List<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void guardarRanking(String nombre, int puntaje) throws IOException {
       // leo el ranking actual
        List<String> ranking = obtenerRanking();

        // inserto el nuevo puntaje
        ranking.add(nombre + " - " + puntaje);

        // saco el numero despues del -, y ordeno de mayor a menor
        ranking.sort((a, b) -> Integer.compare(
                Integer.parseInt(b.split(" - ")[1]),
                Integer.parseInt(a.split(" - ")[1])
        ));

        // solo dejo 5 puntajes
        if (ranking.size() > MAX_RANKING) {
            ranking = new ArrayList<>(ranking.subList(0, MAX_RANKING));
        }

        // creo el directorio si no existe
        new File("Data").mkdirs();

        // guardo el ranking actualizado
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RANKING))) {
            oos.writeObject(ranking);
    }
}
}
