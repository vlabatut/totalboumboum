package org.totalboumboum.ai.v201213.ais.oralozugur.v4.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v4.OralOzugur;

/**
 * Ce critere est valable si on peut tuer un ennemi ou si on est proche d'un ennemi dans cette case.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class PeutTuerEnnemi extends AiUtilityCriterionInteger<OralOzugur> {
	/** Nom de ce critère */
	public static final String NAME = "PeutTuerEnnemi";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PeutTuerEnnemi(OralOzugur ai) throws StopRequestException {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		
		return this.ai.getPeutTuerEnnemiofTile(tile);
	}




}
