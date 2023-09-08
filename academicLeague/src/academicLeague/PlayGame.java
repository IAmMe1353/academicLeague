package academicLeague;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlayGame {
	
	Scene scene;
	Stage window;
	boolean[] ran;
	ArrayList<String> allDecks = new ArrayList<>();
	
	
	public PlayGame(Stage window, String[] decks, String team1, String team2) {
		this.window = window;
		createMegaDeck(decks);
	//	set up Labels
		Label team1Label = new Label(team1);
		team1Label.setFont(Font.font(Main.titleSize));
		Label team2Label = new Label(team2);
		team2Label.setFont(Font.font(Main.titleSize));
	//	set up Button
		Button buzz = new Button("Buzz!");
		buzz.setOnAction(null);
	//	set up VBox and create scene
		VBox mainBox = new VBox(25);
		mainBox.setAlignment(Pos.TOP_CENTER);
		mainBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		mainBox.setPadding(new Insets(10,10,10,10));
		mainBox.getChildren().addAll(team1Label,team2Label,buzz);
		scene = new Scene(mainBox,Main.stageHeight*2,Main.stageHeight);
	//	set window to question
		window.setScene(scene);
	}
	private void createMegaDeck(String[] decks) {
		for(String deck : decks) {
			for(String line : readFileAsArray(deck + ".txt")) {
				allDecks.add(line);
			}
		}
	}

	private String[] readFileAsArray(String fileName) {
		Path filePath = Paths.get(System.getProperty("user.dir"),"resources","decks",fileName);
		try {
			return (new String(Files.readAllBytes(filePath))).split("\n");
		}
		catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}
}

