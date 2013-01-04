package org.totalboumboum.ai.v201213.ais.oralozugur.v2.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
//import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v2.OralOzugur;


/**
 * La critère vaut 0 si le distance entre l’agent et la case est plus grand qu’un rayon qui
dépend du vitesse.Le rayon initial sera 5 et nous allons ajouter +1 pour chaque bonus vitesse collecté.
Si le bonus est dans le rayon, alors il vaut 1.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
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
	
	//yapilacak determine these constants
	/**
	 * normal walking speed of a hero without speed bonus
	 */
	//private final double 	NORMAL_SPEED = 100.0;
	/**
	 * how much one extra speed effects our speeds
	 */
	//private final double 	ONE_BONUS_GIVEN_SPEED = 20.0;
	/**
	 * if it's bigger than this value without extra speeds it's returned distant
	 */
	//private final double	DISTANT = 5.0;
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		
		//AiHero hero=ai.getZone().getOwnHero();
		//speed = pixel/second
		//DISTANCE_UPPER_LIMIT = DISTANT + (kac speed bonusu var);
		//DISTANCE_UPPER_LIMIT = DISTANT +  (hero.getWalkingSpeed() - NORMAL_SPEED) / ONE_BONUS_GIVEN_SPEED ;
		
		if ( ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(), tile ) <= DISTANCE_UPPER_LIMIT ) result = true;
	
		return result;
	}

}

