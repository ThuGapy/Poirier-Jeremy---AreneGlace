import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe contenant le programme JavaFX
 */
public class TP3 extends Application {

	/**
	 * Largeur de la fenêtre
	 */
	public static int WindowWidth = 400;
	
	/**
	 * Hauteur de la fenêtre
	 */
	public static int WindowHeight = 300;
	
	/**
	 * Fenêtre principale de l'application
	 */
	public static Stage mainWindow;
	
	/**
	 * Méthode executé au lancement de l'application
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// On stocke la fenêtre principale de l'application
		mainWindow = primaryStage;
		
		// On met un titre et la grandeur de la fenêtre
		primaryStage.setTitle("TP3 - Arène de glace");
		primaryStage.setMinHeight(WindowHeight);
		primaryStage.setMinWidth(WindowWidth);
		primaryStage.setMaxHeight(WindowHeight * 2);
		primaryStage.setMaxWidth(WindowWidth * 2);
		
		// On crée notre StackPane racine
		StackPane root = new StackPane();
		
		// On crée notre fenêtre et on la montre
		primaryStage.setScene(new Scene(root, WindowWidth, WindowHeight));
		primaryStage.show();
		
		// On crée une VBox pour l'affichage
		VBox buttonVBox = new VBox();
		
		// Création du bouton jouer
		Button boutonJouer = new Button();
		boutonJouer.setMaxSize(80, 35);
		boutonJouer.setMinSize(80, 35);
		boutonJouer.setText("Jouer");
		
		// Lorsqu'on appuie sur jouer, on cache le menu principal et on montre le menu de sélection de niveaux
		boutonJouer.setOnAction(e -> {
			mainWindow.hide();
			new ChoixDeNiveau();
		});
		
		// Création du bouton créer
		Button boutonCree = new Button();
		boutonCree.setMaxSize(130, 35);
		boutonCree.setMinSize(130, 35);
		boutonCree.setText("Créer une carte");
		
		// Lorsqu'on appuie sur créer, on cache le menu principal et on montre l'éditeur de niveau
		boutonCree.setOnAction(e -> {
			mainWindow.hide();
			new MapCreator();
		});
		
		// Création du bouton quitter
		Button boutonQuitter = new Button();
		boutonQuitter.setMaxSize(80, 35);
		boutonQuitter.setMinSize(80, 35);
		boutonQuitter.setText("Quitter");
		
		// Lorsqu'on appuie sur quitter, on ferme l'application
		boutonQuitter.setOnAction(e -> {
			System.exit(0);
		});
		
		// On espace les boutons de 10
		buttonVBox.setSpacing(10);
		
		// On ajoute les boutons à la VBox
		buttonVBox.getChildren().addAll(boutonJouer, boutonCree, boutonQuitter);
		
		// On centre le contenu de la VBox au milieu
		buttonVBox.setAlignment(Pos.CENTER);
		
		// On ajoute la VBox au StackPane racine
		root.getChildren().add(buttonVBox);
	}
	
	/**
	 * Méthode qui sort à obtenir l'extension d'un fichier
	 * @param fichier Fichier qu'on veut obtenir l'extension
	 * @return L'extension du fichier (sans le point)
	 */
	public static String ObtenirExtension(File fichier) {
		// Obtient le nom du fichier
		String nomFichier = fichier.getName();
		
		// On retourne ce qui a après le dernier point
		return nomFichier.substring(nomFichier.lastIndexOf('.') + 1);
	}
	
	/**
	 * Méthode qui lance le programme JavaFX
	 * @param args Arguments de lancement
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Constructeur de base de la classe
	 */
	public TP3() {}
}
