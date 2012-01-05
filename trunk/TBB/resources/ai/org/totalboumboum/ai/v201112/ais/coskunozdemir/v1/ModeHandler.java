package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class ModeHandler extends AiModeHandler<CoskunOzdemir>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(CoskunOzdemir ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// TODO à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	
		ai.checkInterruption();		
		return (  this.ai.getZone().getOwnHero().getBombNumberMax() >= 3 || this.ai.getZone().getOwnHero().getBombRange() >= 3  );
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	
		ai.checkInterruption();
		return ( this.ai.getZone().getHiddenItemsCount() > 0 );
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
		//System.out.println( "Bomb Max : " + this.ai.getZone().getOwnHero().getBombNumberMax() + " Bomb Range : " + this.ai.getZone().getOwnHero().getBombRange() + " Mode : " + this.mode );
	}
}
