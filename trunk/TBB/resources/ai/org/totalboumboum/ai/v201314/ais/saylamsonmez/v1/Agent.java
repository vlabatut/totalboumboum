package org.totalboumboum.ai.v201314.ais.saylamsonmez.v1;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de notre agent qui inclus des methodes utilisées. Cf. la
 * documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Agent extends ArtificialIntelligence {
	/** our zone */
	AiZone zone;
	/** our hero */
	AiHero ourHero;
	/** our current tile */
	AiTile ourTile;

	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();
		// active/désactive la sortie texte
		verbose = false;
	}

	@Override
	protected void initOthers() {
		checkInterruption();
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() {
		checkInterruption();
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	@Override
	protected void updatePercepts() {
		checkInterruption();
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = true;
		bombHandler.verbose = false;
		moveHandler.verbose = false;
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	@Override
	protected AiModeHandler<Agent> getModeHandler() {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler() {
		checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler() {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler() {
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() {
		checkInterruption();

		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// /////////////////////////////////////////////////////////////////
	// OUR METHODS /////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////

	/**
	 * Methode pour pouvoir calculer les cases dangereux.
	 * 
	 * @return La liste des cases qui ont une potentielle d'être dangereux.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles()
			throws StopRequestException {
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();

		for (AiBomb currentBomb : getZone().getBombs()) {
			checkInterruption();
			dangerousTiles.add(currentBomb.getTile());
			for (AiTile currentTile : currentBomb.getBlast()) {
				checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : getZone().getFires()) {
			checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}

		return dangerousTiles;
	}

	/**
	 * Méthode pour calculer la distance entre ennemie et la case qu'on veut
	 * calculer la distance.
	 * 
	 * @param aiTile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Cette méthode returne la distance Manhattan entre enemie et case.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public int getDistanceBetweenEnemy(AiTile aiTile)
			throws StopRequestException {
		checkInterruption();
		AiZone zone = this.getZone();
		int currentEnemyDistance = 0;
		for (AiHero heroes : zone.getRemainingOpponents()) {
			checkInterruption();
			AiTile enemyTile = heroes.getTile();
			currentEnemyDistance = zone.getTileDistance(aiTile, enemyTile);
		}
		return currentEnemyDistance;
	}

	/**
	 * Méthode pour aider le critère Alarme de Danger. Ici, on calcule est-ce
	 * qu'il y a une case destruclible ou indestructible pour savoir que case
	 * assure notre critère d'alarme de danger ou pas. Car quand on veut dans la
	 * portée du bombe mais entre la case et ennemie il est possible de trouver
	 * une case destructible ou indestructible.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Cette méthode returne boolean, est-ce que la case se trouve dans
	 *         la direction d'ennemie.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isDirectionEnemy(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		AiZone zone = this.getZone();
		int i = 0;
		int search = 0;
		for (AiHero heroes : zone.getRemainingOpponents()) {
			checkInterruption();
			AiTile enemyTile = heroes.getTile();

			if (Math.abs(tile.getCol() - heroes.getTile().getCol()) <= heroes
					.getBombRange()
					&& Math.abs(tile.getCol() - heroes.getTile().getCol()) > 0) {
				if (Math.abs(tile.getRow() - heroes.getTile().getRow()) == 0) {
					if (tile.getCol() - heroes.getTile().getCol() < 0) {
						search = Math.abs(tile.getCol()
								- heroes.getTile().getCol());
						i = 0;
						enemyTile = heroes.getTile();
						while (enemyTile.getNeighbor(Direction.LEFT)
								.isCrossableBy(heroes, false, false, false,
										false, true, true)
								&& i < search
								&& Math.abs(tile.getCol()
										- heroes.getTile().getCol()) <= heroes
											.getBombRange()) {
							checkInterruption();
							enemyTile = enemyTile.getNeighbor(Direction.LEFT);
							i++;
						}
					} else if (tile.getCol() - heroes.getTile().getCol() > 0) {
						search = Math.abs(tile.getCol()
								- heroes.getTile().getCol());
						i = 0;
						enemyTile = heroes.getTile();
						while (enemyTile.getNeighbor(Direction.RIGHT)
								.isCrossableBy(heroes, false, false, false,
										false, true, true)
								&& i < search
								&& Math.abs(tile.getCol()
										- heroes.getTile().getCol()) <= heroes
											.getBombRange()) {
							checkInterruption();
							enemyTile = enemyTile.getNeighbor(Direction.RIGHT);
							i++;
						}
					} else if (tile.getCol() - heroes.getTile().getCol() == 0)
						result = false;
					else
						result = true;
					if (i < search && i >= 0) {
						result = true;
					} else
						result = false;
				}
			} else if (Math.abs(tile.getCol() - heroes.getTile().getCol()) > heroes
					.getBombRange())
				result = true;
			else if (Math.abs(tile.getCol() - heroes.getTile().getCol()) == 0) {
				if (Math.abs(tile.getRow() - heroes.getTile().getRow()) <= heroes
						.getBombRange()) {
					if (tile.getRow() - heroes.getTile().getRow() < 0) {

						search = Math.abs(tile.getRow()
								- heroes.getTile().getRow());
						i = 0;
						enemyTile = heroes.getTile();

						while (enemyTile.getNeighbor(Direction.UP)
								.isCrossableBy(heroes, false, false, false,
										false, true, true)
								&& i < search
								&& Math.abs(tile.getRow()
										- heroes.getTile().getRow()) <= heroes
											.getBombRange()) {
							checkInterruption();
							enemyTile = enemyTile.getNeighbor(Direction.UP);
							i++;
						}
					} else if (tile.getRow() - heroes.getTile().getRow() > 0) {

						search = Math.abs(tile.getRow()
								- heroes.getTile().getRow());

						i = 0;
						enemyTile = heroes.getTile();
						while (enemyTile.getNeighbor(Direction.DOWN)
								.isCrossableBy(heroes, false, false, false,
										false, true, true)
								&& i < search
								&& Math.abs(tile.getRow()
										- heroes.getTile().getRow()) <= heroes
											.getBombRange()) {
							checkInterruption();
							i++;
							enemyTile = enemyTile.getNeighbor(Direction.DOWN);
						}

					} else if (tile.getRow() - heroes.getTile().getRow() == 0)
						result = false;
					else
						result = true;
					if (i < search && i >= 0)
						result = true;
					else
						result = false;
				}
			}
		}
		return result;

	}

	/**
	 * Méthode pour tester s'il y a un critère de bloque.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Cette méthode returne une valeur booléan, est-ce que la situation
	 *         d'etre bloque assure.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Boolean isBlockingEnemy(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		AiZone zone = this.getZone();
		ourHero = zone.getOwnHero();
		ArrayList<AiTile> currentDangerousTiles = getCurrentDangerousTiles();
		int bombRange = ourHero.getBombRange();
		int i = 0;
		ourTile = ourHero.getTile();
		AiSimZone simZone = new AiSimZone(zone);
		AiBomb myBomb = simZone.createBomb(tile, simZone.getOwnHero());
		if (!simZone.getRemainingOpponents().isEmpty()) {
			for (AiHero hero : simZone.getRemainingOpponents()) {
				checkInterruption();
				AiTile tileHero = hero.getTile();
				i = 0;
				if (myBomb.getBlast().contains(tileHero)) {
					while (tileHero.getNeighbor(Direction.DOWN).isCrossableBy(
							hero, false, false, false, false, true, true)
							&& i <= bombRange
							&& !tileHero.getNeighbor(Direction.DOWN).equals(
									myBomb.getTile())) {
						checkInterruption();
						tileHero = tileHero.getNeighbor(Direction.DOWN);
						if (result == true) {
							if ((!tileHero.getNeighbor(Direction.LEFT)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.LEFT)))
									&& (!tileHero.getNeighbor(Direction.RIGHT)
											.isCrossableBy(hero, false, false,
													false, false, true, true) || currentDangerousTiles
											.contains(tileHero
													.getNeighbor(Direction.RIGHT)))
									&& myBomb.getBlast().contains(tileHero))
								result = true;
							else
								result = false;
						}
						i++;
					}
					i = 0;
					tileHero = hero.getTile();
					while (tileHero.getNeighbor(Direction.UP).isCrossableBy(
							hero, false, false, false, false, true, true)
							&& i <= bombRange
							&& !tileHero.getNeighbor(Direction.UP).equals(
									myBomb.getTile())) {
						checkInterruption();
						tileHero = tileHero.getNeighbor(Direction.UP);
						if (result == true) {
							if ((!tileHero.getNeighbor(Direction.LEFT)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.LEFT)))
									&& (!tileHero.getNeighbor(Direction.RIGHT)
											.isCrossableBy(hero, false, false,
													false, false, true, true) || currentDangerousTiles
											.contains(tileHero
													.getNeighbor(Direction.RIGHT)))
									&& myBomb.getBlast().contains(tileHero))
								result = true;
							else
								result = false;
						}
						i++;
					}
					i = 0;
					tileHero = hero.getTile();
					while (tileHero.getNeighbor(Direction.LEFT).isCrossableBy(
							hero, false, false, false, false, true, true)
							&& i <= bombRange
							&& !tileHero.getNeighbor(Direction.LEFT).equals(
									myBomb.getTile())) {
						checkInterruption();
						tileHero = tileHero.getNeighbor(Direction.LEFT);
						if (result == true) {
							if ((!tileHero.getNeighbor(Direction.UP)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.UP)))
									&& (!tileHero.getNeighbor(Direction.DOWN)
											.isCrossableBy(hero, false, false,
													false, false, true, true) || currentDangerousTiles
											.contains(tileHero
													.getNeighbor(Direction.DOWN)))
									&& myBomb.getBlast().contains(tileHero))
								result = true;
							else
								result = false;
						}
						i++;
					}
					i = 0;
					tileHero = hero.getTile();
					while (tileHero.getNeighbor(Direction.RIGHT).isCrossableBy(
							hero, false, false, false, false, true, true)
							&& i <= bombRange
							&& !tileHero.getNeighbor(Direction.RIGHT).equals(
									myBomb.getTile())) {
						checkInterruption();
						tileHero = tileHero.getNeighbor(Direction.RIGHT);
						if (result == true) {
							if ((!tileHero.getNeighbor(Direction.UP)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.UP)))
									&& (!tileHero.getNeighbor(Direction.DOWN)
											.isCrossableBy(hero, false, false,
													false, false, true, true) || currentDangerousTiles
											.contains(tileHero
													.getNeighbor(Direction.DOWN)))
									&& myBomb.getBlast().contains(tileHero))
								result = true;

							else
								result = false;
						}
						i++;
					}
				} else
					result = false;
			}
		}
		return result;
	}
}
