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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyPartFormController implements Initializable {
    Inventory inventory;
    Part part;
    private int index;
    @FXML
    private RadioButton radioInHouse;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private ToggleGroup PartType;
    @FXML
    private Label partTypeLabel;

    public TextField partId;
    public TextField partName;
    public TextField partInventory;
    public TextField partCost;
    public TextField partMax;
    public TextField partMin;
    public TextField partTypeId;

    public ModifyPartFormController(Inventory inv, Part part, int index) {
        this.inventory = inv;
        this.index = index;
        this.part = part;
    }

    private void updateRadio() {
        if (radioInHouse.isSelected()) {
            partTypeLabel.setText("Machine ID");
        } else if (radioOutsourced.isSelected()) {
            partTypeLabel.setText("Company Name");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.partId.setText(String.valueOf(part.getId()));
        this.partName.setText(part.getName());
        this.partInventory.setText(String.valueOf(part.getStock()));
        this.partCost.setText(String.valueOf(part.getPrice()));
        this.partMax.setText(String.valueOf(part.getMax()));
        this.partMin.setText(String.valueOf(part.getMin()));
        if (part.getClass() == OutSourced.class) {
            OutSourced outSourced = (OutSourced)part;
            this.partTypeId.setText(outSourced.getCompanyName());
            PartType.selectToggle(radioOutsourced);
        } else if (part.getClass() == InHouse.class) {
            InHouse inHouse = (InHouse)part;
            this.partTypeId.setText(String.valueOf(inHouse.getMachineId()));
            PartType.selectToggle(radioInHouse);
        }
        updateRadio();
        PartType.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            updateRadio();
        });
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
        if (!partMax.getText().isEmpty()) {
            if (min > Integer.parseInt(this.partMax.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    private boolean validateMax(int max) {
        if (!partMin.getText().isEmpty()) {
            if (max < Integer.parseInt(this.partMin.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    private boolean validateInventory(int inv) {
        if (!partMax.getText().isEmpty() && !partMin.getText().isEmpty()) {
            if (inv >= Integer.parseInt(partMin.getText()) && inv <= Integer.parseInt(partMax.getText())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void save(ActionEvent actionEvent) throws IOException {
        if (partName.getText().isEmpty() ||
                partCost.getText().isEmpty() ||
                partInventory.getText().isEmpty() ||
                partMin.getText().isEmpty() ||
                partMax.getText().isEmpty()) {
            errorMessage(3);
            return;
        }
        if (!validateInteger(partCost.getText()) ||
                !validateInteger(partInventory.getText()) ||
                !validateInteger(partMin.getText()) ||
                !validateInteger(partMax.getText())) {
            return;
        }
        int id = Integer.parseInt(partId.getText().trim());
        String name = partName.getText().trim();
        int count = Integer.parseInt(partInventory.getText().trim());
        double cost = Double.parseDouble(partCost.getText().trim());
        int max = Integer.parseInt(partMax.getText().trim());
        int min = Integer.parseInt(partMin.getText().trim());

        if(!validateMax(max))
            return;
        if(!validateMin(min))
            return;
        if(!validateInventory(count)) {
            errorMessage(4);
            return;
        }

        if (radioInHouse.isSelected()) {
            updateInHouse(id, name, count, cost, max, min);
        } else if (radioOutsourced.isSelected()) {
            updateOutsourced(id, name, count, cost, max, min);
        }
        toMainForm(actionEvent);
    }

    private void updateInHouse(int id, String name, int count, double cost, int max, int min) {
        int machineId = Integer.parseInt(partTypeId.getText().trim());
        InHouse updated = new InHouse(id, name, cost, count, min, max, machineId);
        inventory.updatePart(this.index, updated);
    }

    private void updateOutsourced(int id, String name, int count, double cost, int max, int min) {
        String companyName = partTypeId.getText().trim();
        OutSourced updated = new OutSourced(id, name, cost, count, min, max, companyName);
        inventory.updatePart(this.index, updated);
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
