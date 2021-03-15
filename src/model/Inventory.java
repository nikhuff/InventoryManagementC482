package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart(Part newPart) { allParts.addAll(newPart); }
    public static void addProduct(Product newProduct) { allProducts.addAll(newProduct); }

    public static Part lookupPart(int partId) {
        for (Part currentPart : allParts) {
            if (currentPart.getId() == partId) {
                return currentPart;
            }
        }
        return null;
    }

    public static Product lookupProduct(int productId) {
        for (Product currentProduct : allProducts) {
            if (currentProduct.getId() == productId) {
                return currentProduct;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> matches = FXCollections.observableArrayList();
        Pattern pattern = Pattern.compile("^" + partName + "|" + partName + "$|\\s" + partName, Pattern.CASE_INSENSITIVE);
        Matcher matcher = null;
        for (Part currentPart : allParts) {
            matcher = pattern.matcher(currentPart.getName());
            if (matcher.find()) {
                matches.add(currentPart);
            }
        }
        return matches;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> matches = FXCollections.observableArrayList();
        Pattern pattern = Pattern.compile(productName, Pattern.CASE_INSENSITIVE);
        Matcher matcher = null;
        for (Product currentProduct : allProducts) {
            matcher = pattern.matcher(currentProduct.getName());
            if (matcher.find()) {
                matches.add(currentProduct);
            }
        }
        return matches;
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
