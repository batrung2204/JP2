// Main.java - Full JavaFX application for HeroGame
package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.dao.NationalDAO;
import org.example.dao.PlayerDAO;
import org.example.model.National;
import org.example.model.Player;

import java.util.List;

public class Main extends Application {
    private final PlayerDAO playerDAO = new PlayerDAO();
    private final NationalDAO nationalDAO = new NationalDAO();

    private TableView<Player> table;
    private ObservableList<Player> playerList = FXCollections.observableArrayList();

    private TextField txtName, txtHighScore, txtLevel, txtSearch;
    private ComboBox<National> cboNational;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Input fields
        HBox inputBox = new HBox(10);
        txtName = new TextField();
        txtName.setPromptText("Name");
        txtHighScore = new TextField();
        txtHighScore.setPromptText("High Score");
        txtLevel = new TextField();
        txtLevel.setPromptText("Level");

        cboNational = new ComboBox<>();
        try {
            cboNational.getItems().addAll(nationalDAO.getAllNationals());
        } catch (Exception e) {
            showAlert("Lỗi load national: " + e.getMessage());
        }

        inputBox.getChildren().addAll(txtName, txtHighScore, txtLevel, cboNational);

        // Buttons
        HBox buttonBox = new HBox(10);
        Button btnAdd = new Button("Add");
        Button btnDelete = new Button("Delete");
        Button btnSearch = new Button("Search");
        Button btnTop10 = new Button("Top 10");
        txtSearch = new TextField();
        txtSearch.setPromptText("Search by name");

        buttonBox.getChildren().addAll(btnAdd, btnDelete, txtSearch, btnSearch, btnTop10);

        // TableView
        table = new TableView<>();
        TableColumn<Player, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        colId.setPrefWidth(50);

        TableColumn<Player, Integer> colNationalId = new TableColumn<>("National ID");
        colNationalId.setCellValueFactory(new PropertyValueFactory<>("nationalId"));

        TableColumn<Player, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("playerName"));

        TableColumn<Player, Integer> colHighScore = new TableColumn<>("High Score");
        colHighScore.setCellValueFactory(new PropertyValueFactory<>("highScore"));

        TableColumn<Player, Integer> colLevel = new TableColumn<>("Level");
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));

        table.getColumns().addAll(colId, colNationalId, colName, colHighScore, colLevel);
        table.setItems(playerList);
        refreshTable();

        // Button actions
        btnAdd.setOnAction(e -> addPlayer());
        btnDelete.setOnAction(e -> {
            try {
                deletePlayer();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnSearch.setOnAction(e -> searchPlayer());
        btnTop10.setOnAction(e -> showTop10Players());

        // Layout
        root.getChildren().addAll(new Label("HeroGame Manager"), inputBox, buttonBox, table);

        stage.setScene(new Scene(root, 800, 500));
        stage.setTitle("HeroGame Player Manager");
        stage.show();
    }

    private void addPlayer() {
        try {
            String name = txtName.getText();
            int highScore = Integer.parseInt(txtHighScore.getText());
            int level = Integer.parseInt(txtLevel.getText());
            National national = cboNational.getValue();

            if (national == null || name.isBlank()) {
                showAlert("Please fill all fields.");
                return;
            }

            Player player = new Player(0, national.getNationalId(), name, highScore, level);
            playerDAO.insertPlayer(player);
            refreshTable();
            clearForm();
        } catch (Exception e) {
            showAlert("Invalid input: " + e.getMessage());
        }
    }

    private void deletePlayer() throws Exception {
        Player selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select player to delete.");
            return;
        }

        playerDAO.deletePlayer(selected.getPlayerId());
        refreshTable();
    }

    private void searchPlayer() {
        try {
            String keyword = txtSearch.getText();
            List<Player> result = playerDAO.displayByName(keyword);
            playerList.setAll(result);
        } catch (Exception e) {
            showAlert("Search failed: " + e.getMessage());
        }
    }

    private void showTop10Players() {
        try {
            List<Player> top = playerDAO.displayTop10();
            playerList.setAll(top);
        } catch (Exception e) {
            showAlert("Error loading top 10: " + e.getMessage());
        }
    }

    private void refreshTable() {
        try {
            playerList.setAll(playerDAO.displayAll());
        } catch (Exception e) {
            showAlert("Error loading players: " + e.getMessage());
        }
    }

    private void clearForm() {
        txtName.clear();
        txtHighScore.clear();
        txtLevel.clear();
        cboNational.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}