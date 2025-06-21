package Observer;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void agregarObserver(Observer observer){
        observers.add(observer);
    }

    public void notificarObservers(){
        List<Observer> copiaObservers = new ArrayList<>(observers);
        for(Observer o : copiaObservers){
            o.actualizar();
        }
        observers = copiaObservers;
    }
}
