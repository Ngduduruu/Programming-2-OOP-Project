package controller;

import javafx.fxml.FXML;
import au.edu.uts.ap.javafx.*;
import model.application.*;
import javafx.scene.control.*;
import model.*;
import model.exception.*;

public class ErrorController extends Controller<Exception> {
    @FXML private Label errorLbl;
    @FXML private Label messageLbl;
    @FXML private Button closeBtn;

    @FXML
    private void initialize() {
        errorLbl.setText(model.getClass().getSimpleName());
        messageLbl.setText(model.getMessage());
    }

    @FXML private void handleClose() {
        stage.close();
    }
    
}
