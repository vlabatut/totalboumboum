package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * Une description d'une implément 
 * qui peut trouver un chemin d'un emplacement sur une carte de carreau 
 * à un autre a bas� l'information fournie par cette carte de carreau. 
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
public interface PathFinder {

	/**
	 * @param sx la coordonnée de x de l'emplacement de d�but
	 * @param sy la coordonnée de y de l'emplacement de d�but
	 * @param tx la coordonnée de x de l'emplacement de cible
	 * @param ty la coordonnée de y de l'emplacement de cible
	 * @return Le chemin a trouv� de commence à terminer, ou nul si aucun chemin peut être trouv�. 
	 */
	public Path findPath(int sx, int sy, int tx, int ty);
}
