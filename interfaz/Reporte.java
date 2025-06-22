package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Reporte extends JFrame {
    private JTable tablaReporte;
    private DefaultTableModel modelo;

    public Reporte() {
        setTitle("Reporte de Inventario");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnas = {"Nombre", "Descripción", "Proveedor", "Tipo"};
        modelo = new DefaultTableModel(columnas, 0);
        tablaReporte = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tablaReporte);
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnExportar = new JButton("Exportar PDF");
        JButton btnImprimir = new JButton("Imprimir");

        btnExportar.addActionListener(e -> exportarPDF());
        btnImprimir.addActionListener(e -> imprimir());

        panelBotones.add(btnExportar);
        panelBotones.add(btnImprimir);

        add(panelBotones, BorderLayout.SOUTH);

        // Simulación de carga de datos
        cargarDatosSimulados();
    }

    private void exportarPDF() {
        JOptionPane.showMessageDialog(this, "(Simulación) Reporte exportado como PDF.");
    }

    private void imprimir() {
        try {
            tablaReporte.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir el reporte: " + ex.getMessage());
        }
    }

    private void cargarDatosSimulados() {
        modelo.addRow(new Object[]{"Router Cisco", "Router empresarial", "Cisco", "ACTIVO"});
        modelo.addRow(new Object[]{"Laptop Dell", "Equipo portátil para TI", "Dell", "ACTIVO"});
        modelo.addRow(new Object[]{"Switch TP-Link", "Conmutador de red", "TP-Link", "ACTIVO"});
    }
}
