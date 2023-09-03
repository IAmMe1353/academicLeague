package academicLeague;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.stage.Stage;

public class PlayGame {
	ArrayList<String> allDecks;
	boolean[] ran;
	
	public PlayGame(Stage window, String[] decks, String team1, String team2) {

		ran = new boolean[allDecks.size()];
	}
	
	private Stage createBuzz() {
		return new Stage();
	}
	
	private Stage createAnswer() {
		return new Stage();
	}
	
	
}
