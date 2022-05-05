import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe Map. Correspond au information d'une map jouable ainsi que divers méthode pour gérer la sauvegarde et le chargement
 */
@SuppressWarnings("serial")
public class Map implements Serializable {
	/**
	 * Contenu de la map
	 */
	private MapComponent[][] MapContent;
	
	/**
	 * Nom de la map
	 */
	private String MapName;
	
	/**
	 * Nombre de colonnes de la map
	 */
	private int Columns;
	
	/**
	 * Nombre de lignes de la map
	 */
	private int Rows;
	
	/**
	 * Constructeur de la map
	 * @param mContent Contenu de la map, correspond à un tableau d'enum de MapComponent
	 * @param column Nombre de colonnes
	 * @param rows Nombre de lignes
	 * @param mName Nom de la map, aussi utilisé pour le nom du fichier
	 */
	public Map(MapComponent[][] mContent, int column, int rows, String mName) {
		MapContent = mContent;
		MapName = mName;
		
		Columns = column;
		Rows = rows;
	}
	
	/**
	 * Méthode qui sert à sauvegarder la map de façon permanente
	 */
	public void SaveMap() {
		try {
			// Crée le dossier "maps" si il n'existe pas
			new File("maps").mkdir();
			
			// Sauvegarder la map sur le disque dur
            FileOutputStream FileOutput = new FileOutputStream("maps/" + this.GetMapPath() + ".map");
            ObjectOutputStream MapOutput = new ObjectOutputStream(FileOutput);
            
            MapOutput.writeObject(this);
            MapOutput.close();
        } catch (Exception ex) {}
	}
	
	/**
	 * Méthode qui s'occupe de charger une map
	 * @param MapPath Chemin de la carte
	 * @return La map
	 */
	public static Map ReadMap(String MapPath) {
		try {
			 // Lit la map
            FileInputStream FileInput = new FileInputStream("maps/" + MapPath);
            ObjectInputStream MapInput = new ObjectInputStream(FileInput);
 
            Object obj = MapInput.readObject();
            MapInput.close();
            
            // Retourne l'objet en tant que map
            return (Map) obj;
 
        } catch(Exception e) {e.printStackTrace(); return null;}
	}
	
	/**
	 * Méthode pour obtenir le nom de la map pour l'écriture du fichier. Remplace les espaces par des _
	 * @return Le nom de la map pour l'écriture du fichier
	 */
	public String GetMapPath() {
		return MapName.replace(' ', '_');
	}
	
	/**
	 * Méthode pour obtenir le nom de la map
	 * @return Le nom de la map
	 */
	public String GetMapName() {
		return this.MapName;
	}
	
	/**
	 * Méthode pour obtenir les composants de la map
	 * @return Les composants de la map
	 */
	public MapComponent[][] GetMapContent() {
		return this.MapContent;
	}
	
	/**
	 * Méthode pour obtenir le nombre de colonnes de la map
	 * @return Le nombre de colonnes de la map
	 */
	public int GetColumns() {
		return this.Columns;
	}
	
	/**
	 * Méthode pour obtenir le nombre de lignes de la map
	 * @return Le nombre de lignes de la map
	 */
	public int GetRows() {
		return this.Rows;
	}
}
