import java.io.File;
import java.util.Optional;

import javax.swing.JOptionPane;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Classe contenant l'éditeur de map de l'arene de glace
 */
public class MapCreator {
	/**
	 * Largeur de la fenêtre par défaut
	 */
	public static int WindowWidth = 1200;
	
	/**
	 * Hauteur de la fenêtre par défaut
	 */
	public static int WindowHeight = 800;
	
	/**
	 * Nombre de ligne par défaut
	 */
	public static int defaultRows = 10;
	
	/**
	 * Nombre de colonnes par défaut
	 */
	public static int defaultColumns = 10;
	
	/**
	 * Nombre minimum de lignes
	 */
	public static int minRow = 3;
	
	/**
	 * Nombre maximum de lignes
	 */
	public static int maxRow = 10;
	
	/**
	 * Nombre minimum de colonnes
	 */
	public static int minColumn = 3;
	
	/**
	 * Nombre maximum de colonnes
	 */
	public static int maxColumn = 10;
	
	/**
	 * Détermine si la map est validé
	 */
	public static boolean MapValidated;
	
	/**
	 * Style d'un Pane de type air
	 */
	public static String AirStyle = "-fx-background-color: white; -fx-border-color: black";
	
	/**
	 * Style d'un Pane de type mur
	 */
	public static String WallStyle = "-fx-background-color: black; -fx-border-color: black";
	
	/**
	 * Style d'un Pane de type joueur
	 */
	public static String PlayerStyle = "-fx-background-color: green; -fx-border-color: black";
	
	/**
	 * Style d'un Pane de type sortie
	 */
	public static String EndStyle = "-fx-background-color: red; -fx-border-color: black";
	
	/**
	 * Quantité de lignes dans la map
	 */
	public int Rows;
	
	/**
	 * Quantité de colonnes dans la map
	 */
	public int Columns;
	
	/**
	 * Label montrant la quantité de lignes
	 */
	public Label rowLabel;
	
	/**
	 * Label montrant la quantité de
	 */
	public Label columnLabel;
	
	/**
	 * RadioButton de l'air
	 */
	public RadioButton airRadio;
	
	/**
	 * RadioButton du mur
	 */
	public RadioButton murRadio;
	
	/**
	 * RadioButton du joueur
	 */
	public RadioButton playerRadio;
	
	/**
	 * RadioButton de la sortie
	 */
	public RadioButton endRadio;
	
	/**
	 * GridPane, correspondant à la grille des tuiles
	 */
	public GridPane MapCreatorGrid;
	
	/**
	 * Matrice contenant les données de la map
	 */
	public MapComponent[][] MapComponents;
	
	/**
	 * Matrices contenant les differents Pane pour la visualisation des tuiles
	 */
	public Pane[][] MapPanes;
	
	/**
	 * Button pour sauvegarder la map
	 */
	public Button saveButton;
	
	/**
	 * Button pour valider la map
	 */
	public Button validateButton;
	
	/**
	 * Timeline pour la validation de la map
	 */
	public Timeline MapCreatorUpdate;
	
	/**
	 * Constructeur de MapCreator. Lance une nouvelle fenêtre d'éditeur
	 */
	public MapCreator() {
		// Les lignes et colonnes sont mis par défaut
		Rows = defaultRows;
		Columns = defaultColumns;
		
		// Initialisation des divers composants de la map
		MapComponents = InitializeMapComponents();
		MapPanes = new Pane[Columns][Rows];
		
		// La map n'est pas validé par défaut
		MapCreator.MapValidated = false;
		
		// On ouvre le menu
		CreeMenu();
	}
	
	/**
	 * Méthode CreeMenu, s'occupe de créer la fenêtre de l'éditeur
	 */
	private void CreeMenu() {
		// Création d'un stage et on met la grosseur de la fenêtre
		Stage mapCreatorWindow = new Stage();
		mapCreatorWindow.setMinHeight(WindowHeight);
		mapCreatorWindow.setMinWidth(WindowWidth);
		mapCreatorWindow.setMaxHeight(WindowHeight * 1.4);
		mapCreatorWindow.setMaxWidth(WindowWidth * 1.4);
		
		// On change le titre de la fenêtre
		mapCreatorWindow.setTitle("Créateur de map");
		
		// Lorsqu'on ferme la fenêtre
		mapCreatorWindow.setOnHiding(e -> {
			// On arrête la mise à jour de l'UI
			MapCreatorUpdate.stop();
			MapCreatorUpdate = null;
			
			// On retourne au menu principal
			TP3.mainWindow.show();
			mapCreatorWindow.close();
		});
		
		// On crée notre StackPane racine
		StackPane root = new StackPane();

		// On crée la scene (fenêtre) et on la montre.
		mapCreatorWindow.setScene(new Scene(root, WindowWidth, WindowHeight));
		mapCreatorWindow.show();
		
		// On crée le GridView contenant les tuiles de la map
		CreeGridView(root);
		
		// On crée le GridControl correspondant au choix de la grosseur de la map
		CreeGridControl(root);
		
		// On crée la VBox des types possibles, correspond au type des composants de map
		CreeTypeVBox(root);
		
		// On crée la HBox, correspondant au bouton Valider et Sauvegarder
		CreeControlHBox(root, mapCreatorWindow);
		
		// Création de la timeline qui gêre la possibilité de cliquer les boutons Valider et Sauvegarder
		MapCreatorUpdate = new Timeline(new KeyFrame(Duration.millis(100), e -> {
			// Si la map n'est pas valider, on ne peut pas la sauvegarder
			if(MapCreator.MapValidated) {
				saveButton.setDisable(false);
			} else {
				saveButton.setDisable(true);
			}
			
			// Si la map a un joueur, une sortie et qu'elle n'est pas validé, on peut la valider
			if((!HasPlayer() || !HasEnd()) && !MapCreator.MapValidated) {
				validateButton.setDisable(true);
			} else {
				validateButton.setDisable(false);
			}
		}));
		 
		// On fait tourner la timeline pour une durée indéterminé
		MapCreatorUpdate.setCycleCount(Animation.INDEFINITE);
		
		// On lance la timeline
		MapCreatorUpdate.play();
	}
	
	/**
	 * Méthode qui crée la grille de tuiles
	 * @param root StackPane racine de la fenêtre
	 */
	private void CreeGridView(StackPane root) {
		// On crée le GridPane
		MapCreatorGrid = new GridPane();
		
		// On l'ajoute au StackPane
		root.getChildren().add(MapCreatorGrid);
		
		// On crée les tuiles correspondant au composants de la map
		for(int i = 0; i < Rows; i++) {
			for(int j = 0; j < Columns; j++) {
				MapPanes[i][j] = CreateClickPane(i, j);
			}
		}
		
		// On aligne le GridPane au centre de la fenêtre
		MapCreatorGrid.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Méthode qui sert à créer les boutons de gestion de la grille de la map
	 * @param root Stackpane racine de la fenêtre
	 */
	private void CreeGridControl(StackPane root) {
		// Création de la HBox contenant les boutons
		HBox gridControl = new HBox();
		gridControl.setMaxSize(WindowWidth, 50);
		
		// Création du bouton moins pour les lignes
		Button minusRowButton = new Button("-");
		
		// Lorsqu'on appuie sur le bouton moins pour les lignes, on diminue le nombre de lignes et on met le tableau à jour
		minusRowButton.setOnAction(e -> {
			if(Rows - 1 >= minRow) {
				Rows--;
				UpdateGrid(Columns, Rows);
			}
		});
		
		// Label montrant le nombre de lignes
		rowLabel = new Label("Lignes: " + defaultRows);
		HBox.setMargin(rowLabel, new Insets(5, 0, 0, 0));

		// Bouton plus pour les lignes
		Button plusRowButton = new Button("+");
		
		// Lorsqu'on appuie sur le bouton plus pour les lignes, on augmente le nombre de lignes et on met le tableau à jour
		plusRowButton.setOnAction(e -> {
			if(Rows + 1 <= maxRow) {
				Rows++;
				UpdateGrid(Columns, Rows);
			}
		});
		
		// Bouton moins pour les colonnes
		Button minusColumnButton = new Button("-");
		HBox.setMargin(minusColumnButton, new Insets(0, 0, 0, 30));
		
		// Lorsqu'on appuie sur le bouton moins pour les colonnes, on diminue le nombre de colonnes et on met le tableau à jour
		minusColumnButton.setOnAction(e -> {
			if(Columns - 1 >= minColumn) {
				Columns--;
				UpdateGrid(Columns, Rows);
			}
		});
		
		// Label montrant le nombre de colonnes
		columnLabel = new Label("Colonnes: " + defaultColumns);
		HBox.setMargin(columnLabel, new Insets(5, 0, 0, 0));

		// Bouton plus pour les colonnes
		Button plusColumnButton = new Button("+");
		
		// Lorsqu'on appuie sur le bouton plus pour les colonnes, on augmente le nombre de colonnes et on met le tableau à jour
		plusColumnButton.setOnAction(e -> {
			if(Columns + 1 <= maxColumn) {
				Columns++;
				UpdateGrid(Columns, Rows);
			}
		});
		
		// On ajoute les boutons et les labels à la HBox
		gridControl.getChildren().addAll(minusRowButton, rowLabel, plusRowButton, minusColumnButton, columnLabel, plusColumnButton);
		
		// On aligne la HBox en haut, au milieu de la fenêtre
		StackPane.setAlignment(gridControl, Pos.TOP_CENTER);
		gridControl.setAlignment(Pos.TOP_CENTER);
		gridControl.setPadding(new Insets(20, 0, 0, 0));
		gridControl.setSpacing(10);
		
		// On ajoute la HBox au StackPane racine
		root.getChildren().add(gridControl);
	}
	
	/**
	 * Méthode qui crée les boutons de sélection de type
	 * @param root StackPane racine de la fenêtre
	 */
	private void CreeTypeVBox(StackPane root) {
		// Création d'une VBox pour l'affichage
		VBox choixVBox = new VBox();
		choixVBox.setMaxSize(100, 250);
		
		// On crée un ToggleGroup pour avoir un seul bouton coché à la fois
		ToggleGroup choixToggle = new ToggleGroup();
		
		// On crée le RadioButton air, coché par défaut
		airRadio = new RadioButton("Air");
		airRadio.setSelected(true);
		airRadio.setToggleGroup(choixToggle);
		
		// On crée le RadioButton mur
		murRadio = new RadioButton("Mur");
		murRadio.setToggleGroup(choixToggle);
		
		// On crée le RadioButton joueur
		playerRadio = new RadioButton("Joueur");
		playerRadio.setToggleGroup(choixToggle);
		
		// On crée le RadioButton sortie
		endRadio = new RadioButton("Sortie");
		endRadio.setToggleGroup(choixToggle);
		
		// On ajoute les différents RadioButton à la VBox
		choixVBox.getChildren().addAll(airRadio, murRadio, playerRadio, endRadio);
		
		// On aligne la VBox à droit, au milieu de l'écran
		StackPane.setAlignment(choixVBox, Pos.CENTER_RIGHT);
		choixVBox.setAlignment(Pos.CENTER_RIGHT);
		choixVBox.setPadding(new Insets(0, 30, 0, 0));
		choixVBox.setSpacing(10);
		
		// On ajoute la VBox au StackPane racine de la fenêtre.
		root.getChildren().add(choixVBox);
	}
	
	/**
	 * Méthode qui crée les boutons de controles Valider et Sauvegarder
	 * @param root StackPane racine de la fenêtre
	 * @param window Stage de la fenêtre
	 */
	private void CreeControlHBox(StackPane root, Stage window) {
		// On crée la HBox pour l'affichage
		HBox controlHBox = new HBox();
		controlHBox.setMaxSize(300, 50);
		
		// On crée le bouton Valider
		validateButton = new Button("Valider");
		validateButton.setMinSize(100, 40);
		
		// Lorsqu'on appuie sur Valider
		validateButton.setOnAction(e -> {
			// On crée une map appelé "validation" qui sera sauvegardé sous "validation.map"
			Map ValidationMap = new Map(MapComponents, Columns, Rows, "validation");
			ValidationMap.SaveMap();
			
			// On lance la map "validation.map" en mode validation
			new AreneJouer("validation.map", true);
		});
		
		// On crée le bouton sauvegarder
		saveButton = new Button("Sauvegarder la carte");
		saveButton.setMinSize(150, 40);
		saveButton.setDisable(true);
		
		// Lorsqu'on appuie sur le bouton valider
		saveButton.setOnAction(e -> {
			boolean MapNameValidated = false;
			
			// On demande un nom jusqu'à temps que l'utilisateur fournisse un nom valide
			do {
				// On crée un TextInputDialog pour entré le nom de la map
				TextInputDialog MapName = new TextInputDialog();
				MapName.setTitle("Nom de la map");
				MapName.setHeaderText(null);
				MapName.setContentText("Nom:");
				
				Optional<String> nomMap = MapName.showAndWait();
				
				// Si le nom de la map est présent, on continue, sinon on arrête
				if(nomMap.isPresent()) {
					// On crée la map
					Map NewMap = new Map(MapComponents, Columns, Rows, nomMap.get());
					File MapFile = new File("maps/" + NewMap.GetMapPath());
					// On vérifie que la map n'existe pas déjà
					if(!MapFile.exists()) {
						// On sauvegarde la map
						NewMap.SaveMap();
						MapNameValidated = true;
						
						JOptionPane.showMessageDialog(null, "La map a bien été sauvegardé", "Map sauvegardé", JOptionPane.INFORMATION_MESSAGE);
						
						window.close();
					} else {
						JOptionPane.showMessageDialog(null, "Mauvais nom de map, celui-ci est déjà utilisé", "Mauvais nom de map", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					MapNameValidated = true;
				}
			} while(!MapNameValidated);
		});
		
		// On ajoute le bouton Valider et Sauvegarder à la HBox
		controlHBox.getChildren().addAll(validateButton, saveButton);
		
		// On aligne la HBox en bas à gauche de la fenêtre
		StackPane.setAlignment(controlHBox, Pos.BOTTOM_LEFT);
		controlHBox.setAlignment(Pos.BOTTOM_LEFT);
		controlHBox.setPadding(new Insets(0, 0, 10, 10));
		controlHBox.setSpacing(10);
		
		// On ajoute la HBox au StackPane racine
		root.getChildren().add(controlHBox);
	}
	
	/**
	 * Méthode qui gêre le click sur un Pane
	 * @param PaneClicked Le Pane qui a été cliqué
	 * @param x La position horizontale du Pane
	 * @param y La position verticale du Pane
	 */
	private void ClickOnCell(Pane PaneClicked, int x, int y) {
		// On stocke le type de la tuile avant le click
		MapComponent typeInitial = MapComponents[x][y];
		
		// Si le type est de l'air, on met à jour
		if(airRadio.isSelected()) {
			MapComponents[x][y] = MapComponent.AIR;
			MapPanes[x][y].setStyle(AirStyle);
		} else if(murRadio.isSelected()) { // Si le type est un mur, on met à jour
			MapComponents[x][y] = MapComponent.WALL;
			MapPanes[x][y].setStyle(WallStyle);
		} else if(playerRadio.isSelected()) { // Si le type est un joueur, on enlève le joueur de la map et on met le nouveau
			for(int i = 0; i < Columns; i++) {
				for(int j = 0; j < Rows; j++) {
					if(MapComponents[i][j] == MapComponent.PLAYER) {
						MapComponents[i][j] = MapComponent.AIR;
						MapPanes[i][j].setStyle(AirStyle);
					}
				}
			}
			MapComponents[x][y] = MapComponent.PLAYER;
			MapPanes[x][y].setStyle(PlayerStyle);
		} else if(endRadio.isSelected()) { // Si le type est une sortie, on enlève la sortie de la map et on met la nouvelle
			for(int i = 0; i < Columns; i++) {
				for(int j = 0; j < Rows; j++) {
					if(MapComponents[i][j] == MapComponent.END) {
						MapComponents[i][j] = MapComponent.AIR;
						MapPanes[i][j].setStyle(AirStyle);
					}
				}
			}
			MapComponents[x][y] = MapComponent.END;
			MapPanes[x][y].setStyle(EndStyle);
		}
		
		// Si le nouveau type de la tuile n'est pas similaire à l'ancien, on annule la validation de la map.
		if(typeInitial != MapComponents[x][y]) {
			MapCreator.MapValidated = false;
		}
	}
	
	/**
	 * Méthode qui sert à mettre à jour la grille de la map
	 * @param w Largeur de la grille
	 * @param h Hauteur de la grille
	 */
	private void UpdateGrid(int w, int h) {
		// On vide la grille
		MapCreatorGrid.getChildren().clear();
		
		// On crée un nouveau tableau de map vide
		MapComponent[][] NewMapComponents = InitializeMapComponents();
		
		// On crée un nouveau tableau de Pane vide
		Pane[][] NewMapPanes = new Pane[Columns][Rows];
		
		// On boucle dans la map présentement créé
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				// On garde le plus d'information de la map présentement créé
				if(i < MapComponents.length && j < MapComponents[i].length) {
					NewMapComponents[i][j] = MapComponents[i][j];
				}
			}
		}
		
		// On met à jour la map présentement créé
		MapComponents = NewMapComponents;
		
		// On créé des nouveaux panes clickable
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				NewMapPanes[i][j] = CreateClickPane(i, j);
			}
		}
		
		// On met à jour le tableau de Pane
		MapPanes = NewMapPanes;
		
		// On met à jour les labels de colonnes et de lignes
		columnLabel.setText("Colonnes: " + w);
		rowLabel.setText("Lignes: " + h);
	}
	
	/**
	 * Méthode qui initialise les composants de la map comme étant de l'air
	 * @return Une grille de map initialisé
	 */
	private MapComponent[][] InitializeMapComponents() {
		// On crée un nouveau tableau de la grosseur adéquate
		MapComponent[][] NewMapComponents = new MapComponent[Columns][Rows];
		
		// On boucle à travers le nouveau tableau
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// On met que le type est de l,air
				NewMapComponents[i][j] = MapComponent.AIR;
			}
		}
		
		// On retourne le nouveau tableau
		return NewMapComponents;
	}
	
	/**
	 * Méthode qui crée un Pane clickable
	 * @param x Position horizontale du Pane
	 * @param y Position verticale du Pane
	 * @return Le Pane créé
	 */
	private Pane CreateClickPane(int x, int y) {
		// On crée un nouveau Pane
		Pane newPane = new Pane();
		newPane.setMinSize(60, 60);
		
		// On lui met un style en fonction de son type
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
		
		// On ajoute le Pane à la grille
		MapCreatorGrid.add(newPane, x, y);
		
		// Lorsque le Pane est cliqué, on handle le click
		newPane.setOnMouseClicked(e -> {
			ClickOnCell(newPane, x, y);
		});
		
		// On retourne le nouveau Pane
		return newPane;
	}
	
	/**
	 * Méthode qui détermine si un joueur est présent sur la map
	 * @return True, si un joueur est présent, sinon False.
	 */
	private boolean HasPlayer() {
		// On boucle à travers les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// Si un composant correspond à un joueur, le joueur est présent
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					return true;
				}
			}
		}
		
		// Si aucun joueur est trouvé, on retourne false
		return false;
	}
	
	/**
	 * Méthode qui détermine si une sortie est présente sur la map
	 * @return True, si une sortie est présente, sinon False.
	 */
	private boolean HasEnd() {
		// On boucle à travers les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// Si un composant correspond à une sortie, la sortie est présente
				if(MapComponents[i][j] == MapComponent.END) {
					return true;
				}
			}
		}
		
		// Si aucune sortie est trouvé, on retourne false
		return false;
	}
}
