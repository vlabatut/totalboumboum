package org.totalboumboum.ai.v201213.ais.saglamseven.v4;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 *  Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<SaglamSeven>
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
	protected ModeHandler(SaglamSeven ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
		//  à compléter
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		AiZone gameArea =ai.getZone();
		AiHero ownHero = gameArea.getOwnHero();
		int bombNum = ownHero.getBombNumberMax();
		int bombRange = ownHero.getBombRange();
		
		//pour notre criteria
		if(bombNum < 3 && bombRange < 2){//collecte
			return false;
		}
		else{//attaque
			return true;
		}
	}
	
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	    AiZone gameArea =ai.getZone();
	    List<AiItem> items= gameArea.getItems();
	    List<AiHero>  heros= gameArea.getRemainingHeroes();
	    int numBonus = items.size()
			, numHeros = heros.size()
			, averageBomb = (numBonus / numHeros)
			, totalTime = (int) gameArea.getLimitTime()
			, elapTime = (int) gameArea.getElapsedTime();
	
	//special case 
	if((averageBomb > 0) 
			&& ((totalTime/4)> elapTime)
			&& (numHeros>2)){
		return true;
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
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
