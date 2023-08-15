package academicLeague;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert {
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
		layout.getChildren().addAll(label,closeButton);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(10,40,10,40));
		Scene scene =new Scene(layout);
		alertWindow.getIcons().add(new Image(System.getProperty("user.dir")+"/resources/images/alertIcon.png"));
		alertWindow.setScene(scene);
		alertWindow.showAndWait();
	}
}
