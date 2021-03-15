package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart part to be added
     */
    public static void addPart(Part newPart) { allParts.addAll(newPart); }

    /**
     *
     * @param newProduct product to be added
     */
    public static void addProduct(Product newProduct) { allProducts.addAll(newProduct); }

    /**
     * Searches all parts for an id match.
     * @param partId the part id
     * @return part that matches
     */
    public static Part lookupPart(int partId) {
        for (Part currentPart : allParts) {
            if (currentPart.getId() == partId) {
                return currentPart;
            }
        }
        return null;
    }

    /**
     * Looks up product by id
     * @param productId the product id
     * @return the found produc
     */
    public static Product lookupProduct(int productId) {
        for (Product currentProduct : allProducts) {
            if (currentProduct.getId() == productId) {
                return currentProduct;
            }
        }
        return null;
    }

    /**
     * Finds a list of all parts containing partName
     * @param partName name to be found
     * @return list of parts
     */
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

    /**
     * Finds a list of all products containing productName
     * @param productName name to be matched
     * @return list of products
     */
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

    /**
     *
     * @param index index of part to be updated
     * @param selectedPart new values of part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     *
     * @param index index of product to be updated
     * @param newProduct new values of product
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     *
     * @param selectedPart part to be deleted
     * @return if successful
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     *
     * @param selectedProduct product to be deleted
     * @return if successful
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     *
     * @return list of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     *
     * @return list of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
