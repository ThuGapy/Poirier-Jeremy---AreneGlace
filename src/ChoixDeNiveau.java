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

/**
 * Classe contenu le menu de choix de niveau
 */
public class ChoixDeNiveau {
	/**
	 * Largeur de la fen�tre
	 */
	public static int WindowWidth = 400;
	
	/**
	 * Hauteur de la fen�tre
	 */
	public static int WindowHeight = 600;
	
	/**
	 * D�termine si nous devons retourner au menu principal lorsque la fen�tre est ferm�e
	 */
	public boolean MainMenuOnClose = true;
	
	/**
	 * Constructeur du menu de choix de niveau
	 */
	public ChoixDeNiveau() {
		// Valide la pr�sence d'un dossier "maps"
		CheckMapFiles();
		
		// Cr�e le menu de s�lection
		CreeMenu();
	}
	
	/**
	 * M�thode qui s'occupe de la cr�ation du menu de s�lection
	 */
	private void CreeMenu() {
		// On cr�e un nouveau Stage pour le menu de s�lection
		Stage choixNiveauMenu = new Stage();
		choixNiveauMenu.setTitle("Choix de niveau");
		choixNiveauMenu.setMinHeight(WindowHeight);
		choixNiveauMenu.setMinWidth(WindowWidth);
		choixNiveauMenu.setMaxHeight(WindowHeight * 2);
		choixNiveauMenu.setMaxWidth(WindowWidth);
		
		// Lorsqu'on ferme le menu, on retourne au menu principal si il le faut
		choixNiveauMenu.setOnHiding(e -> {
			if(MainMenuOnClose) {
				TP3.mainWindow.show();
				choixNiveauMenu.close();
			}
		});
		
		// Cr�ation de notre StackPane racine
		StackPane root = new StackPane();

		// Cr�ation de la fen�tre et on la montre
		choixNiveauMenu.setScene(new Scene(root, WindowWidth, WindowHeight));
		choixNiveauMenu.show();
		
		// On cr�e un file pour le dossier "maps"
		File dossier = new File("maps");
		
		// On valide une derni�re fois qu'il existe, pour �tre prudent
		dossier.mkdir();
		
		// On cr�e un ScrollVBox pour montrer les diff�rents niveaux
		VBox ScrollVBox = new VBox();
		
		// On met que le spacing entre les niveaux est de 10
		ScrollVBox.setSpacing(10);
		
		// On cr�e un ScrollPane pour contenir les Pane des maps
		ScrollPane ScrollMap = new ScrollPane();
		
		// On ajoute le ScrollPane au StackPane racine
		root.getChildren().add(ScrollMap);
		
		// On boucle dans les fichiers contenu dans le dossier "maps"
		for(File fichier : dossier.listFiles()) {
			// Si l'extension du fichier est .map et que le fichier n'est pas validation.map, on le traite
			if(TP3.ObtenirExtension(fichier).equals("map") && !fichier.getName().equals("validation.map")) {
				// On cr�e un StackPane pour contenir les informations de la map
				StackPane MapPane = new StackPane();
				MapPane.setStyle("-fx-border-color: black");
				MapPane.setMinSize(WindowWidth - 2, 80);
				ScrollVBox.getChildren().add(MapPane);
				
				// On lit la map
				Map LoadMap = Map.ReadMap(fichier.getName());
				
				// On cr�e un label pour le nom de la map
				Label MapNameLabel = new Label(LoadMap.GetMapName());
				MapNameLabel.setStyle("-fx-font: 24 arial;");
				
				// On aligne le label de nom
				StackPane.setMargin(MapNameLabel, new Insets(5, 0, 0, 0));
				StackPane.setAlignment(MapNameLabel, Pos.TOP_CENTER);
			
				// Cr�ation du bouton joueur
				Button MapPlayButton = new Button("Jouer");
				MapPlayButton.setMinSize(100, 30);
				
				// On aligne le bouton jouer
				StackPane.setMargin(MapPlayButton, new Insets(0, 0, 5, 0));
				StackPane.setAlignment(MapPlayButton, Pos.BOTTOM_CENTER);
				
				// On ajoute le nom de la map ainsi que le bouton jouer au StackPane de la map
				MapPane.getChildren().addAll(MapNameLabel, MapPlayButton);
				
				// Lorsqu'on appuie sur jouer, on lance la partie et on ferme le menu de s�lection
				MapPlayButton.setOnAction(e -> {
					MainMenuOnClose = false;
					choixNiveauMenu.close();
					new AreneJouer(fichier.getName(), false);
				});
			}
		}
		
		// On fait en sorte que le contenu du ScrollPane est la VBox contenu les informations des maps
		ScrollMap.setContent(ScrollVBox);
	}
	
	/**
	 * M�thode qui cr�e le dossier "maps" si il n'existe pas d�j�
	 */
	private void CheckMapFiles() {
		new File("maps").mkdir();
	}
}
