// SistemaInventario.java
package inventario;

import java.util.Scanner;
import java.util.Map;

public class SistemaInventario {
    public static void main(String[] args) {
        Grafo<Ubicacion> grafo = new Grafo<>();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE INVENTARIO ===");
            System.out.println("1. Registrar nueva ubicación logística");
            System.out.println("2. Conectar ubicaciones (definir ruta)");
            System.out.println("3. Registrar producto en una ubicación");
            System.out.println("4. Buscar producto en el almacén");
            System.out.println("5. Mover producto a otra ubicación");
            System.out.println("6. Eliminar producto o ubicación");
            System.out.println("7. Simular cierre de zona");
            System.out.println("8. Ver resumen del almacén");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nombre de la nueva ubicación: ");
                    String nombre = scanner.nextLine();
                    grafo.agregarNodo(new Ubicacion(nombre));
                    System.out.println("Ubicación registrada.");
                }
                case 2 -> {
                    System.out.print("Nombre de ubicación origen: ");
                    String ori = scanner.nextLine();
                    System.out.print("Nombre de ubicación destino: ");
                    String dest = scanner.nextLine();
                    System.out.print("Distancia entre ubicaciones: ");
                    double peso = scanner.nextDouble();
                    scanner.nextLine();
                    Ubicacion u1 = grafo.buscarNodoPorNombre(ori);
                    Ubicacion u2 = grafo.buscarNodoPorNombre(dest);
                    if (u1 != null && u2 != null) {
                        grafo.agregarArista(u1, u2, peso);
                        System.out.println("Ruta registrada.");
                    } else {
                        System.out.println("Una o ambas ubicaciones no existen.");
                    }
                }
                case 3 -> {
                    System.out.print("Nombre de la ubicación: ");
                    String nombreUbic = scanner.nextLine();
                    Ubicacion ubic = grafo.buscarNodoPorNombre(nombreUbic);
                    if (ubic != null) {
                        System.out.print("Nombre del producto: ");
                        String producto = scanner.nextLine();
                        ubic.getProductos().insertar(producto);
                        System.out.println("Producto registrado en " + nombreUbic);
                    } else {
                        System.out.println("Ubicación no encontrada.");
                    }
                }
                case 4 -> {
                    System.out.print("Nombre del producto a buscar: ");
                    String producto = scanner.nextLine();
                    Ubicacion resultado = grafo.buscarProducto(producto);
                    if (resultado != null) {
                        System.out.println("Producto encontrado en: " + resultado.getNombre());
                    } else {
                        System.out.println("Producto no encontrado en el almacén.");
                    }
                }
                case 5 -> {
                    System.out.print("Producto a mover: ");
                    String producto = scanner.nextLine();
                    System.out.print("Ubicación destino: ");
                    String nuevaUbic = scanner.nextLine();
                    Ubicacion origen = grafo.buscarProducto(producto);
                    Ubicacion destino = grafo.buscarNodoPorNombre(nuevaUbic);
                    if (origen != null && destino != null) {
                        origen.getProductos().eliminar(producto);
                        destino.getProductos().insertar(producto);
                        System.out.println("Producto movido correctamente.");
                    } else {
                        System.out.println("Error: verifique ubicaciones y producto.");
                    }
                }
                case 6 -> {
                    System.out.println("1. Eliminar producto");
                    System.out.println("2. Eliminar ubicación");
                    int sub = scanner.nextInt();
                    scanner.nextLine();
                    if (sub == 1) {
                        System.out.print("Producto: ");
                        String prod = scanner.nextLine();
                        Ubicacion u = grafo.buscarProducto(prod);
                        if (u != null) {
                            u.getProductos().eliminar(prod);
                            System.out.println("Producto eliminado.");
                        } else {
                            System.out.println("Producto no encontrado.");
                        }
                    } else {
                        System.out.print("Nombre de ubicación: ");
                        String nom = scanner.nextLine();
                        grafo.eliminarNodoPorNombre(nom);
                        System.out.println("Ubicación eliminada.");
                    }
                }
                case 7 -> {
                    System.out.print("Zona a cerrar temporalmente: ");
                    String zona = scanner.nextLine();
                    grafo.simularCierreZona(zona);
                    System.out.println("Zona cerrada para simulación.");
                }
                case 8 -> {
                    System.out.println("=== Resumen del almacén ===");
                    for (Ubicacion u : grafo.getNodos().keySet()) {
                        System.out.println("Ubicación: " + u.getNombre());
                        System.out.println("Productos: " + u.getProductos().getClaves());
                    }
                }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);

        scanner.close();
    }
}