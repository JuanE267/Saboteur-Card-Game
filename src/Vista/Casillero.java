package Vista;

import javax.swing.*;

public class Casillero extends JLabel {

    private int x;
    private int y;

    public Casillero(int x, int i) {
        super();
        this.x = x;
        this.y = i;
    }

    public int posX(){
        return x;
    }
    public int posY(){
        return y;
    }
}
