package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant le changement de mode. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class ModeHandler extends AiModeHandler<Agent>
{	
	/**notre zone */
	private AiZone zone=ai.getZone();
	/**notre hero */
	private AiHero myHero=zone.getOwnHero();
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
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
	/**Si on a au moins un bombe qu'on poser , et si la portee de cette bombe est plus grand
	 * ou egale a 2 case, notre Items sont suffisantes pour la mode attaque.  */
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
		boolean result = true;		
		int myBombLimit=this.myHero.getBombNumberLimit();
		int myBombRange=this.myHero.getBombRange();
		if(myBombLimit<1||myBombRange<2){
			result=false;
		}		
		return result;
	}
	
	/**Si le portee dont l'ennemie plus proche a nous possede , est plus petit a la distance
	 * entre nous et lui , on est convenable pour passer au mode collect */
	@Override
	protected boolean isCollectPossible()
	{	ai.checkInterruption();
		boolean result = true;
		int enemyBombRange=ai.nearestEnemy().getBombRange();
		if(zone.getTileDistance(myHero.getTile(), ai.nearestEnemy().getTile())<enemyBombRange){
			result=false;
			
			
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
	{	ai.checkInterruption();
		
	}
}
