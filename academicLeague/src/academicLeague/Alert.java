package academicLeague;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert {
	public Button exitButton, saveButton;
	public ComboBox<String> tempDeck;
	public CheckBox overwrite;

	public static void display(String title, String message) {
		Stage alertWindow = new Stage();

		alertWindow.initModality(Modality.APPLICATION_MODAL);
		alertWindow.setTitle(title);
		alertWindow.setMinWidth(250);

		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button("ok");
		closeButton.setOnAction(e -> alertWindow.close());

		VBox layout = new VBox(5);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10, 40, 10, 40));
		Scene scene = new Scene(layout);
		alertWindow.getIcons().add(new Image(System.getProperty("user.dir") + "/resources/images/alertIcon.png"));
		alertWindow.setScene(scene);
		alertWindow.showAndWait();
	}

	public void exit() {
		Stage alertWindow = new Stage();

		alertWindow.initModality(Modality.APPLICATION_MODAL);
		alertWindow.setTitle("Save and Exit?");
		alertWindow.setMinWidth(250);

		Label label = new Label();
		label.setText("Would you like to save before exiting?");

		// set up save option
		overwrite = new CheckBox("Overwrite");
		HBox hbox = new HBox(5);
		tempDeck = new ComboBox<>();
		tempDeck.getItems().addAll("Temp1", "Temp2", "Temp3");
		tempDeck.setPromptText("Choose a Deck");
		saveButton = new Button("Save");
		hbox.getChildren().addAll(tempDeck, overwrite, saveButton);

		VBox layout = new VBox(5);
		exitButton = new Button("Exit");
		exitButton.setOnAction(e -> {
			alertWindow.close();
			Main.window.close();

		});
		saveButton.setOnAction(e -> {
			if (Practice.shuffle) {
				for (int i = 0; i < Practice.ran.length;i++) {
					if (!Practice.ran[i]) {
						Practice.questionsWrong += Practice.questions[i*3] + "\n" + Practice.questions[i*3+1] + "\n" + Practice.questions[i*3+2] + "\n"; 
					}
				}
				Practice.saveToFile(overwrite.isSelected(), Practice.questionsWrong, tempDeck.getValue());
			} else {
				for (int i = Practice.line; i < Practice.questions.length; i++)
					Practice.questionsWrong += Practice.questions[i] + "\n";
				Practice.saveToFile(overwrite.isSelected(), Practice.questionsWrong, tempDeck.getValue());
			}
		});
		layout.getChildren().addAll(label, hbox, exitButton);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10, 40, 10, 40));
		Scene scene = new Scene(layout);
		alertWindow.getIcons().add(new Image(System.getProperty("user.dir") + "/resources/images/alertIcon.png"));
		alertWindow.setScene(scene);
		alertWindow.showAndWait();
	}
}
