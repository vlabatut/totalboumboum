package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette critere entier pour savoir que est-ce que la case est propre de tuer
 * l'ennemi, qu'on veut mettre une bombe. ici, on controle les couloirs avec la
 * methode isCorridor pour se proteger, ne coince pas dans un coloir. plus,
 * cette methode nous a donne 0 comme la valeur si on peut tuer l'ennemi quand
 * on pose une bombe dans la case qu'on examine,la valeur est 1 si l'ennemi est
 * proche du mort et 2 si on ne peur pas tuer l'ennemi en posant une bombe
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
@SuppressWarnings("deprecation")
public class EnemyInDanger extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "ENNEMI_EN_DANGER";

	/**
	 * Crée un critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public EnemyInDanger(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();

		int result = 2;
		boolean in = false;
		// AiHero hero = ai.getZone().getOwnHero();
		int range = 1;

		if (ai.enemyHandler.enemyTiles().size() > 1) {
			for (AiTile aiTile : tile.getNeighbors()) {
				ai.checkInterruption();
				if (isCorridor(aiTile)
						&& (ai.enemyHandler.enemyTiles().contains(aiTile)))
					return 0;
			}
		}

		if (ai.enemyHandler.enemyTiles().size() == 1) {
			range = 0;

			for (AiTile aiTile : tile.getNeighbors()) {
				ai.checkInterruption();
				if (isCorridor(aiTile)
						&& (ai.enemyHandler.enemyTiles().contains(aiTile)))
					return 0;
			}

			if (tile == ai.enemyHandler.enemyTiles().get(0)) {

				if (isCorridor(tile) || ai.tileHandler.tileIsCorridor(tile))
					return 1;
				else
					return 0;
			}
		}

		for (AiTile til : ai.enemyHandler.enemyTiles()) {
			ai.checkInterruption();
			if (ai.tileHandler.simpleTileDistance(tile, til) <= range)
				in = true;
		}
		if (!in)
			return 2;

		if (ai.enemyHandler.getEnemyInColumn(tile) == 0) {
			if (!tileIsInFrontOfEnemy(tile))
				return 2;
			else if (isCorridor(tile))
				return 1;
			else
				return 0;
		}

		if (ai.enemyHandler.getEnemyInRow(tile) == 0) {
			if (!tileIsInFrontOfEnemy(tile))
				return 2;
			else if (isCorridor(tile))
				return 1;
			else
				return 0;
		}

		for (AiTile enemyTile : ai.enemyHandler.enemyTiles()) {
			ai.checkInterruption();
			if (ai.tileHandler.accessibleTiles.contains(enemyTile))
				return 2;
			else {
				int neighbourBlock = 0;

				for (AiTile aiTile : enemyTile.getNeighbors()) {
					ai.checkInterruption();
					if (!aiTile.getBlocks().isEmpty())
						neighbourBlock++;
				}
				if (neighbourBlock > 1)
					return 0;
				else
					return 1;
			}
		}

		if (ai.enemyHandler.getEnemyInRow(tile.getNeighbor(Direction.DOWN)) == 0)
			return 1;

		if (ai.enemyHandler.getEnemyInRow(tile.getNeighbor(Direction.UP)) == 0)
			return 1;

		if (ai.enemyHandler.getEnemyInColumn(tile.getNeighbor(Direction.LEFT)) == 0)
			return 1;

		if (ai.enemyHandler.getEnemyInColumn(tile.getNeighbor(Direction.RIGHT)) == 0)
			return 1;

		return result;
	}

	/**
	 * Cette methode controle et prend les cases entre nous et l'ennemi pour
	 * eviter d'avoir les valeurs 0 comme preference qui sont derriere de
	 * l'ennemi.
	 * 
	 * @param aiTile
	 *            est la case qu'on observe
	 * @return true si la distance entre nous et l'ennemi est plus petite que la
	 *         distance entre nous et la case qu'on observe
	 */
	public boolean tileIsInFrontOfEnemy(AiTile aiTile) {
		ai.checkInterruption();
		AiHero hero = ai.getZone().getOwnHero();

		if (!ai.enemyHandler.enemyTiles().contains(hero.getTile()))
			if (ai.tileHandler.simpleTileDistance(hero.getTile(),
					ai.enemyHandler.dangerEnemy) < ai.tileHandler
					.simpleTileDistance(hero.getTile(), aiTile))
				return true;

		return false;
	}

	/**
	 * cette methode examine une case pour savoir est-ce que cette case est dans
	 * une forme de couloir, ou elle peut etre dans une forme de couloir. aussi
	 * on regarde est-ce qu'il y a un ennemi.
	 * 
	 * @param aiTile
	 *            est la case qu'on observe pour la formalisation de couloir
	 * @return true si la case est une forme de couloir, 0 sinon
	 */
	public boolean isCorridor(AiTile aiTile) {
		ai.checkInterruption();
		int i = 0;

		for (AiTile tile : aiTile.getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBlocks().isEmpty())
				i++;
			if (!tile.getBombs().isEmpty())
				i++;
		}

		if (i > 2) {
			return true;
		} else {
			return false;
		}
	}

}
