package Vista;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaInicioSesion extends JFrame {

    private JPanel contentPane;
    private JTextField textUsuario;
    private JButton btnEntrar;
    private JTextField textEdad;

    /**
     * Create the frame.
     */
    public VentanaInicioSesion() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 247, 150);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new MigLayout("", "[][]", "[][]10[]"));

        JLabel lblUsuario = new JLabel("Nombre");
        contentPane.add(lblUsuario, "cell 0 0,alignx trailing");

        JLabel lblEdad = new JLabel("Edad");
        contentPane.add(lblEdad, "cell 0 1,alignx trailing");

        textUsuario = new JTextField();
        contentPane.add(textUsuario, "cell 1 0,growx");
        textUsuario.setColumns(10);

        textEdad = new JTextField();
        contentPane.add(textEdad, "cell 1 1, growx");
        textUsuario.setColumns(10);

        btnEntrar = new JButton("Entrar");
        contentPane.add(btnEntrar, "cell 0 2,alignx right");

        SwingUtilities.getRootPane(btnEntrar).setDefaultButton(btnEntrar);

    }

    public void onClickEntrar(ActionListener listener) {
        this.btnEntrar.addActionListener(listener);
    }


    public String getNombreJugador() {
        return this.textUsuario.getText();
    }

    public int getEdadJugador() {
        int edad = 0;
        try {
            edad = Integer.parseInt(this.textEdad.getText());

        }catch (Exception e){
            e.printStackTrace();
        }
        return edad;
    }

}

