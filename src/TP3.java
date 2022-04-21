import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TP3 extends Application {

	public static int WindowWidth = 800;
	public static int WindowHeight = 600;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("TP3 - Arène de glace");
		primaryStage.setMinHeight(WindowHeight);
		primaryStage.setMinWidth(WindowWidth);
		
		StackPane root = new StackPane();
		
		primaryStage.setScene(new Scene(root, WindowWidth, WindowHeight));
		primaryStage.show();
		
		Button boutonJouer = new Button();
		boutonJouer.setMaxSize(80, 35);
		boutonJouer.setMinSize(80, 35);
		boutonJouer.setText("Jouer");
		
		boutonJouer.setOnAction(e -> {
			new ChoixDeNiveau();
		});
		
		StackPane.setAlignment(boutonJouer, Pos.TOP_CENTER);
		
		Button boutonCree = new Button();
		boutonCree.setMaxSize(80, 35);
		boutonCree.setMinSize(80, 35);
		boutonCree.setText("Créer une carte");
		
		boutonCree.setOnAction(e -> {
			new MapCreator();
		});
		
		StackPane.setAlignment(boutonCree, Pos.CENTER);
		
		Button boutonQuitter = new Button();
		boutonQuitter.setMaxSize(80, 35);
		boutonQuitter.setMinSize(80, 35);
		boutonQuitter.setText("Quitter");
		
		boutonQuitter.setOnAction(e -> {
			System.exit(0);
		});
		
		StackPane.setAlignment(boutonQuitter, Pos.BOTTOM_CENTER);
		
		root.getChildren().addAll(boutonJouer, boutonCree, boutonQuitter);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
