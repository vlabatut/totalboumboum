package org.totalboumboum.ai.v200809.ais.akpolatsener.v2.comparators;

import java.util.Comparator;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.akpolatsener.v2.AkpolatSener;
import org.totalboumboum.ai.v200809.ais.akpolatsener.v2.Danger;
import org.totalboumboum.ai.v200809.ais.akpolatsener.v2.Target;


/**
 * la classe comparateur d'apres les distances directes aux dangers ou aux
 * cibles
 * 
 * @author Cem Akpolat
 * @author Emre Şener
 *
 */
public class ComparatorByHypotenuse implements Comparator<AiTile> {
	/** la classe principale de notre IA */
	AkpolatSener as;

	/** objet cible/danger pour les comparaisons */
	Target target;
	Danger danger;

	/**
	 * pour que la comparaison soit d'apres d'une danger
	 * 
	 * @param as
	 * @param danger
	 */
	public void addDanger(AkpolatSener as, Danger danger) {
		this.as = as;
		this.danger = danger;
		target = null;
	}

	/**
	 * pour que la comparaison soit d'apres d'une cible
	 * 
	 * @param as
	 * @param target
	 */
	public void addTarget(AkpolatSener as, Target target) {
		this.as = as;
		this.target = target;
		danger = null;
	}

	public int compare(AiTile tile1, AiTile tile2) {
		int result = 0;

		if (target != null) {
			try {
				if (target.getHypotenuseToTarget(tile1) > target
						.getHypotenuseToTarget(tile2))
					result = 1;
				else
					result = -1;
			} catch (StopRequestException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				if (danger.getHypotenuseToDanger(tile1) > danger
						.getHypotenuseToDanger(tile2))
					result = -1;
				else
					result = 1;
			} catch (StopRequestException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
