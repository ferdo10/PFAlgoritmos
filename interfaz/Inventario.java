package interfaz;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Inventario extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;

    public Inventario() {
        setTitle("Gestión del Inventario");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnas = {"Nombre", "Descripción", "Proveedor", "Tipo"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnEditar = new JButton("Editar");

        btnEliminar.addActionListener(e -> eliminarProducto());
        btnEditar.addActionListener(e -> editarProducto());

        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            modelo.removeRow(fila);
            JOptionPane.showMessageDialog(this, "Producto eliminado");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
        }
    }

    private void editarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String nombre = (String) modelo.getValueAt(fila, 0);
            String descripcion = (String) modelo.getValueAt(fila, 1);
            String proveedor = (String) modelo.getValueAt(fila, 2);
            String tipo = (String) modelo.getValueAt(fila, 3);

            JTextField txtNombre = new JTextField(nombre);
            JTextField txtDescripcion = new JTextField(descripcion);
            JTextField txtProveedor = new JTextField(proveedor);
            JComboBox<String> comboTipo = new JComboBox<>(new String[]{"CONSUMO", "ACTIVO", "LICENCIA"});
            comboTipo.setSelectedItem(tipo);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
            panel.add(new JLabel("Descripción:")); panel.add(txtDescripcion);
            panel.add(new JLabel("Proveedor:")); panel.add(txtProveedor);
            panel.add(new JLabel("Tipo:")); panel.add(comboTipo);

            int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                modelo.setValueAt(txtNombre.getText(), fila, 0);
                modelo.setValueAt(txtDescripcion.getText(), fila, 1);
                modelo.setValueAt(txtProveedor.getText(), fila, 2);
                modelo.setValueAt(comboTipo.getSelectedItem(), fila, 3);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar");
        }
    }
}
