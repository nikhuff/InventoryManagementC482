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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

    public void save(ActionEvent actionEvent) throws IOException {
        int id = Integer.parseInt(partId.getText().trim());
        String name = partName.getText().trim();
        int count = Integer.parseInt(partInventory.getText().trim());
        double cost = Double.parseDouble(partCost.getText().trim());
        int max = Integer.parseInt(partMax.getText().trim());
        int min = Integer.parseInt(partMin.getText().trim());
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
