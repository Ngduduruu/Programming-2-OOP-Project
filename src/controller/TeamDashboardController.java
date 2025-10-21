package controller;

import au.edu.uts.ap.javafx.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.application.*;
import model.exception.FillException;
import model.exception.InvalidSigningException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class TeamDashboardController extends Controller<Team> {

    private Team getTeam() { return model; }


    private int clickCount = 0;
    private Player trackedPlayer = null;

     public ObservableList<Player> getCurrentTeamPlayers() {
        ObservableList<Player> currentTeamPlayers = FXCollections.observableArrayList();
        // Use League's global player list
        for (Player player : League.getInstance().getPlayers().getPlayers()) {
            if (player != null && player.getTeam() == model) {  //Only players signed to THIS team
                currentTeamPlayers.add(player);
            }
        }
        return currentTeamPlayers;
    }

    @FXML private Label teamLbl;
    @FXML private TextField playerNameTf;
    @FXML private Button signBtn;
    @FXML private TableView<Player> playersTv;
    @FXML private TableColumn<Player, String> nameClm;
    @FXML private TableColumn<Player, String> positionClm;
    @FXML private Button unsignBtn;
    @FXML private Button closeBtn;

    @FXML private ImageView jersey1Iv;
    @FXML private ImageView jersey2Iv;
    @FXML private ImageView jersey3Iv;
    @FXML private ImageView jersey4Iv;
    @FXML private ImageView jersey5Iv;

    @FXML private Button jersey1Btn;
    @FXML private Button jersey2Btn;
    @FXML private Button jersey3Btn;
    @FXML private Button jersey4Btn;
    @FXML private Button jersey5Btn;
    
    //private Button[] jerseyBtns;
    private ImageView[] jerseyIVs;
    private Tooltip[] tooltips;

    @FXML
    private void handlejersey1() { handleJerseyClick(0); }
    @FXML
    private void handlejersey2() { handleJerseyClick(1); }
    @FXML
    private void handlejersey3() { handleJerseyClick(2); }
    @FXML
    private void handlejersey4() { handleJerseyClick(3); }
    @FXML
    private void handlejersey5() { handleJerseyClick(4); }

    @FXML
    private void initialize() {
        teamLbl.setText(getTeam().toString());

        jerseyIVs = new ImageView[]{jersey1Iv, jersey2Iv, jersey3Iv, jersey4Iv, jersey5Iv};
        tooltips = new Tooltip[]{
            new Tooltip("Unallocated"),
            new Tooltip("Unallocated"),
            new Tooltip("Unallocated"),
            new Tooltip("Unallocated"),
            new Tooltip("Unallocated")
        };

        // Buttons are transparent in FXML, no need to hide them
        
        // Install tooltips
        for (int i = 0; i < jerseyIVs.length; i++) {
            final int index = i;
            jerseyIVs[i].setOnMouseClicked(event -> handleJerseyClick(index));
            jerseyIVs[i].setStyle("-fx-cursor: hand;");//hover replacement for button
            Tooltip.install(jerseyIVs[i], tooltips[i]);
        }

        updateJerseyDisplay();

        // Fix extra column issue in table
        playersTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        nameClm.setCellValueFactory(cellData -> cellData.getValue().fullNameProperty());
        positionClm.setCellValueFactory(cellData -> cellData.getValue().positionProperty());

        refreshTable();

        // Listeners
        playersTv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            unsignBtn.setDisable(newSelection == null);

        });


    playersTv.setOnMouseClicked(event -> {
        Player current = playersTv.getSelectionModel().getSelectedItem();
    
        if (current != null && current.equals(trackedPlayer)) {
            clickCount++;
            if (clickCount % 2 == 0) {
            playersTv.getSelectionModel().clearSelection();
    }
        } else {
        trackedPlayer = current;
        clickCount = 1;
        }
    });

        playerNameTf.textProperty().addListener((obs, oldText, newText) -> {
            signBtn.setDisable(newText.isEmpty());
        });


    }


    private void refreshTable() {
        ObservableList<Player> teamPlayers = FXCollections.observableArrayList();
        for (Player player : League.getInstance().getPlayers().getPlayers()) {
            if (player != null && player.getTeam() == model) {
                teamPlayers.add(player);
            }
        }
        playersTv.setItems(teamPlayers);
        playersTv.refresh();
        System.out.println("Table refreshed. Players signed to team: " + teamPlayers.size());
         for (Player p : teamPlayers) {
            System.out.println("  - " + p.getFullName());
        }
    }

    private void updateJerseyDisplay() {
        Player[] currentTeam = model.getCurrentTeam();

        for (int i = 0; i < Team.REQUIRED_TEAM_SIZE; i++) {
            if (i >= jerseyIVs.length || i >= tooltips.length) {
                continue;
            }
            
            Player p = currentTeam[i];
            if (p == null) {
                // Empty position - grey jersey
                tooltips[i].setText("Unallocated");
                jerseyIVs[i].setImage(new Image("/view/image/none.png"));
            
            } else {
                // Player assigned - team colored jersey
                tooltips[i].setText(p.toString());
                
                // Use the team's name to load the correct colored jersey
                String teamName = model.getTeamName().toLowerCase();
                String jerseyPath = "/view/image/" + teamName + ".png";
                System.out.println("Loading jersey: " + jerseyPath);
                jerseyIVs[i].setImage(new Image(jerseyPath));
            }
        }
    }

    private boolean isPlayerInActiveTeam(Player player) {
        Player[] currentTeam = model.getCurrentTeam();
        for (Player p : currentTeam) {
            if (p != null && p.equals(player)) {
                return true;
            }
        }
        return false;
    }

    private void handleJerseyClick(int index) {
        Player selected = playersTv.getSelectionModel().getSelectedItem();
        Player[] currentTeam = model.getCurrentTeam();

        try {
            if (selected != null) {
                // Player selected - assign to jersey
                if (isPlayerInActiveTeam(selected)) {
                    throw new FillException(selected.getFullName() + " is already in the active team.");
                }
                // Assign new player
                currentTeam[index] = selected;
                updateJerseyDisplay();
                
            } else {
                // No player selected - remove player from jersey
                if (currentTeam[index] != null) {
                    currentTeam[index] = null;
                    updateJerseyDisplay();
                }
            }
        } catch (FillException fe) {
            ViewLoader.showStage(fe, "/view/ErrorView.fxml", "Error", new Stage());
        }
    }

    @FXML
    private void handleSign(ActionEvent event) {
        try {
            String playerName = playerNameTf.getText();
            Player findPlayer = League.getInstance().getPlayers().player(playerName);
        
            if (findPlayer == null) {
                throw new InvalidSigningException("Player does not exist within the League.");
            }
            //check if player exists in another team or current team
            if (findPlayer.getTeam() != null) {
                if (findPlayer.getTeam().equals(model)) {
                    throw new InvalidSigningException(findPlayer.getFullName() + " is already signed to " + model.toString());
                } else {
                    throw new InvalidSigningException("Player is already signed to " + findPlayer.getTeam().toString());
                }
            }
            // Sign the player
            findPlayer.setTeam(model);
            System.out.println("Signed " + findPlayer.getFullName() + " to " + model.toString());
            System.out.println("Player's team is now: " + findPlayer.getTeam());
            playerNameTf.clear();//clear textfield
            refreshTable();
        } catch (InvalidSigningException e) {
            ViewLoader.showStage(e, "/view/ErrorView.fxml", "Error", new Stage());
        }
    }
    
    @FXML
    private void handleUnsign() {
        Player selectedPlayer = playersTv.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            try {
                // Check if player is in active team first
                if (isPlayerInActiveTeam(selectedPlayer)) {
                    throw new InvalidSigningException("Cannot remove " + selectedPlayer.getFullName()+ ". " + "Player is in the active team.");
                }
                // If not in active team, unsign from team roster
                selectedPlayer.setTeam(null);
                playersTv.getSelectionModel().clearSelection();
                refreshTable();
                updateJerseyDisplay();
                
            } catch (InvalidSigningException e) {
                ViewLoader.showStage(e, "/view/ErrorView.fxml", "Error", new Stage());
            }
        }
    }

    @FXML
    private void handleClose() {
        stage.close();
    }
}