package inventario;

public class SistemaInventario {
    public static void main(String[] args) {
        Grafo<String> grafo = new Grafo<>();
        grafo.agregarArista("Entrada", "Pasillo A", 5);
        grafo.agregarArista("Pasillo A", "Estante 1", 2);

        System.out.println("Distancias desde Entrada: " + grafo.dijkstra("Entrada"));

        ArbolB<String> arbol = new ArbolB<>(3);
        arbol.insertar("Manzanas");
        arbol.insertar("Peras");
        arbol.insertar("Zanahorias");

        System.out.println("Buscar 'Peras': " + arbol.buscar("Peras"));
    }
}
