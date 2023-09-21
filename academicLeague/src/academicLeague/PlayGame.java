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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlayGame {

	Scene scene, questionScene, bonusScene;
	Stage window;
	// in questions not lines
	boolean[] ran;
	ArrayList<String> allDecks = new ArrayList<>();
	int score1, score2, line, numQuestions, deckNum;
	String[] decks;
	String team1, team2;
	// in questions not questions
	int[] deckSizes, lines;
	Speak speak;
	public TextArea a1,a2,a3;

	public PlayGame(Stage window, String[] decks, String team1In, String team2In) {
		speak = new Speak();
		this.window = window;
		this.decks = decks;
		createMegaDeck(decks);
		numQuestions = (int) (allDecks.size() / 3.0 + .5);

		// set up Labels
		if (team1In.equals(""))
			team1 = "Team 1";
		else
			team1 = team1In;
		if (team2In.equals(""))
			team2 = "Team 2";
		else
			team2 = team2In;
		Label team1Label = new Label(team1 + ": " + score1);
		team1Label.setFont(Font.font(Main.titleSize));
		Label team2Label = new Label(team2 + ": " + score2);
		team2Label.setFont(Font.font(Main.titleSize));
		// set up Button
		Button buzz = new Button("Buzz!");
		buzz.setMinSize(300, 150);
		buzz.setOnAction(e ->{
			window.setScene(questionScene);
			speak.clip.stop();
		});
		buzz.setFont(Font.font(Main.stageHeight / 5));
		// set up VBox and create scene
		VBox mainBox = new VBox(25);
		mainBox.setAlignment(Pos.TOP_CENTER);
		mainBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY, Insets.EMPTY)));
		mainBox.setPadding(new Insets(10, 10, 10, 10));
		mainBox.getChildren().addAll(team1Label, team2Label, buzz);
		scene = new Scene(mainBox, Main.stageHeight * 2, Main.stageHeight);
		// set up questions
		ran = new boolean[numQuestions];
		line = (int) (Math.random() * (numQuestions));
		// set scene
		window.setScene(scene);
		speak.speak(allDecks.get(line * 3));
		// create answer Scene
		TextArea answerT = new TextArea();
		answerT.setPromptText("Press Enter When Finished");
		answerT.setPrefRowCount(2);
		// if enter is pressed check answer
		answerT.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				check(answerT.getText().replaceAll("\\r\\n|\\r|\\n", ""));
				team1Label.setText(team1 + ": " + score1);
				answerT.clear();
			}
		});
		// set up new label
		Label team1LabelQ = new Label(team1 + ": " + score1);
		team1LabelQ.setFont(Font.font(Main.titleSize));
		Label team2LabelQ = new Label(team2 + ": " + score2);
		team2LabelQ.setFont(Font.font(Main.titleSize));
		// set up VBox
		VBox QuestionBox = new VBox(5);
		QuestionBox.setAlignment(Pos.TOP_CENTER);
		QuestionBox.setBackground(new Background(new BackgroundFill(Main.gray, CornerRadii.EMPTY, Insets.EMPTY)));
		QuestionBox.setPadding(new Insets(10, 10, 10, 10));
		QuestionBox.getChildren().addAll(team1LabelQ, team2LabelQ, answerT);
		questionScene = new Scene(QuestionBox, Main.stageHeight * 2, Main.stageHeight);
		//	create question scene
		Button q1 = new Button("Question 1");
		q1.setOnAction(e->{
			speak.clip.stop();
			speak.speak(allDecks.get(getAdress(deckNum,lines[1])));
		});
		Button q2 = new Button("Question 2");
		q2.setOnAction(e->{
			speak.clip.stop();
			speak.speak(allDecks.get(getAdress(deckNum,lines[2])));
		});
		Button q3 = new Button("Question 3");
		q3.setOnAction(e->{
			speak.clip.stop();
			speak.speak(allDecks.get(getAdress(deckNum,lines[3])));
		});
		Button answer = new Button("Answer");
		answer.setOnAction(e-> checkBonus());
		a1 = new TextArea();
		a1.setPrefRowCount(2);
		a1.setMaxWidth(Main.stageHeight);
		a2 = new TextArea();
		a2.setPrefRowCount(2);
		a2.setMaxWidth(Main.stageHeight);
		a3 = new TextArea();
		a3.setPrefRowCount(2);
		a3.setMaxWidth(Main.stageHeight);
		VBox bonusBox = new VBox(5);
		bonusBox.setAlignment(Pos.TOP_CENTER);
		bonusBox.getChildren().addAll(q1,a1,q2,a2,q3,a3, answer);
		bonusScene = new Scene(bonusBox,Main.stageHeight * 2, Main.stageHeight);
	}

	private void doBonusQuestion() {
		// TODO create a scene with three answer slots and a enter button
		// speak type (everything before _ in deck title)
		if (checkIfBonusQuestions()) {	
			// select deck
			int deckNum = (int) (Math.random() * (decks.length - 1));
			//	making sure there are enough remaining questions
			while(deckSizes[deckNum] < 3) {
				deckNum = (int) (Math.random() * (decks.length - 1));
			}
			int[] lines = new int[3];
			//	get lines for questions
			for (int i = 0; i < 3; i++) {
				boolean notValid = true;
				while (notValid) {
					int  questionNum =(int)(Math.random()*deckSizes[deckNum]);
					lines[i] = questionNum*3;
					notValid = false;
					//	check that that question has not been done
					if (ran[getAdress(deckNum,lines[i])/3])
						notValid = true;
					//	check that each question is different
					if (i==1)
						if(lines[1] == lines[0])
							notValid = true;
					if (i==2)
						if(lines[2] == lines[1]||lines[2] == lines[0])
							notValid = true;
				}
			}
			//	create dialog to speak
			String dialog = "The Next Question is a Bonus question (_)(_)";
			for (int i:lines) {
				System.out.println("Line: " + i);
				System.out.println("deckNum: " + deckNum);
				System.out.println("Adress: " + getAdress(deckNum,i));
				System.out.println("Question: " +  allDecks.get(getAdress(deckNum,i)));
				
				dialog += allDecks.get(getAdress(deckNum,i));
				dialog += "(_)(_)";
			}
			//	remove trailing pause
			dialog = dialog.substring(0,dialog.length()-3);
			window.setScene(bonusScene);
			speak.speak(dialog);
			
		}
		else {
			new Speak("There are no remaining bonus Questions");
		}
	}
	private void checkBonus() {
		int correct = 0;

		if(checkAnswer(a1.getText(),allDecks.get(getAdress(deckNum,lines[0]))));
			correct++;
		if(checkAnswer(a2.getText(),allDecks.get(getAdress(deckNum,lines[1]))));
			correct++;
		if(checkAnswer(a3.getText(),allDecks.get(getAdress(deckNum,lines[2]))));
			correct++;
		if (correct == 2)
			correct = 3;
		else if (correct == 3)
			correct =5;
		score1 += correct;
		
		//	change question
		line = (int) (Math.random() * (numQuestions));
		while(ran[line/3]) {
			line = (int) (Math.random() * (numQuestions));
		}
	}
	private boolean checkIfBonusQuestions(){
		for(int i:deckSizes) {
			if (i >=3) {
				return true;
			}
		}
		return false;
	}
	private void check(String answer) {
		
		System.out.println("checked");
		ran[line/3] = true;
		// decrease deck length array
		int sum = 0;
		for (int i = 0; i < deckSizes.length; i++) {
			sum += deckSizes[i]*3;
			if (line < sum) {
				deckSizes[i] -= 1;
				break;
			}
		}
		String correctAnswers = allDecks.get(line * 3 + 1);
		// if correct
		if (checkAnswer(answer, correctAnswers)) {
			score1 += 3;
			new Speak("Correct");
			doBonusQuestion();
		}
		else {
			new Speak("Incorrect");
			score1 --;
			
			window.setScene(scene);
		}
		
		//	change question
		line = (int) (Math.random() * (numQuestions));
		while(ran[line/3]) {
			line = (int) (Math.random() * (numQuestions));
		}
		System.out.println(allDecks.get(line*3));
		speak.speak(allDecks.get(line*3));
	}

	private boolean checkAnswer(String answer, String answerLine) {
		String[] answers = answerLine.split(";");

		for (String i : answers) {
			if (i.replaceAll("\\r\\n|\\r|\\n", "").toUpperCase().equals(answer.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	private void createMegaDeck(String[] decks) {
		deckSizes = new int[decks.length];
		for (int i = 0; i < decks.length; i++) {
			// TODO get file sizes
			String[] deckArray = readFileAsArray(decks[i] + ".txt");
			deckSizes[i] = (int)(deckArray.length/3.0 + .5);
			for (String line : deckArray) {
				allDecks.add(line);
			}
		}
	}

	private String[] readFileAsArray(String fileName) {
		Path filePath = Paths.get(System.getProperty("user.dir"), "resources", "decks", fileName);
		try {
			return (new String(Files.readAllBytes(filePath))).split("\n");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private int getAdress(int deckNum, int line) {
		int adress = 0;
		for (int i = 0; i < deckNum; i++) {
			adress += deckSizes[i]*3;
		}
		adress += line;
		return adress;
	}
}
//	TODO create showText checkBox
//	TODO subtract 3 from deckSizes when bonus question
//	TODO make sure questions are random
//	TODO make sure index is not out of bounds in bonus questions (ran[] and otherwise) (change range of random?)
//	TODO make sure question isn't done twice in bonus (line[i] != line[i-1])
//	TODO make buzz sound
//	TODO make voice interruptible
//	TODO figure way not to do same bonus question twice 
