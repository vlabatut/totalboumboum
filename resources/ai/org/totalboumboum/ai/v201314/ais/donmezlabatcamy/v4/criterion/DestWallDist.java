package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4.Agent;

/**
 * Cette class calcule la distance entre notre agent et les murs destructible et elle nous permet
 * de dire si on est à coté d'un mur destructible ou pas.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class DestWallDist extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "DESTWALLDIST";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 */
	public DestWallDist(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Calcul la valeur du critère.
	 * 
	 * @param tile
	 *            la case séléctionnée.
	 * @return result true si la case est à coté d'un mur destructible 
	 * 				  false sinon
	 * 
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();

		boolean result = true;

		ArrayList<Integer> distanceBloks = new ArrayList<Integer>();

		for (AiBlock destBlocks : ai.getZone().getDestructibleBlocks()) {
			ai.checkInterruption();
			distanceBloks.add(ai.getZone().getTileDistance(tile, destBlocks.getTile()));
		}
		if ( !distanceBloks.contains(1) )
			result = false;
		return result;
	}
}
