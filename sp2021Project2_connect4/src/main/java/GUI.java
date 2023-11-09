import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class GUI {
	public Scene welcomeScene() {
		GridPane welcome = new GridPane();
		Button startButton = new Button();
		startButton.setPrefSize(100,100);
		startButton.setText("Start Game");
		TextField welcomeText = new TextField();
		welcomeText.setText("Welcome to Connect 4");
		
		
		GridPane.setColumnIndex(startButton, 1);
		GridPane.setRowIndex(startButton, 1);
		return welcome.getScene();
	}

	/*public Scene gamePlayScene() {
		
	}

	public Scene winMessageScene() {
		
	}*/
}