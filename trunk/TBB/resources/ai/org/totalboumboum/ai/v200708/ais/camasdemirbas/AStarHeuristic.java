package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
public interface AStarHeuristic {

	/**
	 * Obtenir le co�t heuristique supplémentaire du carreau donn�.
	 * Ceci contr�le l'ordre dans lequel carreaux est cherch� 
	 * pendant que tentant de trouver un chemin à l'emplacement de cible. 
	 * Le plus bas le co�t le plus probable le carreau sera cherch�. 
	 * 
	 * @param map ZoneMatrix
	 * @param x la coordonnée du carreau est �valu�e
	 * @param y la coordonnée du carreau est �valu�e
	 * @param tx la coordonnée de l'emplacement de cible
	 * @param ty la coordonnée de l'emplacement de cible
	 * @return le co�t a associé avec le carreau donn�
	 */
	public float getCost(GameMap map, int x, int y, int tx, int ty);
}
