package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;


/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<Agent>
{	
	/**
	 * Construit un gestionnaire de depot de bombe pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai)
    {	
		super(ai);
    	ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Description : 
	 * 		Méthode permettant de déterminer si l'agent doit poser une bombe ou pas. 
	 * 		Cette décision dépend, entre autres, des valeurs de préférence courantes, 
	 * 		et éventuellement d'autres informations.
	 * 		La méthode renvoie true si l'agent doit poser une bombe, et false sinon.
	 * 
	 * @return
	 * 		Renvoie {@code true} ssi l'agent doit poser une bombe.
	 */
	@Override
	protected boolean considerBombing()
	{			
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		AiTile ownTile = ownHero.getTile();

		AiPath path = ai.getMoveHandler().getCurrentPath();	
		AiTile nextTile;
		
		if(path==null || path.getLength()<2)
		{
			nextTile = ownHero.getTile();
		}
		else
		{
			nextTile = path.getLocation(1).getTile();
		}
		
		AiTile destination = ai.moveHandler.getCurrentDestination();
		boolean result;
		boolean easyKill = false;
		boolean willDestroy = false;

		
		if(ai.attackHandler.canBomb() && (ownTile.getBombs().isEmpty()) && ai.securityHandler.getTimeLeft(ownTile) > 1350 && !ai.securityHandler.ifExistsAnyoneButUs(ownTile) && ai.securityHandler.surroundLevel(ownTile)<4) 
		{		
			if(ai.securityHandler.isEnemyReachable())
			{
				for(AiItem item : nextTile.getItems())
				{
			    	ai.checkInterruption();
					if(!item.getType().isBonus())
					{
						willDestroy = true;				
					}
				}	
				if(willDestroy)
				{
					result = true;
				}				
				else
				{
					for(AiTile neighbour : ownTile.getNeighbors())
					{			
						ai.checkInterruption();
						if(ai.securityHandler.ifExistsAnyoneButUs(neighbour) && ai.securityHandler.surroundLevel(neighbour) >= 3 && !easyKill)	
						{
							easyKill = true;
						}				
					}
					
					if(ai.getZone().getRemainingOpponents().size()>2 || ai.securityHandler.biggestFlame()>8)
					{
						result = ((ownTile == destination) && (ai.attackHandler.checkIfCanKill(ownTile)) && ai.securityHandler.surroundLevel(ownTile)<3) || easyKill || ai.attackHandler.isTriangle(ownTile);
					}
					else
					{
						result = ((ownTile == destination) && ai.securityHandler.surroundLevel(ownTile)<3 && ((ai.attackHandler.checkIfCanKill(ownTile) || ai.attackHandler.checkIfCanLock(ownTile)))) || easyKill || ai.attackHandler.isTriangle(ownTile);
					}
				}							
			}
			else
			{							
				for(AiBlock block : nextTile.getBlocks())
				{
			    	ai.checkInterruption();
					if(block.isDestructible() && ownTile.getBombs().isEmpty() && !ai.securityHandler.willBurn(nextTile))
					{
						willDestroy = true;
					}
				}
				if(willDestroy)
				{
					result = true;
				}
				else
				{
					for(AiItem item : nextTile.getItems())
					{
				    	ai.checkInterruption();
						if(!item.getType().isBonus())
						{
							willDestroy = true;				
						}
					}	
					if(willDestroy)
					{
						result = true;	
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
		
		if(result)
		{
			result = result && ai.securityHandler.isBombingSafe();
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
	}
}
