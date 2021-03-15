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

    /**
     * generates random id and initializes inv
     * @param inv singleton inventory
     */
    public AddPartFormController(Inventory inv) {
        this.inventory = inv;
        id = ThreadLocalRandom.current().nextInt(1000,2000);
    }

    /**
     * set up listener for label change
     * @param url
     * @param resourceBundle
     */
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

    /**
     * check to see if string is an integer
     * @param name string to be checked
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
     * is this string a double
     * @param name string to be check
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
     * is min less than max
     * @param min min
     * @return is less
     */
    private boolean validateMin(int min) {
        if (!max.getText().isEmpty()) {
            if (min > Integer.parseInt(this.max.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    /**
     * is max more than min
     * @param max max
     * @return is greater
     */
    private boolean validateMax(int max) {
        if (!min.getText().isEmpty()) {
            if (max < Integer.parseInt(this.min.getText())) {
                errorMessage(2);
                return false;
            }
        }
        return true;
    }

    /**
     * is inventory between min and max
     * @param inv inv count
     * @return is between
     */
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

    /**
     * displays assorted errors
     * should be made into a class
     * @param code id of error
     */
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
        } else if (code == 6) {
            alert.setHeaderText("Wrong Input type");
            alert.setContentText("Must be a valid price");
        }
        alert.showAndWait();
    }

    /**
     * adds part after validation
     * @param actionEvent
     * @throws IOException
     */
    public void addPart(ActionEvent actionEvent) throws IOException {
        if (partName.getText().isEmpty() ||
                price.getText().isEmpty() ||
                stock.getText().isEmpty() ||
                min.getText().isEmpty() ||
                max.getText().isEmpty()) {
            errorMessage(3);
            return;
        }
        if (!validateDouble(price.getText()) ||
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
            if (!validateInteger(partInfo.getText())) {
                return;
            }
            int machineId = Integer.parseInt(partInfo.getText());
            inventory.addPart(new InHouse(id, name, cost, inv, minimum, maximum, machineId));
        } else if (partType == "outsourced") {
            String companyName = partInfo.getText();
            if (companyName.isEmpty()) {
                errorMessage(3);
                return;
            }
            inventory.addPart(new OutSourced(id, name, cost, inv, minimum, maximum, companyName));
        }
        toMainForm(actionEvent);
    }

    /**
     * returns to the main view
     * @param actionEvent
     * @throws IOException
     */
    public void toMainForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainForm.fxml"));
        controller.MainFormController controller = new controller.MainFormController(inventory);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
