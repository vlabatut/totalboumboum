package org.totalboumboum.ai.v201112.ais.unluyildirim.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v3.UnluYildirim;

/**
 * Cette classe a but d'exprimer si l'item est menacé par une bombe ou pas.
 * 
 * @author Merve ÜNLÜ
 * @author Gülay YILDIRIM
 */
@SuppressWarnings("deprecation")
public class CriterionMenace extends AiUtilityCriterionBoolean
{	/** */
	public static final String NAME = "MENACE";
	
	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public CriterionMenace(UnluYildirim ai) throws StopRequestException
	{	
		super(NAME);
		ai.checkInterruption();
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
	
		AiZone zone ;
		List<AiBomb> zone_bomb ;
		
		int i=0;
		
		zone=ai.getZone();
		zone_bomb = zone.getBombs();// tous les bomb dans la zone 
		
		if(!zone_bomb.isEmpty())// s'il n'y a pas une bombe dans la zone , Ça veut dire que l'item n'est pas menace 
		{ i=0;
		boolean result1 =false ; 
		while(i!=zone_bomb.size() && !result1)//si le tile est inclus dans la portee d'une bombe , il retourne true.
			{
				ai.checkInterruption();
				result1=zone_bomb.get(i).getBlast().contains(tile);
				i++;
				result=result1;
			}
		}
		return result;
	}
}
