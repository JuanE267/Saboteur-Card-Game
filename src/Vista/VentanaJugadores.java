package Vista;

import Controlador.ControladorJuego;
import Modelo.Jugador;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class VentanaJugadores extends JFrame {
    private JPanel contentPane;
    private JButton btnIniciarPartida;
    private JList listUsuarios;

    public VentanaJugadores(){
        setTitle("Saboteur - Juan Espinosa");
        setSize(230, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(100,500);
        setResizable(false);
        setLayout(new BorderLayout());

        inicializarVentana();
    }

    private void inicializarVentana() {


        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new MigLayout("wrap","[][]","[]"));


        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, "cell 0 0,grow");

        btnIniciarPartida = new JButton("Iniciar Partida");
        contentPane.add(btnIniciarPartida, "cell 0 1, alignx center ");

        listUsuarios = new JList();
        listUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(listUsuarios);

        add(contentPane);

    }

    public void onClickIniciarPartida(ActionListener listener){
        this.btnIniciarPartida.addActionListener(listener);
    }

    public void actualizarListaJugadores(Jugador[] jugadores) {
        this.listUsuarios.setModel(new AbstractListModel() {
            @Override
            public Object getElementAt(int arg0) {
                return jugadores[arg0].getNombre();
            }
            @Override
            public int getSize() {
                return jugadores.length;
            }
        });
    }



}
