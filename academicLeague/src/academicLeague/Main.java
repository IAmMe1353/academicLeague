package academicLeague;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {
	BorderPane mainMenu;
	VBox leftMenu, practiceBox;
	GridPane playBox;
	HBox practiceSettings;
	static Scene scene;
	Button practiceB, playGameB, importDeckB, exitB, startPractice;
	Label practiceT;
	ColorAdjust colorAdjust;
	CheckBox shuffleCheck;
	ComboBox<String> deckSelect;
	static final Color green = new Color(42.0/255, 130.0/255, 65.0/255, 1.0);
	static final Color gray =  new Color(227.0/255, 227.0/255, 227.0/255, 1.0);
	static int stageHeight = 500;
	static int buttonHeight = (stageHeight-35)/4;
	static int buttonWidth = buttonHeight*2;
	static int fontSize = buttonHeight/4;
	static double titleSize = fontSize * (1.5);
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		//	standard insets 10,10,10,10; standard button separation 5
		colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-.25);
		//	set up left Menu
			practiceB = new Button("Practice Deck");
			practiceB.setPrefHeight(buttonHeight);
			practiceB.setPrefWidth(buttonWidth);
			practiceB.setFont(Font.font(fontSize));
			practiceB.setOnAction(e -> {
				playGameB.setEffect(null);
				importDeckB.setEffect(null);
				practiceB.setEffect(colorAdjust);
				mainMenu.setCenter(practiceBox);
			});
			practiceB.setEffect(colorAdjust);
			
			playGameB = new Button("Play Game");
			playGameB.setPrefHeight(buttonHeight);
			playGameB.setPrefWidth(buttonWidth);
			playGameB.setFont(Font.font(fontSize));
			playGameB.setOnAction(e -> {
				practiceB.setEffect(null);
				importDeckB.setEffect(null);
				playGameB.setEffect(colorAdjust);
				mainMenu.setCenter(playBox);
			});
			
			importDeckB = new Button("Import Deck");
			importDeckB.setPrefHeight(buttonHeight);
			importDeckB.setPrefWidth(buttonWidth);
			importDeckB.setFont(Font.font(fontSize));
			
			exitB = new Button("Exit");
			exitB.setPrefHeight(buttonHeight);
			exitB.setPrefWidth(buttonWidth);
			exitB.setFont(Font.font(fontSize));
			exitB.setOnAction(e-> primaryStage.close());
			leftMenu = new VBox(5);
			leftMenu.setPadding(new Insets(10, 10, 10, 10));
			leftMenu.getChildren().addAll(practiceB,playGameB,importDeckB,exitB);
			leftMenu.setBackground(new Background(new BackgroundFill(green, CornerRadii.EMPTY,Insets.EMPTY )));
			
		//	set up practiceBox
			//	set up label
			practiceT = new Label("Use Flashcards to Practice a Deck");
			practiceT.setFont(Font.font(titleSize));
			practiceT.setAlignment(Pos.CENTER);
			//	set up button
			startPractice = new Button("Start");
			startPractice.setPrefHeight(buttonHeight/1.5);
			startPractice.setPrefWidth(buttonWidth/1.5);
			startPractice.setOnAction(e -> startPractice(primaryStage));
			//	set up Deck drop down
			deckSelect = new ComboBox<>();
			
			ObservableList<String> strings = FXCollections.observableArrayList();
			
			Path userFilesDirectory = Paths.get(System.getProperty("user.dir"), "resources","decks");
			try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(userFilesDirectory)){
				for (Path path : directoryStream) {
					if (Files.isRegularFile(path)) {
						String file = "" + path.getFileName();
						deckSelect.getItems().add(("" + file.substring(0,file.length()-4)));
						}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			//	set up shuffle check box
			shuffleCheck = new CheckBox("Shuffle Deck?");
			shuffleCheck.setSelected(false);
			//	set up HBox for shuffle check box and deck select
			practiceSettings = new HBox(10);
			practiceSettings.setAlignment(Pos.CENTER);
			practiceSettings.getChildren().addAll(deckSelect,shuffleCheck);
			//	set up VBox for practice page
			practiceBox = new VBox(40);
			practiceBox.setPadding(new Insets(10, 10, -00, 10));
			practiceBox.setAlignment(Pos.TOP_CENTER);
			practiceBox.getChildren().addAll(practiceT,practiceSettings,startPractice);
		//	set up playBox
			//	set up labels
			//	TODO make sure everything is standard size
			Label team1Label = new Label("Team 1 Name:");
			team1Label.setMinWidth(150);
			team1Label.setFont(Font.font(Main.titleSize));
			Label team2Label = new Label("Team 2 Name:");
			team2Label.setFont(Font.font(Main.titleSize));
			//	set up textAreas
			TextArea team1 = new TextArea();
			team1.setPrefRowCount(1);
			team1.setPromptText("Enter Name");
			TextArea team2 = new TextArea();
			team2.setPrefRowCount(1);
			team2.setPromptText("Enter Name");
			//	set up drop down
			ComboBox<String> deckSelectPlay = new ComboBox<>();
			deckSelectPlay.setPromptText("Decks");
			
			//	set up grid pane
			playBox = new GridPane();
			GridPane.setConstraints(team1Label, 0, 0);
			GridPane.setConstraints(team1, 1, 0);
			GridPane.setConstraints(team2Label, 0, 1);
			GridPane.setConstraints(team2,1,1);
			GridPane.setConstraints(deckSelectPlay, 0, 2);
			playBox.setAlignment(Pos.TOP_CENTER);
			playBox.setPadding(new Insets(10,10,10,10));
			playBox.getChildren().addAll(team1Label,team1,team2Label,team2,deckSelectPlay);
			// set up main menu
			mainMenu = new BorderPane();
			mainMenu.setLeft(leftMenu);
			mainMenu.setCenter(practiceBox);
			mainMenu.setBackground(new Background(new BackgroundFill(gray, CornerRadii.EMPTY,Insets.EMPTY )));
			scene = new Scene(mainMenu,stageHeight*2,stageHeight);
		//	set up stage			
			primaryStage.getIcons().add(new Image(System.getProperty("user.dir")+"/resources/images/Helix_High_School_logo.jpg"));
			primaryStage.setTitle("Home");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
	}
	
	private void startPractice(Stage stage) {
		
		String deck = deckSelect.getValue(); 
		if (deck != null)
			new Practice(stage,shuffleCheck.isSelected(),deck+".txt");
		else Alert.display("No Deck Selected", "Please Select a Deck Before Starting");
			
	}

}
