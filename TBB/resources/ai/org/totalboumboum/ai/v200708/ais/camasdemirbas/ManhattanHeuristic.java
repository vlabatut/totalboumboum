package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * Un heuristique que conduit la recherche a bas� la distance de Manhattan
 *  entre l'emplacement actuel et le cible
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
public class ManhattanHeuristic implements AStarHeuristic {
	/** Le coût minimum de mouvement de n'importe quel un carr� à l'apr�s */
	private int minimumCost;
	
	/**
	 * créer un nouvel heuristique 
	 * 
	 * @param minimumCost The le coût minimum de mouvement de n'importe quel un carr� à l'apr�s
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
