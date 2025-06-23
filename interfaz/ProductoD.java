package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;           
import java.util.ArrayList;  

public class ProductoD extends JFrame {
    private JTextField txtNombre, txtDescripcion, txtProveedor;
    private JComboBox<String> comboEstanteria, comboTipo;
    private JButton btnGuardar, btnCancelar;
    
    // Árboles B+ por categoría
    private static Map<String, BPlusTree> categoryTrees;
    
    // Singleton para asegurar que todos usen la misma instancia
    private static ProductoD instance;
    
    public ProductoD() {
        setTitle("Gestión de Productos");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Inicializar árboles B+ por categoría si no existen
        if (categoryTrees == null) {
            categoryTrees = new HashMap<>();
            categoryTrees.put("Laptops", new BPlusTree());
            categoryTrees.put("Impresoras", new BPlusTree());
            categoryTrees.put("Routers", new BPlusTree());
            categoryTrees.put("PCs", new BPlusTree());
            categoryTrees.put("Monitores", new BPlusTree());
            categoryTrees.put("Proyectores", new BPlusTree());
        }
        
        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Componentes del formulario
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);
        
        gbc.gridx = 1;
        txtDescripcion = new JTextField(20);
        panel.add(txtDescripcion, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Proveedor:"), gbc);
        
        gbc.gridx = 1;
        txtProveedor = new JTextField(20);
        panel.add(txtProveedor, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 1;
        comboTipo = new JComboBox<>(new String[]{"Laptops", "Impresoras", "Routers", "PCs", "Monitores", "Proyectores"});
        panel.add(comboTipo, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Estantería:"), gbc);
        
        gbc.gridx = 1;
        comboEstanteria = new JComboBox<>(new String[]{"A-Laptops", "B-Impresoras", "C-Routers", "D-PCs", "E-Monitores", "F-Proyectores"});
        panel.add(comboEstanteria, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        // Eventos
        btnGuardar.addActionListener(e -> guardarProducto());
        btnCancelar.addActionListener(e -> dispose());
        
        // Agregar componentes al frame
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    public static ProductoD getInstance() {
        if (instance == null) {
            instance = new ProductoD();
        }
        return instance;
    }
    
    private void guardarProducto() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String proveedor = txtProveedor.getText().trim();
        String tipo = (String) comboTipo.getSelectedItem();
        String estanteria = (String) comboEstanteria.getSelectedItem();
        
        if (nombre.isEmpty() || descripcion.isEmpty() || proveedor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear array con datos del producto, incluyendo la estantería
        String[] producto = {nombre, descripcion, proveedor, tipo, estanteria};
        
        // Determinar categoría basada en el tipo de producto
        String categoria = tipo; 
        
        // Insertar en el árbol B+ correspondiente
        if (categoryTrees.containsKey(categoria)) {
            categoryTrees.get(categoria).insert(nombre, producto);
            
            // Notificar a los observers ANTES de mostrar el mensaje de éxito
            ProductoObservable.getInstance().notifyProductAdded(producto);
            
            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this, 
                "Producto guardado exitosamente\n" +
                "Categoría: " + categoria + "\n" +
                "Estantería: " + estanteria,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpiar campos
            txtNombre.setText("");
            txtDescripcion.setText("");
            txtProveedor.setText("");
            comboTipo.setSelectedIndex(0);
            comboEstanteria.setSelectedIndex(0);
            
            dispose(); // Cerrar la ventana
        } else {
            JOptionPane.showMessageDialog(this, 
                "Categoría no encontrada para el tipo de producto: " + categoria, 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para obtener todos los productos de todas las categorías
    public static List<String[]> obtenerTodosLosProductos() {
        List<String[]> todosLosProductos = new ArrayList<>();
        
        if (categoryTrees != null) {
            for (Map.Entry<String, BPlusTree> entry : categoryTrees.entrySet()) {
                String categoria = entry.getKey();
                BPlusTree tree = entry.getValue();
                
                // Usar búsqueda por rango para obtener todos los productos
                // Buscar desde "A" hasta "z" para cubrir todos los nombres posibles
                List<String[]> productosCategoria = tree.rangeSearch("", "zzzzz");
                todosLosProductos.addAll(productosCategoria);
            }
        }
        
        return todosLosProductos;
    }
    
    // Método para buscar productos en una categoría
    public static List<String[]> buscarProductos(String categoria, String nombre) {
        if (categoryTrees != null && categoryTrees.containsKey(categoria)) {
            return categoryTrees.get(categoria).search(nombre);
        }
        return new ArrayList<>();
    }
    
    // Método para búsqueda por rango
    public static List<String[]> buscarPorRango(String categoria, String inicio, String fin) {
        if (categoryTrees != null && categoryTrees.containsKey(categoria)) {
            return categoryTrees.get(categoria).rangeSearch(inicio, fin);
        }
        return new ArrayList<>();
    }
    
    // Método para eliminar un producto
    public static boolean eliminarProducto(String categoria, String nombre) {
        if (categoryTrees != null && categoryTrees.containsKey(categoria)) {
            categoryTrees.get(categoria).delete(nombre);
            return true;
        }
        return false;
    }
    
    // Método para actualizar un producto
    public static boolean actualizarProducto(String nombreOriginal, String[] nuevoProducto) {
        // Primero buscar en qué categoría está el producto original
        for (Map.Entry<String, BPlusTree> entry : categoryTrees.entrySet()) {
            String categoria = entry.getKey();
            BPlusTree tree = entry.getValue();
            
            List<String[]> encontrados = tree.search(nombreOriginal);
            if (!encontrados.isEmpty()) {
                // Eliminar el producto original
                tree.delete(nombreOriginal);
                
                // Insertar el producto actualizado en la nueva categoría
                String nuevaCategoria = nuevoProducto[3]; // El tipo está en el índice 3
                if (categoryTrees.containsKey(nuevaCategoria)) {
                    categoryTrees.get(nuevaCategoria).insert(nuevoProducto[0], nuevoProducto);
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductoD productoD = new ProductoD();
            productoD.setVisible(true);
        });
    }
}

