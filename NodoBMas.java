// NodoBMas.java
package inventario;

import java.util.*;

public class NodoBMas<T extends Comparable<T>> {
    private List<T> claves;
    private List<NodoBMas<T>> hijos;
    private boolean hoja;
    private NodoBMas<T> siguiente;

    public NodoBMas(boolean hoja) {
        this.hoja = hoja;
        this.claves = new ArrayList<>();
        this.hijos = new ArrayList<>();
        this.siguiente = null;
    }

    public List<T> getClaves() {
        return claves;
    }

    public List<NodoBMas<T>> getHijos() {
        return hijos;
    }

    public boolean esHoja() {
        return hoja;
    }

    public NodoBMas<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoBMas<T> siguiente) {
        this.siguiente = siguiente;
    }
}
