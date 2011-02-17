package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2;

/**
 * 
 * @author Eser Devecioglu
 * @author Alev Korkmaz
 *
 */
public class SearchLink {
	private Noeud origin;
	private Noeud target;

	public SearchLink(Noeud origin, Noeud target) {
		this.origin = origin;
		this.target = target;
	}

	public Noeud getOrigin() {
		return origin;
	}

	public Noeud getTarget() {
		return target;
	}

	public boolean equals(Object object) {
		boolean result;
		if (object == null)
			result = false;
		else if (!(object instanceof SearchLink))
			result = false;
		else {
			SearchLink temp = (SearchLink) object;
			result = temp.getOrigin() == getOrigin()
					&& temp.getTarget() == getTarget();
		}
		return result;
	}
}
