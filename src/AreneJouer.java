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

/**
 * Classe correspondant au jeu de l'arene de glace
 */
public class AreneJouer {
	/**
	 * Largeur de la fenêtre
	 */
	public static int WindowWidth = 600;
	
	/**
	 * Hauteur de la fenêtre
	 */
	public static int WindowHeight = 600;
	
	/**
	 * Style d'une tuile d'air
	 */
	public static String AirStyle = "-fx-background-color: white;";
	
	/**
	 * Style d'une tuile de mur
	 */
	public static String WallStyle = "-fx-background-color: black;";
	
	/**
	 * Style d'une tuile de joueur
	 */
	public static String PlayerStyle = "-fx-background-color: green;";
	
	/**
	 * Style d'une tuile de sortie
	 */
	public static String EndStyle = "-fx-background-color: red;";
	
	/**
	 * Stage de la fenêtre de jeu
	 */
	public Stage jouerWindow;
	
	/**
	 * StackPane racine de la fenêtre
	 */
	public StackPane root;
	
	/**
	 * Grille de jeu
	 */
	public GridPane MapGrid;
	
	/**
	 * Composant de la map
	 */
	public MapComponent[][] MapComponents;
	
	/**
	 * Pane correspondant aux différentes tuiles de la map
	 */
	public Pane[][] MapPanes;
	
	/**
	 * Nombre de colonnes de la map
	 */
	public int Columns;
	
	/**
	 * Nombre de lignes de la map
	 */
	public int Rows;
	
	/**
	 * IsTesting, correspondant au mode de validation
	 */
	public boolean IsTesting;
	
	/**
	 * HasWon, correspondant à si la partie a été gagné
	 */
	public boolean HasWon;
	
	/**
	 * Constructeur de la classe à un argument
	 * @param MapFile Chemin d'accès à la map
	 */
	public AreneJouer(String MapFile) {
		// On détermine que la map n'est pas en validation
		IsTesting = false;
		
		// On ouvre le jeu avec la map
		AreneJouerMenu(MapFile);
	}
	
	/**
	 * Constructeur de la classe à deux arguments
	 * @param MapFile Chemin d'accès à la map
	 * @param Validation Si la map est lancé en mode validation
	 */
	public AreneJouer(String MapFile, boolean Validation) {
		// On met si la map est en mode validation ou pas
		IsTesting = Validation;
		
		// On lance le jeu avec la map
		AreneJouerMenu(MapFile);
	}
	
	/**
	 * Méthode qui crée la fenêtre de jeu
	 * @param MapFile Chemin d'accès à la map
	 */
	public void AreneJouerMenu(String MapFile) {
		// On déclare que la partie n'est pas gagné
		HasWon = false;
		
		// On crée un nouveau stage pour la fenêtre de jeu avec différents paramètres
		jouerWindow = new Stage();
		jouerWindow.setTitle("Arene de glace");
		jouerWindow.setMinHeight(WindowHeight);
		jouerWindow.setMinWidth(WindowWidth);
		jouerWindow.setMaxHeight(WindowHeight * 2);
		jouerWindow.setMaxWidth(WindowWidth * 2);
		
		// Lorsque la fenêtre est fermé
		jouerWindow.setOnHiding(e -> {
			// Si le jeu n'est pas en validation, on retourne au menu principal
			if(!IsTesting) {
				jouerWindow.close();
				TP3.mainWindow.show();
			}
		});
		
		// On crée notre StackPane racine
		root = new StackPane();

		// On crée notre scene
		Scene playScene = new Scene(root, WindowWidth, WindowHeight);
		
		// On set la scene et on la montre
		jouerWindow.setScene(playScene);
		jouerWindow.show();
		
		// Lorsqu'une touche du clavier est appuyé
		playScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				// Si la partie n'est pas gagné
				if(!HasWon) {
					switch(keyEvent.getCode()) {
					// Si le joueur appuie sur la flèche du haut ou W, on va en haut
					case W:
					case UP:
						MovePlayerUp();
						break;
					// Si le joueur appuie sur la flèche de droite ou D, on va à droite.
					case D:
					case RIGHT:
						MovePlayerRight();
						break;
					// Si le joueur appuie sur la flèche du bas ou S, on va en bas
					case S:
					case DOWN:
						MovePlayerDown();
						break;
					// Si le joueur appuie sur la flèche de gauche ou A, on va à gauche
					case A:
					case LEFT:
						MovePlayerLeft();
						break;
					// Sinon on ne fait rien
					default:
						break;
					}
				}
			}
		});
		
		// On crée un GridPane et on l'aligne au milieu de la fenêtre
		MapGrid = new GridPane();
		MapGrid.setAlignment(Pos.CENTER);
		
		// On ajoute le GridPane à la racine de la fenêtre
		root.getChildren().add(MapGrid);
		
		// On charge la map de jeu
		Map MapLoad = Map.ReadMap(MapFile);
		
		// On obtient le nombre de colonnes de la map
		Columns = MapLoad.GetColumns();
		
		// On obtient le nombre de lignes de la map
		Rows = MapLoad.GetRows();
		
		// On charge les composants de la map
		MapComponents = MapLoad.GetMapContent();
		
		// On crée un tableau pour les tuiles de la map
		MapPanes = new Pane[Columns][Rows];
		
		// On crée les différents tuiles de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				MapPanes[i][j] = CreateArenaPane(i, j);
			}
		}
	}
	
	/**
	 * Méthode qui permet de créé une tuile de la Map
	 * @param x Position horizontale
	 * @param y Position verticale
	 * @return Le nouveau Pane
	 */
	private Pane CreateArenaPane(int x, int y) {
		// On crée le nouveau Pane
		Pane newPane = new Pane();
		newPane.setMinSize(60, 60);
		
		// On met le style en fonction du type de tuile
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
		
		// On ajoute le nouveau Pane à la grille
		MapGrid.add(newPane, x, y);
		
		// On retourne le nouveau Pane
		return newPane;
	}
	
	/**
	 * Méthode qui déplace le joueur vers le haut
	 */
	private void MovePlayerUp() {
		// On obtient la présente position du joueur
		Vector2 PlayerPos = GetPlayerPos();
		
		// Déclaration de la variable du nouveau Y
		int newY = 0;
		
		// On boucle jusqu'à temps qu'on rencontre un mur
		for(int i = PlayerPos.y; i >= 0; i--) {
			if(MapComponents[PlayerPos.x][i] == MapComponent.WALL) {
				// On détermine le nouveau Y
				newY = i + 1;
				break;
			}
		}
		
		// On déplace le joueur à sa nouvelle position
		MovePlayer(new Vector2(PlayerPos.x, newY));
		
		// On valide si la partie est gagné
		HasWon = HasWon();
	}
	
	/**
	 * Méthode qui déplace le joueur vers le bas
	 */
	private void MovePlayerDown() {
		// On obtient la présente position du joueur
		Vector2 PlayerPos = GetPlayerPos();
		
		// Déclaration de la variable du nouveau Y
		int newY = 0;
		
		// On boucle jusqu'à temps qu'on rencontre un mur
		for(int i = PlayerPos.y; i < Rows; i++) {
			if(MapComponents[PlayerPos.x][i] == MapComponent.WALL) {
				// On détermine le nouveau Y
				newY = i - 1;
				break;
			}
		}
		
		// On déplace le joueur à sa nouvelle position
		MovePlayer(new Vector2(PlayerPos.x, newY));
		
		// On valide si la partie est gagné
		HasWon = HasWon();
	}
	
	/**
	 * Méthode qui déplace le joueur vers la gauche
	 */
	private void MovePlayerLeft() {
		// On obtient la présente position du joueur
		Vector2 PlayerPos = GetPlayerPos();
		
		// Déclaration de la variable du nouveau X
		int newX = 0;
		
		// On boucle jusqu'à temps qu'on rencontre un mur
		for(int i = PlayerPos.x; i >= 0; i--) {
			if(MapComponents[i][PlayerPos.y] == MapComponent.WALL) {
				// On détermine le nouveau X
				newX = i + 1;
				break;
			}
		}
		
		// On déplace le joueur à sa nouvelle position
		MovePlayer(new Vector2(newX, PlayerPos.y));
		
		// On valide si la partie est gagné
		HasWon = HasWon();
	}
	
	/**
	 * Méthode qui déplace le joueur vers la droite
	 */
	private void MovePlayerRight() {
		// On obtient la présente position du joueur
		Vector2 PlayerPos = GetPlayerPos();
		
		// Déclaration de la variable du nouveau X
		int newX = 0;
		
		// On boucle jusqu'à temps qu'on rencontre un mur
		for(int i = PlayerPos.x; i < Columns; i++) {
			if(MapComponents[i][PlayerPos.y] == MapComponent.WALL) {
				// On détermine le nouveau X
				newX = i - 1;
				break;
			}
		}
		
		// On déplace le joueur à sa nouvelle position
		MovePlayer(new Vector2(newX, PlayerPos.y));
		
		// On valide si la partie est gagné
		HasWon = HasWon();
	}
	
	/**
	 * Méthode qui permet d'obtenir la position du joueur
	 * @return La position du joueur
	 */
	private Vector2 GetPlayerPos() {
		// On boucle dans les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// Lorsqu'on trouve le joueur, on retourne sa position
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					return new Vector2(i, j);
				}
			}
		}
		
		// Si le joueur n'est pas trouvé, on retourne null
		return null;
	}
	
	/**
	 * Méthode qui bouge le player sur la map
	 * @param NewPos
	 */
	private void MovePlayer(Vector2 NewPos) {
		// On boucle dans les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// Lorsqu'on trouve le joueur, on le remplace par de l'air
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					MapComponents[i][j] = MapComponent.AIR;
					MapPanes[i][j].setStyle(AirStyle);
				}
			}
		}
		
		// On met le joueur à sa nouvelle position
		MapComponents[NewPos.x][NewPos.y] = MapComponent.PLAYER;
		MapPanes[NewPos.x][NewPos.y].setStyle(PlayerStyle);
	}
	
	/**
	 * Méthode qui détermine si la partie est gagné
	 * @return Si la partie est gagné
	 */
	private boolean HasWon() {
		// On boucle dans les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				if(MapComponents[i][j] == MapComponent.END) {
					// Si on trouve la sortie, la partie n'est pas gagné
					return false;
				}
			}
		}
		
		// Si la map est en validation
		if(IsTesting) {
			// On valide la map et on ferme le jeu
			MapCreator.MapValidated = true;
			jouerWindow.close();
		}
		
		// Si on ne trouve pas la sortie, la partie est gagné
		ShowVictoryScreen();
		return true;
	}
	
	/**
	 * Méthode qui montre l'écran de victoire
	 */
	private void ShowVictoryScreen() {
		// Création d'un StackPane pour l'écran de victoire
		StackPane VictoryPane = new StackPane();
		VictoryPane.setMinSize(WindowWidth, WindowHeight / 5);
		VictoryPane.setMaxSize(WindowWidth * 2, (WindowHeight * 2) / 5);
		VictoryPane.setStyle("-fx-background-color: white; -fx-border-color: black");
		
		// On ajoute le nouveau StackPane à la racine de la fenêtre
		root.getChildren().add(VictoryPane);
		
		// Création d'un label victoire
		Label VictoryLabel = new Label("VICTOIRE !");
		VictoryLabel.setStyle("-fx-font: 32 arial;");
		
		// On l'aligne au centre
		StackPane.setMargin(VictoryLabel, new Insets(0, 0, 90, 0));
		StackPane.setAlignment(VictoryLabel, Pos.CENTER);
		
		// Création d'un bouton menu principal
		Button MainMenuButton = new Button("Menu principal");
		MainMenuButton.setMinSize(150, 40);
		
		// On l'aligne
		StackPane.setMargin(MainMenuButton, new Insets(30, 0, 0, 0));
		StackPane.setAlignment(MainMenuButton, Pos.CENTER);
		
		// Lorsqu'on clique sur le bouton menu principal, on ferme le jeu et on montre le menu principal
		MainMenuButton.setOnAction(e -> {
			jouerWindow.close();
			TP3.mainWindow.show();
		});
		
		// Création d'un bouton quitter
		Button QuitButton = new Button("Quitter");
		QuitButton.setMinSize(150, 40);
		
		// On l'aligne
		StackPane.setMargin(QuitButton, new Insets(140, 0, 0, 0));
		StackPane.setAlignment(QuitButton, Pos.CENTER);
		
		// Lorsqu'on appuie sur le bouton quitter, on ferme l'application
		QuitButton.setOnAction(e -> {
			System.exit(0);
		});
		
		// On aligne le StackPane de victoire au centre de la fenêtre
		StackPane.setAlignment(VictoryPane, Pos.CENTER);
		
		// On ajoute les differents élements au StackPane de victoire
		VictoryPane.getChildren().addAll(VictoryLabel, MainMenuButton, QuitButton);
	}
}