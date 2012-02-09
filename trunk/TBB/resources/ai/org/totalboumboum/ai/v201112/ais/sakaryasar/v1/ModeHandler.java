package org.totalboumboum.ai.v201112.ais.sakaryasar.v1;

import java.util.List;

import java.util.ListIterator;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<SakarYasar>
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
	protected ModeHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// 
	}

	//definitions of the limits of range and numbers of our bombs
	private int RANGE = 4;
	private int BOMB = 3;
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
    	// 
		// number of bombes we have got
		int nbOfBombs = ai.getZone().getOwnHero().getBombNumberMax();
		//range of our bombs
		int rangeOfBombs = ai.getZone().getOwnHero().getBombRange();
		
		if(nbOfBombs >= BOMB && rangeOfBombs >= RANGE)
			return true;
		else
			return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
   		// 
		boolean result=true;
		
		int currentBombs = ai.getZone().getOwnHero().getBombNumberMax();
		int currentRange = ai.getZone().getOwnHero().getBombRange();
		
		List <AiItem> items = ai.getZone().getItems();
		int numberOfItems = items.size();
				
		int nbHiddenBomb = ai.getZone().getHiddenItemsCount(AiItemType.EXTRA_BOMB);
		int nbHiddenRange = ai.getZone().getHiddenItemsCount(AiItemType.EXTRA_FLAME);
		int nbOfHiddenItems = nbHiddenBomb+nbHiddenRange;
		
		double remainingTime = (ai.getZone().getLimitTime() - ai.getZone().getTotalTime());
		double ratioLimit = 0.4*ai.getZone().getLimitTime();
		ListIterator<AiItem> li = items.listIterator();
				
		if(numberOfItems + nbOfHiddenItems ==0){
			result = false;
		}
		else if(remainingTime < ratioLimit){
			result = false;
		}
		else if(nbOfHiddenItems > 0){
			if(BOMB - currentBombs > 0 && RANGE - currentRange <= 0 && nbHiddenBomb > 0){
				result = true;
			}
			else if(BOMB - currentBombs <= 0 && RANGE - currentRange > 0 && nbHiddenRange >0){
				result = true;
			}
		}
		else{
			if(BOMB - currentBombs > 0 && RANGE - currentRange <= 0){
				while(li.hasNext() && !li.equals(AiItemType.EXTRA_BOMB))
					li.next();

				if(li.equals(AiItemType.EXTRA_BOMB) )
					result = true;
				else 
					result = false;
			}
			else if(BOMB - currentBombs <= 0 && RANGE - currentRange > 0){
				while(li.hasNext() && !li.equals(AiItemType.EXTRA_FLAME))
					li.next();

				if(li.equals(AiItemType.EXTRA_FLAME))
					result = true;
				else 
					result = false;
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
