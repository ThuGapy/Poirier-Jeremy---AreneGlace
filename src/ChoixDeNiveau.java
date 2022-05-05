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
	 * Largeur de la fenêtre
	 */
	public static int WindowWidth = 400;
	
	/**
	 * Hauteur de la fenêtre
	 */
	public static int WindowHeight = 600;
	
	/**
	 * Détermine si nous devons retourner au menu principal lorsque la fenêtre est fermée
	 */
	public boolean MainMenuOnClose = true;
	
	/**
	 * Constructeur du menu de choix de niveau
	 */
	public ChoixDeNiveau() {
		// Valide la présence d'un dossier "maps"
		CheckMapFiles();
		
		// Crée le menu de sélection
		CreeMenu();
	}
	
	/**
	 * Méthode qui s'occupe de la création du menu de sélection
	 */
	private void CreeMenu() {
		// On crée un nouveau Stage pour le menu de sélection
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
		
		// Création de notre StackPane racine
		StackPane root = new StackPane();

		// Création de la fenêtre et on la montre
		choixNiveauMenu.setScene(new Scene(root, WindowWidth, WindowHeight));
		choixNiveauMenu.show();
		
		// On crée un file pour le dossier "maps"
		File dossier = new File("maps");
		
		// On valide une dernière fois qu'il existe, pour être prudent
		dossier.mkdir();
		
		// On crée un ScrollVBox pour montrer les différents niveaux
		VBox ScrollVBox = new VBox();
		
		// On met que le spacing entre les niveaux est de 10
		ScrollVBox.setSpacing(10);
		
		// On crée un ScrollPane pour contenir les Pane des maps
		ScrollPane ScrollMap = new ScrollPane();
		
		// On ajoute le ScrollPane au StackPane racine
		root.getChildren().add(ScrollMap);
		
		// On boucle dans les fichiers contenu dans le dossier "maps"
		for(File fichier : dossier.listFiles()) {
			// Si l'extension du fichier est .map et que le fichier n'est pas validation.map, on le traite
			if(TP3.ObtenirExtension(fichier).equals("map") && !fichier.getName().equals("validation.map")) {
				// On crée un StackPane pour contenir les informations de la map
				StackPane MapPane = new StackPane();
				MapPane.setStyle("-fx-border-color: black");
				MapPane.setMinSize(WindowWidth - 2, 80);
				ScrollVBox.getChildren().add(MapPane);
				
				// On lit la map
				Map LoadMap = Map.ReadMap(fichier.getName());
				
				// On crée un label pour le nom de la map
				Label MapNameLabel = new Label(LoadMap.GetMapName());
				MapNameLabel.setStyle("-fx-font: 24 arial;");
				
				// On aligne le label de nom
				StackPane.setMargin(MapNameLabel, new Insets(5, 0, 0, 0));
				StackPane.setAlignment(MapNameLabel, Pos.TOP_CENTER);
			
				// Création du bouton joueur
				Button MapPlayButton = new Button("Jouer");
				MapPlayButton.setMinSize(100, 30);
				
				// On aligne le bouton jouer
				StackPane.setMargin(MapPlayButton, new Insets(0, 0, 5, 0));
				StackPane.setAlignment(MapPlayButton, Pos.BOTTOM_CENTER);
				
				// On ajoute le nom de la map ainsi que le bouton jouer au StackPane de la map
				MapPane.getChildren().addAll(MapNameLabel, MapPlayButton);
				
				// Lorsqu'on appuie sur jouer, on lance la partie et on ferme le menu de sélection
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
	 * Méthode qui crée le dossier "maps" si il n'existe pas déjà
	 */
	private void CheckMapFiles() {
		new File("maps").mkdir();
	}
}
