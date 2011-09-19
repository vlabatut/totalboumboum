package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * Une description d'une implément 
 * qui peut trouver un chemin d'un emplacement sur une carte de carreau 
 * à un autre a basé l'information fournie par cette carte de carreau. 
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
public interface PathFinder {

	/**
	 * @param sx la coordonnée de x de l'emplacement de début
	 * @param sy la coordonnée de y de l'emplacement de début
	 * @param tx la coordonnée de x de l'emplacement de cible
	 * @param ty la coordonnée de y de l'emplacement de cible
	 * @return Le chemin a trouvé de commence à terminer, ou nul si aucun chemin peut être trouvé. 
	 */
	public Path findPath(int sx, int sy, int tx, int ty);
}
