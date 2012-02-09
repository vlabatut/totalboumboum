package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.GungorKavus;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe représente est un simple exemple de critère binaire. Copiez-la,
 * renommez-la, modifiez-la pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class VDMDestMPBDistance extends AiUtilityCriterionBoolean {
	/** Nom de ce critère */
	public static final String NAME = "Distance";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDMDestMPBDistance(GungorKavus ai) throws StopRequestException { // init
																			// nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

	public int distance(int d, int e, int f, int g) throws StopRequestException {
		ai.checkInterruption();
		int resultat = Math.abs(d - f) + Math.abs(e - g);

		return resultat;

	}

	// ///////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected GungorKavus ai;

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();

		boolean result = false;

		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();

		List<AiBlock> blockL = zone.getDestructibleBlocks();
		int shortest = 200;
		AiBlock sBlock = null;
		Direction dBlock = null;
		if (blockL.size() > 0) {
			if (zone.getTiles().contains(
					blockL.get(0).getTile().getNeighbor(Direction.UP))) {
				shortest = zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.UP), ownHero.getTile());
				sBlock = blockL.get(0);
				dBlock = Direction.UP;
			}
			AiBlock sDBlock = null;
			Direction dSBlock = null;

			if (zone.getTiles().contains(
					blockL.get(0).getTile().getNeighbor(Direction.DOWN))) {
				if (shortest > zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.DOWN), ownHero.getTile())) {
					shortest = zone.getTileDistance(blockL.get(0).getTile()
							.getNeighbor(Direction.DOWN), ownHero.getTile());
					dBlock = Direction.DOWN;
				}
				if (shortest == zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.DOWN), ownHero.getTile())) {
					dSBlock = Direction.DOWN;
				}
			}
			if (zone.getTiles().contains(
					blockL.get(0).getTile().getNeighbor(Direction.LEFT))) {
				if (shortest > zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.LEFT), ownHero.getTile())) {
					shortest = zone.getTileDistance(blockL.get(0).getTile()
							.getNeighbor(Direction.LEFT), ownHero.getTile());
					dBlock = Direction.LEFT;
				}
				if (shortest == zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.LEFT), ownHero.getTile())) {
					dSBlock = Direction.LEFT;
				}
			}
			if (zone.getTiles().contains(
					blockL.get(0).getTile().getNeighbor(Direction.RIGHT))) {
				if (shortest > zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.RIGHT), ownHero.getTile())) {
					shortest = zone.getTileDistance(blockL.get(0).getTile()
							.getNeighbor(Direction.RIGHT), ownHero.getTile());
					dBlock = Direction.RIGHT;
				}
				if (shortest == zone.getTileDistance(blockL.get(0).getTile()
						.getNeighbor(Direction.RIGHT), ownHero.getTile())) {
					dSBlock = Direction.RIGHT;
				}
			}
			for (int i = 0; i < blockL.size() - 1; i++) {
				ai.checkInterruption();
				if (zone.getTiles()
						.contains(
								blockL.get(i + 1).getTile()
										.getNeighbor(Direction.DOWN))) {
					if (shortest > zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.DOWN),
							ownHero.getTile())) {
						shortest = zone.getTileDistance(blockL.get(i + 1)
								.getTile().getNeighbor(Direction.DOWN),
								ownHero.getTile());
						dBlock = Direction.DOWN;
						sBlock = blockL.get(i + 1);
					}
					if (shortest == zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.DOWN),
							ownHero.getTile())) {
						dSBlock = Direction.DOWN;
						sDBlock = blockL.get(i + 1);
					}
				}
				if (zone.getTiles().contains(
						blockL.get(i + 1).getTile().getNeighbor(Direction.UP))) {
					if (shortest > zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.UP),
							ownHero.getTile())) {
						shortest = zone.getTileDistance(blockL.get(i + 1)
								.getTile().getNeighbor(Direction.UP),
								ownHero.getTile());
						dBlock = Direction.UP;
						sBlock = blockL.get(i + 1);

					}
					if (shortest == zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.UP),
							ownHero.getTile())) {
						dSBlock = Direction.UP;
						sDBlock = blockL.get(i + 1);
					}
				}
				if (zone.getTiles()
						.contains(
								blockL.get(i + 1).getTile()
										.getNeighbor(Direction.LEFT))) {
					if (shortest > zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.LEFT),
							ownHero.getTile())) {
						shortest = zone.getTileDistance(blockL.get(i + 1)
								.getTile().getNeighbor(Direction.LEFT),
								ownHero.getTile());
						dBlock = Direction.LEFT;
						sBlock = blockL.get(i + 1);
					}
					if (shortest == zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.LEFT),
							ownHero.getTile())) {
						dSBlock = Direction.LEFT;
						sDBlock = blockL.get(i + 1);
					}
				}
				if (zone.getTiles().contains(
						blockL.get(i + 1).getTile()
								.getNeighbor(Direction.RIGHT))) {
					if (shortest > zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.RIGHT),
							ownHero.getTile())) {
						shortest = zone.getTileDistance(blockL.get(i + 1)
								.getTile().getNeighbor(Direction.RIGHT),
								ownHero.getTile());
						dBlock = Direction.RIGHT;
						sBlock = blockL.get(i + 1);
					}
					if (shortest == zone.getTileDistance(blockL.get(i + 1)
							.getTile().getNeighbor(Direction.RIGHT),
							ownHero.getTile())) {
						dSBlock = Direction.RIGHT;
						sDBlock = blockL.get(i + 1);
					}
				}

			}

			AiTile sTile1 = sBlock.getTile().getNeighbor(dBlock);
			AiTile sTile2 = null;
			if (dSBlock != null && sDBlock != null)
				sTile2 = sDBlock.getTile().getNeighbor(dSBlock);

			if (distance(tile.getCol(), tile.getRow(), sTile1.getCol(),
					sTile1.getRow()) == 0) {
				result = true;
			}
			if (sTile2 != null)
				if (distance(tile.getCol(), tile.getRow(), sTile2.getCol(),
						sTile2.getRow()) == 0) {
					result = true;
				}

		}

		return result;
	}
}