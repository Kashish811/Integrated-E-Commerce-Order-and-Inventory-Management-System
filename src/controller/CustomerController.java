package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import model.CartItem;
import model.Product;
import model.Order;
import model.OrderItem;

import service.CartService;
import service.ProductService;
import service.OrderService;

import util.Session;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class CustomerController
 * Simple comment for CustomerController
 */
public class CustomerController implements Initializable {

    private ProductService productService = new ProductService();
    private CartService cartService = new CartService();
    private OrderService orderService = new OrderService();
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProductId;
    @FXML private TableColumn<Product, String> colProductName;
    @FXML private TableColumn<Product, Double> colProductPrice;
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, Integer> colCartProductId;
    @FXML private TableColumn<CartItem, String> colCartName;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, Double> colCartPrice;
    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, Integer> colOrderId;
    @FXML private TableColumn<Order, String> colOrderStatus;
    @FXML private TableColumn<Order, Double> colOrderTotal;
    @FXML private TableView<OrderItem> orderItemTable;
    @FXML private TableColumn<OrderItem, String> colItemName;
    @FXML private TableColumn<OrderItem, Integer> colItemQty;
    @FXML private TableColumn<OrderItem, Double> colItemPrice;
    @FXML private TextField productIdField;
    @FXML private TextField quantityField;
    @FXML private TextField updateCartQtyField;

    @Override
    // Method initialize
    public void initialize(URL url, ResourceBundle rb) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCartProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colOrderTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @FXML
    // Method loadProducts
    public void loadProducts() {
        try {
            productTable.getItems().setAll(productService.getAllProducts());
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method addToCart
    public void addToCart() {
        try {
            int customerId = Session.getCustomerId();
            int productId;
            int quantity;

            String pIdStr = productIdField.getText();
            String qtyStr = quantityField.getText();

            if (pIdStr == null || pIdStr.trim().isEmpty()) {
                Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
                if (selectedProduct == null) {
                    showError("Please enter a Product ID or select a product from the Available Products table.");
                    return;
                }
                productId = selectedProduct.getId();
            } else {
                productId = Integer.parseInt(pIdStr.trim());
            }

            if (qtyStr == null || qtyStr.trim().isEmpty()) {
                quantity = 1;
            } else {
                quantity = Integer.parseInt(qtyStr.trim());
            }

            if (quantity <= 0) {
                showError("Quantity must be a positive number.");
                return;
            }

            cartService.addToCart(customerId, productId, quantity);
            showInfo("Added to cart!");
            viewCart();

        } catch (NumberFormatException ex) {
            showError("Please enter valid numeric values for Product ID and Quantity.");
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage() != null ? e.getMessage() : "Unknown error occurred while adding to cart.");
        }
    }
    @FXML
    // Method viewCart
    public void viewCart() {
        try {
            int customerId = Session.getCustomerId();
            cartTable.getItems().setAll(cartService.getCartItems(customerId));
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method updateCartQuantity
    public void updateCartQuantity() {
        try {
            CartItem selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an item from the cart first");
                return;
            }

            int customerId = Session.getCustomerId();
            int newQty = Integer.parseInt(updateCartQtyField.getText());

            cartService.updateCartQuantity(customerId, selected.getProductId(), newQty);
            showInfo("Cart updated!");
            viewCart();

        } catch (NumberFormatException ex) {
            showError("Please enter a valid number for quantity");
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method removeFromCart
    public void removeFromCart() {
        try {
            CartItem selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an item from the cart first");
                return;
            }

            int customerId = Session.getCustomerId();
            cartService.removeFromCart(customerId, selected.getProductId());
            showInfo("Item removed from cart!");
            viewCart();

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method checkout
    public void checkout() {
        try {
            int customerId = Session.getCustomerId();

            orderService.checkout(customerId);

            cartTable.getItems().clear();
            showInfo("Order placed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method viewOrders
    public void viewOrders() {
        try {
            int customerId = Session.getCustomerId();
            orderTable.getItems().setAll(orderService.getOrders(customerId));
        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method viewOrderDetails
    public void viewOrderDetails() {
        try {
            Order selected = orderTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showError("Select an order first");
                return;
            }

            List<OrderItem> items =
                    orderService.getOrderItems(selected.getOrderId());

            orderItemTable.getItems().setAll(items);

        } catch (Exception e) {
            e.printStackTrace();
            showError(e.getMessage());
        }
    }
    @FXML
    // Method logout
    public void logout() {
        try {
            Session.setCustomerId(0);

            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/view/login.fxml"))
            ));

        } catch (Exception e) {
            e.printStackTrace();
            showError("Logout failed");
        }
    }
    private void showError(String msg) {
        String safeMsg = (msg == null || msg.trim().isEmpty()) ? "An unexpected error occurred." : msg;
        Alert alert = new Alert(Alert.AlertType.ERROR, safeMsg);
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        String safeMsg = (msg == null || msg.trim().isEmpty()) ? "Success" : msg;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, safeMsg);
        alert.setHeaderText("Information");
        alert.showAndWait();
    }
}
