package org.totalboumboum.ai.v201213.ais.oralozugur.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;

import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class ModeHandler extends AiModeHandler<OralOzugur> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	/**
	 * Si l'agent a plus de bombe que ce nombre, alors il suffit.
	 * 
	 */
	private int BOMB_NUMBER_LIMIT;
	/**
	 * Si l'agent a plus de puissance que ce nombre , alors il suffit
	 */
	private int BOMB_RANGE_LIMIT;
	/**
	 * Le nombre d'item reste dans le zone pour finir le mode collecte.
	 */
	private final int ITEM_LEFT_CONTROL = 1;
	/** Si le nombre de case accessible est plus petite ou egale à ce nombre alors on passe à mode attack pour échappe cette zone*/
	private static final int TILE_NUMBER_CONTROL=5;

	/**
	 * La vitesse neccessaire pour sortir de la mode collecte
	 * 
	 * 
	 */
	private final double SPEED_LIMIT = 200;

	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	protected ModeHandler(OralOzugur ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		// setting limit values
		int height = ai.getZone().getHeight();
		int width = ai.getZone().getWidth();
		if (width > height)
			BOMB_RANGE_LIMIT = width / 3;
		else
			BOMB_RANGE_LIMIT = height / 3;
		if (ai.getZone().getTiles().size() > 54)
			BOMB_NUMBER_LIMIT = 4;
		else
			BOMB_NUMBER_LIMIT = 3;

		/*System.out.println("Height: " + height + " Width: " + width
				+ " Flame range:" + BOMB_RANGE_LIMIT + "Bomb number: "
				+ BOMB_NUMBER_LIMIT);*/
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		AiHero hero = zone.getOwnHero();
		
		
		if (hero.getBombNumberMax() >= BOMB_NUMBER_LIMIT
				&& hero.getBombRange() >= BOMB_RANGE_LIMIT
				&& hero.getWalkingSpeed() > SPEED_LIMIT)
			return true;
		else
			return false;

	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException {
		
		
		ai.checkInterruption();
		
		if(this.ai.accessibleTiles.size()<=TILE_NUMBER_CONTROL)
		{
			return false;
		}
		AiZone zone = ai.getZone();
		AiHero hero = zone.getOwnHero();
		
		
		if(!this.ai.accessiblesHaveBonus()&&zone.getHiddenItemsCount() < ITEM_LEFT_CONTROL)
			return false;
			
		boolean result = true;

		
		if (zone.getHiddenItemsCount() > ITEM_LEFT_CONTROL
				|| zone.getItems().size() > 0) {
			boolean needOfextraBomb = (hero.getBombNumberCurrent() < BOMB_NUMBER_LIMIT) ? true
					: false;
			boolean needOfextraFlame = (hero.getBombRange() < BOMB_RANGE_LIMIT);
			boolean needOfextraSpeed = (hero.getCurrentSpeed() < SPEED_LIMIT);
			boolean extraBomb_done = false;
			boolean extraFlame_done = false;
			boolean extraSpeed_done = false;
			
			
			if(needOfextraBomb)
			{
				if(this.ai.zoneHasItem(AiItemType.EXTRA_BOMB)||this.ai.zoneHasItem(AiItemType.GOLDEN_BOMB))
				{}
				else if((zone.getHiddenItemsCount(AiItemType.EXTRA_BOMB) > 0 || zone.getHiddenItemsCount(AiItemType.GOLDEN_BOMB) > 0))
				{}
				else 
					extraBomb_done=true;
			}
			else 
				extraBomb_done=true;
			if(needOfextraFlame)
			{
				if(this.ai.zoneHasItem(AiItemType.EXTRA_FLAME)||this.ai.zoneHasItem(AiItemType.GOLDEN_FLAME))
					{}
				else if((zone.getHiddenItemsCount(AiItemType.EXTRA_FLAME) > 0 || zone.getHiddenItemsCount(AiItemType.GOLDEN_FLAME) > 0))
					{}
				else 
					extraFlame_done=true;
			}
			else extraFlame_done=true;
			if(needOfextraSpeed)
			{
				if(this.ai.zoneHasItem(AiItemType.EXTRA_SPEED)||this.ai.zoneHasItem(AiItemType.GOLDEN_SPEED))
				{}
				else if((zone.getHiddenItemsCount(AiItemType.EXTRA_BOMB) > 0 || zone.getHiddenItemsCount(AiItemType.GOLDEN_BOMB) > 0))
				{}
				else 
					extraSpeed_done=true;
			}
			else extraSpeed_done=true;

			
			
			if (extraBomb_done && extraFlame_done && extraSpeed_done)
				result = false;

		} else
			result = false;
	
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

	}
}
