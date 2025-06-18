package inventario;

import java.util.*;

public class NodoB<T extends Comparable<T>> {
    List<T> claves;
    List<NodoB<T>> hijos;
    boolean hoja;

    public NodoB(boolean hoja) {
        this.hoja = hoja;
        this.claves = new ArrayList<>();
        this.hijos = new ArrayList<>();
    }

    public List<T> getClaves() {
        return claves;
    }

    public List<NodoB<T>> getHijos() {
        return hijos;
    }

    public boolean esHoja() {
        return hoja;
    }
}
