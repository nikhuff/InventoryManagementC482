package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     *
     * @return id
     */
    public int getId() { return id; }

    /**
     *
     * @return name
     */
    public String getName() { return name; }

    /**
     *
     * @return price
     */
    public double getPrice() { return price; }

    /**
     *
     * @return stock
     */
    public int getStock() { return stock; }

    /**
     *
     * @return min
     */
    public int getMin() { return min; }

    /**
     *
     * @return max
     */
    public int getMax() { return max; }

    /**
     *
     * @param id product id
     */
    public void setId(int id) { this.id = id; }

    /**
     *
     * @param name product name
     */
    public void setName(String name) { this.name = name; }

    /**
     *
     * @param price product price
     */
    public void setPrice(double price) { this.price = price; }

    /**
     *
     * @param stock product stock
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     *
     * @param min minimum inventory
     */
    public void setMin(int min) { this.min = min; }

    /**
     *
     * @param max maximum inventory
     */
    public void setMax(int max) { this.max = max; }

    /**
     *
     * @return all parts associated with product
     */
    public ObservableList<Part> getAllAssociatedParts() { return this.associatedParts; }

    /**
     *
     * @param part the part to add
     */
    public void addAssociatedPart(Part part) { this.associatedParts.add(part); }

    /**
     *
     * @param part the part to delete
     * @return if successful
     */
    public boolean deleteAssociatedPart(Part part) { return this.associatedParts.remove(part); }

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
}
