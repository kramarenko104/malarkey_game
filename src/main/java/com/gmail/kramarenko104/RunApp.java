package com.gmail.kramarenko104;

import com.gmail.kramarenko104.controller.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

public class RunApp extends Application {

    private Stage primaryStage;
    private AnchorPane gamePane;
    private TextArea sentencesText;
    private TextField whoText;
    private TextField whatDoesText;
    private TextField whereText;
    private TextField whyText;
    private Game game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Malarkey game v1.1      by Kramarenko Iuliia");
        game = new Game();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RunApp.class.getResource("/GameField.fxml"));
            gamePane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(gamePane);
        primaryStage.setScene(scene);
        initScreenElements();
        primaryStage.show();
    }

    private void initScreenElements() {
        // text fields
        for (Node node : gamePane.getChildren()) {
            if (node.getId() != null) {
                switch (node.getId()) {
                    case "sentencesText":
                        sentencesText = (TextArea) node;
                        break;
                    case "whoText":
                        whoText = (TextField) node;
                        break;
                    case "whatDoesText":
                        whatDoesText = (TextField) node;
                        break;
                    case "whereText":
                        whereText = (TextField) node;
                        break;
                    case "whyText":
                        whyText = (TextField) node;
                        break;
                }
            }
        }

        // buttons' actions
        for (Node node : gamePane.getChildren()) {
            if (node.getStyleClass().toString().equals("button") && node.getId() != null) {
                ((Button) node).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        switch (node.getId()) {
                            case "btnStartGame":
                                String newSentence = game.createSentence();
                                sentencesText.appendText(newSentence + "\n");
                                break;
                            case "btnWho":
                                game.getWho().addWord(whoText.getText());
                                whoText.clear();
                                break;
                            case "btnWhatDoes":
                                game.getWhatDoes().addWord(whatDoesText.getText());
                                whatDoesText.clear();
                                break;
                            case "btnWhere":
                                game.getWhere().addWord(whereText.getText());
                                whereText.clear();
                                break;
                            case "btnWhy":
                                game.getWhy().addWord(whyText.getText());
                                whyText.clear();
                                break;
                            case "btnExit":
                                game.exit();
                                break;
                            case "btnMusic":
                                Button btnMus = (Button) node;
                                String btnMusText = btnMus.getText();
                                if (btnMusText.equals("Play music")) {
                                    game.playMusic(true);
                                    btnMus.setText("Stop music");
                                } else if (btnMusText.equals("Stop music")) {
                                    game.playMusic(false);
                                    btnMus.setText("Play music");
                                }
                                break;
                        }
                    }
                });
            }
        }
    }
}

