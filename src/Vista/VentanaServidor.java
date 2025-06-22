package Vista;

import Controlador.ControladorJuego;
import Modelo.Jugador;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class VentanaServidor extends JFrame {
    private ControladorJuego controlador;
    private JPanel contentPane;
    private JButton btnIniciarPartida;
    private JList listJugadores;

    public VentanaServidor(ControladorJuego controlador){
        this.controlador = controlador;
        this.controlador.setVistaServidor(this);
        setTitle("Saboteur - Juan Espinosa");
        setSize(230, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(100,500);
        setResizable(false);
        setLayout(new BorderLayout());


    }

    private void inicializarVentana() {


        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new MigLayout("wrap","[][]","[]"));


        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, "cell 0 0,grow");

        btnIniciarPartida = new JButton("Iniciar Partida");
        contentPane.add(btnIniciarPartida, "cell 0 1, alignx center ");

        listJugadores = new JList();
        listJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(listJugadores);

        // inicio la partida
        btnIniciarPartida.addActionListener(e -> {
            try {
                if(controlador.iniciarPartida()){
                    ocultarVentanaServidor();
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        add(contentPane);

    }

    private void ocultarVentanaServidor() {
        setVisible(false);
    }

    public void actualizarListaJugadores(Jugador[] jugadores) {
        this.listJugadores.setModel(new AbstractListModel() {
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


    public void iniciar() {
        inicializarVentana();
        setVisible(true);
    }

    public void actualizar() {
    }
}
