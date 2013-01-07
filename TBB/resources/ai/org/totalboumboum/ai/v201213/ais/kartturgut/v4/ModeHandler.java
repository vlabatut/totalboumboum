package org.totalboumboum.ai.v201213.ais.kartturgut.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;



/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 
 * @author Yunus Kart
 * @author Siyabend Turgut
 */
public class ModeHandler extends AiModeHandler<KartTurgut>
{	
	/** */
	private AiHero notreHero;
	
	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected ModeHandler(KartTurgut ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
    	notreHero = ai.getZone().getOwnHero();
		verbose = false;
	}

    
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{
		ai.checkInterruption();
		if(notreHero==null)
			return true;
		else if(notreHero.getBombNumberMax() > 2 && notreHero.getBombRange() >= 2)
	        return true;
		else 
			return false;
	             
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	
		ai.checkInterruption();
		return ( this.ai.getZone().getHiddenItemsCount() >0);
	}
	
			
	
	
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
