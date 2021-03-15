package controller;//package sample;

import javafx.application.Platform;
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

public class MainFormController implements Initializable {

    public TableView partTable;
    public TableColumn partId;
    public TableColumn partName;
    public TableColumn partInventory;
    public TableColumn partPrice;

    public TableView productTable;
    public TableColumn productId;
    public TableColumn productName;
    public TableColumn productInventory;
    public TableColumn productPrice;

    @FXML
    private TextField partSearchBar;
    @FXML
    private TextField productSearchBar;

    Inventory inventory;

    /**
     * initiate simpleton
     * @param inv global
     */
    public MainFormController(Inventory inv) {
        this.inventory = inv;
    }

    /**
     * initialize tables and search bar listeners
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTable.setItems(Inventory.getAllParts());
        productTable.setItems(Inventory.getAllProducts());

        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        partSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPart(newValue);
        });

        productSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProduct(newValue);
        });
    }

    /**
     * go to a new window
     * @param actionEvent event
     * @param windowName window name
     * @param height window height
     * @param width window width
     * @param loader use the loader attached to our custom controller
     * @throws IOException
     */
    public void changeScene(ActionEvent actionEvent, String windowName, int height, int width, FXMLLoader loader) throws IOException {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, height, width);
        stage.setTitle(windowName);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * are you sure?
     * @return yes or no
     */
    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
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
     * remove part from inventory
     * @param actionEvent button press
     */
    public void deletePart(ActionEvent actionEvent) {
        Part part = (Part)partTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            errorMessage(1);
            return;
        }
        if (confirmDelete())
            inventory.deletePart(part);
    }

    /**
     * remove product from inventory
     * @param actionEvent buttong press
     */
    public void deleteProduct(ActionEvent actionEvent) {
        Product product = (Product)productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            errorMessage(1);
            return;
        } else if (!product.getAllAssociatedParts().isEmpty()) {
            errorMessage(3);
            return;
        }
        if (confirmDelete())
            inventory.deleteProduct(product);
    }

    /**
     * filters part list
     * @param input string to filter by
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
            if (matches.isEmpty()) {
                partTable.setPlaceholder(new Label("No items match your search."));
            } else {
                partTable.setPlaceholder(new Label("No Content in Table"));
            }
            partTable.setItems(matches);
        } else {
            partTable.setItems(inventory.getAllParts());
        }
    }

    /**
     * filters product list
     * @param input string to filter by
     */
    private void filterProduct(String input) {
        if (!input.isEmpty()) {
            Pattern pattern = Pattern.compile("^\\d+$");
            Matcher matcher = pattern.matcher(input);
            ObservableList<Product> matches = FXCollections.observableArrayList();
            if (matcher.find()) {
                int intput = Integer.parseInt(input);
                matches.add(inventory.lookupProduct(intput));
            } else {
                matches.addAll(inventory.lookupProduct(input));
            }
            if (matches.isEmpty()) {
                productTable.setPlaceholder(new Label("No items match your search."));
            } else {
                productTable.setPlaceholder(new Label("No Content in Table"));
            }
            productTable.setItems(matches);
        } else {
            productTable.setItems(inventory.getAllProducts());
        }
    }

    /**
     * error message handler
     * should be another class
     * @param code error code
     */
    private void errorMessage(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (code == 1) {
            alert.setHeaderText("Delete Failed");
            alert.setContentText("No option selected");
        } else if (code == 2) {
            alert.setHeaderText("No Selection");
            alert.setContentText("Please select an item");
        } else if (code == 3) {
            alert.setHeaderText("Non-empty Product");
            alert.setContentText("Unable to delete products with associated parts");
        }
        alert.showAndWait();
    }

    /**
     * go to add part window
     * instantiated controller and passed inventory
     * @param actionEvent
     * @throws IOException
     */
    public void toAddPartForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddPartForm.fxml"));
        AddPartFormController controller = new AddPartFormController(inventory);
        loader.setController(controller);
        try {
            changeScene(actionEvent, "Add Part", 600, 400, loader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * go to modify part window pass inventory
     * @param actionEvent
     * @throws IOException
     */
    public void toModifyPartForm(ActionEvent actionEvent) throws IOException {
        Part part = (Part)partTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            errorMessage(2);
            return;
        }
        int index = partTable.getSelectionModel().getFocusedIndex();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPartForm.fxml"));
        ModifyPartFormController controller = new ModifyPartFormController(inventory, part, index);
        loader.setController(controller);
        try {
            changeScene(actionEvent, "Modify Part", 600, 400, loader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * go to add product pass inventory
     * @param actionEvent
     * @throws IOException
     */
    public void toAddProductForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddProductForm.fxml"));
        AddProductFormController controller = new AddProductFormController(inventory);
        loader.setController(controller);
        try {
            changeScene(actionEvent, "Add Product", 850, 600, loader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * go to modify product pass inventory
     * @param actionEvent
     * @throws IOException
     */
    public void toModifyProductForm(ActionEvent actionEvent) throws IOException {
        Product product = (Product)productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            errorMessage(2);
            return;
        }
        int index = productTable.getSelectionModel().getSelectedIndex();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProductForm.fxml"));
        ModifyProductFormController controller = new ModifyProductFormController(inventory, product, index);
        loader.setController(controller);
        try {
            changeScene(actionEvent, "Modify Product", 850, 600, loader);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * exit
     * @param actionEvent
     */
    public void exitApplication(ActionEvent actionEvent) {
        Platform.exit();
    }
}
