package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<CaliskanGeckalanSeven>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(CaliskanGeckalanSeven ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
		
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		Collection<AiHero> heroes = zone.getHeroes();
		int ownRange = ownHero.getBombRange();
		int ownBombs = ownHero.getBombNumberCurrent();
		
		if(ownBombs * ownRange<2)
			return false;
		Iterator<AiHero> it = heroes.iterator();
		double totalRange=0;
		double totalBomb = 0;
		int numberOfHeroes = 0;
		
		while(it.hasNext()) {
			ai.checkInterruption();
			AiHero tempHero = it.next();
			if(!tempHero.equals(ownHero))
			{
				totalRange = totalRange + tempHero.getBombRange();
				totalBomb = totalBomb + tempHero.getBombNumberCurrent();
				numberOfHeroes++;
			}
		}
		if(((totalBomb * totalRange)/numberOfHeroes)<=ownRange*ownBombs )
		{
			return true;
		}
			

		return false;
		
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		AiZone zone = ai.getZone();
		int hiddenItems = zone.getHiddenItemsCount();
		boolean result = hiddenItems>0;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		
	}
}
