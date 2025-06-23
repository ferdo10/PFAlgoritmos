package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private Inventario inventario;
    private Reporte reporte;

    public Main() {
        setTitle("Sistema de Gestión y Optimización de Inventarios en Almacén TI");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Barra de menú
        JMenuBar menuBar = new JMenuBar();

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);

        // Menú Productos
        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemRegistrar = new JMenuItem("Registrar Producto");
        itemRegistrar.addActionListener(e -> new ProductoD().setVisible(true));
        
        JMenuItem itemGestionar = new JMenuItem("Gestionar Inventario");
        itemGestionar.addActionListener(e -> {
            if (inventario == null || !inventario.isVisible()) {
                inventario = new Inventario();
            }
            inventario.setVisible(true);
        });
        menuProductos.add(itemRegistrar);
        menuProductos.add(itemGestionar);

        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemGenerar = new JMenuItem("Generar Reporte");
        itemGenerar.addActionListener(e -> {
            if (reporte == null || !reporte.isVisible()) {
                reporte = new Reporte();
            }
            reporte.setVisible(true);
        });
        menuReportes.add(itemGenerar);
        
        // Menú Visualizar
        JMenu menuVisualizar = new JMenu("Visualizar");
        JMenuItem itemEstanterias = new JMenuItem("Ver Estanterías");
        itemEstanterias.addActionListener(e -> new Estanteria().setVisible(true));
        menuVisualizar.add(itemEstanterias);

        // Agregar menús a la barra
        menuBar.add(menuArchivo);
        menuBar.add(menuProductos);
        menuBar.add(menuReportes);
        menuBar.add(menuVisualizar);

        setJMenuBar(menuBar);

        // Panel principal
        JLabel titulo = new JLabel("Sistema de Inventario de Productos TI", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(titulo, BorderLayout.CENTER);

        // Panel de estado
        JLabel estado = new JLabel("Sistema listo", SwingConstants.LEFT);
        estado.setBorder(BorderFactory.createEtchedBorder());
        add(estado, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Configurar el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Crear y mostrar la ventana principal
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.setVisible(true);
        });
    }
}


