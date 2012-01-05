package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * Cette classe a but pour determiner l'adversaire plus faible dans la zone de jeu.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("rawtypes")
public class CriterionPlusFaible extends AiUtilityCriterion
{	/** Nom de ce critère */
	public static final String NAME = "PLUSFAIBLE";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionPlusFaible(UnluYildirim ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME);
		
		// init agent
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("null")
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	
		int result = 0;
		
	    AiZone zone = null ;
		List<AiHero> heros ;
		List<AiHero> heros_case; 
		
		heros_case=tile.getHeroes(); //les heros qui se trouve dans le tile spécifié
		heros=zone.getRemainingOpponents(); // tous les heros dans la zone
		int i=0,rank=0,index=0;
		
		for(i=0;i<heros.size();i++) 
		{
			if((heros.get(i).getRoundRank()) >rank ) //les ordres des heros
			{
				rank=heros.get(i).getRoundRank();
			    index=i;	
			}
			
		}//for renvoie le heros qui a le rank plus grande , ça veut dire plus faible
		if(heros_case.contains(heros.get(index))) // regarde si le tile contient l'adversaire plus faible dans la zone
		{
			result=1;
		}
		
		
		return result;
	}
}

