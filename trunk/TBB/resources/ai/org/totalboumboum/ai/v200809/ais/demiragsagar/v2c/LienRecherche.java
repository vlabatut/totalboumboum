package org.totalboumboum.ai.v200809.ais.demiragsagar.v2c;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class LienRecherche {
	// noeud parent
	private Node origin;
	// noeud fils
	private Node target;
	ArtificialIntelligence ai;
	
	/**
	 * Constructeur.
	 * 
	 * @param origin noeud parent
	 * @param target noeud fils
	 * @param action action de transition
	 * @throws StopRequestException 
	 */
	public LienRecherche(Node origin, Node target, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.origin = origin;
		this.target = target;
	}

	/**
	 * Renvoie le noeud parent du lien
	 * 
	 * @return le noeud parent
	 * @throws StopRequestException 
	 */
	public Node getOrigin() throws StopRequestException {
		ai.checkInterruption();
		return origin;
	}

	/**
	 * Renvoie le noeud fils du lien
	 * 
	 * @return le noeud fils
	 * @throws StopRequestException 
	 */
	public Node getTarget() throws StopRequestException {
		ai.checkInterruption();
		return target;
	}

	/**
	 * Renvoie l'action de transition
	 * 
	 * @return l'action associée au lien
	 */

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object == null)
			result = false;
		else if (!(object instanceof LienRecherche))
			result = false;
		else {
			LienRecherche temp = (LienRecherche) object;
			try {
				result = temp.getOrigin() == getOrigin()
						&& temp.getTarget() == getTarget();
			} catch (StopRequestException e) {
				// 
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String toString() {
		String result = "[";
		try {
			result = result + getOrigin().toString();
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		result = result + "] )->";
		result = result + "[";
		try {
			result = result + getTarget().toString();
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		result = result + "]";
		return result;
	}
}
