package org.totalboumboum.ai.v201213.ais.oralozugur.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.oralozugur.v4.OralOzugur;

/**
Il vaut 1 si on a besoin de ce bonus.Donc, pour bonus vitesse il va retourne 1 tel que
la vitesse n’est pas maximum.Pour extra feu, si notre portee est plus petit que 4 on en a besoin.
Et pour la extra bombe, s’il existe plus que 55 tile dans la zone
alors, alors on besoin de 4 bombe.S’il est plus petite 3 bombe est suffisante.
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
@SuppressWarnings("deprecation")
public class Necessity extends AiUtilityCriterionBoolean<OralOzugur>
{	/** Nom de ce critère */
	public static final String NAME = "Necessity";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Necessity(OralOzugur ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	/**
	 * Maximum speed of a hero
	 */
	private double MAXIMUM_HERO_SPEED = 200.0;
	
	/**
	 * We need 4 or more bombs if there are 55 tiles or more.
	 */
	private int ZONE_TILE_LIMIT = 55;
	
	/**
	 * The minimum range limit to have
	 */
	private final int BOMBE_RANGE_LIMIT = 4;
	
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = true;
		AiZone zone=ai.getZone();
		AiHero hero = zone.getOwnHero();
		
		
		for (AiItem item : tile.getItems()) {
			ai.checkInterruption();
			if (item.getType() == AiItemType.EXTRA_BOMB) {
				int fireRange = hero.getBombRange();
				if ( fireRange < BOMBE_RANGE_LIMIT )
				{
					result = true;
				}
				else
					result = false;
			}
			
			if (item.getType() == AiItemType.EXTRA_FLAME) {
				int bombNumber = hero.getBombNumberMax();
				int wantedBombs = ((zone.getHeight()*zone.getWidth()) < ZONE_TILE_LIMIT) ? 3 : 4;
				
				if ( bombNumber < wantedBombs )
				{
					result = true;
				}
				else
					result = false;
			}
			
			if (item.getType() == AiItemType.EXTRA_SPEED) {
				double heroSpeed = hero.getWalkingSpeed();
				if ( MAXIMUM_HERO_SPEED > heroSpeed )
				{
					result = true;
				}
				else
					result = false;
			}
		}
		
		
		
		return result;
	}
}
