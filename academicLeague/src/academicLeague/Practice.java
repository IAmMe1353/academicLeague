package academicLeague;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
	VBox mainBox, correctBox, wrongBox, completeBox;
	HBox saveBox;
	Label question, wrong, correctLabel, correctAnswer, deckComplete, numCorrect;
	TextArea answerT;
	String[] questions;
	String questionsWrong;
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
		mainBox = new VBox(25);
		mainBox.setAlignment(Pos.TOP_CENTER);
		mainBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		mainBox.setPadding(new Insets(10,10,10,10));
		mainBox.getChildren().addAll(question,answerT);
		scene = new Scene(mainBox,Main.stageHeight*2,Main.stageHeight);
		window.setScene(scene);
		createCorrect();
		createWrong();
	}
	private Scene createComplete(int correct, int total) {
		deckComplete = new Label("Deck Complete!");
		deckComplete.setFont(Font.font(Main.titleSize));
		numCorrect = new Label(correct + "/" + total + "  Answers Correct");
		numCorrect.setFont(Font.font(Main.titleSize));
		save = new Button("Save Incorrect");
		exit = new Button("Exit");
		exit.setOnAction(e -> window.setScene(Main.scene));
		saveBox = new HBox(25);
		saveBox.setAlignment(Pos.CENTER);
		saveBox.getChildren().addAll(save,exit);
		completeBox = new VBox(25);
		completeBox.setAlignment(Pos.TOP_CENTER);
		completeBox.getChildren().addAll(deckComplete,numCorrect,saveBox);
		completeBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		completeBox.setPadding(new Insets(10,10,10,10));
		return new Scene(completeBox,Main.stageHeight*2,Main.stageHeight);		
	}
	private void createCorrect(){
		correctLabel = new Label("Correct!");
		correctLabel.setFont(Font.font(Main.titleSize));
		Button button = new Button("Continue");
		button.setOnAction(e -> window.setScene(scene));
		correctBox = new VBox(25);
		correctBox.setAlignment(Pos.TOP_CENTER);
		correctBox.getChildren().addAll(correctLabel,button);
		correctBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		correctBox.setPadding(new Insets(10,10,10,10));
		correctScene = new Scene(correctBox,Main.stageHeight*2,Main.stageHeight);
	}
	
	private void createWrong() {
		wrong = new Label("Incorrect!");
		wrong.setFont(Font.font(Main.titleSize));
		Button button = new Button("Continue");
		button.setOnAction(e -> window.setScene(scene));
		correctAnswer = new Label("Lorem Ipsum");
		correctAnswer.setFont(Font.font(Main.titleSize));
		wrongBox = new VBox(25);
		wrongBox.setAlignment(Pos.TOP_CENTER);
		wrongBox.getChildren().addAll(wrong,correctAnswer,button);
		wrongBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY,Insets.EMPTY )));
		wrongBox.setPadding(new Insets(10,10,10,10));
		wrongScene = new Scene(wrongBox,Main.stageHeight*2,Main.stageHeight);
	}
	
	private String[] readFileAsArray(String fileName) {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/decks/" + fileName)));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return sb.toString().split("\n");
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
	    	window.setScene(createComplete(this.correct,questions.length/3));
	    }
	    else {
	    question.setText(questions[line]);
	    answerT.clear();
	    	}
	    }
	private void sleep(int sec) {
		try {
			Thread.sleep(sec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
