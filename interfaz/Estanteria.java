// Estanteria.java (ahora con representación de grafo)
package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Estanteria extends JFrame {
    private Map<String, NodoEstanteria> nodos = new HashMap<>();
    private List<Arista> aristas = new ArrayList<>();
    private JPanel panelGrafo;
    private JPanel panelControles;
    
    private static class NodoEstanteria {
        String nombre;
        int x, y;
        List<Arista> conexiones = new ArrayList<>();
        
        public NodoEstanteria(String nombre, int x, int y) {
            this.nombre = nombre;
            this.x = x;
            this.y = y;
        }
    }
    
    private static class Arista {
        NodoEstanteria origen;
        NodoEstanteria destino;
        int peso;
        
        public Arista(NodoEstanteria origen, NodoEstanteria destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }

    public Estanteria() {
        setTitle("Grafo de Estanterías");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Inicializar con datos de ejemplo
        inicializarDatos();
        
        // Panel para dibujar el grafo
        panelGrafo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarGrafo(g);
            }
        };
        
        // Panel de controles
        panelControles = new JPanel();
        configurarControles();
        
        add(panelGrafo, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
    }
    
    private void inicializarDatos() {
        // Nodos iniciales
        nodos.put("A-Laptops", new NodoEstanteria("A-Laptops", 100, 100));
        nodos.put("B-Impresoras", new NodoEstanteria("B-Impresoras", 300, 100));
        nodos.put("C-Routers", new NodoEstanteria("C-Routers", 500, 100));
        nodos.put("D-PCs", new NodoEstanteria("D-PCs", 100, 300));
        nodos.put("E-Monitores", new NodoEstanteria("E-Monitores", 300, 300));
        nodos.put("F-Proyectores", new NodoEstanteria("F-Proyectores", 500, 300));
        
        // Aristas iniciales
        agregarArista("A-Laptops", "B-Impresoras", 10);
        agregarArista("B-Impresoras", "C-Routers", 15);
        agregarArista("A-Laptops", "D-PCs", 20);
        agregarArista("D-PCs", "E-Monitores", 5);
        agregarArista("E-Monitores", "F-Proyectores", 12);
        agregarArista("C-Routers", "F-Proyectores", 8);
    }
    
    private void dibujarGrafo(Graphics g) {
        // Dibujar aristas primero
        for (Arista a : aristas) {
            g.setColor(Color.BLUE);
            g.drawLine(a.origen.x, a.origen.y, a.destino.x, a.destino.y);
            
            // Dibujar peso
            g.setColor(Color.BLACK);
            int midX = (a.origen.x + a.destino.x) / 2;
            int midY = (a.origen.y + a.destino.y) / 2;
            g.drawString(String.valueOf(a.peso), midX, midY);
        }
        
        // Dibujar nodos después
        for (NodoEstanteria n : nodos.values()) {
            g.setColor(Color.RED);
            g.fillOval(n.x - 15, n.y - 15, 30, 30);
            
            g.setColor(Color.BLACK);
            g.drawString(n.nombre, n.x - 15, n.y - 20);
        }
    }
    
    private void configurarControles() {
        panelControles.setLayout(new FlowLayout());
        
        // Combo box para seleccionar nodos
        JComboBox<String> comboNodos = new JComboBox<>(nodos.keySet().toArray(new String[0]));
        
        // Campos para agregar aristas
        JTextField txtPeso = new JTextField(5);
        
        // Botones
        JButton btnAgregarNodo = new JButton("Agregar Nodo");
        JButton btnEliminarNodo = new JButton("Eliminar Nodo");
        JButton btnAgregarArista = new JButton("Agregar Arista");
        JButton btnEliminarArista = new JButton("Eliminar Arista");
        JButton btnRutaMasCorta = new JButton("Ruta Más Corta");
        JButton btnDetectarCiclos = new JButton("Detectar Ciclos");
        
        // Agregar controles al panel
        panelControles.add(new JLabel("Nodo:"));
        panelControles.add(comboNodos);
        panelControles.add(new JLabel("Peso:"));
        panelControles.add(txtPeso);
        panelControles.add(btnAgregarNodo);
        panelControles.add(btnEliminarNodo);
        panelControles.add(btnAgregarArista);
        panelControles.add(btnEliminarArista);
        panelControles.add(btnRutaMasCorta);
        panelControles.add(btnDetectarCiclos);
        
        // Manejadores de eventos
        btnAgregarNodo.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog("Nombre del nuevo nodo:");
            if (nombre != null && !nombre.trim().isEmpty()) {
                agregarNodo(nombre, 100, 100);
                comboNodos.addItem(nombre);
                panelGrafo.repaint();
            }
        });
        
        btnEliminarNodo.addActionListener(e -> {
            String nodo = (String) comboNodos.getSelectedItem();
            if (nodo != null) {
                eliminarNodo(nodo);
                comboNodos.removeItem(nodo);
                panelGrafo.repaint();
            }
        });
        
        btnAgregarArista.addActionListener(e -> {
            String origen = (String) comboNodos.getSelectedItem();
            String destino = JOptionPane.showInputDialog("Nodo destino:");
            
            if (origen != null && destino != null && nodos.containsKey(destino)) {
                try {
                    int peso = Integer.parseInt(txtPeso.getText());
                    agregarArista(origen, destino, peso);
                    panelGrafo.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese un peso válido");
                }
            }
        });
        
        btnEliminarArista.addActionListener(e -> {
            String origen = (String) comboNodos.getSelectedItem();
            String destino = JOptionPane.showInputDialog("Nodo destino:");
            
            if (origen != null && destino != null) {
                eliminarArista(origen, destino);
                panelGrafo.repaint();
            }
        });
        
        btnRutaMasCorta.addActionListener(e -> {
            String origen = (String) comboNodos.getSelectedItem();
            String destino = JOptionPane.showInputDialog("Nodo destino:");
            
            if (origen != null && destino != null && nodos.containsKey(destino)) {
                List<String> ruta = encontrarRutaMasCorta(origen, destino);
                if (ruta != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Ruta más corta: " + String.join(" -> ", ruta) +
                        "\nDistancia total: " + calcularDistanciaRuta(ruta));
                } else {
                    JOptionPane.showMessageDialog(this, "No hay ruta disponible");
                }
            }
        });
        
        btnDetectarCiclos.addActionListener(e -> {
            if (tieneCiclos()) {
                JOptionPane.showMessageDialog(this, "El grafo contiene ciclos");
            } else {
                JOptionPane.showMessageDialog(this, "El grafo no contiene ciclos");
            }
        });
    }
    
    // Métodos de manipulación del grafo
    private void agregarNodo(String nombre, int x, int y) {
        nodos.put(nombre, new NodoEstanteria(nombre, x, y));
    }
    
    private void eliminarNodo(String nombre) {
        nodos.remove(nombre);
        aristas.removeIf(a -> a.origen.nombre.equals(nombre) || a.destino.nombre.equals(nombre));
    }
    
    private void agregarArista(String origen, String destino, int peso) {
        NodoEstanteria nodoOrigen = nodos.get(origen);
        NodoEstanteria nodoDestino = nodos.get(destino);
        
        if (nodoOrigen != null && nodoDestino != null) {
            Arista nueva = new Arista(nodoOrigen, nodoDestino, peso);
            aristas.add(nueva);
            nodoOrigen.conexiones.add(nueva);
        }
    }
    
    private void eliminarArista(String origen, String destino) {
        aristas.removeIf(a -> 
            a.origen.nombre.equals(origen) && a.destino.nombre.equals(destino));
    }
    
    // Algoritmo de Dijkstra para encontrar la ruta más corta
    private List<String> encontrarRutaMasCorta(String origen, String destino) {
        Map<String, Integer> distancias = new HashMap<>();
        Map<String, String> anteriores = new HashMap<>();
        PriorityQueue<NodoDistancia> cola = new PriorityQueue<>();
        
        // Inicialización
        for (String nodo : nodos.keySet()) {
            distancias.put(nodo, nodo.equals(origen) ? 0 : Integer.MAX_VALUE);
        }
        cola.add(new NodoDistancia(origen, 0));
        
        // Procesamiento
        while (!cola.isEmpty()) {
            NodoDistancia actual = cola.poll();
            
            if (actual.nombre.equals(destino)) {
                break; // Hemos encontrado el destino
            }
            
            for (Arista arista : nodos.get(actual.nombre).conexiones) {
                String vecino = arista.destino.nombre;
                int nuevaDistancia = distancias.get(actual.nombre) + arista.peso;
                
                if (nuevaDistancia < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDistancia);
                    anteriores.put(vecino, actual.nombre);
                    cola.add(new NodoDistancia(vecino, nuevaDistancia));
                }
            }
        }
        
        // Reconstruir la ruta
        if (!anteriores.containsKey(destino)) {
            return null; // No hay ruta
        }
        
        List<String> ruta = new ArrayList<>();
        String actual = destino;
        while (actual != null) {
            ruta.add(0, actual);
            actual = anteriores.get(actual);
        }
        
        return ruta;
    }
    
    private static class NodoDistancia implements Comparable<NodoDistancia> {
        String nombre;
        int distancia;
        
        public NodoDistancia(String nombre, int distancia) {
            this.nombre = nombre;
            this.distancia = distancia;
        }
        
        @Override
        public int compareTo(NodoDistancia otro) {
            return Integer.compare(this.distancia, otro.distancia);
        }
    }
    
    private int calcularDistanciaRuta(List<String> ruta) {
        int distancia = 0;
        for (int i = 0; i < ruta.size() - 1; i++) {
            String origen = ruta.get(i);
            String destino = ruta.get(i + 1);
            
            for (Arista a : aristas) {
                if (a.origen.nombre.equals(origen) && a.destino.nombre.equals(destino)) {
                    distancia += a.peso;
                    break;
                }
            }
        }
        return distancia;
    }
    
    // Detección de ciclos usando DFS
    private boolean tieneCiclos() {
        Set<String> visitados = new HashSet<>();
        Set<String> enProceso = new HashSet<>();
        
        for (String nodo : nodos.keySet()) {
            if (dfsDetectarCiclos(nodo, visitados, enProceso)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean dfsDetectarCiclos(String nodo, Set<String> visitados, Set<String> enProceso) {
        if (enProceso.contains(nodo)) {
            return true;
        }
        
        if (visitados.contains(nodo)) {
            return false;
        }
        
        visitados.add(nodo);
        enProceso.add(nodo);
        
        for (Arista a : nodos.get(nodo).conexiones) {
            if (dfsDetectarCiclos(a.destino.nombre, visitados, enProceso)) {
                return true;
            }
        }
        
        enProceso.remove(nodo);
        return false;
    }
}

