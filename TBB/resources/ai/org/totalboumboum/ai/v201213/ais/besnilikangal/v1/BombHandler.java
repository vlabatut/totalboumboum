package org.totalboumboum.ai.v201213.ais.besnilikangal.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.
 * 
 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
public class BombHandler extends AiBombHandler<BesniliKangal>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(BesniliKangal ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{
		ai.checkInterruption();
		if ( 0 < ( ai.getHero().getBombNumberMax() - ai.getHero().getBombNumberCurrent() ) && ai.getHeroOperation().canReachSafety() )
		{
			AiTile currentTile = ai.getHero().getTile();
			if ( ai.modeHandler.getMode() == AiMode.COLLECTING )
				return ( ai.getTileOperation().getBiggestTile() == ai.getHero().getTile() );
			else if ( ai.modeHandler.getMode() == AiMode.ATTACKING )
			{
				// Is there any ennemy in our bomb range
				if ( ai.getHeroOperation().hasEnemyInBombRange() )
				{
					return true;
				}
				// there is no ennemy in our bomb range
				else
				{
					// There is at least an ennemy accessible
					if ( ai.getHeroOperation().isEnemyAccessible() )
						return false;
					else
					{
						return ( ai.getTileOperation().getAccessibleDestructibleTiles().containsKey( currentTile ) && ( ai.getTileOperation().getBiggestTile() == currentTile ) );
					}
				}
			}
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
	}
}
