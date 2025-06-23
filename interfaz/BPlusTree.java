package interfaz;

import java.util.ArrayList;
import java.util.List;

public class BPlusTree {
    private static final int DEFAULT_ORDER = 4;
    private Node root;
    private final int order;
    private LeafNode firstLeaf; // Para enlazar hojas
    
    public BPlusTree() {
        this(DEFAULT_ORDER);
    }
    
    public BPlusTree(int order) {
        this.order = order;
        this.root = new LeafNode();
        this.firstLeaf = (LeafNode) root;
    }
    
    // Método para insertar un nuevo producto
    public void insert(String key, String[] productData) {
        root.insert(key, productData);
        
        if (root.isOverflow()) {
            IndexNode newRoot = new IndexNode();
            newRoot.addChild(root);
            newRoot.splitChild(0, this);
            root = newRoot;
        }
    }
    
    // Método para buscar productos por clave
    public List<String[]> search(String key) {
        return root.search(key);
    }
    
    // Método para búsqueda por rango
    public List<String[]> rangeSearch(String startKey, String endKey) {
        List<String[]> results = new ArrayList<>();
        LeafNode current = firstLeaf;
        
        while (current != null) {
            for (int i = 0; i < current.getKeys().size(); i++) {
                String currentKey = current.getKeys().get(i);
                if (currentKey.compareTo(startKey) >= 0 && currentKey.compareTo(endKey) <= 0) {
                    results.add(current.getValues().get(i));
                }
            }
            current = current.getNext();
        }
        
        return results;
    }
    
    // Método para eliminar un producto
    public void delete(String key) {
        root.delete(key);
        
        if (root instanceof IndexNode && ((IndexNode)root).getChildren().size() == 1) {
            root = ((IndexNode) root).getChildren().get(0);
        }
    }
    
    // Método para mostrar todo el árbol (debug)
    public void printTree() {
        root.print("", true);
    }
    
    // Clase abstracta Node
    private abstract class Node {
        protected List<String> keys = new ArrayList<>();
        
        public List<String> getKeys() {
            return keys;
        }
        
        abstract void insert(String key, String[] value);
        abstract List<String[]> search(String key);
        abstract void delete(String key);
        abstract boolean isOverflow();
        abstract boolean isUnderflow();
        abstract void splitChild(int index, BPlusTree tree);
        abstract void mergeChild(int index);
        abstract void print(String prefix, boolean isTail);
    }
    
    // Clase para nodos índice
    private class IndexNode extends Node {
        private List<Node> children = new ArrayList<>();
        
        public List<Node> getChildren() {
            return children;
        }
        
        public void addChild(Node child) {
            children.add(child);
        }
        
        @Override
        public void insert(String key, String[] value) {
            int i = 0;
            while (i < keys.size() && key.compareTo(keys.get(i)) >= 0) {
                i++;
            }
            children.get(i).insert(key, value);
            
            if (children.get(i).isOverflow()) {
                splitChild(i, BPlusTree.this);
            }
        }
        
        @Override
        public List<String[]> search(String key) {
            int i = 0;
            while (i < keys.size() && key.compareTo(keys.get(i)) >= 0) {
                i++;
            }
            return children.get(i).search(key);
        }
        
        @Override
        public void delete(String key) {
            int i = 0;
            while (i < keys.size() && key.compareTo(keys.get(i)) >= 0) {
                i++;
            }
            
            children.get(i).delete(key);
            
            if (children.get(i).isUnderflow()) {
                if (i > 0 && children.get(i - 1).getKeys().size() > order / 2) {
                    // Pedir prestado del hermano izquierdo
                    children.get(i - 1).mergeChild(i - 1);
                } else if (i < children.size() - 1 && children.get(i + 1).getKeys().size() > order / 2) {
                    // Pedir prestado del hermano derecho
                    children.get(i + 1).mergeChild(i);
                } else if (i > 0) {
                    // Fusionar con el hermano izquierdo
                    children.get(i - 1).mergeChild(i - 1);
                    children.remove(i);
                    keys.remove(i - 1);
                } else {
                    // Fusionar con el hermano derecho
                    children.get(i + 1).mergeChild(i);
                    children.remove(i);
                    keys.remove(i);
                }
            }
        }
        
        @Override
        public boolean isOverflow() {
            return keys.size() > order;
        }
        
        @Override
        public boolean isUnderflow() {
            return keys.size() < order / 2;
        }
        
        @Override
        public void splitChild(int index, BPlusTree tree) {
            Node child = children.get(index);
            
            if (child instanceof IndexNode) {
                IndexNode splitNode = (IndexNode) child;
                IndexNode newChild = new IndexNode();
                
                int splitAt = splitNode.getKeys().size() / 2;
                String middleKey = splitNode.getKeys().get(splitAt);
                
                // Mover claves e hijos al nuevo nodo
                newChild.getKeys().addAll(splitNode.getKeys().subList(splitAt + 1, splitNode.getKeys().size()));
                newChild.getChildren().addAll(splitNode.getChildren().subList(splitAt + 1, splitNode.getChildren().size()));
                
                // Limpiar el nodo original
                splitNode.getKeys().subList(splitAt, splitNode.getKeys().size()).clear();
                splitNode.getChildren().subList(splitAt + 1, splitNode.getChildren().size()).clear();
                
                // Insertar el nuevo nodo
                children.add(index + 1, newChild);
                keys.add(index, middleKey);
            } else {
                LeafNode splitNode = (LeafNode) child;
                LeafNode newChild = new LeafNode();
                
                int splitAt = (splitNode.getKeys().size() + 1) / 2;
                
                // Mover claves y valores al nuevo nodo
                newChild.getKeys().addAll(splitNode.getKeys().subList(splitAt, splitNode.getKeys().size()));
                newChild.getValues().addAll(splitNode.getValues().subList(splitAt, splitNode.getValues().size()));
                
                // Actualizar enlaces de hojas
                newChild.setNext(splitNode.getNext());
                splitNode.setNext(newChild);
                
                // Limpiar el nodo original
                splitNode.getKeys().subList(splitAt, splitNode.getKeys().size()).clear();
                splitNode.getValues().subList(splitAt, splitNode.getValues().size()).clear();
                
                // Insertar el nuevo nodo
                children.add(index + 1, newChild);
                keys.add(index, newChild.getKeys().get(0));
                
                // Actualizar firstLeaf si es necesario
                if (splitNode == tree.firstLeaf) {
                    tree.firstLeaf = splitNode;
                }
            }
        }
        
        @Override
        public void mergeChild(int index) {
            Node leftChild = children.get(index);
            Node rightChild = children.get(index + 1);
            
            if (leftChild instanceof IndexNode) {
                IndexNode left = (IndexNode) leftChild;
                IndexNode right = (IndexNode) rightChild;
                
                // Mover claves e hijos del derecho al izquierdo
                left.getKeys().add(keys.get(index));
                left.getKeys().addAll(right.getKeys());
                left.getChildren().addAll(right.getChildren());
                
                // Eliminar nodo derecho
                keys.remove(index);
                children.remove(index + 1);
            } else {
                LeafNode left = (LeafNode) leftChild;
                LeafNode right = (LeafNode) rightChild;
                
                // Mover claves y valores del derecho al izquierdo
                left.getKeys().addAll(right.getKeys());
                left.getValues().addAll(right.getValues());
                left.setNext(right.getNext());
                
                // Eliminar nodo derecho
                keys.remove(index);
                children.remove(index + 1);
            }
        }
        
        @Override
        public void print(String prefix, boolean isTail) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + "IndexNode: " + keys);
            for (int i = 0; i < children.size() - 1; i++) {
                children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
            }
            if (children.size() > 0) {
                children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
    
    // Clase para nodos hoja
    private class LeafNode extends Node {
        private List<String[]> values = new ArrayList<>();
        private LeafNode next; // Enlace a la siguiente hoja
        
        public List<String[]> getValues() {
            return values;
        }
        
        public LeafNode getNext() {
            return next;
        }
        
        public void setNext(LeafNode next) {
            this.next = next;
        }
        
        @Override
        public void insert(String key, String[] value) {
            int i = 0;
            while (i < keys.size() && key.compareTo(keys.get(i)) > 0) {
                i++;
            }
            
            keys.add(i, key);
            values.add(i, value);
        }
        
        @Override
        public List<String[]> search(String key) {
            List<String[]> result = new ArrayList<>();
            int i = 0;
            while (i < keys.size() && key.compareTo(keys.get(i)) > 0) {
                i++;
            }
            
            if (i < keys.size() && keys.get(i).equals(key)) {
                result.add(values.get(i));
            }
            
            return result;
        }
        
        @Override
        public void delete(String key) {
            int i = 0;
            while (i < keys.size() && key.compareTo(keys.get(i)) > 0) {
                i++;
            }
            
            if (i < keys.size() && keys.get(i).equals(key)) {
                keys.remove(i);
                values.remove(i);
            }
        }
        
        @Override
        public boolean isOverflow() {
            return keys.size() > order;
        }
        
        @Override
        public boolean isUnderflow() {
            return keys.size() < (order + 1) / 2;
        }
        
        @Override
        public void splitChild(int index, BPlusTree tree) {
            // No aplica para hojas
        }
        
        @Override
        public void mergeChild(int index) {
            // No aplica para hojas
        }
        
        @Override
        public void print(String prefix, boolean isTail) {
            System.out.print(prefix + (isTail ? "└── " : "├── ") + "LeafNode: ");
            for (int i = 0; i < keys.size(); i++) {
                System.out.print(keys.get(i) + ":" + java.util.Arrays.toString(values.get(i)));
                if (i < keys.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }
}

