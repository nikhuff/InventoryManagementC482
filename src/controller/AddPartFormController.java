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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartFormController implements Initializable {
    Inventory inventory;

    public TextField partName;
    public TextField stock;
    public TextField price;
    public TextField min;
    public TextField max;
    public TextField partInfo;

    @FXML
    private RadioButton radioInHouse;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private ToggleGroup PartType;
    @FXML
    private Label partTypeLabel;
    private String partType = "inHouse";
    private int id = 1;

    public AddPartFormController(Inventory inv) {
        this.inventory = inv;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartType.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == radioInHouse) {
                partTypeLabel.setText("Machine ID");
                partType = "inHouse";
            } else if (newToggle == radioOutsourced) {
                partTypeLabel.setText("Company Name");
                partType = "outsourced";
            } else {
                partTypeLabel.setText("Invalid Option");
                partType = null;
            }
        });
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

    public void addPart(ActionEvent actionEvent) throws IOException {
        // temporary
        String name = partName.getText();
        double cost = Double.parseDouble(price.getText());
        int inv = Integer.parseInt(stock.getText());
        int minimum = Integer.parseInt(min.getText());
        int maximum = Integer.parseInt(max.getText());

        if (partType == "inHouse") {
            int machineId = Integer.parseInt(partInfo.getText());
            inventory.addPart(new InHouse(id, name, cost, inv, minimum, maximum, machineId));
        } else if (partType == "outsourced") {
            String companyName = partInfo.getText();
            inventory.addPart(new OutSourced(id, name, cost, inv, minimum, maximum, companyName));
        }
        toMainForm(actionEvent);
    }
}
