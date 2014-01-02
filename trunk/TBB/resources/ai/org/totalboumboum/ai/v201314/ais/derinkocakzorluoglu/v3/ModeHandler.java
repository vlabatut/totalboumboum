package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;

/**
 * Classe gérant le changement de mode. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class ModeHandler extends AiModeHandler<Agent>
{	

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
		
	
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**Si on a au moins un bombe qu'on poser , et si la portee de cette bombe est plus grand
	 * ou egale a  case, notre Items sont suffisantes pour la mode attaque.  */
	@Override
	protected boolean hasEnoughItems()
	{	ai.checkInterruption();
	boolean result = true;		
	int myBombLimit=ai.myHero.getBombNumberLimit();
	int myBombRange=ai.myHero.getBombRange();
	if(myBombLimit<1||myBombRange<1){
		result=false;
	}		
	return result;
	}

	/*Si on a n'a pas un bombe a poser et si il y a un extra bombe ou golden bombe donc c'est logique
	 * a passer au mode collecte.
	 *Si le bombe qu'on va poser n'a pas de portes et si il y a un extra flame ou golden flame donc c'est logique
	 * a passer au mode collecte.   
	 * 
	 * */
	@Override
	protected boolean isCollectPossible()
	{		ai.checkInterruption();
	boolean result = true;	
	List<AiItem> items =new ArrayList<AiItem>();
	items=ai.getZone().getItems();
	
	int myBombLimit=ai.myHero.getBombNumberLimit();
	int myBombRange=ai.myHero.getBombRange();
	
	if(myBombLimit<1){
		if(!items.contains(AiItemType.EXTRA_BOMB)||!items.contains(AiItemType.GOLDEN_BOMB))
			result=false;
	}
	if(myBombRange<1){
	if(!items.contains(AiItemType.EXTRA_FLAME)||!items.contains(AiItemType.GOLDEN_FLAME))
		result=false;
	}		
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
