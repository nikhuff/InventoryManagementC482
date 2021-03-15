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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddProductFormController implements Initializable {
    Inventory inventory;
    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    int id = 1;

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

    public TextField productName;
    public TextField productCount;
    public TextField productPrice;
    public TextField productMax;
    public TextField productMin;

    @FXML
    private TextField partSearchBar;

    public AddProductFormController(Inventory inv) {
        this.inventory = inv;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    public void addAssociatedPart(ActionEvent actionEvent) {
        Part part = (Part)partTable.getSelectionModel().getSelectedItem();
        if (part == null)
            return;
        if (!associatedParts.contains(part))
            this.associatedParts.add(part);
    }

    public void removeAssociatedPart(ActionEvent actionEvent) {
        Part part = (Part)associatedPartTable.getSelectionModel().getSelectedItem();
        if (part == null)
            return;
        this.associatedParts.remove(part);
    }

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

    public void addProduct(ActionEvent actionEvent) throws IOException {
        String name = productName.getText();
        double cost = Double.parseDouble(productPrice.getText());
        int inv = Integer.parseInt(productCount.getText());
        int minimum = Integer.parseInt(productMin.getText());
        int maximum = Integer.parseInt(productMax.getText());

        Product newProduct = new Product(this.id, name, cost, inv, minimum, maximum);
        for (Part currentPart : associatedParts) {
            newProduct.addAssociatedPart(currentPart);
        }
        inventory.addProduct(newProduct);
        toMainForm(actionEvent);
    }

    public void toMainForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainForm.fxml"));
        controller.MainFormController controller = new controller.MainFormController(inventory);
        loader.setController(controller);
        Parent root = loader.load();
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 350);
        stage.setTitle("Inventory Management");
        stage.setScene(scene);
        stage.show();
    }
}
