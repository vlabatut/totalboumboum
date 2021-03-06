package org.totalboumboum.ai.v200809.ais.devecioglukorkmaz.v2c;

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

	/** */
	AiTile t;
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
	public AiTileComparator(AiTile t, DeveciogluKorkmaz dk)
			throws StopRequestException {

		dk.checkInterruption();
		this.t = t;
		this.dk = dk;
	}

	@Override
	public int compare(AiTile n1, AiTile n2) {
		int resultat = 0;
		try {
			if (dk.getDistance(t, n1) > dk.getDistance(t, n2))
				resultat = 1;
			else if (dk.getDistance(t, n1) < dk.getDistance(t, n2))
				resultat = -1;
		} catch (StopRequestException e) {
			//e.printStackTrace();
			throw new RuntimeException();
		}
		return resultat;
	}

}
