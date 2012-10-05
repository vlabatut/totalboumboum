package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Eser Devecioğlu
 * @author lev Korkmaz
 *
 */
@SuppressWarnings("deprecation")
public class AiTileComparator implements Comparator<AiTile> {

	AiTile t;
	DeveciogluKorkmaz dk;

	/**
	 * 
	 * @param t
	 * @param dk
	 * @throws StopRequestException
	 */
	public AiTileComparator(AiTile t, DeveciogluKorkmaz dk)
			throws StopRequestException {

		this.t = t;
		dk.checkInterruption();
		this.dk = dk;
	}

	public int compare(AiTile n1, AiTile n2) {
		try {
			dk.checkInterruption();
		} catch (StopRequestException e1) {
			e1.printStackTrace();
		}
		int resultat = 0;
		try {
			if (dk.getDistance(t, n1) > dk.getDistance(t, n2))
				resultat = 1;
			else if (dk.getDistance(t, n1) < dk.getDistance(t, n2))
				resultat = -1;
		} catch (StopRequestException e) {
			e.printStackTrace();
		}
		return resultat;
	}

}
