package Observer;

import Modelo.Enums.Evento;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void agregarObserver(Observer observer){
        observers.add(observer);
    }

    public void notificarObservers(Evento evento){
        for(Observer o : this.observers) {
            o.actualizar(evento);
        }
    }
}
