package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class CriterionMenace extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "MENACE";
	
	/**
	 * Ce critere determine si le tile defini est menacé par une bombe..
	 * Si il est menacé , il renvoie la valeur 0.
	 * Sinon il renvoie 1.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionMenace(UnluYildirim ai) throws StopRequestException
	{	// init nom
		super(NAME,1,2);
		
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
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	Integer result = 0;
	
	    AiZone zone = ai.getZone();
		List<AiBomb> zone_bombs; //tous les bomb dans la zone .
		List<AiTile> tile_blast ; // stocke la portée de chaque bomb 
		
		
	    int i;
	    zone_bombs = zone.getBombs() ; //tous les bombs dans la zone
		
		for(i=0;i< zone_bombs.size();i++) //fait une controle pour chaque bomb dans la zone
		{
			tile_blast=zone_bombs.get(i).getBlast(); // tile_blast prend les cases inclus dans la portee de la bombe
			if(!(tile_blast.contains(tile))) // fait le controle si le tile dans la portee d'une bombe
			{
				result= 1;// renvoie 1 , si le tile n'est pas inclus dans la portee d'une bombe
			}
		}
		
		
		return result;
	}
}
