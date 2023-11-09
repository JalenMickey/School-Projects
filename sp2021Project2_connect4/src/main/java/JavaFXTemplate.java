import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Button;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text; 
import javafx.stage.Stage;
import java.util.Stack;
import javafx.util.Duration;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.util.concurrent.TimeUnit;
import javafx.animation.PauseTransition;
import javafx.animation.Transition.*;


public class JavaFXTemplate extends Application {
	public static char turn = 'r';
	public Stack<Integer> colR = new Stack<Integer>();
	public Stack<Integer> rowR = new Stack<Integer>();
	public static int numTurns = 0;
	public static boolean gameOver = false;
	public static String colorOne = "-fx-background-color: #FF0000";
	public static String colorTwo = "-fx-background-color: #2b02f7";
	public static String colorUnused = "-fx-background-color: #000000";
	
	// GameButton class that every game button will be using
	public class GameButton extends Button {
		private Button bt;
		private char mode;
		private int r;
		private int c;
		
		
		public GameButton() {
			bt = new Button();
			bt.setStyle("-fx-background-color: #73ff00");
			mode = 'x';
		}
		
		public boolean canMove() {
			if (mode == 'x') {
				return true;
			}
			return false;
		}
		public char getMode() {
			return mode;
		}
		public void setMode(char x) {
			mode = x;
		}
		public Button getButton() {
			return bt;
		}
		public void setR(int x) {
			r = x;
		}
		public void setC(int x) {
			c = x;
		}
		public int getR() {
			return r;
		}
		public int getC() {
			return c;
		}
		
 	}
//	function that returns node from gridpane
	private GameButton getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return (GameButton) node;
	        }
	    }
	    return null;
	}
	// horizontal win check helper
	private ArrayList<Pair<Integer, Integer>> checkWinHoriz(GridPane gridPane, int col, int row) {
		int total = 1;
		int it = 1;
		GameButton temp = getNodeFromGridPane(gridPane, col, row);
		char team = temp.getMode();
		ArrayList<Pair<Integer, Integer>> data = new ArrayList<Pair<Integer, Integer>>();
		data.add(new Pair<Integer, Integer>(row, col));
		temp = getNodeFromGridPane(gridPane, col + it, row);
		if (temp != null){
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer>(row, col + it));
				total += 1;
				it += 1;
				temp = getNodeFromGridPane(gridPane, col + it, row);
				if (total == 4) { return data;}
				if (temp == null) {break;}
			}
		}
			it = 1;
			temp = getNodeFromGridPane(gridPane, col - it, row);
			if (temp == null) {
				data.clear();
				return data;
			}
			
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer>(row, col - it));
				total += 1;
				it += 1;
				if (total == 4) { return data;}
				if (col - it < 0) { 
					data.clear();
					return data;}
				temp = getNodeFromGridPane(gridPane, col - it, row);
			}
			data.clear();
			return data;
	}
	// vertical win check helper
	private ArrayList<Pair<Integer, Integer>> checkWinVert(GridPane gridPane, int col, int row) {
		int total = 1;
		ArrayList<Pair<Integer, Integer>> data = new ArrayList<Pair<Integer, Integer>>();
		GameButton temp = getNodeFromGridPane(gridPane, col, row);
		char team = temp.getMode();
		if (row == 0 || row == 1 || row == 2) {
			int it = 1;
			
			data.add(new Pair<Integer, Integer>(row, col));
			temp = getNodeFromGridPane(gridPane, col, row + it);
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer>(row + it, col));
				it += 1;
				total += 1;
				if (total == 4) {return data;}
				temp = getNodeFromGridPane(gridPane, col, row + it);
				
			}
			data.clear();
			return data;
		}
		else {
			return data;
			}
		}
	// diagonal win check helper
	private ArrayList<Pair<Integer, Integer>> checkWinDiag(GridPane gridPane, int col, int row) {
		int total = 1;
		int it = 1;
		ArrayList<Pair<Integer, Integer>> data = new ArrayList<Pair<Integer, Integer>>();
		GameButton temp = getNodeFromGridPane(gridPane, col, row);
		data.add(new Pair<Integer, Integer>(row, col));
		char team = temp.getMode();
		temp = getNodeFromGridPane(gridPane, col + it, row + it);
		if (temp != null) {
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer>(row + it, col + it));
				it += 1;
				total += 1;
				if (total == 4) { return data;}
				temp = getNodeFromGridPane(gridPane, col + it, row + it);
				if (temp == null) {break;}
				}
		}
		it = 1;
		temp = getNodeFromGridPane(gridPane, col - it, row - it);
		if (temp != null) {
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer> (row - it, col - it));
				it += 1;
				total += 1;
				if (total == 4) {return data;}
				temp = getNodeFromGridPane(gridPane, col - it, row - it);
				if (temp == null) {break;}
			}
		}
		data.clear(); // must clear the ArrayList at this point as we check for a win the the opposite direction
		it = 1;
		total = 1;
		data.add(new Pair<Integer, Integer>(row, col));
		temp = getNodeFromGridPane(gridPane, col - it, row + it);
		if (temp != null) {
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer>(row + it, col - it));
				it += 1;
				total += 1;
				if (total == 4) { return data;}
				temp = getNodeFromGridPane(gridPane, col - it, row + it);
				if (temp == null) { break;}
			}
		}
		it = 1;
		temp = getNodeFromGridPane(gridPane, col + it, row - it);
		if (temp != null) {
			while (temp.getMode() == team) {
				data.add(new Pair<Integer, Integer> (row - it, col + it));
				it += 1;
				total += 1;
				if (total == 4) { return data;}
				temp = getNodeFromGridPane(gridPane, col + it, row - it);
				if (temp == null) { break;}
			}
		}
		data.clear();
		return data;
	}
	// calls the win check helpers and returns the ArrayList IF one of them passes and returns an array of rows/cols
	private ArrayList<Pair<Integer, Integer>> checkWin(GridPane gridPane, int col, int row) {
		ArrayList<Pair<Integer, Integer>> data = new ArrayList<Pair<Integer, Integer>>();

		if (!checkWinHoriz(gridPane, col, row).isEmpty()) {
			data = checkWinHoriz(gridPane, col, row);
			for (int i = 0; i < 4; i++) {
				System.out.println(data.get(i).getKey() + " " + data.get(i).getValue());
			}
			return data;
		}
		else if (!checkWinVert(gridPane, col, row).isEmpty()) {
			data = checkWinVert(gridPane, col, row);
			for (int i = 0; i < 4; i++) {
				System.out.println(data.get(i));
			}
			return data;
		}
		else if (!checkWinDiag(gridPane, col, row).isEmpty()){
			data = checkWinDiag(gridPane, col, row);
			for (int i = 0; i < 4; i++) {
				System.out.println(data.get(i));
			}
			return data;
		}
		data.clear();
		return data;
	}
	// the welcoming stage of the game
	public Scene welcome(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(800, 700);
        
        Text welcome = new Text();
        welcome.setFont(Font.font ("Comic Sans MS", 40));
        welcome.setText("\n\n\nWELCOME TO CONNECT 4");
        welcome.setFill(Color.RED);
        BorderPane.setAlignment(welcome, Pos.CENTER);
        pane.setStyle("-fx-background-color: #add8e6;");
        
        pane.setTop(welcome);
        
        Button playButton = new Button("BEGIN GAME");
        playButton.setFont(Font.font ("Comic Sans MS", 40));
        playButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #0000FF; -fx-border-color: #FF0000; -fx-border-width: 3px;");
        
        pane.setCenter(playButton);
        
        // play button handler
        EventHandler<ActionEvent> playHandler = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                primaryStage.setScene(gameBoard(primaryStage));
            }
        };
        
        playButton.setOnAction(playHandler);
        
        Scene scene = new Scene(pane, 800,700);
        return scene;
    }
	// the winning/stalemate stage
	public Scene winScreen(Stage primaryStage, char winner) {
		BorderPane pane = new BorderPane();
		pane.setPrefSize(800, 700);
		pane.setStyle("-fx-background-color: #add8e6;");
		
		Text winMessage = new Text();
		winMessage.setFont(Font.font ("Comic Sans MS", 40));
		winMessage.setFill(Color.RED);
		
		if(winner == 'b') { winMessage.setText("\n\n\nPLAYER ONE WINS"); }
		else if (winner == 'r' ) { winMessage.setText("\n\n\nPLAYER TWO WINS"); }
		else if (winner == 'x' ) { winMessage.setText("\n\n\nSTALEMATE!"); }
		
		Button exitButton = new Button("EXIT");
		Button newGameButton = new Button("NEW GAME");

		exitButton.setFont(Font.font ("Comic Sans MS", 40));
		exitButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #0000FF; -fx-border-color: #FF0000; -fx-border-width: 3px;");

		newGameButton.setFont(Font.font ("Comic Sans MS", 40));
		newGameButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #0000FF; -fx-border-color: #FF0000; -fx-border-width: 3px;");
		
		pane.setCenter(newGameButton);
		
		BorderPane.setAlignment(winMessage, Pos.CENTER);
		pane.setTop(winMessage);
		
		BorderPane.setAlignment(exitButton, Pos.BOTTOM_RIGHT);
		pane.setBottom(exitButton);
		
		// exit button handler
		EventHandler<ActionEvent> exitHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Platform.exit();
			}
		};
		// new game handler
		EventHandler<ActionEvent> newGameHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				turn = 'r';
				numTurns = 0;
				primaryStage.setScene(gameBoard(primaryStage));
			}
		};
		
		exitButton.setOnAction(exitHandler);
		newGameButton.setOnAction(newGameHandler);
		Scene scene = new Scene(pane, 800,700);
		return scene;
	}
	
	public Scene gameBoard(Stage primaryStage) {
		GridPane gridpane = new GridPane();
		gridpane.setCenterShape(true);
		
		// setting up 8 columns and 7 rows on the gridpane
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getColumnConstraints().add(new ColumnConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		gridpane.getRowConstraints().add(new RowConstraints(100));
		Text game = new Text();
		Text playerTurn = new Text();
		game.setText("Nothing has\n happened yet\n");
		playerTurn.setText("P1's turn");
		gridpane.add(game, 7, 0, 1, 6);
		gridpane.add(playerTurn, 7, 6);
		// this nested for loop creates the grid like structure of buttons/slots
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				GameButton temp = new GameButton();
				int col;
				int row;
				temp.setMinHeight(75);
				temp.setMinWidth(75);
				temp.setStyle(colorUnused);
				temp.setR(i);
				temp.setC(j);
				col = temp.getC();
				row = temp.getR();
				gridpane.add(temp, j, i);
//				gridpane.setCenterShape(true);
				temp.setOnAction(event -> { // this handles all of
					if (temp.getMode() == 'x' && (row == 5 || (getNodeFromGridPane(gridpane, col, row + 1).getMode() != 'x'))) {
						if (turn == 'r') {
							numTurns++;
							temp.setStyle(colorOne);
							temp.setMode('r');
							turn = 'b';
							playerTurn.setText("P2's turn");
							game.setText("Player 1\nmoved to\nrow: " + (row + 1) + " col: " + (col + 1));
							rowR.push(row);
							colR.push(col);
							temp.setDisable(true);
							
						}
						else if (turn == 'b') {
							numTurns++;
							temp.setStyle(colorTwo);
							temp.setMode('b');
							turn = 'r';
							playerTurn.setText("P1's turn");
							game.setText("Player 2\nmoved to\nrow: " + (row + 1) + " col: " + (col + 1));

							rowR.push(row);
							colR.push(col);
							temp.setDisable(true);
						}
						
						if (numTurns == 42) {
							primaryStage.setScene(winScreen(primaryStage, 'x'));
						}

						if (!checkWin(gridpane, col, row).isEmpty()) {
							GameButton temp1;
							ArrayList<Pair<Integer, Integer>>list = checkWin(gridpane, col, row);
							gameOver = true;
							int r;
							int c;
							for (int k = 0; k < 4; k++) {
								r = list.get(k).getKey();
								c = list.get(k).getValue();
								temp1 = getNodeFromGridPane(gridpane, c, r);
								temp1.setStyle("-fx-background-color: #F8FF0B");
							}
							
							for (int m = 0; m < 6; m++) {
								for (int n = 0; n < 7; n++) {
									temp1 = getNodeFromGridPane(gridpane, n, m);
									temp1.setDisable(true);
								}
							}
							PauseTransition pause = new PauseTransition(Duration.seconds(3));
							pause.setOnFinished(e -> primaryStage.setScene(winScreen(primaryStage, turn)));
							pause.play();
						}

					}
					else if (getNodeFromGridPane(gridpane, col, row + 1).getMode() == 'x') {
						game.setText("Invalid play\n try again.");
					}				
				}
				);
			}
		}
		
		Menu gamePlayMenu = new Menu("Game Play");
		MenuItem reverseMove = new MenuItem("reverse move");
		gamePlayMenu.getItems().add(reverseMove);
		Menu optionsMenu = new Menu("Options");
		MenuItem howToPlay = new MenuItem("how to play");
		MenuItem newGame = new MenuItem("new game");
		MenuItem exit = new MenuItem("exit");
		optionsMenu.getItems().add(howToPlay);
		optionsMenu.getItems().add(newGame);
		optionsMenu.getItems().add(exit);
		Menu themesMenu = new Menu("Themes");
		MenuItem originalTheme = new MenuItem("original theme");
		MenuItem themeOne = new MenuItem("theme one");
		MenuItem themeTwo = new MenuItem("theme two");
		themesMenu.getItems().add(originalTheme);
		themesMenu.getItems().add(themeOne);
		themesMenu.getItems().add(themeTwo);
		
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(gamePlayMenu);
		menuBar.getMenus().add(themesMenu);
		menuBar.getMenus().add(optionsMenu);
		
		gridpane.add(menuBar, 0, 6, 3, 1);
		
		EventHandler<ActionEvent> rulesHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				Text instructions = new Text();
				instructions.setText("Each player should take alternating turns placing a token on a tile.\n"
						+ "Tokens may only be placed on the bottommost unoccupied tile in any\ngiven row."
						+ "To win the game, a player must place four of their tokens in a\nvertical, horizontal, "
						+ "or diagonal grouping before the other player.");
				gridpane.add(instructions, 3, 6, 3, 1);
			}
		};
		
		EventHandler<ActionEvent> undoHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (!colR.isEmpty() && gameOver == false) { 
					numTurns--;
					int row = rowR.pop();
					int col = colR.pop();
					GameButton temp = getNodeFromGridPane(gridpane, col, row);
					game.setText("Undo");
					temp.setStyle(colorUnused);
					temp.setMode('x');
					temp.setDisable(false);
					if (turn == 'r') {
						playerTurn.setText("P2's turn");
						turn = 'b';
					} else {
						playerTurn.setText("P1's turn");
						turn = 'r';
					}
				}
			}
		};
		
		EventHandler<ActionEvent> exitHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				System.exit(0);
			}
		};
		
		EventHandler<ActionEvent> newGameHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				turn = 'r';
				numTurns = 0;
				primaryStage.setScene(gameBoard(primaryStage));
			}
		};
		
		EventHandler<ActionEvent> themeOneHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				colorOne = "-fx-background-color: #FF0065";
				colorTwo = "-fx-background-color: #00F7FF";
				colorUnused = "-fx-background-color: #6D6D6D";
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 7; j++) {
						GameButton obj = getNodeFromGridPane(gridpane, j, i);
						if(obj.getMode() == 'x') { obj.setStyle(colorUnused); }
						else if(obj.getMode() == 'r') { obj.setStyle(colorOne); }
						else if(obj.getMode() == 'b') { obj.setStyle(colorTwo); }
					}
				}
			}
		};
		
		EventHandler<ActionEvent> themeTwoHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				colorOne = "-fx-background-color: #680000";
				colorTwo = "-fx-background-color: #000368";
				colorUnused = "-fx-background-color: #DFDFDF";
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 7; j++) {
						GameButton obj = getNodeFromGridPane(gridpane, j, i);
						if(obj.getMode() == 'x') { obj.setStyle(colorUnused); }
						else if(obj.getMode() == 'r') { obj.setStyle(colorOne); }
						else if(obj.getMode() == 'b') { obj.setStyle(colorTwo); }
					}
				}
			}
		};
		
		EventHandler<ActionEvent> originalThemeHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				colorOne = "-fx-background-color: #FF0000";
				colorTwo = "-fx-background-color: #2B02F7";
				colorUnused = "-fx-background-color: #000000";
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 7; j++) {
						GameButton obj = getNodeFromGridPane(gridpane, j, i);
						if(obj.getMode() == 'x') { obj.setStyle(colorUnused); }
						else if(obj.getMode() == 'r') { obj.setStyle(colorOne); }
						else if(obj.getMode() == 'b') { obj.setStyle(colorTwo); }
					}
				}
			}
		};
		
		reverseMove.setOnAction(undoHandler);	
		howToPlay.setOnAction(rulesHandler);
		exit.setOnAction(exitHandler);
		newGame.setOnAction(newGameHandler);
		themeOne.setOnAction(themeOneHandler);
		themeTwo.setOnAction(themeTwoHandler);
		originalTheme.setOnAction(originalThemeHandler);
		
		Scene scene = new Scene(gridpane, 800,700);
		return scene;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect 4");
		primaryStage.setScene(welcome(primaryStage));
		primaryStage.show();
	}

}
