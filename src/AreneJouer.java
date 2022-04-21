import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AreneJouer {
	public static int WindowWidth = 800;
	public static int WindowHeight = 600;
	
	public AreneJouer(String MapFile) {
		CreeMenu(MapFile, false);
	}
	
	public AreneJouer(String MapFile, boolean IsTesting) {
		CreeMenu(MapFile, IsTesting);
	}
	
	public void CreeMenu(String MapFile, boolean IsTesting) {
		Stage newWindow = new Stage();
		newWindow.setTitle("Arene de glace");
		
		StackPane root = new StackPane();

		newWindow.setScene(new Scene(root, WindowWidth, WindowHeight));
		newWindow.show();
	}
}
