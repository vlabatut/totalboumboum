package org.totalboumboum.ai.v201314.ais.asilizeryuce.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v1.Agent;

/**
 * Cette classe est un simple exemple de 
 * critÃ¨re binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter Ã  vos besoin.
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class DistanceOfEnemy extends AiCriterionBoolean<Agent>
{	/** Nom de ce critÃ¨re */
	public static final String NAME = "DISTANCE_ADVERSAIRE";
	
	/**
	 * CrÃ©e un nouveau critÃ¨re binaire.
	 * 
	 * @param ai
	 * 		l'agent concernÃ©. 
	 */
	public DistanceOfEnemy(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
	/**
	 * Distance radius limit to check to determine the tile's state.
	 */
	private double		DISTANCE_UPPER_LIMIT	= 5.0;
	
	/**
	 * how much estimately a speed bonus augments our speed
	 */
	private final static double 	ONE_BONUS_GIVEN_SPEED = 35.0;
	
	
	
	/**
	 * Default Walking speed of a hero at the beginning of a map
	 */
	private final static double DEFAULT_WALKING_SPEED = 190.625;
	/////////////////////////////////////////////////////////////////
	// PROCESS		    /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
		boolean result = true;
		
		double walkingSpeed = ai.getZone().getOwnHero().getWalkingSpeed();
	
	if (walkingSpeed > DEFAULT_WALKING_SPEED) {
		DISTANCE_UPPER_LIMIT = walkingSpeed   / ONE_BONUS_GIVEN_SPEED ;
	}
	
	
	if (ai.simpleTileDistance(ai.getZone().getOwnHero().getTile(), tile ) <= DISTANCE_UPPER_LIMIT) result = false;

	return result;
	}
}
