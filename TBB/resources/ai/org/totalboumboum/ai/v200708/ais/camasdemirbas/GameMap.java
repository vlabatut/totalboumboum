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
	public static final int WIDTH = 17;
	/** La hauteur de carte dans les carreaux */
	public static final int HEIGHT = 15;	
	/** Les cadres de terrain pour chaque carreau dans la carte */
	private int[][] zoneMatrix = new int[WIDTH][HEIGHT];
	/** L'indicateur si un carreau donné a été visité pendant la recherche */
	private boolean[][] visited = new boolean[WIDTH][HEIGHT];
	
	/**
	 * créer une nouvelle carte avec la matrice de zone
	 * @param zoneMatrix 
	 */
	public GameMap(int[][] zoneMatrix) {
		this.zoneMatrix = zoneMatrix;
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
	 * @param y 
	 * @return
	 * 		? 
	 * 
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
	 * @param y 
	 * @return
	 * 		? 
	 * 
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
	 * @param sy 
	 * @param tx 
	 * @param ty 
	 * @return
	 * 		? 
	 * 
	 */
	public float getCost(int sx, int sy, int tx, int ty) {
		return 1;
	}

	/**
	 * @return
	 * 		? 
	 * 
	 */
	public int getHeightInTiles() {
		return HEIGHT;
	}

	/**
	 * @return
	 * 		? 
	 * 
	 */
	public int getWidthInTiles() {
		return WIDTH;
	}

	/**
	 * @param x 
	 * @param y 
	 * 
	 */
	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}
	
	
}
