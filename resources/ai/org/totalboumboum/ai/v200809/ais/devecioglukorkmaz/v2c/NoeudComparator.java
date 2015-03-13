package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2c;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
@SuppressWarnings("deprecation")
public class NoeudComparator implements Comparator<Noeud> {

	/** */
	private Noeud t;
	/** */
	DeveciogluKorkmaz dk;

	/**
	 * 
	 * @param t
	 * 		Description manquante !
	 * @param dk
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public NoeudComparator(Noeud t, DeveciogluKorkmaz dk)
			throws StopRequestException {
		dk.checkInterruption();
		this.t = t;
		this.dk = dk;
	}

	@Override
	public int compare(Noeud n1, Noeud n2) {
		//dk.checkInterruption();
		int resultat = 0;
		try {
			if (dk.getDistance(t.getTile(), n1.getTile()) > dk.getDistance(t
					.getTile(), n2.getTile()))
				resultat = 1;
			else if (dk.getDistance(t.getTile(), n1.getTile()) < dk
					.getDistance(t.getTile(), n2.getTile()))
				resultat = -1;
		} catch (StopRequestException e) {
			//e.printStackTrace();
			throw new RuntimeException();
		}
		return resultat;
	}

}
