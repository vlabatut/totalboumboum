package org.totalboumboum.ai.v200708.ais.camasdemirbas;

/**
 * Une description d'une impl�mentation 
 * qui peut trouver un chemin d'un emplacement sur une carte de carreau 
 * � un autre a bas� l'information fournie par cette carte de carreau. 
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
public interface PathFinder {

	/**
	 * @param sx la coordonn�e de x de l'emplacement de d�but
	 * @param sy la coordonn�e de y de l'emplacement de d�but
	 * @param tx la coordonn�e de x de l'emplacement de cible
	 * @param ty la coordonn�e de y de l'emplacement de cible
	 * @return Le chemin a trouv� de commence � terminer, ou nul si aucun chemin peut �tre trouv�. 
	 */
	public Path findPath(int sx, int sy, int tx, int ty);
}
