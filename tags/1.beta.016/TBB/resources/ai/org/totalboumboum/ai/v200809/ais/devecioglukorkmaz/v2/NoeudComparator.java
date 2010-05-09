package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.StopRequestException;


public class NoeudComparator implements Comparator<Noeud> {

	private Noeud t;
	DeveciogluKorkmaz dk;

	public NoeudComparator(Noeud t, DeveciogluKorkmaz dk)
			throws StopRequestException {
		this.t = t;
		dk.checkInterruption();
		this.dk = dk;
	}

	public int compare(Noeud n1, Noeud n2) {
		try {
			dk.checkInterruption();
		} catch (StopRequestException e1) {
			e1.printStackTrace();
		}
		int resultat = 0;
		try {
			if (dk.getDistance(t.getTile(), n1.getTile()) > dk.getDistance(t
					.getTile(), n2.getTile()))
				resultat = 1;
			else if (dk.getDistance(t.getTile(), n1.getTile()) < dk
					.getDistance(t.getTile(), n2.getTile()))
				resultat = -1;
		} catch (StopRequestException e) {
			e.printStackTrace();
		}
		return resultat;
	}

}
