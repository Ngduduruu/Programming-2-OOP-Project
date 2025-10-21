package controller;

import au.edu.uts.ap.javafx.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.application.*;

public class ManagerDashboardController extends Controller<Manager> {
    
    @FXML private Label teamLbl;
    @FXML private ImageView jerseyIv;
    @FXML private Button withdrawBtn;
    @FXML private Button manageBtn;
    @FXML private Button swapBtn;
    @FXML private Button closeBtn;
    
    @FXML
    private void initialize() {

        withdrawBtn.disableProperty().bind(model.teamProperty().isNull());
        manageBtn.disableProperty().bind(model.teamProperty().isNull());
        
        updateDashboard();
        model.teamProperty().addListener((obs, oldTeam, newTeam) -> {
            updateDashboard();
            
        });
    
        
    }  

    @FXML 
    private void handleWithdraw() {
        League league = League.getInstance();
        league.withdrawManagerFromTeam(model);

    }

    private void updateDashboard() {
        Team team = model.getTeam();
        
        if (team != null) {
            teamLbl.setText(team.toString());
            
            String teamname = team.getTeamName().toLowerCase();
            String jerseyPath = "/view/image/" + teamname + ".png";
            Image jerseyImage = new Image(getClass().getResourceAsStream(jerseyPath));
            jerseyIv.setImage(jerseyImage);

        } else {
            teamLbl.setText("No Team");
            Image nonImage = new Image(getClass().getResourceAsStream("/view/image/none.png"));
            jerseyIv.setImage(nonImage);
        }
    }

    @FXML 
    private void handleManage(){
        if(model.getTeam() != null) {
            ViewLoader.showStage(model.getTeam(), "/view/TeamDashboardView.fxml", "Team Dashboard", stage);
        }
    }

    @FXML
    private void handleSwap(){
        League model = League.getInstance();
        Stage stage = new Stage();
        ViewLoader.showStage(model, "/view/SwapView.fxml", "Swap", stage);
    }

    @FXML
    private void handleExit() {
        stage.close();
    }
}
