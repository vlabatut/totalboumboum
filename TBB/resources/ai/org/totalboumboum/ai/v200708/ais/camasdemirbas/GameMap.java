package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * Ceci tient l'état et le contexte de chaque carreau sur la carte. 
 * 
 * @author Gökhan Çamaş
 * @author İrem Demirbaş
 *
 */
public class GameMap {
	/** La largeur de carte dans les carreaux */
//	public static final int WIDTH;
	public static int WIDTH; // adjustment
	/** La hauteur de carte dans les carreaux */
//	public static final int HEIGHT = 15;	
	public static int HEIGHT;	// adjustment
	
	/** Les cadres de terrain pour chaque carreau dans la carte */
	private int[][] zoneMatrix = new int[WIDTH][HEIGHT];
	/** L'indicateur si un carreau donné a été visité pendant la recherche */
	private boolean[][] visited = new boolean[WIDTH][HEIGHT];
	
	/**
	 * créer une nouvelle carte avec la matrice de zone
	 * @param zoneMatrix 
	 * 		Description manquante !
	 */
	public GameMap(int[][] zoneMatrix) {
		this.zoneMatrix = zoneMatrix;
		WIDTH = zoneMatrix.length; // adjustment
		HEIGHT = zoneMatrix[0].length; // adjustment
	}
	
	/**
	 * Eclaircir le tableau marquant quels carreaux ont été visted par path 
	 * finder.
	 */
	public void clearVisited() {
		for (int x=0;x<getWidthInTiles();x++) {
			for (int y=0;y<getHeightInTiles();y++) {
				visited[x][y] = false;
			}
		}
	}
	
	/**
	 * @param x 
	 * 		Description manquante !
	 * @param y 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public boolean visited(int x, int y) {
		return visited[x][y];
	}
	
	/**
	 * Obtenir le terrain à un emplacement donné
	 * 
	 * @param x la coordonnée du carreau de terrain pour rapporter
	 * @param y la coordonnée du carreau de terrain pour rapporter
	 * @return Le carreau de terrain à l'emplacement donné
	 */
	public int getTerrain(int x, int y) {
		return zoneMatrix[x][y];
	}
	
	/**
	 * @param x 
	 * 		Description manquante !
	 * @param y 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public boolean blocked(int x, int y) {
		int state = getTerrain(x, y);
		if(AStarPathFinder.findPathWithSoftWall && (state==2 || state==4)) 
				return true;
		else if(!AStarPathFinder.findPathWithSoftWall && (state==1 || state==2 || state==4))
				return true;
		else 
			return false;
		}		

	/**
	 * @param sx 
	 * 		Description manquante !
	 * @param sy 
	 * 		Description manquante !
	 * @param tx 
	 * 		Description manquante !
	 * @param ty 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public float getCost(int sx, int sy, int tx, int ty) {
		return 1;
	}

	/**
	 * @return
	 * 		Description manquante !
	 * 
	 */
	public int getHeightInTiles() {
		return HEIGHT;
	}

	/**
	 * @return
	 * 		Description manquante !
	 * 
	 */
	public int getWidthInTiles() {
		return WIDTH;
	}

	/**
	 * @param x 
	 * 		Description manquante !
	 * @param y 
	 * 		Description manquante !
	 */
	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}
	
	
}
