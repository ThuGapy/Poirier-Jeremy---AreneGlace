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
 * Classe contenant l'�diteur de map de l'arene de glace
 */
public class MapCreator {
	/**
	 * Largeur de la fen�tre par d�faut
	 */
	public static int WindowWidth = 1200;
	
	/**
	 * Hauteur de la fen�tre par d�faut
	 */
	public static int WindowHeight = 800;
	
	/**
	 * Nombre de ligne par d�faut
	 */
	public static int defaultRows = 10;
	
	/**
	 * Nombre de colonnes par d�faut
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
	 * D�termine si la map est valid�
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
	 * Quantit� de lignes dans la map
	 */
	public int Rows;
	
	/**
	 * Quantit� de colonnes dans la map
	 */
	public int Columns;
	
	/**
	 * Label montrant la quantit� de lignes
	 */
	public Label rowLabel;
	
	/**
	 * Label montrant la quantit� de
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
	 * GridPane, correspondant � la grille des tuiles
	 */
	public GridPane MapCreatorGrid;
	
	/**
	 * Matrice contenant les donn�es de la map
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
	 * Constructeur de MapCreator. Lance une nouvelle fen�tre d'�diteur
	 */
	public MapCreator() {
		// Les lignes et colonnes sont mis par d�faut
		Rows = defaultRows;
		Columns = defaultColumns;
		
		// Initialisation des divers composants de la map
		MapComponents = InitializeMapComponents();
		MapPanes = new Pane[Columns][Rows];
		
		// La map n'est pas valid� par d�faut
		MapCreator.MapValidated = false;
		
		// On ouvre le menu
		CreeMenu();
	}
	
	/**
	 * M�thode CreeMenu, s'occupe de cr�er la fen�tre de l'�diteur
	 */
	private void CreeMenu() {
		// Cr�ation d'un stage et on met la grosseur de la fen�tre
		Stage mapCreatorWindow = new Stage();
		mapCreatorWindow.setMinHeight(WindowHeight);
		mapCreatorWindow.setMinWidth(WindowWidth);
		mapCreatorWindow.setMaxHeight(WindowHeight * 1.4);
		mapCreatorWindow.setMaxWidth(WindowWidth * 1.4);
		
		// On change le titre de la fen�tre
		mapCreatorWindow.setTitle("Cr�ateur de map");
		
		// Lorsqu'on ferme la fen�tre
		mapCreatorWindow.setOnHiding(e -> {
			// On arr�te la mise � jour de l'UI
			MapCreatorUpdate.stop();
			MapCreatorUpdate = null;
			
			// On retourne au menu principal
			TP3.mainWindow.show();
			mapCreatorWindow.close();
		});
		
		// On cr�e notre StackPane racine
		StackPane root = new StackPane();

		// On cr�e la scene (fen�tre) et on la montre.
		mapCreatorWindow.setScene(new Scene(root, WindowWidth, WindowHeight));
		mapCreatorWindow.show();
		
		// On cr�e le GridView contenant les tuiles de la map
		CreeGridView(root);
		
		// On cr�e le GridControl correspondant au choix de la grosseur de la map
		CreeGridControl(root);
		
		// On cr�e la VBox des types possibles, correspond au type des composants de map
		CreeTypeVBox(root);
		
		// On cr�e la HBox, correspondant au bouton Valider et Sauvegarder
		CreeControlHBox(root, mapCreatorWindow);
		
		// Cr�ation de la timeline qui g�re la possibilit� de cliquer les boutons Valider et Sauvegarder
		MapCreatorUpdate = new Timeline(new KeyFrame(Duration.millis(100), e -> {
			// Si la map n'est pas valider, on ne peut pas la sauvegarder
			if(MapCreator.MapValidated) {
				saveButton.setDisable(false);
			} else {
				saveButton.setDisable(true);
			}
			
			// Si la map a un joueur, une sortie et qu'elle n'est pas valid�, on peut la valider
			if((!HasPlayer() || !HasEnd()) && !MapCreator.MapValidated) {
				validateButton.setDisable(true);
			} else {
				validateButton.setDisable(false);
			}
		}));
		 
		// On fait tourner la timeline pour une dur�e ind�termin�
		MapCreatorUpdate.setCycleCount(Animation.INDEFINITE);
		
		// On lance la timeline
		MapCreatorUpdate.play();
	}
	
	/**
	 * M�thode qui cr�e la grille de tuiles
	 * @param root StackPane racine de la fen�tre
	 */
	private void CreeGridView(StackPane root) {
		// On cr�e le GridPane
		MapCreatorGrid = new GridPane();
		
		// On l'ajoute au StackPane
		root.getChildren().add(MapCreatorGrid);
		
		// On cr�e les tuiles correspondant au composants de la map
		for(int i = 0; i < Rows; i++) {
			for(int j = 0; j < Columns; j++) {
				MapPanes[i][j] = CreateClickPane(i, j);
			}
		}
		
		// On aligne le GridPane au centre de la fen�tre
		MapCreatorGrid.setAlignment(Pos.CENTER);
	}
	
	/**
	 * M�thode qui sert � cr�er les boutons de gestion de la grille de la map
	 * @param root Stackpane racine de la fen�tre
	 */
	private void CreeGridControl(StackPane root) {
		// Cr�ation de la HBox contenant les boutons
		HBox gridControl = new HBox();
		gridControl.setMaxSize(WindowWidth, 50);
		
		// Cr�ation du bouton moins pour les lignes
		Button minusRowButton = new Button("-");
		
		// Lorsqu'on appuie sur le bouton moins pour les lignes, on diminue le nombre de lignes et on met le tableau � jour
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
		
		// Lorsqu'on appuie sur le bouton plus pour les lignes, on augmente le nombre de lignes et on met le tableau � jour
		plusRowButton.setOnAction(e -> {
			if(Rows + 1 <= maxRow) {
				Rows++;
				UpdateGrid(Columns, Rows);
			}
		});
		
		// Bouton moins pour les colonnes
		Button minusColumnButton = new Button("-");
		HBox.setMargin(minusColumnButton, new Insets(0, 0, 0, 30));
		
		// Lorsqu'on appuie sur le bouton moins pour les colonnes, on diminue le nombre de colonnes et on met le tableau � jour
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
		
		// Lorsqu'on appuie sur le bouton plus pour les colonnes, on augmente le nombre de colonnes et on met le tableau � jour
		plusColumnButton.setOnAction(e -> {
			if(Columns + 1 <= maxColumn) {
				Columns++;
				UpdateGrid(Columns, Rows);
			}
		});
		
		// On ajoute les boutons et les labels � la HBox
		gridControl.getChildren().addAll(minusRowButton, rowLabel, plusRowButton, minusColumnButton, columnLabel, plusColumnButton);
		
		// On aligne la HBox en haut, au milieu de la fen�tre
		StackPane.setAlignment(gridControl, Pos.TOP_CENTER);
		gridControl.setAlignment(Pos.TOP_CENTER);
		gridControl.setPadding(new Insets(20, 0, 0, 0));
		gridControl.setSpacing(10);
		
		// On ajoute la HBox au StackPane racine
		root.getChildren().add(gridControl);
	}
	
	/**
	 * M�thode qui cr�e les boutons de s�lection de type
	 * @param root StackPane racine de la fen�tre
	 */
	private void CreeTypeVBox(StackPane root) {
		// Cr�ation d'une VBox pour l'affichage
		VBox choixVBox = new VBox();
		choixVBox.setMaxSize(100, 250);
		
		// On cr�e un ToggleGroup pour avoir un seul bouton coch� � la fois
		ToggleGroup choixToggle = new ToggleGroup();
		
		// On cr�e le RadioButton air, coch� par d�faut
		airRadio = new RadioButton("Air");
		airRadio.setSelected(true);
		airRadio.setToggleGroup(choixToggle);
		
		// On cr�e le RadioButton mur
		murRadio = new RadioButton("Mur");
		murRadio.setToggleGroup(choixToggle);
		
		// On cr�e le RadioButton joueur
		playerRadio = new RadioButton("Joueur");
		playerRadio.setToggleGroup(choixToggle);
		
		// On cr�e le RadioButton sortie
		endRadio = new RadioButton("Sortie");
		endRadio.setToggleGroup(choixToggle);
		
		// On ajoute les diff�rents RadioButton � la VBox
		choixVBox.getChildren().addAll(airRadio, murRadio, playerRadio, endRadio);
		
		// On aligne la VBox � droit, au milieu de l'�cran
		StackPane.setAlignment(choixVBox, Pos.CENTER_RIGHT);
		choixVBox.setAlignment(Pos.CENTER_RIGHT);
		choixVBox.setPadding(new Insets(0, 30, 0, 0));
		choixVBox.setSpacing(10);
		
		// On ajoute la VBox au StackPane racine de la fen�tre.
		root.getChildren().add(choixVBox);
	}
	
	/**
	 * M�thode qui cr�e les boutons de controles Valider et Sauvegarder
	 * @param root StackPane racine de la fen�tre
	 * @param window Stage de la fen�tre
	 */
	private void CreeControlHBox(StackPane root, Stage window) {
		// On cr�e la HBox pour l'affichage
		HBox controlHBox = new HBox();
		controlHBox.setMaxSize(300, 50);
		
		// On cr�e le bouton Valider
		validateButton = new Button("Valider");
		validateButton.setMinSize(100, 40);
		
		// Lorsqu'on appuie sur Valider
		validateButton.setOnAction(e -> {
			// On cr�e une map appel� "validation" qui sera sauvegard� sous "validation.map"
			Map ValidationMap = new Map(MapComponents, Columns, Rows, "validation");
			ValidationMap.SaveMap();
			
			// On lance la map "validation.map" en mode validation
			new AreneJouer("validation.map", true);
		});
		
		// On cr�e le bouton sauvegarder
		saveButton = new Button("Sauvegarder la carte");
		saveButton.setMinSize(150, 40);
		saveButton.setDisable(true);
		
		// Lorsqu'on appuie sur le bouton valider
		saveButton.setOnAction(e -> {
			boolean MapNameValidated = false;
			
			// On demande un nom jusqu'� temps que l'utilisateur fournisse un nom valide
			do {
				// On cr�e un TextInputDialog pour entr� le nom de la map
				TextInputDialog MapName = new TextInputDialog();
				MapName.setTitle("Nom de la map");
				MapName.setHeaderText(null);
				MapName.setContentText("Nom:");
				
				Optional<String> nomMap = MapName.showAndWait();
				
				// Si le nom de la map est pr�sent, on continue, sinon on arr�te
				if(nomMap.isPresent()) {
					// On cr�e la map
					Map NewMap = new Map(MapComponents, Columns, Rows, nomMap.get());
					File MapFile = new File("maps/" + NewMap.GetMapPath());
					// On v�rifie que la map n'existe pas d�j�
					if(!MapFile.exists()) {
						// On sauvegarde la map
						NewMap.SaveMap();
						MapNameValidated = true;
						
						JOptionPane.showMessageDialog(null, "La map a bien �t� sauvegard�", "Map sauvegard�", JOptionPane.INFORMATION_MESSAGE);
						
						window.close();
					} else {
						JOptionPane.showMessageDialog(null, "Mauvais nom de map, celui-ci est d�j� utilis�", "Mauvais nom de map", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					MapNameValidated = true;
				}
			} while(!MapNameValidated);
		});
		
		// On ajoute le bouton Valider et Sauvegarder � la HBox
		controlHBox.getChildren().addAll(validateButton, saveButton);
		
		// On aligne la HBox en bas � gauche de la fen�tre
		StackPane.setAlignment(controlHBox, Pos.BOTTOM_LEFT);
		controlHBox.setAlignment(Pos.BOTTOM_LEFT);
		controlHBox.setPadding(new Insets(0, 0, 10, 10));
		controlHBox.setSpacing(10);
		
		// On ajoute la HBox au StackPane racine
		root.getChildren().add(controlHBox);
	}
	
	/**
	 * M�thode qui g�re le click sur un Pane
	 * @param PaneClicked Le Pane qui a �t� cliqu�
	 * @param x La position horizontale du Pane
	 * @param y La position verticale du Pane
	 */
	private void ClickOnCell(Pane PaneClicked, int x, int y) {
		// On stocke le type de la tuile avant le click
		MapComponent typeInitial = MapComponents[x][y];
		
		// Si le type est de l'air, on met � jour
		if(airRadio.isSelected()) {
			MapComponents[x][y] = MapComponent.AIR;
			MapPanes[x][y].setStyle(AirStyle);
		} else if(murRadio.isSelected()) { // Si le type est un mur, on met � jour
			MapComponents[x][y] = MapComponent.WALL;
			MapPanes[x][y].setStyle(WallStyle);
		} else if(playerRadio.isSelected()) { // Si le type est un joueur, on enl�ve le joueur de la map et on met le nouveau
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
		} else if(endRadio.isSelected()) { // Si le type est une sortie, on enl�ve la sortie de la map et on met la nouvelle
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
		
		// Si le nouveau type de la tuile n'est pas similaire � l'ancien, on annule la validation de la map.
		if(typeInitial != MapComponents[x][y]) {
			MapCreator.MapValidated = false;
		}
	}
	
	/**
	 * M�thode qui sert � mettre � jour la grille de la map
	 * @param w Largeur de la grille
	 * @param h Hauteur de la grille
	 */
	private void UpdateGrid(int w, int h) {
		// On vide la grille
		MapCreatorGrid.getChildren().clear();
		
		// On cr�e un nouveau tableau de map vide
		MapComponent[][] NewMapComponents = InitializeMapComponents();
		
		// On cr�e un nouveau tableau de Pane vide
		Pane[][] NewMapPanes = new Pane[Columns][Rows];
		
		// On boucle dans la map pr�sentement cr��
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				// On garde le plus d'information de la map pr�sentement cr��
				if(i < MapComponents.length && j < MapComponents[i].length) {
					NewMapComponents[i][j] = MapComponents[i][j];
				}
			}
		}
		
		// On met � jour la map pr�sentement cr��
		MapComponents = NewMapComponents;
		
		// On cr�� des nouveaux panes clickable
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				NewMapPanes[i][j] = CreateClickPane(i, j);
			}
		}
		
		// On met � jour le tableau de Pane
		MapPanes = NewMapPanes;
		
		// On met � jour les labels de colonnes et de lignes
		columnLabel.setText("Colonnes: " + w);
		rowLabel.setText("Lignes: " + h);
	}
	
	/**
	 * M�thode qui initialise les composants de la map comme �tant de l'air
	 * @return Une grille de map initialis�
	 */
	private MapComponent[][] InitializeMapComponents() {
		// On cr�e un nouveau tableau de la grosseur ad�quate
		MapComponent[][] NewMapComponents = new MapComponent[Columns][Rows];
		
		// On boucle � travers le nouveau tableau
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
	 * M�thode qui cr�e un Pane clickable
	 * @param x Position horizontale du Pane
	 * @param y Position verticale du Pane
	 * @return Le Pane cr��
	 */
	private Pane CreateClickPane(int x, int y) {
		// On cr�e un nouveau Pane
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
		
		// On ajoute le Pane � la grille
		MapCreatorGrid.add(newPane, x, y);
		
		// Lorsque le Pane est cliqu�, on handle le click
		newPane.setOnMouseClicked(e -> {
			ClickOnCell(newPane, x, y);
		});
		
		// On retourne le nouveau Pane
		return newPane;
	}
	
	/**
	 * M�thode qui d�termine si un joueur est pr�sent sur la map
	 * @return True, si un joueur est pr�sent, sinon False.
	 */
	private boolean HasPlayer() {
		// On boucle � travers les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// Si un composant correspond � un joueur, le joueur est pr�sent
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					return true;
				}
			}
		}
		
		// Si aucun joueur est trouv�, on retourne false
		return false;
	}
	
	/**
	 * M�thode qui d�termine si une sortie est pr�sente sur la map
	 * @return True, si une sortie est pr�sente, sinon False.
	 */
	private boolean HasEnd() {
		// On boucle � travers les composants de la map
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				// Si un composant correspond � une sortie, la sortie est pr�sente
				if(MapComponents[i][j] == MapComponent.END) {
					return true;
				}
			}
		}
		
		// Si aucune sortie est trouv�, on retourne false
		return false;
	}
}
