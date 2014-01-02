package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2;


import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class BombHandler extends AiBombHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
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
	 * Construit un gestionnaire de bombe pour l'agent passé en paramètre.
	 * 
	 * @return 	
	 * 		renvoie une valeur booléen selon les conditions.
	 * 		true si on est sur une case de destination ET s'il n'y a pas de bombe sur la case 
	 * 			ET si elle vérifie une de nos criteres CanKill CanLock ou PossibleTriangle
	 * 		false sinon
	 */
	@Override
	protected boolean considerBombing()
	{			
		ai.checkInterruption();
		AiTile tile = ai.getZone().getOwnHero().getTile();
		boolean result;
		boolean lock = false;
		
		for(AiTile neighbour : tile.getNeighbors())
		{			
			ai.checkInterruption();
			if(ai.ifExistsAnyoneButUs(neighbour) && ai.checkIfSurrounded(neighbour) >= 2 && !lock && ai.checkIfSurrounded(ai.getZone().getOwnHero().getTile())<=2)	
			{
				lock = true;
			}				
		}	
		
		result = (tile == ai.moveHandler.getCurrentDestination()) && ai.getZone().getOwnHero().getTile().getBombs().isEmpty() && (ai.checkIfCanKill(tile) || ai.checkIfCanLock(tile) || ai.isTriangle(tile)) || lock;
		
		
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
