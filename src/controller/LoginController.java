package controller;

import au.edu.uts.ap.javafx.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.application.*;
import model.exception.UnauthorisedAccessException;

public class LoginController extends Controller<League> {
    @FXML private TextField managerIdTf;
    @FXML private Button loginBtn;
    @FXML private Button exitBtn;

    @FXML private void initialize() {
        loginBtn.disableProperty().bind(managerIdTf.textProperty().isEmpty());
        loginBtn.setDefaultButton(true);
    };

    @FXML private void handleLogin(){
    
    try {
        String input = managerIdTf.getText();
        int managerId = Integer.parseInt(input);

        Manager manager = model.validateManager(managerId);
        model.setLoggedInManager(manager);

        ViewLoader.showStage(manager, "/view/ManagerDashboardView.fxml", "Manager Dashboard", stage);
    } catch (UnauthorisedAccessException e){
        ViewLoader.showStage(e, "/view/ErrorView.fxml", "Error", new Stage());
    } catch (NumberFormatException e) {
        UnauthorisedAccessException ex = new UnauthorisedAccessException("Invalid format for manager id.");
        ViewLoader.showStage(ex, "/view/ErrorView.fxml", "Error", new Stage());
    }
}

    @FXML private void handleExit(){
        stage.close();
    }
}


