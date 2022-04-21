import javafx.application.Application;
import javafx.scene.Scene;
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
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
