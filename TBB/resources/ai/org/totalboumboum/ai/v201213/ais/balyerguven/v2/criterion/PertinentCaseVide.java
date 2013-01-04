package org.totalboumboum.ai.v201213.ais.balyerguven.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.balyerguven.v2.BalyerGuven;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 */
public class PertinentCaseVide extends AiUtilityCriterionBoolean<BalyerGuven>
{	/** Nom de ce critère */
	public static final String NAME = "PertinentCaseVide";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		?	
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public PertinentCaseVide(BalyerGuven ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		ai.checkInterruption();
	}

	

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
	ai.checkInterruption();
	boolean result = false;
	
	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();

	if(tile.getBlocks().isEmpty() && tile.getBombs().isEmpty() && tile.getFires().isEmpty() && (tile.getHeroes().isEmpty() || tile.getHeroes().contains(ownHero)))
		result = true;

	return result;
	}
}