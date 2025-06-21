package Observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void agregarObserver(Observer observer){
        observers.add(observer);
    }

    public void notificarObservers(){
        for(Observer o : observers){
            o.actualizar();
        }
    }
}
