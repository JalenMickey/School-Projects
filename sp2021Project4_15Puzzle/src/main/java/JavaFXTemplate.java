import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.text.*;


public class JavaFXTemplate extends Application {
	
	HashMap <String, Scene> sceneMap;
	GridPane grid;
	BorderPane bp, welcomePane;
	Text welcome, win, state;
	Button newGame, exit, AI1, AI2, seeSolution;
	ArrayList<Integer> game0;
	HBox h, t;
	ExecutorService ex1;
	String heuristicChoice;
	ArrayList<Node> solution;
	AtomicInteger atomic;
	ArrayList<ArrayList<Integer>> games;
	
	
	// GameButton class that is used to create every button on the gridpane
	public class GameButton extends Button {
		private Button bt;
		private int row;
		private int col;
		private int val;
		
		public GameButton() {
			bt = new Button();
			bt.setStyle("-fx-background-color: #73ff00");
			row = -1;
			col = -1;
			val = -1;
		}
		public void setRow(int x) {
			row = x;
		}
		public void setCol(int x) {
			col = x;
		}
		public int getRow() {
			return row;
		}
		public int getCol() {
			return col;
		}
		public void setVal(int x) {
			val = x;
		}
		public int getVal() {
			return val;
		}
	}
	
	public class MyCall implements Callable<ArrayList<Node>> {
		String heur;
		int[] state;
		
		MyCall(int[] gameState, String h) {
			heur = h;
			state = gameState;
		}
		
		@Override
		public ArrayList<Node> call() throws Exception {
			Node board = new Node(state);
			board.setDepth(0);
			A_IDS_A_15solver solver2 = new A_IDS_A_15solver();
			return solver2.A_Star(board, heur);
		}
	}
	
	// returns specific button on gridpane
	private GameButton getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    for (javafx.scene.Node node : gridPane.getChildren()) {
	        if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
	            return (GameButton) node;
	        }
	    }
	    return null;
	}
	
	// converts ArrayList<Integer> to int []
	private int[] convertBoardToIntArray(ArrayList<Integer> board) {
	    int[] array = new int[board.size()];
	    for (int i = 0; i < array.length; i++) {
	        array[i] = board.get(i).intValue();
	    }
	    return array;
	}
	
	// generates solution and assigns it to solution
	public void solve(String heuristic) {
		grid.setDisable(true);
		AI1.setDisable(true);
		AI2.setDisable(true);
		ex1 = Executors.newFixedThreadPool(10);
		ex1.submit(() ->  {
			ExecutorService ex2 = Executors.newFixedThreadPool(10);
			updateList();
			Future<ArrayList<Node>> future = ex2.submit(new MyCall(convertBoardToIntArray(game0), heuristic));
			while(true) {
				try {
					solution = future.get();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				if(future.isDone()) {
					try {
						solution = future.get();
						grid.setDisable(false);
						AI1.setDisable(false);
						AI2.setDisable(false);
						seeSolution.setDisable(false);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		});
		ex1.shutdown();
	}
	
	// updates the gameboard list
	private void updateList() {
		game0.clear();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				GameButton temp = getNodeFromGridPane(grid, j, i);
				game0.add(temp.getVal());
			}
		}
	}
	// checks board for win
	private boolean win(Stage primaryStage) {
		game0.clear();
		for (int i = 0, k = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++, k++) {
				GameButton temp = getNodeFromGridPane(grid, j, i);
				game0.add(temp.getVal());
				if (temp.getVal() != k) {
					return false;
				}
			}
		}
		// code past this line is only executed if there is a win
		win = new Text("Congrats!\nYou won the game!");
		newGame = new Button("Replay");
		newGame.setMinSize(75, 75);
		exit = new Button("Exit");
		exit.setMinSize(75, 75);
		h = new HBox(50, newGame, win, exit);
		h.setAlignment(Pos.CENTER);
		bp.setBottom(h);
		newGame.setOnAction(event -> {
			primaryStage.setScene(gameplay(primaryStage));
		});
		exit.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
		return true;
	}
	
	// fills array list with winnable games
	public void fillGames() {
        games = new ArrayList<ArrayList<Integer>>();
        game0 = new ArrayList<Integer>(Arrays.asList(14,8,3,7,6,9,0,2,12,10,15,5,4,11,1,13));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(6,12,3,2,15,5,0,14,4,9,7,13,10,8,11,1));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(3,6,5,0,10,2,1,4,13,12,11,7,9,8,15,14));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(1,2,3,7,4,5,6,0,8,9,10,11,12,13,14,15));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(1,2,3,7,4,5,6,11,8,9,10,0,12,13,14,15));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(1,2,3,7,4,5,6,11,8,9,10,15,12,13,14,0));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(1,2,3,7,4,5,6,11,8,9,10,15,12,13,0,14));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(1,2,3,7,4,5,6,11,8,9,10,15,12,0,13,14));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(0,1,7,6,5,3,2,15,4,9,13,11,8,12,14,10));
        games.add(game0);
        game0 = new ArrayList<Integer>(Arrays.asList(1,0,7,6,5,3,2,15,4,9,13,11,8,12,14,10));
        games.add(game0);
        
        Random rand = new Random();
        int randInt = rand.nextInt(3);
        game0 = games.get(randInt);
    }
	
	// welcome scene
	public Scene welcome(Stage primaryStage) {
		welcomePane = new BorderPane();
		welcome = new Text("WELCOME TO\n   15 PUZZLE");
		welcome.setFont(Font.font ("Comic Sans MS", 80));
		welcome.setFill(Color.RED);
		BorderPane.setAlignment(welcome, Pos.CENTER);
		welcomePane.setStyle("-fx-background-color: #add8e6;");
		welcomePane.setCenter(welcome);
		PauseTransition welcomePause = new PauseTransition(Duration.seconds(3));
		welcomePause.setOnFinished(event -> primaryStage.setScene(gameplay(primaryStage)));
		welcomePause.play();
		return new Scene(welcomePane, 700, 700);
	}
	// animates the solution given from the AI solver
	public void seeSol(Stage primaryStage) {
		//AI1.setDisable(true);
        //AI2.setDisable(true);
        //grid.setDisable(true);
        atomic = new AtomicInteger(0);
        PauseTransition solPause = new PauseTransition(Duration.seconds(1));
        solPause.play();
        solPause.setOnFinished(event -> {
//        	grid.setDisable(true);
//        	AI1.setDisable(true);
//        	AI2.setDisable(true);
        	seeSolution.setDisable(true);
            int iter = atomic.get();
            if (iter < 10) {
            	if(win(primaryStage)) { 
//            		grid.setDisable(false);
//            		AI1.setDisable(false);
//            		AI2.setDisable(false);
            		return; }
                int[] s = solution.get(iter).getKey();
                for (int i = 0, k = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++, k++) {
                        String temp1 = String.valueOf(s[k]);
                        GameButton temp = getNodeFromGridPane(grid, j, i);
                        temp.setText(temp1);
                        temp.setVal(s[k]);
                        if (s[k] == 0) {
                            temp.setText("");
                            temp.setVal(0);
                        }
                    }
                }
                atomic.incrementAndGet();
                solPause.play();
            }
//            seeSolution.setDisable(false);
//            AI1.setDisable(false);
//            AI2.setDisable(false);
//            grid.setDisable(false);
        });
    }
	
	// the main gameplay scene
	public Scene gameplay(Stage primaryStage) {
		fillGames();
		bp = new BorderPane();
		grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.getColumnConstraints().add(new ColumnConstraints(100));
		grid.getColumnConstraints().add(new ColumnConstraints(100));
		grid.getColumnConstraints().add(new ColumnConstraints(100));
		grid.getColumnConstraints().add(new ColumnConstraints(100));
		grid.getRowConstraints().add(new RowConstraints(100));
		grid.getRowConstraints().add(new RowConstraints(100));
		grid.getRowConstraints().add(new RowConstraints(100));
		grid.getRowConstraints().add(new RowConstraints(100));
		AI1= new Button("Generate solution\nwith AI heuristic 1");
        AI2 = new Button("Generate solution\nwith AI heuristic 2");
		seeSolution = new Button("See\nSolution");
		AI1.setMinSize(75, 75);
		AI2.setMinSize(75, 75);
		seeSolution.setMinSize(75, 75);
		seeSolution.setDisable(true);
		t = new HBox(50, AI1, AI2, seeSolution);
		t.setAlignment(Pos.CENTER);
		state = new Text("Click a tile that is directly next to the blank tile to move a piece.\nIf you are stuck, you can click either of the AI solvers\nand then press 'see solution' to watch the GUI help you solve the puzzle.");
		bp.setTop(t);
		bp.setCenter(grid);
		bp.setBottom(state);
				
		AI1.setOnAction(event -> { solve("heuristicOne"); });
		AI2.setOnAction(event -> { solve("heuristicTwo"); });
		seeSolution.setOnAction(event -> { 
			AI1.setDisable(true);
	        AI2.setDisable(true);
	        grid.setDisable(true);
			seeSol(primaryStage);
			AI1.setDisable(false);
	        AI2.setDisable(false);
	        grid.setDisable(false);
	    });
		
		// creates every button laid out on the grid pane and gives the basic functionality
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				GameButton temp = new GameButton();
				int row = i;
				int col = j;
				temp.setMinSize(75, 75);
				temp.setRow(i);
				temp.setCol(j);
				grid.add(temp, j, i);
				GridPane.setHalignment(temp, HPos.CENTER);
				temp.setOnAction(event -> {
					if (row - 1 >= 0 && getNodeFromGridPane(grid, col, row - 1).getVal() == 0) {
						getNodeFromGridPane(grid, col, row - 1).setVal(temp.getVal());
						getNodeFromGridPane(grid, col, row - 1).setText(temp.getText());
						temp.setText("");
						temp.setVal(0);
					}
					if (row + 1 < 4 && getNodeFromGridPane(grid, col, row + 1).getVal() == 0) {
						getNodeFromGridPane(grid, col, row + 1).setVal(temp.getVal());
						getNodeFromGridPane(grid, col, row + 1).setText(temp.getText());
						temp.setText("");
						temp.setVal(0);
					}
					if (col - 1 >= 0 && getNodeFromGridPane(grid, col - 1, row).getVal() == 0) {
						getNodeFromGridPane(grid, col - 1, row).setVal(temp.getVal());
						getNodeFromGridPane(grid, col - 1, row).setText(temp.getText());
						temp.setText("");
						temp.setVal(0);
					}
					if (col + 1 < 4 && getNodeFromGridPane(grid, col + 1, row).getVal() == 0) {
						getNodeFromGridPane(grid, col + 1, row).setVal(temp.getVal());
						getNodeFromGridPane(grid, col + 1, row).setText(temp.getText());
						temp.setText("");
						temp.setVal(0);
					}
					updateList();
					if (win(primaryStage)) {
					}
				});
			}
		}
		// assigns every button on the gridpane the proper value
		for (int i = 0, k = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++, k++) {
				GameButton temp = getNodeFromGridPane(grid, j, i);
				temp.setVal(game0.get(k));
				temp.setText(game0.get(k).toString());
				if (game0.get(k) == 0) {
					temp.setText("");
				}
			}
		}
		
		Scene scene = new Scene(bp, 700,700);
		return scene;
	}
	

	public static void main(String[] args) {
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("15 Puzzle");
		primaryStage.setScene(welcome(primaryStage));
		primaryStage.show();
	}
}