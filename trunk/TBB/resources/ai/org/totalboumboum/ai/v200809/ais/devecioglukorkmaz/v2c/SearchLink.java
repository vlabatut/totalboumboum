package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2c;

import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
@SuppressWarnings("deprecation")
public class SearchLink {
	/** */
	private Noeud origin;
	/** */
	private Noeud target;
	/** */
	ArtificialIntelligence ai;

	/**
	 * 
	 * @param origin
	 * 		Description manquante !
	 * @param target
	 * 		Description manquante !
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public SearchLink(Noeud origin, Noeud target, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.origin = origin;
		this.target = target;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Noeud getOrigin() throws StopRequestException {
		ai.checkInterruption();
		return origin;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Noeud getTarget() throws StopRequestException {
		ai.checkInterruption();
		return target;
	}

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object == null)
			result = false;
		else if (!(object instanceof SearchLink))
			result = false;
		else {
			SearchLink temp = (SearchLink) object;
			try {
				result = temp.getOrigin() == getOrigin()
						&& temp.getTarget() == getTarget();
			} catch (StopRequestException e) {
				// 
				//e.printStackTrace();
			}
		}
		return result;
	}
}
