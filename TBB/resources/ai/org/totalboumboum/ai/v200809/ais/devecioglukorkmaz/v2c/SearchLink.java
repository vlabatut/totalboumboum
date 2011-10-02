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
	private Noeud origin;
	private Noeud target;
	ArtificialIntelligence ai;

	public SearchLink(Noeud origin, Noeud target, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.origin = origin;
		this.target = target;
	}

	public Noeud getOrigin() throws StopRequestException {
		ai.checkInterruption();
		return origin;
	}

	public Noeud getTarget() throws StopRequestException {
		ai.checkInterruption();
		return target;
	}

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
				e.printStackTrace();
			}
		}
		return result;
	}
}
