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

public class MapCreator {
	public static int WindowWidth = 1200;
	public static int WindowHeight = 800;
	
	public static int defaultRows = 10;
	public static int defaultColumns = 10;
	
	public static int minRow = 3;
	public static int maxRow = 10;
	
	public static int minColumn = 3;
	public static int maxColumn = 10;
	
	public static boolean MapValidated;
	
	public static String AirStyle = "-fx-background-color: white; -fx-border-color: black";
	public static String WallStyle = "-fx-background-color: black; -fx-border-color: black";
	public static String PlayerStyle = "-fx-background-color: green; -fx-border-color: black";
	public static String EndStyle = "-fx-background-color: red; -fx-border-color: black";
	
	public int Rows;
	public int Columns;
	
	public Label rowLabel;
	public Label columnLabel;
	
	public RadioButton airRadio;
	public RadioButton murRadio;
	public RadioButton playerRadio;
	public RadioButton endRadio;
	
	public GridPane MapCreatorGrid;
	
	public MapComponent[][] MapComponents;
	public Pane[][] MapPanes;
	
	public Timeline MapCreatorUpdate;
	
	public MapCreator() {
		Rows = defaultRows;
		Columns = defaultColumns;
		
		MapComponents = InitializeMapComponents();
		MapPanes = new Pane[Columns][Rows];
		
		MapCreator.MapValidated = false;
		
		CreeMenu();
	}
	
	private void CreeMenu() {
		Stage newWindow = new Stage();
		newWindow.setMinHeight(WindowHeight);
		newWindow.setMinWidth(WindowWidth);
		newWindow.setMaxHeight(WindowHeight * 1.4);
		newWindow.setMaxWidth(WindowWidth * 1.4);
		
		newWindow.setTitle("Créateur de map");
		
		newWindow.setOnHiding(e -> {
			MapCreatorUpdate.stop();
			MapCreatorUpdate = null;
		});
		
		StackPane root = new StackPane();

		newWindow.setScene(new Scene(root, WindowWidth, WindowHeight));
		newWindow.show();
		
		MapCreatorGrid = new GridPane();
		
		root.getChildren().add(MapCreatorGrid);
		
		for(int i = 0; i < Rows; i++) {
			for(int j = 0; j < Columns; j++) {
				MapPanes[i][j] = CreateClickPane(i, j);
			}
		}
		
		MapCreatorGrid.setAlignment(Pos.CENTER);
		
		HBox gridControl = new HBox();
		gridControl.setMaxSize(WindowWidth, 50);
		
		Button minusRowButton = new Button("-");
		
		minusRowButton.setOnAction(e -> {
			if(Rows - 1 >= minRow) {
				Rows--;
				UpdateGrid(Columns, Rows);
			}
		});
		
		rowLabel = new Label("Lignes: " + defaultRows);
		HBox.setMargin(rowLabel, new Insets(5, 0, 0, 0));

		Button plusRowButton = new Button("+");
		
		plusRowButton.setOnAction(e -> {
			if(Rows + 1 <= maxRow) {
				Rows++;
				UpdateGrid(Columns, Rows);
			}
		});
		
		Button minusColumnButton = new Button("-");
		HBox.setMargin(minusColumnButton, new Insets(0, 0, 0, 30));
		
		minusColumnButton.setOnAction(e -> {
			if(Columns - 1 >= minColumn) {
				Columns--;
				UpdateGrid(Columns, Rows);
			}
		});
		
		columnLabel = new Label("Colonnes: " + defaultColumns);
		HBox.setMargin(columnLabel, new Insets(5, 0, 0, 0));

		Button plusColumnButton = new Button("+");
		
		plusColumnButton.setOnAction(e -> {
			if(Columns + 1 <= maxColumn) {
				Columns++;
				UpdateGrid(Columns, Rows);
			}
		});
		
		gridControl.getChildren().addAll(minusRowButton, rowLabel, plusRowButton, minusColumnButton, columnLabel, plusColumnButton);
		
		StackPane.setAlignment(gridControl, Pos.TOP_CENTER);
		gridControl.setAlignment(Pos.TOP_CENTER);
		gridControl.setPadding(new Insets(20, 0, 0, 0));
		gridControl.setSpacing(10);
		
		root.getChildren().add(gridControl);
		
		VBox choixVBox = new VBox();
		choixVBox.setMaxSize(100, 250);
		
		ToggleGroup choixToggle = new ToggleGroup();
		
		airRadio = new RadioButton("Air");
		airRadio.setSelected(true);
		airRadio.setToggleGroup(choixToggle);
		
		murRadio = new RadioButton("Mur");
		murRadio.setToggleGroup(choixToggle);
		
		playerRadio = new RadioButton("Joueur");
		playerRadio.setToggleGroup(choixToggle);
		
		endRadio = new RadioButton("Sortie");
		endRadio.setToggleGroup(choixToggle);
		
		choixVBox.getChildren().addAll(airRadio, murRadio, playerRadio, endRadio);
		
		StackPane.setAlignment(choixVBox, Pos.CENTER_RIGHT);
		choixVBox.setAlignment(Pos.CENTER_RIGHT);
		choixVBox.setPadding(new Insets(0, 30, 0, 0));
		choixVBox.setSpacing(10);
		
		root.getChildren().add(choixVBox);
		
		HBox controlHBox = new HBox();
		controlHBox.setMaxSize(300, 50);
		
		Button validateButton = new Button("Valider");
		validateButton.setMinSize(100, 40);
		
		validateButton.setOnAction(e -> {
			Map ValidationMap = new Map(MapComponents, Columns, Rows, "validation");
			ValidationMap.SaveMap();
			
			new AreneJouer("validation.map", true);
		});
		
		Button saveButton = new Button("Sauvegarder la carte");
		saveButton.setMinSize(150, 40);
		saveButton.setDisable(true);
		
		saveButton.setOnAction(e -> {
			boolean MapNameValidated = false;
			
			do {
				TextInputDialog MapName = new TextInputDialog();
				MapName.setTitle("Nom de la map");
				MapName.setHeaderText(null);
				MapName.setContentText("Nom:");
				
				Optional<String> nomMap = MapName.showAndWait();
				
				if(nomMap.isPresent()) {
					Map NewMap = new Map(MapComponents, Columns, Rows, nomMap.get());
					File MapFile = new File("maps/" + NewMap.GetMapPath());
					if(!MapFile.exists()) {
						NewMap.SaveMap();
						MapNameValidated = true;
						
						JOptionPane.showMessageDialog(null, "La map a bien été sauvegardé", "Map sauvegardé", JOptionPane.INFORMATION_MESSAGE);
						
						newWindow.close();
					} else {
						JOptionPane.showMessageDialog(null, "Mauvais nom de map, celui-ci est déjà utilisé", "Mauvais nom de map", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					MapNameValidated = true;
				}
			} while(!MapNameValidated);
		});
		
		controlHBox.getChildren().addAll(validateButton, saveButton);
		
		StackPane.setAlignment(controlHBox, Pos.BOTTOM_LEFT);
		controlHBox.setAlignment(Pos.BOTTOM_LEFT);
		controlHBox.setPadding(new Insets(0, 0, 10, 10));
		controlHBox.setSpacing(10);
		
		root.getChildren().add(controlHBox);
		
		MapCreatorUpdate = new Timeline(new KeyFrame(Duration.millis(100), e -> {
			if(MapCreator.MapValidated) {
				saveButton.setDisable(false);
			} else {
				saveButton.setDisable(true);
			}
			
			if(!HasPlayer() || !HasEnd()) {
				validateButton.setDisable(true);
			} else {
				validateButton.setDisable(false);
			}
		}));
		 
		MapCreatorUpdate.setCycleCount(Animation.INDEFINITE);
		MapCreatorUpdate.play();
	}
	
	private void ClickOnCell(Pane PaneClicked, int x, int y) {
		MapCreator.MapValidated = false;
		
		if(airRadio.isSelected()) {
			MapComponents[x][y] = MapComponent.AIR;
			MapPanes[x][y].setStyle(AirStyle);
		} else if(murRadio.isSelected()) {
			MapComponents[x][y] = MapComponent.WALL;
			MapPanes[x][y].setStyle(WallStyle);
		} else if(playerRadio.isSelected()) {
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
		} else if(endRadio.isSelected()) {
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
	}
	
	private void UpdateGrid(int w, int h) {
		MapCreatorGrid.getChildren().clear();
		
		MapComponent[][] NewMapComponents = InitializeMapComponents();
		Pane[][] NewMapPanes = new Pane[Columns][Rows];
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				if(i < MapComponents.length && j < MapComponents[i].length) {
					NewMapComponents[i][j] = MapComponents[i][j];
				}
			}
		}
		
		MapComponents = NewMapComponents;
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				NewMapPanes[i][j] = CreateClickPane(i, j);
			}
		}
		
		MapPanes = NewMapPanes;
		
		columnLabel.setText("Colonnes: " + w);
		rowLabel.setText("Lignes: " + h);
	}
	
	private MapComponent[][] InitializeMapComponents() {
		MapComponent[][] NewMapComponents = new MapComponent[Columns][Rows];
		
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				NewMapComponents[i][j] = MapComponent.AIR;
			}
		}
		
		return NewMapComponents;
	}
	
	private Pane CreateClickPane(int x, int y) {
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
		
		MapCreatorGrid.add(newPane, x, y);
		
		newPane.setOnMouseClicked(e -> {
			ClickOnCell(newPane, x, y);
		});
		
		return newPane;
	}
	
	private boolean HasPlayer() {
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				if(MapComponents[i][j] == MapComponent.PLAYER) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean HasEnd() {
		for(int i = 0; i < Columns; i++) {
			for(int j = 0; j < Rows; j++) {
				if(MapComponents[i][j] == MapComponent.END) {
					return true;
				}
			}
		}
		
		return false;
	}
}
