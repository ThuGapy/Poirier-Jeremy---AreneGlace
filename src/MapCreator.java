import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MapCreator {
	public static int WindowWidth = 800;
	public static int WindowHeight = 600;
	
	public MapCreator() {
		CreeMenu();
	}
	
	public void CreeMenu() {
		Stage newWindow = new Stage();
		newWindow.setTitle("Créateur de map");
		
		StackPane root = new StackPane();

		newWindow.setScene(new Scene(root, WindowWidth, WindowHeight));
		newWindow.show();
	}
}
