package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.AiBomb;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
public class DurationComparator implements Comparator<AiBomb> {
	public int compare(AiBomb bombe1, AiBomb bombe2) {
		int resultat;
		long t1 = bombe1.getNormalDuration();
		long t2 = bombe2.getNormalDuration();

		if (t1 < t2)
			resultat = -1;

		else
			resultat = 1;

		return resultat;

	}

}
