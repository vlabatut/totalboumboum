package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**
 * Dans cette critere, si notre agent a besoin de quelque item.
 * S'il y a d'item "extra_speed" dans cette case, donc la valeur retourne 3.
 * S'il y a d'item "extra_bomb" dans cette case, donc la valeur retourne 2.
 * S'il y a d'item "extra_flame" dans cette case, donc la valeur retourne 1.
 * S'il n'y a pas d'item dans cette case, donc la valeur retourne 0.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Items extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Items";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Items(Agent ai)
	{	super(ai,NAME,0,3);
		ai.checkInterruption();
	}

	/** 
	 * 
	 * 
	 * */
	private final double SPEED_LIMIT = 180;
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile){	
		ai.checkInterruption();

		AiZone zone = ai.getZone();	
		AiHero myHero= zone.getOwnHero();
		
		int mybombs 	 = myHero.getBombNumberMax();
		int mybombsRange = myHero.getBombRange();
		double mySpeed 	 = myHero.getWalkingSpeed();
		
		boolean needOfExtraBomb  = (mybombs < 3);
		boolean needOfExtraFlame = (mybombsRange < 4);
		boolean needOfExtraSpeed = (mySpeed < SPEED_LIMIT);

		for (AiItem item : zone.getItems()) {
			ai.checkInterruption();
			AiItemType type=item.getType();
			if ((type.equals(AiItemType.EXTRA_SPEED)||type.equals(AiItemType.GOLDEN_SPEED))&& needOfExtraSpeed){
				
				if(item.getTile().equals(tile)){
					return 3;
				}
			}
			else if((type.equals(AiItemType.EXTRA_BOMB)||type.equals(AiItemType.GOLDEN_BOMB))&& needOfExtraBomb){
				
				if(item.getTile().equals(tile) ){
					return 2;
				}
			}
			else if((type.equals(AiItemType.EXTRA_FLAME)||type.equals(AiItemType.GOLDEN_FLAME))&& needOfExtraFlame){
				
				if(item.getTile().equals(tile)){
					return 1;
				}
			}
		}
		return 0;
	}
}
