package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.CiplakErakyol;

/**
 * Cette classe represente le critere de concurrence.
 * Il indique s'il y a une adversaire que se trouve plus proche que notre IA.
 * Si oui , la fonction renvoie false.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionItemPertinent extends AiUtilityCriterionBoolean<CiplakErakyol>
{	/** Nom de ce critère */
	public static final String NAME = "PertinentItem";

	/**
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionItemPertinent(CiplakErakyol ai) throws StopRequestException
	{	
		super( ai, NAME );
		ai.checkInterruption();
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
		AiZone zone=ai.getZone();
		AiHero ajan = zone.getOwnHero();
		int bombaSayisi = ajan.getBombNumberMax(); // le nombre des bombes de notre agent
	    int bombaMenzili = ajan.getBombRange(); //La portée des bombes que notre agent peux posser. 
	    double hizimiz=ajan.getWalkingSpeed();
	    
	    if(tile.getItems().contains(AiItemType.EXTRA_BOMB))
	    {
	    	if ( bombaSayisi < 3 )
			{
				result = true;
			}
			else
				result = false;
	    }
	    
	    if(tile.getItems().contains(AiItemType.EXTRA_FLAME))
	    {
			if ( bombaMenzili<bombaSayisi )
			{
				result = true;
			}
			else
				result = false;
	    }
	    
	    if(tile.getItems().contains(AiItemType.EXTRA_SPEED))
	    {
			if ( hizimiz<3) 
			{
				result = true;
			}
			else
				result = false;
	    }
	    
	    if(tile.getItems().contains(AiItemType.GOLDEN_FLAME))
	    	result = true;
	    
	    if(tile.getItems().contains(AiItemType.GOLDEN_BOMB))
	    	result = true;
	    
	    if(tile.getItems().contains(AiItemType.GOLDEN_SPEED))
	    	result = true;  
	    
	    if(tile.getItems().contains(AiItemType.NO_BOMB) 
	    	|| tile.getItems().contains(AiItemType.NO_FLAME)
	    	|| tile.getItems().contains(AiItemType.NO_SPEED))	
	    	{
	    		result = false;
	    	}

		return result;
	}
}
