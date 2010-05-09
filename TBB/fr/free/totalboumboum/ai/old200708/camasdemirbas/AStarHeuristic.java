package fr.free.totalboumboum.ai.old200708.camasdemirbas;

/**
 * @author Gokhan Camas -- Irem Demirbas
 */
public interface AStarHeuristic {

	/**
	 * Obtenir le co�t heuristique suppl�mentaire du carreau donn�.
	 * Ceci contr�le l'ordre dans lequel carreaux est cherch� 
	 * pendant que tentant de trouver un chemin � l'emplacement de cible. 
	 * Le plus bas le co�t le plus probable le carreau sera cherch�. 
	 * 
	 * @param map ZoneMatrix
	 * @param x la coordonn�e du carreau est �valu�e
	 * @param y la coordonn�e du carreau est �valu�e
	 * @param tx la coordonn�e de l'emplacement de cible
	 * @param ty la coordonn�e de l'emplacement de cible
	 * @return le co�t a associ� avec le carreau donn�
	 */
	public float getCost(GameMap map, int x, int y, int tx, int ty);
}
