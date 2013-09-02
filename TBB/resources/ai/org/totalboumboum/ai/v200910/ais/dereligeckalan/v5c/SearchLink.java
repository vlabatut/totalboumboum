package org.totalboumboum.ai.v200910.ais.dereligeckalan.v5c;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

/**
 * @author Merih Inal Dereli
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class SearchLink {
	/** */
	private Noeud origin;
	/** noeud fils */
	private Noeud target;

	/** */
	private DereliGeckalan source;

	/**
	 * Constructeur.
	 * 
	 * @param origin
	 *            noeud parent
	 * @param target
	 *            noeud fils
	 * @param source
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public SearchLink(Noeud origin, Noeud target, DereliGeckalan source)
			throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		this.source = source;
		this.origin = origin;
		this.target = target;

	}

	/**
	 * Renvoie le noeud parent du lien
	 * 
	 * @return le noeud parent
	 * @throws StopRequestException
	 *             Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public Noeud getOrigin() throws StopRequestException {
		source.checkInterruption();
		return origin;
	}

	/**
	 * Renvoie le noeud fils du lien
	 * 
	 * @return le noeud fils
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public Noeud getTarget() throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		return target;
	}

	/**
	 * Renvoie une valeur indiquant l'egalite de l'objet vrai si l'objet passé
	 * en parametre est egale à cet objet false sinon
	 * 
	 * @return result indique l'egalite
	 */
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
				result = temp.getOrigin() == getOrigin();
			} catch (StopRequestException e) {
				//
				//e.printStackTrace();
			}
		}
		return result;
	}

}
