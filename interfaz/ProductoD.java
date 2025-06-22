package interfaz;

import javax.swing.*;
import java.awt.*;

public class ProductoD extends JDialog {
	 private JTextField txtNombre;
	    private JTextField txtDescripcion;
	    private JTextField txtProveedor;
	    private JComboBox<String> comboTipo;
	    private JComboBox<String> comboEstanteria;

	    public ProductoD(JFrame parent) {
	        super(parent, "Registro de Producto", true);
	        setSize(400, 350);
	        setLocationRelativeTo(parent);
	        setLayout(new BorderLayout());

	        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
	        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	        panelForm.add(new JLabel("Nombre:"));
	        txtNombre = new JTextField();
	        panelForm.add(txtNombre);

	        panelForm.add(new JLabel("Descripción:"));
	        txtDescripcion = new JTextField();
	        panelForm.add(txtDescripcion);

	        panelForm.add(new JLabel("Proveedor:"));
	        txtProveedor = new JTextField();
	        panelForm.add(txtProveedor);

	        panelForm.add(new JLabel("Tipo:"));
	        comboTipo = new JComboBox<>(new String[]{"CONSUMO", "ACTIVO", "LICENCIA"});
	        panelForm.add(comboTipo);

	        panelForm.add(new JLabel("Estantería:"));
	        comboEstanteria = new JComboBox<>(new String[]{
	            "Laptops", "Impresoras", "Routers",
	            "PCs", "Monitores", "Proyectores"
	        });
	        panelForm.add(comboEstanteria);

	        JPanel panelBotones = new JPanel();
	        JButton btnGuardar = new JButton("Guardar");
	        JButton btnCancelar = new JButton("Cancelar");

	        btnGuardar.addActionListener(e -> {
	            JOptionPane.showMessageDialog(this, "Producto guardado exitosamente\nEstantería: " + comboEstanteria.getSelectedItem());
	            dispose();
	        });

	        btnCancelar.addActionListener(e -> dispose());

	        panelBotones.add(btnGuardar);
	        panelBotones.add(btnCancelar);

	        add(panelForm, BorderLayout.CENTER);
	        add(panelBotones, BorderLayout.SOUTH);
	    }
}

