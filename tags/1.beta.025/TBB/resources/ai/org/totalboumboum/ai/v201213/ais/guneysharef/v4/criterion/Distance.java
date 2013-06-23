package org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.GuneySharef;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
@SuppressWarnings("deprecation")
public class Distance extends AiUtilityCriterionBoolean<GuneySharef>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Distance (GuneySharef ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule et renvoie la valeur de critère pour la case passée en paramètre. 
	 */
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		AiTile t=this.ai.getZone().getOwnHero().getTile();
		
		if(tile.equals(this.ai.getClosestTile(t))){
			result = true;
		}
				
		return result;	

	}
}
