import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChoixDeNiveau {
	public static int WindowWidth = 400;
	public static int WindowHeight = 600;
	
	public boolean MainMenuOnClose = true;
	
	public ChoixDeNiveau() {
		CheckMapFiles();
		CreeMenu();
	}
	
	private void CreeMenu() {
		Stage newWindow = new Stage();
		newWindow.setTitle("Choix de niveau");
		
		newWindow.setOnHiding(e -> {
			if(MainMenuOnClose) {
				TP3.mainWindow.show();
				newWindow.close();
			}
		});
		
		StackPane root = new StackPane();

		newWindow.setScene(new Scene(root, WindowWidth, WindowHeight));
		newWindow.show();
		
		File dossier = new File("maps");
		dossier.mkdir();
		
		VBox ScrollVBox = new VBox();
		ScrollVBox.setSpacing(10);
		
		ScrollPane ScrollMap = new ScrollPane();
		
		root.getChildren().add(ScrollMap);
		
		for(File fichier : dossier.listFiles()) {
			if(TP3.ObtenirExtension(fichier).equals("map") && !fichier.getName().equals("validation.map")) {
				StackPane MapPane = new StackPane();
				MapPane.setStyle("-fx-border-color: black");
				MapPane.setMinSize(WindowWidth - 2, 80);
				ScrollVBox.getChildren().add(MapPane);
				
				Map LoadMap = Map.ReadMap(fichier.getName());
				
				Label MapNameLabel = new Label(LoadMap.GetMapName());
				MapNameLabel.setStyle("-fx-font: 24 arial;");
				
				StackPane.setMargin(MapNameLabel, new Insets(5, 0, 0, 0));
				StackPane.setAlignment(MapNameLabel, Pos.TOP_CENTER);
				
				MapPane.getChildren().add(MapNameLabel);
			
				Button MapPlayButton = new Button("Jouer");
				MapPlayButton.setMinSize(100, 30);
				
				MapPane.getChildren().add(MapPlayButton);
				
				StackPane.setMargin(MapPlayButton, new Insets(0, 0, 5, 0));
				StackPane.setAlignment(MapPlayButton, Pos.BOTTOM_CENTER);
				
				MapPlayButton.setOnAction(e -> {
					MainMenuOnClose = false;
					newWindow.close();
					new AreneJouer(fichier.getName(), false);
				});
			}
		}
		
		ScrollMap.setContent(ScrollVBox);
	}
	
	private void CheckMapFiles() {
		new File("maps").mkdir();
	}
}
