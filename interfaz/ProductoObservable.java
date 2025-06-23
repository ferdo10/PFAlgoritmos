package interfaz;

import java.util.ArrayList;
import java.util.List;

public class ProductoObservable {
    private static ProductoObservable instance;
    private List<ProductoObserver> observers = new ArrayList<>();
    
    private ProductoObservable() {}
    
    public static ProductoObservable getInstance() {
        if (instance == null) {
            instance = new ProductoObservable();
        }
        return instance;
    }
    
    public void addObserver(ProductoObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ProductoObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyProductAdded(String[] producto) {
        for (ProductoObserver observer : observers) {
            observer.onProductAdded(producto);
        }
    }
    
    public void notifyProductUpdated(String[] producto) {
        for (ProductoObserver observer : observers) {
            observer.onProductUpdated(producto);
        }
    }
    
    public void notifyProductDeleted(String nombreProducto) {
        for (ProductoObserver observer : observers) {
            observer.onProductDeleted(nombreProducto);
        }
    }
}
