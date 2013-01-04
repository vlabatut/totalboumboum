package org.totalboumboum.ai.v201213.ais.oralozugur.v3.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v3.OralOzugur;

/**
 * Nombre de murs qui vont etre detruit si on met une bombe dans cette une case
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class NbMurDetrui extends AiUtilityCriterionInteger<OralOzugur> {
	/** Nom de ce critère */
	public static final String NAME = "NbMurDetrui";

	/**
	 * Ce critere concern les mur peuvent etre detrui en posant une bombe a
	 * cette case.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NbMurDetrui(OralOzugur ai) throws StopRequestException {
		super(ai, NAME, 0, 4);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		

		return this.ai.getNbMurDetruitofTile(tile);
	}
}
