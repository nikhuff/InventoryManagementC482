package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartFormController implements Initializable {
    @FXML
    private RadioButton radioInHouse;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private ToggleGroup PartType;
    @FXML
    private Label partTypeLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartType.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == radioInHouse) {
                partTypeLabel.setText("Machine ID");
            } else if (newToggle == radioOutsourced) {
                partTypeLabel.setText("Company Name");
            } else {
                partTypeLabel.setText("Invalid Option");
            }
        });
    }

    public void toMainForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Inventory Management");
        stage.setScene(new Scene(root, 788,353));
        stage.show();
    }
}
