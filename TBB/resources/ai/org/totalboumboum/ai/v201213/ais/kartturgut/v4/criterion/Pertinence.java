package org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.KartTurgut;


/**
 * @author Yunus Kart
 * @author Siyabend Turgut
 */

public class Pertinence extends AiUtilityCriterionBoolean<KartTurgut>
{	
	/** */
	public static final String	NAME	= "Pertinence";
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Pertinence( KartTurgut ai ) throws StopRequestException
	{
		super( ai,NAME );
		ai.checkInterruption();
		this.ai = ai;
	}
	
	/**
	 * @param t
	 * @return resultat
	 * @throws StopRequestException
	 */
	public Boolean criterepertinence( AiItemType t ) throws StopRequestException
	{
		ai.checkInterruption();
		boolean resultat = false;
		if(t==AiItemType.EXTRA_BOMB){
			int nbrbombe = ai.getZone().getOwnHero().getBombNumberMax();
			if(nbrbombe <3)
				resultat = true;
		}
		else{
			int flamme = ai.getZone().getOwnHero().getBombRange();
			if(flamme < 2)
				resultat = true;
		}
		
		return resultat;
	}
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean resultat = false;
	
		List<AiItem> items = tile.getItems();
		if(!items.isEmpty()){
			resultat = criterepertinence(items.get(0).getType());
		}
		
		return resultat;
	}

}

