// Estanteria.java
package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Estanteria extends JFrame {
    private final String[] estanterias = {
        "A-Laptops", "B-Impresoras", "C-Routers",
        "D-PCs", "E-Monitores", "F-Proyectores"
    };

    private final Map<String, Object[][]> datosEstanteria = new HashMap<>();

    public Estanteria() {
        setTitle("Visualización de Estanterías");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        inicializarDatosSimulados();

        for (String estanteria : estanterias) {
            JButton btn = new JButton(estanteria);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.addActionListener(e -> mostrarProductos(estanteria));
            add(btn);
        }
    }

    private void mostrarProductos(String estanteria) {
        JFrame frame = new JFrame("Productos en: " + estanteria);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(this);

        String[] columnas = {"Nombre", "Descripción", "Proveedor", "Tipo"};
        Object[][] datos = datosEstanteria.get(estanteria);

        JTable tabla = new JTable(new DefaultTableModel(datos, columnas));
        JScrollPane scroll = new JScrollPane(tabla);

        frame.add(scroll);
        frame.setVisible(true);
    }

    private void inicializarDatosSimulados() {
        datosEstanteria.put("A-Laptops", new Object[][] {
            {"Laptop HP", "Core i5 8GB RAM", "HP", "ACTIVO"},
            {"Laptop Lenovo", "Ryzen 5 16GB RAM", "Lenovo", "ACTIVO"}
        });
        datosEstanteria.put("B-Impresoras", new Object[][] {
            {"Canon Pixma", "Multifuncional", "Canon", "ACTIVO"},
            {"HP LaserJet", "Impresión rápida", "HP", "ACTIVO"}
        });
        datosEstanteria.put("C-Routers", new Object[][] {
            {"TP-Link AC1750", "Dual Band", "TP-Link", "ACTIVO"},
            {"Cisco RV340", "VPN empresarial", "Cisco", "ACTIVO"}
        });
        datosEstanteria.put("D-PCs", new Object[][] {
            {"Dell Optiplex", "Core i7 16GB RAM", "Dell", "ACTIVO"},
            {"HP ProDesk", "Core i5 SSD", "HP", "ACTIVO"}
        });
        datosEstanteria.put("E-Monitores", new Object[][] {
            {"LG 24\"", "Full HD", "LG", "ACTIVO"},
            {"Samsung 27\"", "Curvo", "Samsung", "ACTIVO"}
        });
        datosEstanteria.put("F-Proyectores", new Object[][] {
            {"Epson X41", "3300 lúmenes", "Epson", "ACTIVO"},
            {"BenQ MW560", "WXGA alta resolución", "BenQ", "ACTIVO"}
        });
    }
}

