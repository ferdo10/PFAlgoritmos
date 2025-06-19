// ArbolBMas.java - Árbol B+ simplificado
package inventario;

import java.util.*;

public class ArbolBMas<T extends Comparable<T>> {
    private NodoBMas<T> raiz;
    private int orden;

    public ArbolBMas(int orden) {
        this.orden = orden;
        this.raiz = new NodoBMas<>(true);
    }

    public void insertar(T clave) {
        NodoBMas<T> r = raiz;
        if (r.getClaves().size() == 2 * orden - 1) {
            NodoBMas<T> s = new NodoBMas<>(false);
            s.getHijos().add(r);
            dividirHijo(s, 0);
            raiz = s;
        }
        insertarNoLleno(raiz, clave);
    }

    private void insertarNoLleno(NodoBMas<T> nodo, T clave) {
        if (nodo.esHoja()) {
            int i = Collections.binarySearch(nodo.getClaves(), clave);
            if (i < 0) i = -i - 1;
            nodo.getClaves().add(i, clave);
        } else {
            int i = Collections.binarySearch(nodo.getClaves(), clave);
            if (i < 0) i = -i - 1;
            NodoBMas<T> hijo = nodo.getHijos().get(i);
            if (hijo.getClaves().size() == 2 * orden - 1) {
                dividirHijo(nodo, i);
                if (clave.compareTo(nodo.getClaves().get(i)) > 0) i++;
            }
            insertarNoLleno(nodo.getHijos().get(i), clave);
        }
    }

    private void dividirHijo(NodoBMas<T> padre, int i) {
        NodoBMas<T> y = padre.getHijos().get(i);
        NodoBMas<T> z = new NodoBMas<>(y.esHoja());

        z.getClaves().addAll(y.getClaves().subList(orden, y.getClaves().size()));
        y.getClaves().subList(orden, y.getClaves().size()).clear();

        if (!y.esHoja()) {
            z.getHijos().addAll(y.getHijos().subList(orden, y.getHijos().size()));
            y.getHijos().subList(orden, y.getHijos().size()).clear();
        }

        if (y.esHoja()) {
            z.setSiguiente(y.getSiguiente());
            y.setSiguiente(z);
        }

        padre.getHijos().add(i + 1, z);
        padre.getClaves().add(i, z.getClaves().get(0));
    }

    public boolean buscar(T clave) {
        return buscar(raiz, clave);
    }

    private boolean buscar(NodoBMas<T> nodo, T clave) {
        int i = Collections.binarySearch(nodo.getClaves(), clave);
        if (nodo.esHoja()) {
            return i >= 0;
        } else {
            if (i >= 0) i++;
            else i = -i - 1;
            return buscar(nodo.getHijos().get(i), clave);
        }
    }

    public void eliminar(T clave) {
        eliminar(raiz, clave);
    }

    private void eliminar(NodoBMas<T> nodo, T clave) {
        if (nodo.esHoja()) {
            nodo.getClaves().remove(clave);
        }
        // Eliminación completa para B+ requiere casos complejos no incluidos aquí
    }

    public List<T> recorridoOrdenado() {
        List<T> resultado = new ArrayList<>();
        NodoBMas<T> actual = raiz;
        while (!actual.esHoja()) actual = actual.getHijos().get(0);
        while (actual != null) {
            resultado.addAll(actual.getClaves());
            actual = actual.getSiguiente();
        }
        return resultado;
    }

    public NodoBMas<T> getRaiz() {
        return raiz;
    }
}
