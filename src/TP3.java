import java.io.File;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TP3 extends Application {

	public static int WindowWidth = 400;
	public static int WindowHeight = 300;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TP3 - Arène de glace");
		primaryStage.setMinHeight(WindowHeight);
		primaryStage.setMinWidth(WindowWidth);
		primaryStage.setMaxHeight(WindowHeight * 2);
		primaryStage.setMaxWidth(WindowWidth * 2);
		
		StackPane root = new StackPane();
		
		primaryStage.setScene(new Scene(root, WindowWidth, WindowHeight));
		primaryStage.show();
		
		VBox buttonVBox = new VBox();
		
		Button boutonJouer = new Button();
		boutonJouer.setMaxSize(80, 35);
		boutonJouer.setMinSize(80, 35);
		boutonJouer.setText("Jouer");
		
		boutonJouer.setOnAction(e -> {
			new ChoixDeNiveau();
		});
		
		Button boutonCree = new Button();
		boutonCree.setMaxSize(130, 35);
		boutonCree.setMinSize(130, 35);
		boutonCree.setText("Créer une carte");
		
		boutonCree.setOnAction(e -> {
			new MapCreator();
		});
		
		Button boutonQuitter = new Button();
		boutonQuitter.setMaxSize(80, 35);
		boutonQuitter.setMinSize(80, 35);
		boutonQuitter.setText("Quitter");
		
		boutonQuitter.setOnAction(e -> {
			System.exit(0);
		});
		
		buttonVBox.setSpacing(10);
		
		buttonVBox.getChildren().addAll(boutonJouer, boutonCree, boutonQuitter);
		
		buttonVBox.setAlignment(Pos.CENTER);
		
		root.getChildren().add(buttonVBox);
	}
	
	public static String ObtenirExtension(File fichier) {
		String nomFichier = fichier.getName();
		return nomFichier.substring(nomFichier.lastIndexOf('.') + 1);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
