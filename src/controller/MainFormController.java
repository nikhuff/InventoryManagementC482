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


    public void changeScene(ActionEvent actionEvent, String resourceName, String windowName, int height, int width) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(resourceName));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, height, width);
        stage.setTitle(windowName);
        stage.setScene(scene);
        stage.show();
    }

    public void toAddPartForm(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "/view/AddPartForm.fxml", "Add Part", 600, 400);
    }

    public void toModifyPartForm(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "/view/ModifyPartForm.fxml", "Modify Part", 600, 400);
    }

    public void toAddProductForm(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "/view/AddProductForm.fxml", "Add Product", 850, 600);
    }

    public void toModifyProductForm(ActionEvent actionEvent) throws IOException {
        changeScene(actionEvent, "/view/ModifyProductForm.fxml", "Modify Product", 850, 600);
    }

    public void exitApplication(ActionEvent actionEvent) throws IOException {
        Platform.exit();
    }
}
