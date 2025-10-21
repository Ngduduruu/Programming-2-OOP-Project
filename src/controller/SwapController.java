package controller;

import au.edu.uts.ap.javafx.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.application.*;


public class SwapController extends Controller<League>{

    @FXML private Label title;
    @FXML private ListView<Team> teamsLv;
    @FXML private Button swapBtn;
    @FXML private Button closeBtn;

    @FXML
    private void initialize() {
        teamsLv.setItems(model.getManageableTeams().getTeams());

        teamsLv.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            swapBtn.setDisable(newVal == null);
        });
    }

    private Team getSelectedTeam() {
        return teamsLv.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleSwap(){
        Team selectedTeam = getSelectedTeam();
        System.out.println("Selected team: " + selectedTeam);
        
        if(selectedTeam != null) {
            Manager manager = model.getLoggedInManager();
            model.setManagerForTeam(manager, selectedTeam);
        }
    }

    @FXML
    private void handleClose(){
        stage.close();
    }
}
