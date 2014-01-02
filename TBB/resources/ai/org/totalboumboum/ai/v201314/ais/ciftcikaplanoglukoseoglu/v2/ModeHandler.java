package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.criterion.DistanceEnemy;

/**
 * Classe gérant les déplacements de l'agent. Cf. la documentation de
 * {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class ModeHandler extends AiModeHandler<Agent> {
	
	/**
	 * La zone dans laqueel on se trouve
	 */
	public AiZone zone = ai.getZone();
	/**
	 * Notre agent.
	 */
	public AiHero ownHero = zone.getOwnHero();
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	
	

	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {

		ai.checkInterruption();
		boolean result = true;

		AiHero ownHero = zone.getOwnHero();

		int bomb_number = ownHero.getBombNumberMax();
		int bomb_range = ownHero.getBombRange();
		double speed = ownHero.getWalkingSpeed();

		if (bomb_number < 3 || bomb_range <= 2 || speed <= 2)
			result = false;

		else
			result = true;

		return result;

	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;
		int extrabomb = 0, extraspeed = 0;
		List<AiItem> items = ai.getZone().getItems();
		//le nombre de bonus visible. on ne s'interesse pas avec les bonus qui sont caché.
		for (AiItem i : items) {
			ai.checkInterruption();
			if (i.getType().equals(AiItemType.EXTRA_BOMB))
				extrabomb++;
			if (i.getType().equals(AiItemType.EXTRA_SPEED))
				extraspeed++;
		}

		if (extrabomb >= 3 || extraspeed >= 2) {
			result = true;
		}
		//Si un agent adverse se trouve pres de notre agent, la collecte n'est pas possible.
		DistanceEnemy de = (DistanceEnemy) ai.preferenceHandler
				.getCriterion("DISTANCE_ENEMY");
		if (de.fetchValue(ownHero.getTile()))
			result = false;

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}
