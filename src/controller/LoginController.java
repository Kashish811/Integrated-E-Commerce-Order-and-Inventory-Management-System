package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import service.CustomerService;
import util.Session;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private CustomerService customerService = new CustomerService();

    @FXML
    // Method handleLogin
    public void handleLogin() {
        try {
            String email = emailField.getText();
            String password = passwordField.getText();

            int customerId = customerService.login(email, password);
            Session.setCustomerId(customerId);

            String role = customerService.getRole(email);

            Stage stage = (Stage) emailField.getScene().getWindow();

            if (role.equalsIgnoreCase("ADMIN")) {
                stage.setScene(new Scene(
                        FXMLLoader.load(getClass().getResource("/view/admin_dashboard.fxml"))
                ));
            } else {
                stage.setScene(new Scene(
                        FXMLLoader.load(getClass().getResource("/view/customer_dashboard.fxml"))
                ));
            }

        } catch (Exception e) {
            showAlert(e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}
