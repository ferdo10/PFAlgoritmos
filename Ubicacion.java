// Ubicacion.java
package inventario;

import inventario.ArbolB;

public class Ubicacion {
    private String nombre;
    private ArbolBMas<String> productos;


    public Ubicacion(String nombre) {
        this.nombre = nombre;
        this.productos = new ArbolB<>(3); // Orden del árbol B
    }

    public String getNombre() {
        return nombre;
    }

    public ArbolB<String> getProductos() {
        return productos;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ubicacion u)) return false;
        return nombre.equals(u.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}