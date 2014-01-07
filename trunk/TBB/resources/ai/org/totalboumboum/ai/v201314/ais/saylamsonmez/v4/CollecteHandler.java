package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant des méthodes liée à la mode collecte.
 *  
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class CollecteHandler extends AiAbstractHandler<Agent>
{	
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;

	
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected CollecteHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ourHero = zone.getOwnHero();	
	
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Méthode pour pouvoir decider à un item bonus ou pas.
	 * 
	 * @param item
	 * 		  L'item à controler.
	 * 
	 * @return Cette méthode returne boolean, est-ce que l'item est bonus ou pas
	 */
	public boolean isBonus(AiItem item)  {
		ai.checkInterruption();
		AiItemType type = item.getType();
		if (type.equals(AiItemType.EXTRA_BOMB)
				|| type.equals(AiItemType.EXTRA_FLAME)
				|| type.equals(AiItemType.EXTRA_SPEED)
				|| type.equals(AiItemType.GOLDEN_BOMB)
				|| type.equals(AiItemType.GOLDEN_FLAME)
				|| type.equals(AiItemType.GOLDEN_SPEED)
				|| type.equals(AiItemType.RANDOM_EXTRA))
			return true;
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
	}
}
