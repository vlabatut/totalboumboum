package fr.free.totalboumboum.ai.old200708.camasdemirbas;

/**
 * Un heuristique que conduit la recherche a bas� la distance de Manhattan
 *  entre l'emplacement actuel et le cible
 * 
 * @author Gokhan Camas -- Irem Demirbas
 */
public class ManhattanHeuristic implements AStarHeuristic {
	/** Le co�t minimum de mouvement de n'importe quel un carr� � l'apr�s */
	private int minimumCost;
	
	/**
	 * Cr�er un nouvel heuristique 
	 * 
	 * @param minimumCost The le co�t minimum de mouvement de n'importe quel un carr� � l'apr�s
	 */
	protected ManhattanHeuristic(int minimumCost) {
		this.minimumCost = minimumCost;
	}
	
	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(GameMap map, int x, int y, int tx,
			int ty) {
		return minimumCost * (Math.abs(x-tx) + Math.abs(y-ty));
	}

}
