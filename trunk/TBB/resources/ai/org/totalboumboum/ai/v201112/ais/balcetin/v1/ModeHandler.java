package org.totalboumboum.ai.v201112.ais.balcetin.v1;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 *  Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
	

public class ModeHandler extends AiModeHandler<BalCetin>
{
	
	
	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();
	List<AiItem> items = zone.getItems();
	int i,index=0;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(BalCetin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
	
		for(i=0;i<items.size();i++)
		{
			ai.checkInterruption();
			if(items.get(i).getType() == AiItemType.EXTRA_FLAME)
			index++;
		}
		
		
		
		//  à compléter
		
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
    	if(ownHero.getBombNumberCurrent() >= 2 && ownHero.getBombRange() >= 3 )
		return true;
    		
    	//if remaining time is lower than 15sec.Don't search for items.
    	if(zone.getElapsedTime() > 45000)
    		return true; 
    	
		return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		
   		//  à compléter
	if(ownHero.getBombNumberCurrent() <= ownHero.getBombNumberMax() && (zone.getBombs().size() != 0))
		return true;
	else if(ownHero.getBombNumberCurrent() < 2 && (ownHero.getBombRange() < 3) && (index != 0)) //index : if there is at least 1 extra flame bonus on the zone.
		return true;
	
	
	
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// à compléter, si vous voulez afficher quelque chose
	}
}
