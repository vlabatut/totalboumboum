package org.totalboumboum.ai.v200708.ais.camasdemirbas;

import java.util.ArrayList;
import java.util.List;

/**
 * Un chemin résolu par quelque algorithme de conclusion de chemin. 
 * Un feuilleton d'étapes de l'emplacement commençant à l'emplacement de cible. 
 * Ceci inclut une étape pour l'emplacement initial. 
 * 
 * @author Gökhan Çamaş
 * @author İrem Demirbaş
 *
 */
@SuppressWarnings("unchecked")
public class Path {
	/** La liste d'étapes développant ce chemin */
	@SuppressWarnings("rawtypes")
	private List steps = new ArrayList();
	
	/**
	 * créer un chemin vide
	 */
	public Path() {
		
	}

	/**
	 * Obtenir la longueur du chemin, c.-à-d. le nombre d'étapes
	 * 
	 * @return Le nombre d'étapes dans ce chemin
	 */
	public int getLength() {
		return steps.size();
	}
	
	/**
	 * Obtenir l'étape à un index donné dans le chemin
	 * 
	 * @param index L'index de l'étape pour rapporter.
	 * @return L'information d'étape, la position sur la carte
	 */
	public Step getStep(int index) {
		return (Step) steps.get(index);
	}
	
	/**
	 * Obtenir la coordonnée de x pour l'étape à l'index donné
	 * @param index 
	 * 		L'index de l'étape dont x la coordonnée devrait être rapportée
	 * @return x coordonnée à l'étape
	 */
	public int getX(int index) {
		return getStep(index).x;
	}

	/**
	 * Obtenir  la coordonnée de y pour l'étape à l'index donné
	 * 
	 * @param  index l'index de l'étape dont y la coordonnée devrait être rapportée
	 * @return y
	 */
	public int getY(int index) {
		return getStep(index).y;
	}
	
	/**
	 * Ajouter une étape au chemin.  
	 * 
	 * @param x  la coordonnéede x de la nouvelle étape
	 * @param y la coordonnéede y de la nouvelle étape
	 */
	public void appendStep(int x, int y) {
		steps.add(new Step(x,y));
	}

	/**
	 * Prepend une étape au chemin.   
	 * 
	 * @param x la coordonnéede x de la nouvelle étape
	 * @param y la coordonnéede y de la nouvelle étape
	 */
	public void prependStep(int x, int y) {
		steps.add(0, new Step(x, y));
	}
	
	/**
	 * Le Contrôle si ce chemin contient l'étape donnée
	 * 
	 * @param x The x coordinate of the step to check for
	 * @param y The y coordinate of the step to check for
	 * @return True if the path contains the given step
	 */
	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}
	
	/**
	 * Une étape seule dans le chemin
	 * 
	 * @author Gokhan Camas -- Irem Demirbas
	 */
	public class Step {
		private int x;
		private int y;

		/**
		 * 
		 * @param x
		 * @param y
		 */
		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * 
	 * @return
	 * 		? 
		 */
		public int getX() {
			return x;
		}

		/**
		 * 
	 * @return
	 * 		? 
		 */
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
