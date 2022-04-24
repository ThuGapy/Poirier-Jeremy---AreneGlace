import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Map implements Serializable {
	private MapComponent[][] MapContent;
	private String MapName;
	private int Columns;
	private int Rows;
	
	public Map(MapComponent[][] mContent, int column, int rows, String mName) {
		MapContent = mContent;
		MapName = mName;
		
		Columns = column;
		Rows = rows;
	}
	
	public void SaveMap() {
		try {
			new File("maps").mkdir();
            FileOutputStream FileOutput = new FileOutputStream("maps/" + this.GetMapPath() + ".map");
            ObjectOutputStream MapOutput = new ObjectOutputStream(FileOutput);
            
            MapOutput.writeObject(this);
            MapOutput.close();
        } catch (Exception ex) {}
	}
	
	public static Map ReadMap(String MapPath) {
		try {
			 
            FileInputStream FileInput = new FileInputStream("maps/" + MapPath);
            ObjectInputStream MapInput = new ObjectInputStream(FileInput);
 
            Object obj = MapInput.readObject();
            MapInput.close();
            
            return (Map) obj;
 
        } catch(Exception e) {e.printStackTrace(); return null;}
	}
	
	public String GetMapPath() {
		return MapName.replace(' ', '_');
	}
	
	public String GetMapName() {
		return this.MapName;
	}
	
	public MapComponent[][] GetMapContent() {
		return this.MapContent;
	}
	
	public int GetColumns() {
		return this.Columns;
	}
	
	public int GetRows() {
		return this.Rows;
	}
}
