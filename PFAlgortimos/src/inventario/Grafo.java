package inventario;

import java.util.*;

public class Grafo<T> {
    private Map<T, NodoGrafo<T>> nodos;

    public Grafo() {
        this.nodos = new HashMap<>();
    }

    public void agregarNodo(T id) {
        nodos.putIfAbsent(id, new NodoGrafo<>(id));
    }

    public void agregarArista(T origen, T destino, double peso) {
        agregarNodo(origen);
        agregarNodo(destino);
        nodos.get(origen).agregarAdyacente(nodos.get(destino), peso);
    }

    public Map<T, NodoGrafo<T>> getNodos() {
        return nodos;
    }

    public Map<T, Double> dijkstra(T origen) {
        Map<T, Double> distancias = new HashMap<>();
        PriorityQueue<Map.Entry<T, Double>> cola = new PriorityQueue<>(Map.Entry.comparingByValue());
        Set<T> visitados = new HashSet<>();

        for (T nodo : nodos.keySet()) distancias.put(nodo, Double.POSITIVE_INFINITY);
        distancias.put(origen, 0.0);
        cola.offer(new AbstractMap.SimpleEntry<>(origen, 0.0));

        while (!cola.isEmpty()) {
            T actual = cola.poll().getKey();
            if (!visitados.add(actual)) continue;

            NodoGrafo<T> nodoActual = nodos.get(actual);
            for (var entrada : nodoActual.getAdyacentes().entrySet()) {
                T vecino = entrada.getKey().getId();
                double nuevoPeso = distancias.get(actual) + entrada.getValue();
                if (nuevoPeso < distancias.get(vecino)) {
                    distancias.put(vecino, nuevoPeso);
                    cola.offer(new AbstractMap.SimpleEntry<>(vecino, nuevoPeso));
                }
            }
        }
        return distancias;
    }
}