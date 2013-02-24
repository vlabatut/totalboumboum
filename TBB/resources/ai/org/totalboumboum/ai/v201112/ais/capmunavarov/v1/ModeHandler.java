package org.totalboumboum.ai.v201112.ais.capmunavarov.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * @author Fahri Cap
 * @author Suhrob Munavarov
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<CapMunavarov>
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
	protected ModeHandler(CapMunavarov ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		// à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
    	// à compléter
		if (ai.getZone().getOwnHero().getBombNumberMax()>=2)  //si on a deux bombes, on peut attaquer.
			{
			return true;
			}
		return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		mode=AiMode.COLLECTING;
		
		int count=ai.getZone().getItems().size();
		if (count>0){
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// à compléter, si vous voulez afficher quelque chose
	}
}
