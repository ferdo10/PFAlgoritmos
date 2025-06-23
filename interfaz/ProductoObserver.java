package interfaz;
public interface ProductoObserver {
    void onProductAdded(String[] producto);
    void onProductUpdated(String[] producto);
    void onProductDeleted(String nombreProducto);
}