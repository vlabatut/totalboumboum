package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.criterion.DistanceEnemy;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.criterion.Threat;

/**
 * Classe gérant les déplacements de l'agent. Cf. la documentation de
 * {@link AiModeHandler} pour plus de détails.
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<Agent> {
	/**
	 * La vitesse initiale de la zne
	 */

	public double initial_speed = 0;
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

		int bomb_total = ownHero.getBombNumberMax();

		double speed = ownHero.getWalkingSpeed();

		if (initial_speed == 0 && zone.getElapsedTime() > 0)
			initial_speed = speed;

		if (bomb_total == 0 || speed <= initial_speed
				|| ownHero.getBombPrototype().getRange() < 2) {
			result = false;
		}

		return result;

	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();
		boolean result = false;
		// Si un agent adverse se trouve pres de notre agent, la collecte n'est
		// pas possible.

		DistanceEnemy de = (DistanceEnemy) ai.preferenceHandler
				.getCriterion("DISTANCE_ENEMY");
		Threat th = (Threat) ai.preferenceHandler.getCriterion("THREAT");
		List<AiItem> items = zone.getItems();
		ArrayList<AiItem> pezo = new ArrayList<AiItem>();
		pezo.addAll(items);
		if (items != null)
			if (!items.isEmpty())
				for (AiItem m : items) {
					ai.checkInterruption();
					if (m.getType().isGoldenKind())
						pezo.remove(m);
				}
		boolean col = false;
		int i = 0;
		// si il n'y a que des items golden qui sont accessible on ne passe pas
		// au mode collecte.
		if (pezo != null)
			if (!pezo.isEmpty())
				if (ai.preferenceHandler.accessibleTiles != null)
					while (!col && i < pezo.size()) {
						ai.checkInterruption();
						if (ai.preferenceHandler.accessibleTiles.contains(pezo
								.get(i).getTile())
								&& pezo.get(i).getType().isBonus())
							col = true;
						i++;
					}

		if (!items.isEmpty())
			if (col)
				if (th.fetchValue(ownHero.getTile()) < 1)
					if (!de.fetchValue(ownHero.getTile()))

					{
						result = true;

					} else if (ownHero.getBombNumberMax() == 0)
						result = true;
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
