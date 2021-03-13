package controller;//package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Inventory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable {

    public TableView partTable;
    public TableColumn partId;
    public TableColumn partPartName;
    public TableColumn partInventory;
    public TableColumn partPrice;

    public TableView productTable;
    public TableColumn productId;
    public TableColumn productName;
    public TableColumn productInventory;
    public TableColumn productPrice;

    private Inventory inventory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void toAddPartForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 480);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    public void toModifyPartForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPartForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 480);
        stage.setTitle("Modify Part");
        stage.setScene(scene);
        stage.show();
    }

    public void toAddProductForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 842, 612);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    public void toModifyProductForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProductForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 842, 612);
        stage.setTitle("Modify Product");
        stage.setScene(scene);
        stage.show();
    }

    public void exitApplication(ActionEvent actionEvent) throws IOException {
        Platform.exit();
    }
}
