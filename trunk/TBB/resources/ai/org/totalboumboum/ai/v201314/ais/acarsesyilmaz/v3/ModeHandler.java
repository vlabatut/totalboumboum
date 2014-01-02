package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v3;

import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant les déplacements de l'agent. 
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
	 * Description :
	 * 		Détermine si l'agent possède assez d'items, ou bien s'il doit essayer d'en ramasser 
	 * 		d'autres. Cette distinction est relative à l'environnement, à l'agent lui-même et à 
	 * 		la stratégie qu'il utilise.
	 * 
	 * @return
	 * 		{@code true} ssi l'agent possède assez d'items.
	 */
	@Override
	protected boolean hasEnoughItems()
	{	
		ai.checkInterruption();		
		boolean result = true;		
		for(AiItem item : ai.getZone().getItems())
		{		
			ai.checkInterruption();
			if(result && ai.securityHandler.getAccessibleTiles(ai.getZone().getOwnHero().getTile()).contains(item.getTile()))
			{
				if(item.getType() == AiItemType.EXTRA_FLAME && (ai.getZone().getOwnHero().getBombRange() < ai.getZone().getOwnHero().getBombRangeLimit()))
				{
					result = false;
				}
				else if(item.getType() == AiItemType.EXTRA_BOMB && (ai.getZone().getOwnHero().getBombNumberMax() < ai.getZone().getOwnHero().getBombNumberLimit()))
				{
					result = false;
				}
				else if(item.getType() == AiItemType.EXTRA_SPEED)
				{
					result = false;
				}
				else if(item.getType() == AiItemType.RANDOM_EXTRA)
				{
					result = false;
				}
			}
		}
		return result;
		}
	
	/**
	 * Description :
	 * 		Détermine si l'agent a la possibilité de ramasser des items dans la zone courante : 
	 * 		présence d'items cachés ou découverts, assez de temps restant, etc.
	 * 
	 * @return
	 * 		{@code true} ssi l'agent a la possibilité de ramasser des items.
	 */
	@Override
	protected boolean isCollectPossible()
	{	
		ai.checkInterruption();
		boolean result = false;	
		
		if(ai.itemHandler.isBonusReachable())
		{
			Set<AiTile> accessibleTiles = ai.securityHandler.getAccessibleTiles(ai.getZone().getOwnHero().getTile());						
			if(!ai.securityHandler.isEnemyReachable())
			{
				result = true;
			}
			else
			{
				for(AiItem item : ai.getZone().getItems())
				{					
					ai.checkInterruption();
					if(accessibleTiles.contains(item.getTile()))
					{
						if(ai.securityHandler.surroundLevel(item.getTile())>=3)
						{
							if(ai.getZone().getTileDistance(ai.securityHandler.getClosestEnemy(item.getTile()).getTile(),ai.getZone().getOwnHero().getTile()) > 10)
							{
								result = true;
							}
						}
						else
						{
							result = true;
						}
					}
					else
					{
						result = false;
					}
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