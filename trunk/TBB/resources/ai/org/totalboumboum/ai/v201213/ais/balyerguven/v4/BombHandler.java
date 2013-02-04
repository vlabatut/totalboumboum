package org.totalboumboum.ai.v201213.ais.balyerguven.v4;


import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * our bomb handler class.
 * 
 * @author Leman Sebla Balyer
 * @author Ecem Güven
 *
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<BalyerGuven>
{
	/** represents zone*/
	protected AiZone zone;
	/** represents our hero*/
	protected AiHero myHero;
	

	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 *             
	 *  
	 */
	protected BombHandler(BalyerGuven ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();
		boolean result = false;
		AiHero myHero = ai.getHero();
		if((myHero.getTile().getBombs().size() == 0 && ( myHero.getBombNumberCurrent() < myHero.getBombNumberMax() ) ) )
		{
			if(canRun())
			{
				if (isEnemyInBombRange())
					{
						result = true;
					}
				else if( ai.moveHandler.control )
					{
						result = true;
					}
				else
					{
						result = ( ai.getBiggestTile() == myHero.getTile() );
					}
			}
		}
		return result;
	}

	/**
	 * method for escaping
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 * 			 If the engine demands the termination of the agent.
	 */
	public boolean canRun() throws StopRequestException
	{	
		ai.checkInterruption();
		List<AiTile> reachableTiles = ai.getReachableTiles(ai.getHero().getTile());
		reachableTiles.removeAll(ai.getCurrentDangerousTiles());
		reachableTiles.removeAll(ai.getHero().getBombPrototype().getBlast());
		if (!reachableTiles.isEmpty())
			{
				return true;
			}
		return false;
	}

	/**
	 * method for finding if there is an enemy in bomb range.
	 * @return boolean
	 * 
	 * @throws StopRequestException
	 * 			 If the engine demands the termination of the agent.
	 */
	private boolean isEnemyInBombRange() throws StopRequestException
	{
		ai.checkInterruption();
		for(AiHero hero : ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			if(ai.getHero().getBombPrototype().getBlast().contains( hero.getTile()))
			{
				return true;
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
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
