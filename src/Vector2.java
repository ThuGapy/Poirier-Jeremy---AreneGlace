/**
 * Classe Vector2 qui correspond � une position 2D (x, y)
 */
public class Vector2 {
	/**
	 * Int, correspond � la position horizontale
	 */
	public int x;
	
	/**
	 * Int, correspond � la position verticale
	 */
	public int y;
	
	/**
	 * Constructeur de Vector2
	 * @param xPos Position horizontale
	 * @param yPos Position verticale
	 */
	public Vector2(int xPos, int yPos) {
		x = xPos;
		y = yPos;
	}
	
	/**
	 * M�thode qui retourne la position horizontale
	 * @return La position horizontale
	 */
	public int GetX() {
		return this.x;
	}
	
	/**
	 * M�thode qui retourne la position verticale
	 * @return La position verticale
	 */
	public int GetY() {
		return this.y;
	}
}
