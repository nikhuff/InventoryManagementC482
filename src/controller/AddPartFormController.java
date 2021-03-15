package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private int id;

    public AddPartFormController(Inventory inv) {
        this.inventory = inv;
        id = ThreadLocalRandom.current().nextInt(1000,2000);
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

    private boolean validateInteger(String name) {
        if (!name.isEmpty()) {
            Pattern pattern = Pattern.compile("^\\d*\\.?\\d+|^\\d+\\.?\\d*$");
            Matcher matcher = pattern.matcher(name);
            if (!matcher.find()) {
                errorMessage(1);
                return false;
            }
        }
        return true;
    }

    private boolean validateMin(int min) {
        if (!max.getText().isEmpty()) {
            if (min > Integer.parseInt(this.max.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    private boolean validateMax(int max) {
        if (!min.getText().isEmpty()) {
            if (max < Integer.parseInt(this.min.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }
    private boolean validateInventory(int inv) {
        if (!max.getText().isEmpty() && !min.getText().isEmpty()) {
            if (inv >= Integer.parseInt(min.getText()) && inv <= Integer.parseInt(max.getText())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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

    private void errorMessage(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if (code == 1) {
            alert.setHeaderText("Wrong Input Type");
            alert.setContentText("Must be a number.");
        } else if (code == 2) {
            alert.setHeaderText("Improper Bounds");
            alert.setContentText("Maximum must be larger than Minimum");
        } else if (code == 3) {
            alert.setHeaderText("Empty Field");
            alert.setContentText("Field must not be empty");
        } else if (code == 4) {
            alert.setHeaderText("Invalid Inventory Count");
            alert.setContentText("Inventory must be more than min and less than max");
        }
        alert.showAndWait();
    }

    public void addPart(ActionEvent actionEvent) throws IOException {
        if (partName.getText().isEmpty() ||
                price.getText().isEmpty() ||
                stock.getText().isEmpty() ||
                min.getText().isEmpty() ||
                max.getText().isEmpty()) {
            errorMessage(3);
            return;
        }
        if (!validateInteger(price.getText()) ||
                !validateInteger(stock.getText()) ||
                !validateInteger(min.getText()) ||
                !validateInteger(max.getText())) {
            return;
        }
        String name = partName.getText();
        double cost = Double.parseDouble(price.getText());
        int inv = Integer.parseInt(stock.getText());
        int minimum = Integer.parseInt(min.getText());
        int maximum = Integer.parseInt(max.getText());

        if(!validateMax(maximum))
            return;
        if(!validateMin(minimum))
            return;
        if(!validateInventory(inv)) {
            errorMessage(4);
            return;
        }

        if (partType == "inHouse") {
            int machineId = Integer.parseInt(partInfo.getText());
            if (!validateInteger(partInfo.getText()))
                return;
            inventory.addPart(new InHouse(id, name, cost, inv, minimum, maximum, machineId));
        } else if (partType == "outsourced") {
            String companyName = partInfo.getText();
            inventory.addPart(new OutSourced(id, name, cost, inv, minimum, maximum, companyName));
        }
        toMainForm(actionEvent);
    }
}
