package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyProductFormController implements Initializable {
    Inventory inventory;
    int index;
    Product product;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public TableView partTable;
    public TableColumn partId;
    public TableColumn partName;
    public TableColumn partInventory;
    public TableColumn partPrice;

    public TableView associatedPartTable;
    public TableColumn assPartId;
    public TableColumn assPartName;
    public TableColumn assPartInventory;
    public TableColumn assPartPrice;

    public TextField productId;
    public TextField productName;
    public TextField productInventory;
    public TextField productCost;
    public TextField productMax;
    public TextField productMin;

    @FXML
    private TextField partSearchBar;

    /**
     *
     * @param inv singleton inventory
     * @param product product to modify
     * @param index index of product
     */
    public ModifyProductFormController(Inventory inv, Product product, int index) {
        this.inventory = inv;
        this.index = index;
        this.product = product;
    }

    /**
     * initialize text fields, tables, and search bar listener
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.productId.setText(String.valueOf(this.product.getId()));
        this.productName.setText(this.product.getName());
        this.productInventory.setText(String.valueOf(this.product.getStock()));
        this.productCost.setText(String.valueOf(this.product.getPrice()));
        this.productMin.setText(String.valueOf(this.product.getMin()));
        this.productMax.setText(String.valueOf(this.product.getMax()));
        if (!(product.getAllAssociatedParts().isEmpty()))
            this.associatedParts.addAll(product.getAllAssociatedParts());

        partTable.setItems(Inventory.getAllParts());
        associatedPartTable.setItems(associatedParts);

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        assPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assPartInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        partSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPart(newValue);
        });
    }

    /**
     * add part to association list
     * @param actionEvent
     */
    public void addAssociatedPart(ActionEvent actionEvent) {
        Part part = (Part)partTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            errorMessage(5);
            return;
        }
        if (!associatedParts.contains(part))
            this.associatedParts.add(part);
    }

    /**
     * remove part from association list
     * @param actionEvent
     */
    public void removeAssociatedPart(ActionEvent actionEvent) {
        Part part = (Part)associatedPartTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            errorMessage(5);
            return;
        }
        if (confirmDelete()) {
            this.associatedParts.remove(part);
        }
    }

    /**
     * do you want to delete
     * @return yes or no
     */
    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * error handler
     * @param code error code
     */
    private void errorMessage(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (code == 1) {
            alert.setHeaderText("Wrong Input Type");
            alert.setContentText("Must be an integer.");
        } else if (code == 2) {
            alert.setHeaderText("Improper Bounds");
            alert.setContentText("Maximum must be larger than Minimum");
        } else if (code == 3) {
            alert.setHeaderText("Empty Field");
            alert.setContentText("Field must not be empty");
        } else if (code == 4) {
            alert.setHeaderText("Invalid Inventory Count");
            alert.setContentText("Inventory must be more than min and less than max");
        } else if (code == 5) {
            alert.setHeaderText("Nothing Selected");
            alert.setContentText("Must select item to continue");
        } else if (code == 6) {
            alert.setHeaderText("Wrong Input type");
            alert.setContentText("Must be a valid price");
        }
        alert.showAndWait();
    }

    /**
     * is this a valid integer
     * @param name integer to test
     * @return is valid
     */
    private boolean validateInteger(String name) {
        try {
            Integer.parseInt(name);
            return true;
        } catch (NumberFormatException ex) {
            errorMessage(1);
            return false;
        }
    }

    /**
     * is this a valid double
     * @param name double to test
     * @return is valid
     */
    private boolean validateDouble(String name) {
        if (!name.isEmpty()) {
            Pattern pattern = Pattern.compile("^\\d*\\.?\\d+|^\\d+\\.?\\d*$");
            Matcher matcher = pattern.matcher(name);
            if (!matcher.find()) {
                errorMessage(6);
                return false;
            }
        }
        return true;
    }

    /**
     * min less than max
     * @param min min to test
     * @return is less than
     */
    private boolean validateMin(int min) {
        if (!productMax.getText().isEmpty()) {
            if (min > Integer.parseInt(this.productMax.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    /**
     * max more than min
     * @param max max to test
     * @return is more than
     */
    private boolean validateMax(int max) {
        if (!productMin.getText().isEmpty()) {
            if (max < Integer.parseInt(this.productMin.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    /**
     * inv between min and max
     * @param inv inv count
     * @return yes or no
     */
    private boolean validateInventory(int inv) {
        if (!productMax.getText().isEmpty() && !productMin.getText().isEmpty()) {
            if (inv >= Integer.parseInt(productMin.getText()) && inv <= Integer.parseInt(productMax.getText())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * check user input and update product
     * @param actionEvent
     * @throws IOException
     */
    public void updateProduct(ActionEvent actionEvent) throws IOException {
        if (productName.getText().isEmpty() ||
                productCost.getText().isEmpty() ||
                productInventory.getText().isEmpty() ||
                productMin.getText().isEmpty() ||
                productMax.getText().isEmpty()) {
            errorMessage(3);
            return;
        }
        if (!validateDouble(productCost.getText()) ||
                !validateInteger(productInventory.getText()) ||
                !validateInteger(productMin.getText()) ||
                !validateInteger(productMax.getText())) {
            return;
        }
        String name = productName.getText();
        double cost = Double.parseDouble(productCost.getText());
        int inv = Integer.parseInt(productInventory.getText());
        int minimum = Integer.parseInt(productMin.getText());
        int maximum = Integer.parseInt(productMax.getText());

        if(!validateMax(maximum))
            return;
        if(!validateMin(minimum))
            return;
        if(!validateInventory(inv)) {
            errorMessage(4);
            return;
        }

        Product newProduct = new Product(product.getId(), name, cost, inv, minimum, maximum);
        for (Part currentPart : associatedParts) {
            newProduct.addAssociatedPart(currentPart);
        }
        inventory.updateProduct(this.index, newProduct);
        toMainForm(actionEvent);
    }

    /**
     * filter list by input
     * @param input input to filter by
     */
    private void filterPart(String input) {
        if (!input.isEmpty()) {
            Pattern pattern = Pattern.compile("^\\d+$");
            Matcher matcher = pattern.matcher(input);
            ObservableList<Part> matches = FXCollections.observableArrayList();
            if (matcher.find()) {
                int intput = Integer.parseInt(input);
                matches.add(inventory.lookupPart(intput));
            } else {
                matches.addAll(inventory.lookupPart(input));
            }

            partTable.setItems(matches);

        } else {
            partTable.setItems(inventory.getAllParts());
        }
    }

    /**
     * return to main view
     * @param actionEvent
     * @throws IOException
     */
    public void toMainForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainForm.fxml"));
        controller.MainFormController controller = new controller.MainFormController(inventory);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 350);
            stage.setTitle("Inventory Management");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
