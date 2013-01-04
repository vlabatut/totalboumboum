package org.totalboumboum.ai.v201213.ais.guneysharef.v3.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.guneysharef.v3.GuneySharef;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
public class Competition extends AiUtilityCriterionBoolean<GuneySharef>
{	/** Nom de ce critère */
	public static final String NAME = "Concurrence";
	/**
	 * limite des cases pour la concurrence
	 */
	public static final int ConcurrenceTrue = 4;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Competition(GuneySharef ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai = ai;
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
		AiZone z = this.ai.getZone();
		

		
		if(z.getRemainingHeroes().isEmpty()!=true){
			for(AiHero h : z.getRemainingOpponents()){
				ai.checkInterruption();
				if(this.ai.getDist(h.getTile(), tile) <= ConcurrenceTrue) {
					result = true;
			}
			else
				result = false;
				
				
			}
		}
		

	
		return result;
	}
}
