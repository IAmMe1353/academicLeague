package academicLeague;


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

public class Play {
	
	Scene scene;
	Stage window;
	boolean[] ran;
	
	
	public Play(String decks, Stage window, String team1, String team2) {
		this.window = window;
	//	TODO delete
		team1 = "team1";
		team2 = "team2";
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
}
