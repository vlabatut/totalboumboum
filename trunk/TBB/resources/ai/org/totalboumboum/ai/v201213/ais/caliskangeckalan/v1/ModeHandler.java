package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v1;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class ModeHandler extends AiModeHandler<CaliskanGeckalan>
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
	protected ModeHandler(CaliskanGeckalan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	
		ai.checkInterruption();		
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		Collection<AiHero> heroes = zone.getRemainingHeroes();
		int ownRange = ownHero.getBombRange();
		int ownBombs = ownHero.getBombNumberMax();
		//double ownSpeed = ownHero.getWalkingSpeed();
		
		Iterator<AiHero> it = heroes.iterator();
		int totalRange=0;
		int totalBomb = 0;
		int numberOfHeroes = 0;
		print("ownRange: " + ownRange + " ownBombs:" + ownBombs );
		if(ownRange >=3 && ownBombs>=2)
			return true;
		while(it.hasNext()) { //get all heroes range and bomb number
			ai.checkInterruption();
			AiHero tempHero = it.next();
			if(!tempHero.equals(ownHero))
			{
				totalRange = totalRange + tempHero.getBombRange();
				totalBomb = totalBomb + tempHero.getBombNumberCurrent();
				numberOfHeroes++;
			}
		}
		//control of average of rivals and our hero
		if(numberOfHeroes > 0 && ((totalBomb * totalRange)/numberOfHeroes)<ownRange*ownBombs && totalRange >1 && totalBomb>1)
		{
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		int hiddenItems = zone.getHiddenItemsCount();
		List<AiItem> items = zone.getItems();
		if(items.size()>0)
			return true;
		boolean result = hiddenItems>0;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
