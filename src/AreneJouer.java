import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AreneJouer {
	public static int WindowWidth = 600;
	public static int WindowHeight = 600;
	
	public static String AirStyle = "-fx-background-color: white;";
	public static String WallStyle = "-fx-background-color: black;";
	public static String PlayerStyle = "-fx-background-color: green;";
	public static String EndStyle = "-fx-background-color: red;";
	
	public Stage newWindow;
	
	public StackPane root;
	
	public GridPane MapGrid;
	
	public MapComponent[][] MapComponents;
	public Pane[][] MapPanes;
	
	public int Columns;
	public int Rows;
	
	public boolean IsTesting;
	
	public boolean HasWon;
	
	public AreneJouer(String MapFile) {
		AreneJouerMenu(MapFile);
	}
	
	public AreneJouer(String MapFile, boolean Validation) {
		IsTesting = Validation;
		
		AreneJouerMenu(MapFile);
	}
	
	public void AreneJouerMenu(String MapFile) {
		HasWon = false;
		
		newWindow = new Stage();
		newWindow.setTitle("Arene de glace");
		
		newWindow.setOnHiding(e -> {
			if(!IsTesting) {
				newWindow.close();
				TP3.mainWindow.show();
			}
		});
		
		newWindow.setMinHeight(WindowHeight);
		newWindow.setMinWidth(WindowWidth);
		newWindow.setMaxHeight(WindowHeight * 2);
		newWindow.setMaxWidth(WindowWidth * 2);
		
		root = new StackPane();

		Scene playScene = new Scene(root, WindowWidth, WindowHeight);
		
		newWindow.setScene(playScene);
		newWindow.show();
		
		playScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if(!HasWon) {
					switch(keyEvent.getCode()) {
					case W:
					case UP:
						MovePlayerUp();
						break;
					case D:
					case RIGHT:
						MovePlayerRight();
						break;
					case S:
					case DOWN:
						MovePlayerDown();
						break;
					case A:
					case LEFT:
						MovePlayerLeft();
						break;
					default:
						break;
					}
				}
			}
		});
		
		MapGrid = new GridPane();
		MapGrid.setAlignment(Pos.CENTER);
		
		root.getChildren().add(MapGrid);
		
		Map MapLoad = Map.ReadMap(MapFile);
		
		Columns = MapLoad.GetColumns();
		Rows = MapLoad.GetRows();
		
		MapComponents = MapLoad.GetMapContent();
		MapPanes = new Pane[Columns][Rows];
		
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				MapPanes[i][j] = CreateArenaPane(i, j);
			}
		}
	}
	
	private Pane CreateArenaPane(int x, int y) {
		Pane newPane = new Pane();
		newPane.setMinSize(60, 60);
		
		switch(MapComponents[x][y]) {
		case AIR:
			newPane.setStyle(AirStyle);
			break;
		case WALL:
			newPane.setStyle(WallStyle);
			break;
		case PLAYER:
			newPane.setStyle(PlayerStyle);
			break;
		case END:
			newPane.setStyle(EndStyle);
			break;
		}
		
		MapGrid.add(newPane, x, y);
		
		return newPane;
	}
	
	private void MovePlayerUp() {
		Vector2 PlayerPos = GetPlayerPos();
		
		int newY = 0;
		
		for(int i = PlayerPos.y; i >= 0; i--) {
			if(MapComponents[PlayerPos.x][i] == MapComponent.WALL) {
				newY = i + 1;
				break;
			}
		}
		
		MovePlayer(new Vector2(PlayerPos.x, newY));
		HasWon = HasWon();
		CheckValidation();
	}
	
	private void MovePlayerDown() {
		Vector2 PlayerPos = GetPlayerPos();
		
		int newY = 0;
		
		for(int i = PlayerPos.y; i < Rows; i++) {
			if(MapComponents[PlayerPos.x][i] == MapComponent.WALL) {
				newY = i - 1;
				break;
			}
		}
		
		MovePlayer(new Vector2(PlayerPos.x, newY));
		HasWon = HasWon();
		CheckValidation();
	}
	
	private void MovePlayerLeft() {
		Vector2 PlayerPos = GetPlayerPos();
		
		int newX = 0;
		
		for(int i = PlayerPos.x; i >= 0; i--) {
			if(MapComponents[i][PlayerPos.y] == MapComponent.WALL) {
				newX = i + 1;
				break;
			}
		}
		
		MovePlayer(new Vector2(newX, PlayerPos.y));
		HasWon = HasWon();
		CheckValidation();
	}
	
	private void MovePlayerRight() {
		Vector2 PlayerPos = GetPlayerPos();
		
		int newX = 0;
		
		for(int i = PlayerPos.x; i < Columns; i++) {
			if(MapComponents[i][PlayerPos.y] == MapComponent.WALL) {
				newX = i - 1;
				break;
			}
		}
		
		MovePlayer(new Vector2(newX, PlayerPos.y));
		HasWon = HasWon();
		CheckValidation();
	}
	
	private Vector2 GetPlayerPos() {
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					return new Vector2(i, j);
				}
			}
		}
		return null;
	}
	
	private void MovePlayer(Vector2 NewPos) {
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					MapComponents[i][j] = MapComponent.AIR;
					MapPanes[i][j].setStyle(AirStyle);
				}
			}
		}
		
		MapComponents[NewPos.x][NewPos.y] = MapComponent.PLAYER;
		MapPanes[NewPos.x][NewPos.y].setStyle(PlayerStyle);
	}
	
	private boolean HasWon() {
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				if(MapComponents[i][j] == MapComponent.END) {
					return false;
				}
			}
		}
		
		ShowVictoryScreen();
		return true;
	}
	
	private void ShowVictoryScreen() {
		StackPane VictoryPane = new StackPane();
		VictoryPane.setMinSize(WindowWidth, WindowHeight / 5);
		VictoryPane.setMaxSize(WindowWidth * 2, (WindowHeight * 2) / 5);
		VictoryPane.setStyle("-fx-background-color: white; -fx-border-color: black");
		
		root.getChildren().add(VictoryPane);
		
		Label VictoryLabel = new Label("VICTOIRE !");
		VictoryLabel.setStyle("-fx-font: 32 arial;");
		
		StackPane.setMargin(VictoryLabel, new Insets(0, 0, 90, 0));
		StackPane.setAlignment(VictoryLabel, Pos.CENTER);
		
		VictoryPane.getChildren().add(VictoryLabel);
		
		StackPane.setAlignment(VictoryPane, Pos.CENTER);
		
		Button MainMenuButton = new Button("Menu principal");
		MainMenuButton.setMinSize(150, 40);
		
		StackPane.setMargin(MainMenuButton, new Insets(30, 0, 0, 0));
		
		StackPane.setAlignment(MainMenuButton, Pos.CENTER);
		
		VictoryPane.getChildren().add(MainMenuButton);
		
		MainMenuButton.setOnAction(e -> {
			newWindow.close();
			TP3.mainWindow.show();
		});
		
		Button QuitButton = new Button("Quitter");
		QuitButton.setMinSize(150, 40);
		
		StackPane.setMargin(QuitButton, new Insets(140, 0, 0, 0));
		
		StackPane.setAlignment(QuitButton, Pos.CENTER);
		
		VictoryPane.getChildren().add(QuitButton);
		
		QuitButton.setOnAction(e -> {
			System.exit(0);
		});
	}
	
	private void CheckValidation() {
		if(IsTesting) {
			if(HasWon()) {
				MapCreator.MapValidated = true;
				newWindow.close();
			}
		}
	}
}