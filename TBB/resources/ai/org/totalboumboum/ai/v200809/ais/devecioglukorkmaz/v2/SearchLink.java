package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2;

/**
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
public class SearchLink {
	/** */
	private Noeud origin;
	/** */
	private Noeud target;

	/**
	 * 
	 * @param origin
	 * @param target
	 */
	public SearchLink(Noeud origin, Noeud target) {
		this.origin = origin;
		this.target = target;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public Noeud getOrigin() {
		return origin;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public Noeud getTarget() {
		return target;
	}

	@Override
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
