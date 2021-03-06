package org.totalboumboum.ai.v201213.ais.oralozugur.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
//import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v4.OralOzugur;

/**
 * La critère vaut 0 si le distance entre l’agent et la case est plus grand qu’un rayon qui
dépend du vitesse.Le rayon initial sera 5 et nous allons ajouter +1 pour chaque bonus vitesse collecté.
Si le bonus est dans le rayon, alors il vaut 1.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
@SuppressWarnings("deprecation")
public class Distance extends AiUtilityCriterionBoolean<OralOzugur>
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
	public Distance(OralOzugur ai) throws StopRequestException
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

//	/**
//	 * if it's bigger than this value without extra speeds it's returned distant
//	 */
//	private final double	DISTANT = 5.0;
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		double walkingSpeed = ai.getZone().getOwnHero().getWalkingSpeed();
		
		if (walkingSpeed > DEFAULT_WALKING_SPEED) {
			DISTANCE_UPPER_LIMIT = walkingSpeed   / ONE_BONUS_GIVEN_SPEED ;
		}
		
		
		if ( ai.simpleTileDistance(ai.getZone().getOwnHero().getTile(), tile ) <= DISTANCE_UPPER_LIMIT ) result = true;
	
		return result;
	}

}

