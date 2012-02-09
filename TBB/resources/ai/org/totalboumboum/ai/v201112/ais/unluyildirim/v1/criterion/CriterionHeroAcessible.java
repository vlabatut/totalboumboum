package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * Cette classe représente
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionHeroAcessible extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "HEROACESSİBLE";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionHeroAcessible(UnluYildirim ai) throws StopRequestException
	{	// init nom
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
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;
	

    AiZone zone = null ;
	List<AiHero> heros ;
	List<AiHero> heros_case; 
	
	heros_case=tile.getHeroes(); //les heros qui se trouve dans le tile spécifié
	heros=zone.getRemainingOpponents(); // tous les heros dans la zone
	int i=0,index=0;
	
	for(i=0;i<heros.size();i++) 
	{
		
		
	}
	if(heros_case.contains(heros.get(index))) // regarde si le tile contient l'adversaire plus faible dans la zone
	{
		result=true;
	}
	
	
		return result;
	}
}
