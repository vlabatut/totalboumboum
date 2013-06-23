package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<ArikKoseoglu>
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
	protected ModeHandler(ArikKoseoglu ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
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
	 * 
	 * 
	 * @throws StopRequestException 
	 * 		description manquante !
	 * 
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		//  à compléter, si vous voulez afficher quelque chose
	}
}
