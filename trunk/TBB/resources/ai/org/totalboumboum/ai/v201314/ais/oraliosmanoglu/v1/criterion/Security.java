package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v1.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v1.Agent;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;

/**
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 * cette critere nous envoyer false, si la tile est secure(i n'y a pas de bombe ou souffle de bombe). sinon, true. Donc methode est boolean
 */
public class Security extends AiCriterionBoolean<Agent> {
	/** */
	public static final String NAME = "Criter_Securite";

	/**
	 * @param ai
	 *            l'agent concerné.
	 */
	public Security(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * cette critere nous envoyer false, si la tile est secure(i n'y a pas de bombe ou souffle de bombe).
	 */
	
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		//initialisation de result
		boolean result=false;

		AiZone zone = ai.getZone();
		//loop pour toutes les bombes dans cette zone.
		for (AiBomb bomb : zone.getBombs()) 
		{
			ai.checkInterruption();
		//loop pour les cases correspondant au souffle de cette bombe	
		for (AiTile blastTile : bomb.getBlast()) 
		{
			ai.checkInterruption();

		if (tile == blastTile) {
					result = true;//pas securé
			} 
		}
	}

		return result;
	}
}
