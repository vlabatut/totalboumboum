package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
public interface AStarHeuristic {

	/**
	 * Obtenir le coût heuristique supplémentaire du carreau donné.
	 * Ceci Contrôle l'ordre dans lequel carreaux est cherché 
	 * pendant que tentant de trouver un chemin à l'emplacement de cible. 
	 * Le plus bas le coût le plus probable le carreau sera cherché. 
	 * 
	 * @param map ZoneMatrix
	 * @param x la coordonnée du carreau est évaluée
	 * @param y la coordonnée du carreau est évaluée
	 * @param tx la coordonnée de l'emplacement de cible
	 * @param ty la coordonnée de l'emplacement de cible
	 * @return le coût a associé avec le carreau donné
	 */
	public float getCost(GameMap map, int x, int y, int tx, int ty);
}
