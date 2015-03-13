package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant le choix de l'ennemi cible.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class EnemyHandler extends AiAbstractHandler<Agent> {
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected EnemyHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ourHero = zone.getOwnHero();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Methode pour envoyer l'ennemie qui est choisi.
	 * 
	 * @return L'ennemie ce qui est choisi.
	 */
	public AiHero selectEnemy() {
		ai.checkInterruption();
		AiHero enemy = null;
		enemy = highPointEnemy();
		if (enemy == null)
			enemy = nearestEnemy();
		return enemy;
	}

	/**
	 * Cette méthode nous permet de calculer le plus proche ennemie.
	 * 
	 * @return Le plus proche l'ennemie.
	 */
	public AiHero nearestEnemy() {
		ai.checkInterruption();
		AiHero enemy = null;
		int distance = 1000;
		int distanceBetweenEnemy = 0;
		for (AiHero hero : this.ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			distanceBetweenEnemy = zone.getTileDistance(ourHero.getTile(),
					hero.getTile());
			if (distance > distanceBetweenEnemy) {
				distance = distanceBetweenEnemy;
				enemy = hero;
			}
		}
		return enemy;
	}

	/**
	 * Methode pour decider à quel ennemie on va choisir. Si dans la zone il y a
	 * un ennemie qui a max point, alors on le choisit.
	 * 
	 * @return Retourne comme l'ennemie qui a le plus point.
	 */
	public AiHero highPointEnemy() {
		ai.checkInterruption();
		AiHero enemy = null;
		int maxPoint = Integer.MIN_VALUE;
		for (AiHero hero : zone.getRemainingOpponents()) {
			ai.checkInterruption();
			if (hero.getMatchRank() != 0) {
				if (maxPoint < hero.getMatchRank()) {
					maxPoint = hero.getMatchRank();
					enemy = hero;
				}
			}
		}
		return enemy;

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
