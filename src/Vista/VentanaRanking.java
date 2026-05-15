package Vista;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class VentanaRanking extends JDialog {

    public VentanaRanking(JFrame parent, List<String> ranking) {
        super(parent, "Top 5 Jugadores", true);
        setResizable(false);

        String[] columnas = {"#", "Jugador", "Puntaje"};
        String[][] datos = new String[ranking.size()][3];
        for (int i = 0; i < ranking.size(); i++) {
            String[] partes = ranking.get(i).split(" - ", 2);
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = partes[0];
            datos[i][2] = partes[1];
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setEnabled(false);
        tabla.setFont(new Font("Arial", Font.PLAIN, 16));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        int altoPorFila = 50;
        tabla.setRowHeight(altoPorFila);

        TableColumnModel cols = tabla.getColumnModel();
        cols.getColumn(0).setPreferredWidth(40);
        cols.getColumn(1).setPreferredWidth(220);
        cols.getColumn(2).setPreferredWidth(80);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        cols.getColumn(0).setCellRenderer(centrado);
        cols.getColumn(1).setCellRenderer(centrado);
        cols.getColumn(2).setCellRenderer(centrado);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        // Sin JScrollPane — tamaño exacto para que no aparezca scrollbar
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.add(tabla.getTableHeader(), BorderLayout.NORTH);
        panelTabla.add(tabla, BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);
        add(btnCerrar, BorderLayout.SOUTH);

        // Tamaño calculado exacto
        int ancho = 400;
        int altoTabla = altoPorFila * tabla.getRowCount();
        int altoHeader = tabla.getTableHeader().getPreferredSize().height;
        int altoBoton = 35;
        int altoTitleBar = 30;
        setSize(ancho, altoTabla + altoHeader + altoBoton + altoTitleBar);

        setLocationRelativeTo(parent);
        setVisible(true);
    }
}