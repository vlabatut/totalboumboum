/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.Agent;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.Enemy;

/**
 * Ce critère controle si une case dangereux ou pas.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Danger extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Danger";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Danger(Agent ai) throws StopRequestException {
		super(ai, NAME);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> dangerList = new ArrayList<AiTile>();

		dangerList = ai.getCurrentDangerousTiles();
		ArrayList<AiTile> suddenDeathTiles = new ArrayList<AiTile>();
		suddenDeathTiles = ai.getSuddenDeathTiles();
		// trouve les tiles d'après notre critere de securité
		if (dangerList.contains(tile))
			return true;
		// Mort Subit
		if (!suddenDeathTiles.isEmpty()) {
			if (suddenDeathTiles.contains(tile)) {
				return true;
			}
		}
		// ennemie qui est contagieux
		if (!tile.getHeroes().isEmpty()) {
			Enemy e = new Enemy(ai);
			AiHero hero = e.selectEnemy();
			if (!ai.getZone().getRemainingOpponents().isEmpty())
				if (hero.isContagious())
					return true;
		}
		// les items qui ne sont pas bonus
		// ils ne sont pas intéressant pour nous
		if (!tile.getItems().isEmpty()) {
			for (AiItem item : tile.getItems()) {
				this.ai.checkInterruption();
				if (!ai.isBonus(item))
					return true;
			}
		}
		return false;
	}
}
