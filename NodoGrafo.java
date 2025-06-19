// NodoGrafo.java
package inventario;

import java.util.*;

public class NodoGrafo<T> {
    private T id;
    private Map<NodoGrafo<T>, Double> adyacentes;

    public NodoGrafo(T id) {
        this.id = id;
        this.adyacentes = new HashMap<>();
    }

    public T getId() {
        return id;
    }

    public void agregarAdyacente(NodoGrafo<T> destino, double peso) {
        adyacentes.put(destino, peso);
    }

    public Map<NodoGrafo<T>, Double> getAdyacentes() {
        return adyacentes;
    }
}
