package Vista;

import Controlador.ControladorJuego;
import Controlador.ControladorServer;
import Modelo.IJugador;
import Modelo.Jugador;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.rmi.RemoteException;

public class VentanaServidor extends JFrame implements IVistaServidor {
    private ControladorServer controlador;
    private JPanel contentPane;
    private JButton btnIniciarPartida;
    private JButton btnCargarPartida;
    private JButton btnGuardarPartida;
    private JList listJugadores;

    public VentanaServidor(ControladorServer controlador) {
        this.controlador = controlador;
        this.controlador.setVistaServidor(this);
        setTitle("Saboteur - Juan Espinosa");
        setSize(230, 320);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(500, 500);
        setResizable(false);
        setLayout(new BorderLayout());


    }

    private void inicializarVentana() {


        contentPane = new JPanel();
        contentPane.setLayout(new MigLayout("wrap", "[]", "[grow]8[][][]"));


        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, "cell 0 0,grow");

        btnIniciarPartida = new JButton("Iniciar Partida");
        contentPane.add(btnIniciarPartida, "cell 0 1, alignx center ");

        btnCargarPartida = new JButton("Cargar Partida");
        contentPane.add(btnCargarPartida, "cell 0 2, alignx center ");

        btnGuardarPartida = new JButton("Guardar Partida");
        contentPane.add(btnGuardarPartida, "cell 0 3, alignx center ");


        listJugadores = new JList();
        listJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(listJugadores);

        // inicio la partida
        btnIniciarPartida.addActionListener(e -> {
            try {
                controlador.iniciarPartida();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        //guardo la partida
        btnGuardarPartida.addActionListener(e -> {
            try {
                controlador.guardarPartida();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        // cargo la partida

        btnCargarPartida.addActionListener(e -> {
            try {
                controlador.cargarPartida();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });

        add(contentPane);

    }


    public void actualizarListaJugadores(IJugador[] jugadores) {
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
