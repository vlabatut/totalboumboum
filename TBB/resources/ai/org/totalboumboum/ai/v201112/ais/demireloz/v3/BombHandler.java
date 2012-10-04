package org.totalboumboum.ai.v201112.ais.demireloz.v3;

import java.util.ArrayList;
import java.util.HashMap;
import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<DemirelOz> {
	/** */
	public static final int BLOCK = 15;
	/** Minumun and maximum utility values of a tile in range of an enemy (with danger=true) */
	public static final int MIN_TILE_IN_RANGE = 10;
	/** */
	public static final int MAX_TILE_IN_RANGE = 13;
	/** Min and max utility values of the other cases when an enemy is in our range. */
	public static final int MIN_TILE_RANGE = 6;
	/** */ 
	public static final int MAX_TILE_RANGE = 9;
	/** int straight refers to the minimum value of utility of the case "Tile Close to destructible wall close to enemy". */
	public static final int STRAIGHT = 30;

	/** Utility values of a tile close to an enemy , when we don't have an enemy  in our selected tiles. */
	public static final int MIN_TILE_CLOSE_ENEMY = 2;
	/** */
	public static final int MAX_TILE_CLOSE_ENEMY = 29;

	protected BombHandler(DemirelOz ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		verbose = false;

	}

	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();
		AiHero ourhero = this.ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		boolean resultat = false;

		AiMode mode = this.ai.modeHandler.getMode();
		boolean herocontrol = true;
		if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
			for (AiHero aihero : this.ai.getZone().getRemainingOpponents()) {
				ai.checkInterruption();
				if (this.ai.utilityHandler.selectTiles().contains(
						aihero.getTile())) {
					herocontrol = false;
					break;
				}

			}
		}

		if (mode.equals(AiMode.COLLECTING)) {
			// If we have bombs in the hand
			if ((ourhero.getBombNumberCurrent() < ourhero.getBombNumberMax())
					&& ourtile.getBombs().isEmpty()) {
				if (this.ai.getSafeTiles(ourhero, null, true).size() > 0) {
					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();
						if (this.ai.controlOfDestructibleBlock(ourtile,
								direction) == true) {

							return resultat = true;

						}
					}
					return resultat = false;
				}

			} else {
				return resultat = false;

			}
		}

		else if (mode.equals(AiMode.ATTACKING)) {
			if ((ourhero.getBombNumberCurrent() < ourhero.getBombNumberMax())
					&& ourtile.getBombs().isEmpty()) {
				if (this.ai.getSafeTiles(ourhero, null, true).size() > 0) {

					// List containing tiles which can block the enemy
					ArrayList<AiTile> list = new ArrayList<AiTile>();
					HashMap<AiTile, Float> hashmap;
					hashmap = this.ai.getUtilityHandler().getUtilitiesByTile();
					for (AiTile currentTile : hashmap.keySet()) {
						this.ai.checkInterruption();

						if (herocontrol == false) {

							if (this.ai.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) == BLOCK) {
								list.add(currentTile);
							}

							if (!list.isEmpty() && list.contains(ourtile)) {
								return resultat = true;
							}
						}
					}
					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();

						if (ai.getAnEnemyInMyRange(ourtile, direction, 0) == true) {

							// List1 contains tiles in range of an enemy which
							// will cause danger to him.
							// List2 contains the other tiles in mode attack.
							ArrayList<AiTile> list1 = new ArrayList<AiTile>();
							ArrayList<AiTile> list2 = new ArrayList<AiTile>();
							HashMap<AiTile, Float> hashmap1;
							hashmap1 = this.ai.getUtilityHandler()
									.getUtilitiesByTile();

							for (AiTile currentTile : hashmap1.keySet()) {
								this.ai.checkInterruption();
								if (this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= MIN_TILE_IN_RANGE
										&& this.ai.getUtilityHandler()
												.getUtilitiesByTile()
												.get(currentTile) <= MAX_TILE_IN_RANGE) {

									list1.add(currentTile);

								}
								if (this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= MIN_TILE_RANGE
										&& this.ai.getUtilityHandler()
												.getUtilitiesByTile()
												.get(currentTile) <= MAX_TILE_RANGE) {
									list2.add(currentTile);
								}

							}
							if (list.isEmpty() && !list1.isEmpty()
									&& list1.contains(ourtile)) {
								return resultat = true;
							}

							if (list1.isEmpty() && !list2.isEmpty()
									&& list2.contains(ourtile)) {
								return resultat = true;
							}

						}
					}

					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();
						if (herocontrol == true
								&& this.ai.controlOfDestructibleBlock(ourtile,
										direction) == true) {
							// List2 contains tiles on the same line with the
							// nearest enemy (Straight())
							// List3 contains tiles with a certain distance to
							// the nearest enemy.
							ArrayList<AiTile> list2 = new ArrayList<AiTile>();
							ArrayList<AiTile> list3 = new ArrayList<AiTile>();
							HashMap<AiTile, Float> hashmap2;
							hashmap2 = this.ai.getUtilityHandler()
									.getUtilitiesByTile();

							for (AiTile currentTile : hashmap2.keySet()) {
								this.ai.checkInterruption();
								if (this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= STRAIGHT) {
									list2.add(currentTile);

								}
								if ((this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= MIN_TILE_CLOSE_ENEMY
										&& this.ai.getUtilityHandler()
												.getUtilitiesByTile()
												.get(currentTile) <= MAX_TILE_CLOSE_ENEMY && this.ai
										.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) != 4)) {
									list3.add(currentTile);
								}
							}
							if (!list2.isEmpty() && list2.contains(ourtile)) {

								return resultat = true;
							}
							if (list2.isEmpty() && !list3.isEmpty()
									&& list3.contains(ourtile)) {

								return resultat = true;
							}
						}
					}
					return resultat = false;
				} else {
					return resultat = false;

				}
			} else {
				return resultat = false;

			}
		} else {

			return resultat = false;
		}

		return resultat;
	}

	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

	}
}
