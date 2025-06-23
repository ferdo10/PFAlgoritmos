package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.util.HashMap;
import java.util.Map;

public class Reporte extends JFrame implements ProductoObserver {
    private JTable tablaReporte;
    private DefaultTableModel modelo;
    private Map<String, Integer> conteoPorTipo = new HashMap<>();
    private JTextArea reporteArea;

    public Reporte() {
        setTitle("Reporte de Inventario");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Registrar como observer
        ProductoObservable.getInstance().addObserver(this);
        
        // Modelo de tabla
        String[] columnas = {"Nombre", "Descripción", "Proveedor", "Tipo", "Estantería"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaReporte = new JTable(modelo);
        
        // Área de texto para el reporte
        reporteArea = new JTextArea();
        reporteArea.setEditable(false);
        reporteArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Tabla", new JScrollPane(tablaReporte));
        tabbedPane.addTab("Resumen", new JScrollPane(reporteArea));
        
        // Botones
        JPanel panelBotones = new JPanel();
        JButton btnImprimir = new JButton("Imprimir Reporte");
        btnImprimir.addActionListener(e -> imprimirReporte());
        
        panelBotones.add(btnImprimir);
        
        // Layout principal
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void actualizarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE INVENTARIO ===\n\n");
        sb.append("Total productos: ").append(modelo.getRowCount()).append("\n\n");
        sb.append("Productos por tipo:\n");
        
        conteoPorTipo.forEach((tipo, cantidad) -> {
            sb.append(String.format("- %-15s: %3d\n", tipo, cantidad));
        });
        
        sb.append("\nResumen por estantería:\n");
        // Aquí puedes agregar lógica adicional para resumir por estantería
        
        reporteArea.setText(sb.toString());
    }
    
    @Override
    public void onProductAdded(String[] producto) {
        SwingUtilities.invokeLater(() -> {
            modelo.addRow(producto);
            String tipo = producto[3];
            conteoPorTipo.put(tipo, conteoPorTipo.getOrDefault(tipo, 0) + 1);
            actualizarReporte();
        });
    }
    
    @Override
    public void onProductUpdated(String[] producto) {
        SwingUtilities.invokeLater(() -> {
            // Actualizar conteo por tipo
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if (modelo.getValueAt(i, 0).equals(producto[0])) {
                    String tipoAnterior = (String) modelo.getValueAt(i, 3);
                    String tipoNuevo = producto[3];
                    
                    if (!tipoAnterior.equals(tipoNuevo)) {
                        conteoPorTipo.put(tipoAnterior, conteoPorTipo.get(tipoAnterior) - 1);
                        conteoPorTipo.put(tipoNuevo, conteoPorTipo.getOrDefault(tipoNuevo, 0) + 1);
                    }
                    
                    for (int j = 0; j < producto.length; j++) {
                        modelo.setValueAt(producto[j], i, j);
                    }
                    break;
                }
            }
            actualizarReporte();
        });
    }
    
    @Override
    public void onProductDeleted(String nombreProducto) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if (modelo.getValueAt(i, 0).equals(nombreProducto)) {
                    String tipo = (String) modelo.getValueAt(i, 3);
                    conteoPorTipo.put(tipo, conteoPorTipo.get(tipo) - 1);
                    modelo.removeRow(i);
                    break;
                }
            }
            actualizarReporte();
        });
    }
    
    private void imprimirReporte() {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Reporte de Inventario");
            
            // Configurar el formato de página
            PageFormat pf = job.defaultPage();
            pf.setOrientation(PageFormat.PORTRAIT);
            
            // Crear el printable
            job.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }
                    
                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                    
                    // Encabezado
                    g2d.setFont(new Font("Arial", Font.BOLD, 18));
                    g2d.drawString("REPORTE DE INVENTARIO", 50, 50);
                    
                    // Fecha
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2d.drawString("Fecha: " + new java.util.Date().toString(), 50, 70);
                    
                    // Contenido del reporte
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    int y = 100;
                    
                    // Tabla de productos
                    g2d.drawString("Listado de Productos:", 50, y);
                    y += 20;
                    
                    // Dibujar encabezados de tabla
                    String[] headers = {"Nombre", "Tipo", "Estantería"};
                    int[] columnWidths = {200, 150, 150};
                    int x = 50;
                    
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    for (int i = 0; i < headers.length; i++) {
                        g2d.drawString(headers[i], x, y);
                        x += columnWidths[i];
                    }
                    y += 20;
                    
                    // Dibujar filas de la tabla
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    for (int row = 0; row < Math.min(modelo.getRowCount(), 20); row++) {
                        x = 50;
                        for (int col = 0; col < headers.length; col++) {
                            // Asegurarse de que los índices de columna sean correctos para el modelo
                            // Nombre (0), Tipo (3), Estantería (4)
                            Object value = modelo.getValueAt(row, col == 0 ? 0 : (col == 1 ? 3 : 4));
                            g2d.drawString(value != null ? value.toString() : "", x, y);
                            x += columnWidths[col];
                        }
                        y += 15;
                    }
                    
                    // Resumen
                    y += 30;
                    g2d.setFont(new Font("Arial", Font.BOLD, 14));
                    g2d.drawString("Resumen:", 50, y);
                    y += 20;
                    
                    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
                    for (Map.Entry<String, Integer> entry : conteoPorTipo.entrySet()) {
                        g2d.drawString(entry.getKey() + ": " + entry.getValue(), 70, y);
                        y += 15;
                    }
                    
                    return PAGE_EXISTS;
                }
            });
            
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void dispose() {
        ProductoObservable.getInstance().removeObserver(this);
        super.dispose();
    }
}


