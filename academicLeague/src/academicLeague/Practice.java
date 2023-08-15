package academicLeague;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Practice {
	Scene scene, correctScene, wrongScene;
	Label question, correctAnswer, numCorrect;
	TextArea answerT;
	String[] questions;
	String questionsWrong = "";
	Stage window;
	Button save,exit;
	int line, correct;
	
	//	TODO take in String[]
	public Practice(Stage window,boolean shuffle, String fileName) {
		//	create question label
		questions = readFileAsArray(fileName); 
		this.window = window;
		line = 0;
		question = new Label(questions[0]);
		question.setFont(Font.font(Main.titleSize));
		question.setWrapText(true);
		answerT = new TextArea();
		answerT.setPromptText("Press Enter When Finished");
		answerT.setPrefRowCount(2);
		answerT.setOnKeyPressed(e ->{
			if(e.getCode() == KeyCode.ENTER) {
				check();
			}
		});
		VBox mainBox = new VBox(25);
		mainBox.setAlignment(Pos.TOP_CENTER);
		mainBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		mainBox.setPadding(new Insets(10,10,10,10));
		mainBox.getChildren().addAll(question,answerT);
		scene = new Scene(mainBox,Main.stageHeight*2,Main.stageHeight);
		window.setScene(scene);
		createCorrect();
		createWrong();
	}
	private Scene createComplete(boolean perfect) {
		VBox completeBox = new VBox(25);
		HBox saveBox = new HBox(25);
		final Label deckComplete = new Label("Deck Complete!");
		deckComplete.setFont(Font.font(Main.titleSize));
		completeBox.getChildren().add(deckComplete);
		if(!perfect) {
			int correct = this.correct;
			int total = (int)(questions.length/3.0 +.5);
			numCorrect = new Label(correct + "/" + total + "  Answers Correct");
			numCorrect.setFont(Font.font(Main.titleSize));
			completeBox.getChildren().add(numCorrect);
			
			Button save = new Button("Save Incorrect");
			save.setOnAction(e -> window.setScene(save(questionsWrong,true)));
			saveBox.getChildren().add(save);
		}
		else {
			numCorrect = new Label("Perfect Score!");
			numCorrect.setFont(Font.font(Main.titleSize));
			completeBox.getChildren().add(numCorrect);
		}
		
		Button exit = new Button("Exit");
		exit.setOnAction(e -> window.setScene(Main.scene));
		saveBox.setAlignment(Pos.CENTER);
		saveBox.getChildren().addAll(exit);
		completeBox.setAlignment(Pos.TOP_CENTER);
		completeBox.getChildren().add(saveBox);
		completeBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		completeBox.setPadding(new Insets(10,10,10,10));
		return new Scene(completeBox,Main.stageHeight*2,Main.stageHeight);		
	}
	private void createCorrect(){
		final Label correctLabel = new Label("Correct!");
		correctLabel.setFont(Font.font(Main.titleSize));
		Button button = new Button("Continue");
		button.setOnAction(e -> window.setScene(scene));
		VBox correctBox = new VBox(25);
		correctBox.setAlignment(Pos.TOP_CENTER);
		correctBox.getChildren().addAll(correctLabel,button);
		correctBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		correctBox.setPadding(new Insets(10,10,10,10));
		correctScene = new Scene(correctBox,Main.stageHeight*2,Main.stageHeight);
	}
	
	private void createWrong() {
		final Label wrong = new Label("Incorrect!");
		wrong.setFont(Font.font(Main.titleSize));
		Button button = new Button("Continue");
		button.setOnAction(e -> window.setScene(scene));
		correctAnswer = new Label();
		correctAnswer.setFont(Font.font(Main.titleSize));
		VBox wrongBox = new VBox(25);
		wrongBox.setAlignment(Pos.TOP_CENTER);
		wrongBox.getChildren().addAll(wrong,correctAnswer,button);
		wrongBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		wrongBox.setPadding(new Insets(10,10,10,10));
		wrongScene = new Scene(wrongBox,Main.stageHeight*2,Main.stageHeight);
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
	private Scene save(String text,boolean finishedDeck) {
		HBox deckSave = new HBox(25);
		VBox saveBox = new VBox(25);
		Label saveL = new Label("Save");
		saveBox.getChildren().add(saveL);
		if (!finishedDeck) {
			Label score = new Label("" + correct + "/" + line/3 + " Answers Correct!");
			saveBox.getChildren().add(score);
		}
		saveL.setFont(Font.font(Main.titleSize));
		ComboBox<String> deckSelect = new ComboBox<>();
		deckSelect.setPromptText("Choose Deck");
		deckSelect.getItems().addAll("Temp1","Temp2","Temp3");
		CheckBox overwrite = new CheckBox("Overwrite Text?");
		Button saveButton = new Button("Save");
		saveButton.setOnAction(e ->{saveToFile(overwrite.isSelected(),text,deckSelect.getValue());
									window.setScene(Main.scene);});
		saveBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		saveBox.setPadding(new Insets(10,10,10,10));
		saveBox.setAlignment(Pos.TOP_CENTER);
		deckSave.getChildren().addAll(deckSelect,overwrite);
		deckSave.setAlignment(Pos.CENTER);
		saveBox.getChildren().addAll(deckSave,saveButton);
		return new Scene(saveBox,Main.stageHeight*2,Main.stageHeight);
	}
	private void saveToFile(boolean overwrite,String text, String deck) {
		System.out.println(text);
		if (deck != null) {
			if (overwrite) {
				text = text.substring(0,text.length()-1);
				try {
					Files.write(Paths.get(System.getProperty("user.dir"),"resources","decks", deck +".txt"), text.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				text = "\n" + text;
				text = text.substring(0,text.length()-1);
				try {
					Files.write(Paths.get(System.getProperty("user.dir"),"resources","decks", deck +".txt"), text.getBytes(), StandardOpenOption.APPEND);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			System.out.println("none selected");
			Alert.display("Choose Deck","Please Choose a Deck to Save To");}
	}
	private void check() {
		boolean correct = false;
	    // get and check answer
	    String answer = answerT.getText();
	    answer = answer.substring(0,answer.length()-1);
	    line++;
	    if (checkAnswer(answer, questions[line])) {
	        correct = true;
	        window.setScene(correctScene);
	    } else {
	    // adds wrong questions to global string
	    	questionsWrong += questions[line - 1] + "\n" + questions[line] + "\n\n";
	    	correctAnswer.setText("The correct answer is " + questions[line].split(";")[0]);
	    	window.setScene(wrongScene);
	    }
	    line += 2;
	    if (correct) 
	    	this.correct++;
	    if(line >= questions.length) {
	    	if (this.correct == (int)(questions.length/3.0+.5))
	    		window.setScene(createComplete(true));
	    	else
	    		window.setScene(createComplete(false));
	    }
	    else {
	    question.setText(questions[line]);
	    answerT.clear();
	    	}
	    }

	private boolean checkAnswer(String answer, String answerLine) {
	    String[] answers = answerLine.split(";");

	    for (String i : answers) {
	      if (i.toUpperCase().equals(answer.toUpperCase())) {
	        return true;
	      }
	    }
	    return false;
	}
}
