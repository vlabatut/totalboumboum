package org.totalboumboum.ai.v201213.ais.oralozugur.v2.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v2.OralOzugur;

/**
 * Cette classe est un simple exemple de critère entier. Copiez-la, renommez-la,
 * modifiez-la pour l'adapter à vos besoin. Notez que les bornes du domaine de
 * définition sont spécifiées dans l'appel au constructeur (
 * {@code super(nom,inf,sup)}).
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
