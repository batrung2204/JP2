package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.example.dao.NationalDAO;
import org.example.dao.PlayerDAO;
import org.example.model.National;
import org.example.model.Player;
import javafx.scene.control.TextField;

import javafx.scene.control.TableColumn; // JavaFX

import java.util.List;


public class PlayerController {
    @FXML
    private TableView<Player> tblPlayers;

    @FXML
    private TableColumn<Player, Integer> colId, colNationalId, colHighScore, colLevel;

    @FXML
    private TableColumn<Player, String> colName;

    @FXML
    private TextField txtName, txtHighScore, txtLevel, txtSearch;

    @FXML
    private ComboBox<National> cboNational;

    private final PlayerDAO playerDAO = new PlayerDAO();
    private final NationalDAO nationalDAO = new NationalDAO();
    private final ObservableList<Player> playerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup table columns
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getPlayerId()).asObject());
        colNationalId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getNationalId()).asObject());
        colName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPlayerName()));
        colHighScore.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getHighScore()).asObject());
        colLevel.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getLevel()).asObject());

        try {
            // Load national list
            cboNational.getItems().addAll(nationalDAO.getAllNationals());
            // Load player list
            displayAllPlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addPlayer(Player player) {
        try {
            String name = txtName.getText();
            int highScore = Integer.parseInt(txtHighScore.getText());
            int level = Integer.parseInt(txtLevel.getText());
            National national = cboNational.getValue();

            if (national == null) {
                showAlert("Please select a nationality!");
                return;
            }

            Player players = new Player(0, national.getNationalId(), name, highScore, level);
            playerDAO.insertPlayer(player);
            displayAllPlayers();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    public void deletePlayer(int playerId) {
        Player selected = tblPlayers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a player to delete.");
            return;
        }

        try {
            playerDAO.deletePlayer(selected.getPlayerId());
            displayAllPlayers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    public List<Player> searchPlayer(String keyword) {
        try {
            String keywords = txtSearch.getText();
            playerList.setAll(playerDAO.displayByName(keyword));
            tblPlayers.setItems(playerList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
        return null;
    }

    @FXML
    public List<Player> showTop10Players() {
        try {
            playerList.setAll(playerDAO.displayTop10());
            tblPlayers.setItems(playerList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
        return null;
    }

    public Player displayAllPlayers() {
        try {
            playerList.setAll(playerDAO.displayAll());
            tblPlayers.setItems(playerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearFields() {
        txtName.clear();
        txtHighScore.clear();
        txtLevel.clear();
        cboNational.getSelectionModel().clearSelection();
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
