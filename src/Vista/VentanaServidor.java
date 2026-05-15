package Vista;

import Controlador.ControladorJuego;
import Modelo.IJugador;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class VentanaServidor extends JFrame implements IVistaServidor {
    private ControladorJuego controlador;
    private JPanel contentPane;
    private JButton btnIniciarPartida;
    private JButton btnCargarPartida;
    private JButton btnGuardarPartida;
    private JList listaJugadores;

    public VentanaServidor(ControladorJuego controlador) {
        this.controlador = controlador;
        this.controlador.setVistaServidor(this);
        setTitle("Saboteur");
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


        listaJugadores = new JList();
        listaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(listaJugadores);

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
        if(listaJugadores == null) return;
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (IJugador j : jugadores) {
            modelo.addElement(j.getNombre());
        }
        this.listaJugadores.setModel(modelo);
    }




    public void iniciar() {
        inicializarVentana();
        setVisible(true);
    }

    public void actualizar() {
    }
}
