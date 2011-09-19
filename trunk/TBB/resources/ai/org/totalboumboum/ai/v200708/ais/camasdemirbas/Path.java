package org.totalboumboum.ai.v200708.ais.camasdemirbas;

import java.util.ArrayList;
import java.util.List;

/**
 * Un chemin r�solu par quelque algorithme de conclusion de chemin. 
 * Un feuilleton d'�tapes de l'emplacement commen�ant à l'emplacement de cible. 
 * Ceci inclut une �tape pour l'emplacement initial. 
 * 
 * @author Gokhan Camas
 * @author Irem Demirbas
 *
 */
@SuppressWarnings("unchecked")
public class Path {
	/** La liste d'�tapes d�veloppant ce chemin */
	@SuppressWarnings("rawtypes")
	private List steps = new ArrayList();
	
	/**
	 * créer un chemin vide
	 */
	public Path() {
		
	}

	/**
	 * Obtenir la longueur du chemin, c.-�-d. le nombre d'�tapes
	 * 
	 * @return Le nombre d'�tapes dans ce chemin
	 */
	public int getLength() {
		return steps.size();
	}
	
	/**
	 * Obtenir l'�tape à un index donn� dans le chemin
	 * 
	 * @param index L'index de l'�tape pour rapporter.
	 * @return L'information d'�tape, la position sur la carte
	 */
	public Step getStep(int index) {
		return (Step) steps.get(index);
	}
	
	/**
	 * Obtenir la coordonnée de x pour l'�tape à l'index donn�
	 * 
	 * @param L'index de l'�tape dont x la coordonnée devrait �tre rapport�e
	 * @return x coordonnée à l'�tape
	 */
	public int getX(int index) {
		return getStep(index).x;
	}

	/**
	 * Obtenir  la coordonnée de y pour l'�tape à l'index donn�
	 * 
	 * @param l'index de l'�tape dont y la coordonnée devrait �tre rapport�e
	 * @return y
	 */
	public int getY(int index) {
		return getStep(index).y;
	}
	
	/**
	 * Ajouter une �tape au chemin.  
	 * 
	 * @param x  la coordonnéede x de la nouvelle �tape
	 * @param y la coordonnéede y de la nouvelle �tape
	 */
	public void appendStep(int x, int y) {
		steps.add(new Step(x,y));
	}

	/**
	 * Prepend une �tape au chemin.   
	 * 
	 * @param x la coordonnéede x de la nouvelle �tape
	 * @param y la coordonnéede y de la nouvelle �tape
	 */
	public void prependStep(int x, int y) {
		steps.add(0, new Step(x, y));
	}
	
	/**
	 * Le contr�le si ce chemin contient l'�tape donn�e
	 * 
	 * @param x The x coordinate of the step to check for
	 * @param y The y coordinate of the step to check for
	 * @return True if the path contains the given step
	 */
	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}
	
	/**
	 * Une �tape seule dans le chemin
	 * 
	 * @author Gokhan Camas -- Irem Demirbas
	 */
	public class Step {
		private int x;
		private int y;

		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}
		

		public int getX() {
			return x;
		}


		public int getY() {
			return y;
		}
		

		public int hashCode() {
			return x*y;
		}

		public boolean equals(Object other) {
			if (other instanceof Step) {
				Step o = (Step) other;
				
				return (o.x == x) && (o.y == y);
			}
			
			return false;
		}
	}
}
