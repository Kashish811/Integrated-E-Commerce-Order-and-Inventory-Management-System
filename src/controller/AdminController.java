package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.InventoryService;
import service.ProductService;
import util.Session;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    private ProductService productService = new ProductService();
    private InventoryService inventoryService = new InventoryService();

    @FXML private TextField nameField, priceField, categoryField;
    @FXML private TextField updateIdField, updateNameField, updatePriceField, updateCategoryField;
    @FXML private TextField deleteIdField;
    @FXML private TextField invProductIdField, stockField;
    @FXML private TableView<model.InventoryItem> inventoryTable;
    @FXML private TableColumn<model.InventoryItem, Integer> colInvId;
    @FXML private TableColumn<model.InventoryItem, String> colInvName;
    @FXML private TableColumn<model.InventoryItem, Double> colInvPrice;
    @FXML private TableColumn<model.InventoryItem, Integer> colInvCategory;
    @FXML private TableColumn<model.InventoryItem, Integer> colInvStock;

    @Override
    // Method initialize
    public void initialize(URL url, ResourceBundle rb) {
        if (colInvId != null) {
            colInvId.setCellValueFactory(new PropertyValueFactory<>("productId"));
            colInvName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colInvPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colInvCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
            colInvStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            loadInventory();
        }
    }
    @FXML
    // Method loadInventory
    public void loadInventory() {
        try {
            inventoryTable.getItems().setAll(inventoryService.getAllInventory());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load inventory.");
        }
    }
    @FXML
    // Method addProduct
    public void addProduct() {
        if (nameField.getText().isEmpty() ||
            priceField.getText().isEmpty() ||
            categoryField.getText().isEmpty()) {

            showError("Please fill all fields");
            return;
        }

        try {
            double price = Double.parseDouble(priceField.getText());
            int categoryId = Integer.parseInt(categoryField.getText());

            if (price <= 0 || categoryId <= 0) {
                throw new Exception("Invalid values");
            }

            productService.addProduct(
                    nameField.getText(),
                    price,
                    categoryId
            );

            showInfo("Product added!");

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method updateProduct
    public void updateProduct() {
        try {
            int id = Integer.parseInt(updateIdField.getText());
            double price = Double.parseDouble(updatePriceField.getText());
            int categoryId = Integer.parseInt(updateCategoryField.getText());

            if (id <= 0 || price <= 0 || categoryId <= 0) {
                throw new Exception("Invalid values");
            }

            productService.updateProduct(
                    id,
                    updateNameField.getText(),
                    price,
                    categoryId
            );

            showInfo("Product updated!");

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method deleteProduct
    public void deleteProduct() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Delete product?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                int id = Integer.parseInt(deleteIdField.getText());

                if (id <= 0) throw new Exception("Invalid Product ID");

                productService.deleteProduct(id);
                showInfo("Product deleted!");

            } catch (Exception e) {
                e.printStackTrace();
                showError(e.getMessage());
            }
        }
    }
    @FXML
    // Method addStock
    public void addStock() {
        try {
            int productId = Integer.parseInt(invProductIdField.getText());
            int stock = Integer.parseInt(stockField.getText());

            if (productId <= 0 || stock <= 0) {
                throw new Exception("Values must be positive");
            }

            inventoryService.addStock(productId, stock);
            showInfo("Stock updated!");

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method logout
    public void logout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Logout?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Session.setCustomerId(0);

                Stage stage = (Stage) nameField.getScene().getWindow();
                stage.setScene(new Scene(
                        FXMLLoader.load(getClass().getResource("/view/login.fxml"))
                ));

            } catch (Exception e) {
                e.printStackTrace();
                showError("Logout failed");
            }
        }
    }
    private void showError(String msg) {
        String safeMsg = (msg == null || msg.trim().isEmpty()) ? "An unexpected error occurred." : msg;
        Alert alert = new Alert(Alert.AlertType.ERROR, safeMsg);
        alert.setHeaderText("Error");
        alert.show();
    }

    private void showInfo(String msg) {
        String safeMsg = (msg == null || msg.trim().isEmpty()) ? "Success" : msg;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, safeMsg);
        alert.setHeaderText("Information");
        alert.show();
    }
}
