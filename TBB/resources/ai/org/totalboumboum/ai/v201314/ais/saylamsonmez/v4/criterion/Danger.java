package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.criterion;

import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.CollecteHandler;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.EnemyHandler;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.TileCalculationHandler;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;

/**
 * Ce critère controle si une case dangereux ou pas. Il controle est-ce qu'une
 * case est dangereux ou pas (en utilisant notre méthode
 * getCurrentDangerousTiles), est-ce qu'il y a un mort subit dans une case,
 * ennemie contagious ou bien un item qui n'est pas bonus.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class Danger extends AiCriterionBoolean<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "Danger";
	/** pour acceder aux methodes de TileCalculationHandler */
	TileCalculationHandler tileCalculationHandler;
	/** pour acceder aux methodes de CollecteHandler */
	CollecteHandler collecteHandler;
	/** pour acceder aux methodes de EnemyHandler */
	EnemyHandler enemyHandler;

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
		tileCalculationHandler = ai.tileCalculationHandler;
		collecteHandler = ai.collecteHandler;
		enemyHandler = ai.enemyHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> dangerList = new ArrayList<AiTile>();
		tileCalculationHandler = ai.tileCalculationHandler;
		dangerList = tileCalculationHandler.getCurrentDangerousTiles();
		ArrayList<AiTile> suddenDeathTiles = new ArrayList<AiTile>();
		suddenDeathTiles = tileCalculationHandler.getSuddenDeathTiles();
		// trouve les cases d'après méthode getCurrentDangerousTiles
		if (dangerList.contains(tile))
			return true;
		// Mort Subit
		if (!suddenDeathTiles.isEmpty()) {
			if (suddenDeathTiles.contains(tile)) {
				return true;
			}
		}
		// ennemie qui est contagious
		if (!tile.getHeroes().isEmpty()) {
			enemyHandler = ai.enemyHandler;
			AiHero hero = enemyHandler.selectEnemy();
			if (!ai.getZone().getRemainingOpponents().isEmpty())
				if (hero.isContagious())
					return true;
		}
		// les items qui ne sont pas bonus
		// ils ne sont pas intéressant pour nous
		// on compte dangereux s'il n'est pas bonus
		if (!tile.getItems().isEmpty()) {
			for (AiItem item : tile.getItems()) {
				this.ai.checkInterruption();
				collecteHandler = ai.collecteHandler;
				if (!collecteHandler.isBonus(item))
					return true;
			}
		}
		return false;
	}
}
