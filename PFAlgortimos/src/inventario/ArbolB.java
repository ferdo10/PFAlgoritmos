package inventario;



public class ArbolB<T extends Comparable<T>> {
    private NodoB<T> raiz;
    private int orden;

    public ArbolB(int orden) {
        this.raiz = new NodoB<>(true);
        this.orden = orden;
    }

    public void insertar(T clave) {
        NodoB<T> r = raiz;
        if (r.getClaves().size() == 2 * orden - 1) {
            NodoB<T> s = new NodoB<>(false);
            s.getHijos().add(r);
            dividirHijo(s, 0);
            insertarNoLleno(s, clave);
            raiz = s;
        } else {
            insertarNoLleno(r, clave);
        }
    }

    private void insertarNoLleno(NodoB<T> x, T clave) {
        int i = x.getClaves().size() - 1;
        if (x.esHoja()) {
            x.getClaves().add(null);
            while (i >= 0 && clave.compareTo(x.getClaves().get(i)) < 0) {
                x.getClaves().set(i + 1, x.getClaves().get(i));
                i--;
            }
            x.getClaves().set(i + 1, clave);
        } else {
            while (i >= 0 && clave.compareTo(x.getClaves().get(i)) < 0) i--;
            i++;
            if (x.getHijos().get(i).getClaves().size() == 2 * orden - 1) {
                dividirHijo(x, i);
                if (clave.compareTo(x.getClaves().get(i)) > 0) i++;
            }
            insertarNoLleno(x.getHijos().get(i), clave);
        }
    }

    private void dividirHijo(NodoB<T> padre, int i) {
        NodoB<T> y = padre.getHijos().get(i);
        NodoB<T> z = new NodoB<>(y.esHoja());
        for (int j = 0; j < orden - 1; j++)
            z.getClaves().add(y.getClaves().remove(orden));
        if (!y.esHoja()) {
            for (int j = 0; j < orden; j++)
                z.getHijos().add(y.getHijos().remove(orden));
        }
        padre.getHijos().add(i + 1, z);
        padre.getClaves().add(i, y.getClaves().remove(orden - 1));
    }

    public boolean buscar(T clave) {
        return buscar(raiz, clave);
    }

    private boolean buscar(NodoB<T> nodo, T clave) {
        int i = 0;
        while (i < nodo.getClaves().size() && clave.compareTo(nodo.getClaves().get(i)) > 0) i++;
        if (i < nodo.getClaves().size() && clave.equals(nodo.getClaves().get(i))) return true;
        if (nodo.esHoja()) return false;
        return buscar(nodo.getHijos().get(i), clave);
    }

    public NodoB<T> getRaiz() {
        return raiz;
    }
}
