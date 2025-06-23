package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Inventario extends JFrame implements ProductoObserver {
    private DefaultTableModel model;
    private JTable table;
    private JButton btnEditar, btnEliminar;

    public Inventario() {
        setTitle("Gestión de Inventario");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Registrar como observer
        ProductoObservable.getInstance().addObserver(this);
        
        // Modelo de tabla
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable directamente
            }
        };
        model.addColumn("Nombre");
        model.addColumn("Descripción");
        model.addColumn("Proveedor");
        model.addColumn("Tipo");
        model.addColumn("Estantería"); // Asegurarse de que esta columna exista
        
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        
        // Layout principal
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void editarProducto() {
        int fila = table.getSelectedRow();
        if (fila >= 0) {
            // Obtener datos actuales del producto
            String nombreActual = (String) model.getValueAt(fila, 0);
            String descripcionActual = (String) model.getValueAt(fila, 1);
            String proveedorActual = (String) model.getValueAt(fila, 2);
            String tipoActual = (String) model.getValueAt(fila, 3);
            String estanteriaActual = (String) model.getValueAt(fila, 4);

            // Crear campos de edición
            JTextField txtNombre = new JTextField(nombreActual);
            JTextField txtDescripcion = new JTextField(descripcionActual);
            JTextField txtProveedor = new JTextField(proveedorActual);
            JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Laptop", "Impresora", "Router", "PC", "Monitor", "Proyector"});
            comboTipo.setSelectedItem(tipoActual);
            JComboBox<String> comboEstanteria = new JComboBox<>(new String[]{"A-Laptops", "B-Impresoras", "C-Routers", "D-PCs", "E-Monitores", "F-Proyectores"});
            comboEstanteria.setSelectedItem(estanteriaActual);

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
            panel.add(new JLabel("Descripción:")); panel.add(txtDescripcion);
            panel.add(new JLabel("Proveedor:")); panel.add(txtProveedor);
            panel.add(new JLabel("Tipo:")); panel.add(comboTipo);
            panel.add(new JLabel("Estantería:")); panel.add(comboEstanteria);

            int resultado = JOptionPane.showConfirmDialog(this, panel, "Editar Producto", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                // Crear el array de producto actualizado
                String[] productoActualizado = {
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    txtProveedor.getText(),
                    (String) comboTipo.getSelectedItem(),
                    (String) comboEstanteria.getSelectedItem()
                };
                // Notificar a los observers que el producto ha sido actualizado
                ProductoObservable.getInstance().notifyProductUpdated(productoActualizado);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para editar");
        }
    }
    
    private void eliminarProducto() {
        int fila = table.getSelectedRow();
        if (fila >= 0) {
            String nombreProducto = (String) model.getValueAt(fila, 0);
            // Notificar a los observers que el producto ha sido eliminado
            ProductoObservable.getInstance().notifyProductDeleted(nombreProducto);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
        }
    }
    
    @Override
    public void onProductAdded(String[] producto) {
        SwingUtilities.invokeLater(() -> model.addRow(producto));
    }
    
    @Override
    public void onProductUpdated(String[] producto) {
        SwingUtilities.invokeLater(() -> {
            // Buscar el producto por nombre y actualizar sus datos en la tabla
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(producto[0])) { // Comparar por nombre
                    for (int j = 0; j < producto.length; j++) {
                        model.setValueAt(producto[j], i, j);
                    }
                    break;
                }
            }
        });
    }
    
    @Override
    public void onProductDeleted(String nombreProducto) {
        SwingUtilities.invokeLater(() -> {
            // Buscar el producto por nombre y eliminar la fila de la tabla
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).equals(nombreProducto)) {
                    model.removeRow(i);
                    break;
                }
            }
        });
    }
    
    @Override
    public void dispose() {
        ProductoObservable.getInstance().removeObserver(this);
        super.dispose();
    }
}



