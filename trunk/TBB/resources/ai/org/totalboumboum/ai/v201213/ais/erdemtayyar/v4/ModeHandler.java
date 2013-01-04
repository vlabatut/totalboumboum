package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4;

import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;



/**
 * Classe gérant les déplacements de l'agent. Cf. la documentation de
 * {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class ModeHandler extends AiModeHandler<ErdemTayyar> {

	/**
	 * If the hero's bomb count is greater than this amount, he has enough
	 * bombs.
	 */
	public final int BOMBLIMIT = 3;
	/**
	 * If the hero's range is greater than this number, he has enough item of
	 * flame.
	 */
	public final int FLAMELIMIT = 2;
	/**
	 * If the hero's range is greater than this number, he has enough speed of
	 * item.
	 */
	public final int SPEEDLIMIT = 2;
	/**
	 * Firstly, we have no item.
	 */
	public int ITEMCOUNT = 0;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	protected ModeHandler(ErdemTayyar ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException {
		ai.checkInterruption();
		return ((this.ai.getHero().getBombNumberMax() >= BOMBLIMIT
				&& this.ai.getHero().getBombRange() >= FLAMELIMIT && this.ai
				.getHero().getWalkingSpeed() >= SPEEDLIMIT));
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException {
		ai.checkInterruption();
		for(AiItem item : this.ai.getZone().getItems())
		{
			ai.checkInterruption();
			if(!item.getType().isBonus())
		
				ITEMCOUNT++;
		}
			return ((this.ai.getZone().getHiddenItemsCount()> ITEMCOUNT) && this.ai.getZone().getTotalTime()<20000);
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();
	}
}
