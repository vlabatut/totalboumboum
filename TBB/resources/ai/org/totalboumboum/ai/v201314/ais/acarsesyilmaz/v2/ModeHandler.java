package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**
	 * Construit un gestionnaire de mode pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Construit la partie de hasEnoughItems du gestionnaire de mode pour l'agent passé en paramètre.
	 * 
	 * @return 	
	 * 		renvoie une valeur booléen.
	 * 		true si notre agent possede assez d'item
	 * 		false sinon
	 */
	@Override
	protected boolean hasEnoughItems()
	{	
		ai.checkInterruption();
		
		boolean result = true;
		
		for(AiItem item : ai.getZone().getItems())
		{		
			ai.checkInterruption();
			if(result)
			{
				if(item.getType() == AiItemType.EXTRA_FLAME && (ai.getZone().getOwnHero().getBombRange() < ai.getZone().getOwnHero().getBombRangeLimit()))
				{
					result = false;
				}
				if(item.getType() == AiItemType.EXTRA_BOMB && (ai.getZone().getOwnHero().getBombNumberMax() < ai.getZone().getOwnHero().getBombNumberLimit()))
				{
					result = false;
				}
				if(item.getType() == AiItemType.EXTRA_SPEED && (ai.calculateInterest()[2] >= -1))
				{
					result = false;
				}
				if(item.getType() == AiItemType.RANDOM_EXTRA && ((ai.getZone().getOwnHero().getBombRange()<ai.getZone().getOwnHero().getBombRangeLimit())||(ai.getZone().getOwnHero().getBombNumberMax() < ai.getZone().getOwnHero().getBombNumberLimit())||ai.calculateInterest()[2]<=0))
				{
					result = false;
				}
			}
		}
		return result;
		}
	
	/**
	 * Construit la partie de isCollectPossible du gestionnaire de mode pour l'agent passé en paramètre.
	 * 
	 * @return 	
	 * 		renvoie une valeur booléen.
	 * 		true si on notre agent peut ramasser d'autre item
	 * 		false sinon
	 */
	@Override
	protected boolean isCollectPossible()
	{	
		ai.checkInterruption();
		boolean result = false;	
		
		if(!ai.getZone().getItems().isEmpty())
		{
			for(AiItem item : ai.getZone().getItems())
			{			
				ai.checkInterruption();
				if(ai.isCloseToUs(item.getTile(), 2))
				{
					result = true;
				}
				else
				{
					result = false;
				}
			}
		}
		else 
		{
			result = false;
		}
	
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	
		ai.checkInterruption();
		
		verbose = false;
	}
}
