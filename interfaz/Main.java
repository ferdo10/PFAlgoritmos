package interfaz;


import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Sistema de Gestión y Optimización de Inventarios en Almacén TI");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra de menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);

        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemRegistrar = new JMenuItem("Registrar Producto");
        itemRegistrar.addActionListener(e -> new ProductoD(this).setVisible(true));
        JMenuItem itemGestionar = new JMenuItem("Gestionar Inventario");
        itemGestionar.addActionListener(e -> new Inventario().setVisible(true));
        menuProductos.add(itemRegistrar);
        menuProductos.add(itemGestionar);

        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemGenerar = new JMenuItem("Generar Reporte");
        itemGenerar.addActionListener(e -> new Reporte().setVisible(true));
        menuReportes.add(itemGenerar);
        
        JMenu menuVisualizar = new JMenu("Visualizar");
        JMenuItem itemEstanterias = new JMenuItem("Ver Estanterías");
        itemEstanterias.addActionListener(e -> new Estanteria().setVisible(true));
        menuVisualizar.add(itemEstanterias);

        menuBar.add(menuArchivo);
        menuBar.add(menuProductos);
        menuBar.add(menuReportes);
        menuBar.add(menuVisualizar);

        setJMenuBar(menuBar);

        JLabel titulo = new JLabel("Sistema de Inventario de Productos TI", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(titulo, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
