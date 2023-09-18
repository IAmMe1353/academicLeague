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

	Scene scene, questionScene;
	Stage window;
	// in questions not lines
	boolean[] ran;
	ArrayList<String> allDecks = new ArrayList<>();
	int score1, score2, line;
	String[] decks;
	// in lines not questions
	int[] deckSizes;
	Speak speak;

	public PlayGame(Stage window, String[] decks, String team1, String team2) {
		speak = new Speak();
		this.window = window;
		this.decks = decks;
		createMegaDeck(decks);
		int numQuestions = (int) (allDecks.size() / 3.0 + .5);

		Thread speak = new Thread(() -> {
			new Speak(allDecks.get(line * 3));
		});

		// set up Labels
		if (team1.equals(""))
			team1 = "Team 1";
		if (team2.equals(""))
			team2 = "Team 2";
		Label team1Label = new Label(team1 + ": " + score1);
		team1Label.setFont(Font.font(Main.titleSize));
		Label team2Label = new Label(team2 + ": " + score2);
		team2Label.setFont(Font.font(Main.titleSize));
		// set up Button
		Button buzz = new Button("Buzz!");
		buzz.setMinSize(300, 150);
		buzz.setOnAction(e -> doBonusQuestion());
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
		speak.start();
		// set scene
		window.setScene(scene);

		// create answer Scene
		TextArea answerT = new TextArea();
		answerT.setPromptText("Press Enter When Finished");
		answerT.setPrefRowCount(2);
		// if enter is pressed check answer
		answerT.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER)
				check(answerT.getText().replaceAll("\\r\\n|\\r|\\n", ""));
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
					int unroundedNum =(int)(Math.random()*deckSizes[deckNum]);
					lines[i] = (((int)(unroundedNum+1.5))/3)*3;
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
			speak.speak(dialog);
		}
		else {
			new Speak("There are no remaining bonus Questions");
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
		ran[line] = true;
		// decrease deck length array
		int sum = 0;
		for (int i = 0; i < deckSizes.length; i++) {
			sum += deckSizes[i];
			if (line < sum) {
				deckSizes[i] -= 3;
				break;
			}
		}
		String correctAnswers = allDecks.get(line * 3 + 1);
		// if correct
		if (checkAnswer(answer, correctAnswers)) {
			score1++;
			new Speak("Correct");
			doBonusQuestion();
		}
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
			deckSizes[i] = deckArray.length;
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
			adress += deckSizes[i];
		}
		adress += line;
		return adress;
	}
}
//	TODO create showText checkBox
//	TODO make sure index is not out of bounds in bonus questions (ran[] and otherwise) (change range of random?)
//	TODO make sure question isn't done twice in bonus (line[i] != line[i-1])
//	TODO make buzz sound
//	TODO make voice interruptible
//	TODO figure way not to do same bonus question twice 
