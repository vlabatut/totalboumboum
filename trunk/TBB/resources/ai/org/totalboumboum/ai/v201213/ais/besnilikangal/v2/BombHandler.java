package org.totalboumboum.ai.v201213.ais.besnilikangal.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;

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
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = false ;
		boolean secondaryBombing = ai.moveHandler.secondaryBombing ;
		AiHero ownHero = ai.ownHero;
		if ( ( ownHero.getTile().getBombs().size() == 0 && ( ownHero.getBombNumberCurrent() < ownHero.getBombNumberMax() ) ) )
		{
			if ( ai.heroOperation.canReachSafety() )
			{
				if ( ai.heroOperation.hasEnemyInBombRange() )
					result = true;
				else if ( secondaryBombing )
					result = true;
				else if ( ai.heroOperation.canDestroyMalus() )
					result = true;
				else if ( ai.modeHandler.getMode() == AiMode.COLLECTING )
					result = ( ai.tileOperation.getBiggestTile() == ownHero.getTile() );
			}
		}
		return result;
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
