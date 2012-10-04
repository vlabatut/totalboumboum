package org.totalboumboum.ai.v201112.ais.demireloz.v3;

import java.util.LinkedList;

import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion.Competition;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion.Convenience;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion.Danger;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion.IsEnemyReachable;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion.NbrAdjacentWall;
import org.totalboumboum.ai.v201112.ais.demireloz.v3.criterion.Time;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<DemirelOz> {
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public UtilityHandler(DemirelOz ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		verbose = false;

	}

	// Mode collect cases
	private final String caseName1 = "Visible Item";
	private final String caseName2 = "Tile close to a destructible wall in danger";
	private final String caseName3 = "Tile close to a destructible wall";
	private final String caseName4 = "Dumb Tile Collect";
	// Mode attack cases
	private final String caseName5 = "Block Enemy";
	private final String caseName6 = "Tile in Range of An Enemy";
	private final String caseName7 = "Dumb Tile Attack";
	private final String caseName8 = "Tile Close to destructible wall close to enemy";

	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		AiHero ourhero = this.ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiTile aitile = ourtile;

		Queue<AiTile> qe = new LinkedList<AiTile>();
		qe.add(aitile);
		while (!qe.isEmpty()) {
			ai.checkInterruption();
			aitile = qe.poll();
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (aitile.getNeighbor(direction).getBombs().isEmpty()
						&& aitile.getNeighbor(direction).getBlocks().isEmpty()
						&& !qe.contains(aitile.getNeighbor(direction))
						&& !result.contains(aitile.getNeighbor(direction))
						&& !aitile.getNeighbor(direction).equals(ourtile)) {
					qe.add(aitile.getNeighbor(direction));
				}
			}
			if (!qe.isEmpty()) {

				aitile = qe.peek();

				result.add(aitile);

			} else {
				break;
			}
		}

		result.add(ourtile);

		return result;

	}

	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();
		Time time = new Time(ai);
		Convenience convenience = new Convenience(ai);
		Competition competition = new Competition(ai);
		NbrAdjacentWall nbrAdjacentWall = new NbrAdjacentWall(ai);
		IsEnemyReachable isenemyreachable = new IsEnemyReachable(ai);
		Danger danger = new Danger(ai);

		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(convenience);
		criteria.add(competition);
		AiUtilityCase case1 = new AiUtilityCase(caseName1, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(competition);
		AiUtilityCase case2 = new AiUtilityCase(caseName2, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		criteria.add(nbrAdjacentWall);
		AiUtilityCase case3 = new AiUtilityCase(caseName3, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(nbrAdjacentWall);
		criteria.add(danger);
		AiUtilityCase case6 = new AiUtilityCase(caseName6, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		AiUtilityCase case7 = new AiUtilityCase(caseName7, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(time);
		AiUtilityCase case4 = new AiUtilityCase(caseName4, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(isenemyreachable);
		criteria.add(time);
		AiUtilityCase case8 = new AiUtilityCase(caseName8, criteria);
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(danger);
		AiUtilityCase case5 = new AiUtilityCase(caseName5, criteria);
		cases.put(caseName1, case1);
		cases.put(caseName2, case2);
		cases.put(caseName3, case3);
		cases.put(caseName4, case4);
		cases.put(caseName5, case5);
		cases.put(caseName6, case6);
		cases.put(caseName7, case7);
		cases.put(caseName8, case8);
		AiUtilityCombination combi;
		AiUtilityCombination combi1;

		// ///////////////////////////////////COLLECT///////////////////////////////////////
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(convenience, true);
			combi.setCriterionValue(competition, false);
			referenceUtilities.put(combi, 20);

		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(convenience, false);
			combi.setCriterionValue(competition, false);
			referenceUtilities.put(combi, 19);

		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(convenience, true);
			combi.setCriterionValue(competition, true);
			referenceUtilities.put(combi, 18);
		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(convenience, true);
			combi.setCriterionValue(competition, false);
			referenceUtilities.put(combi, 17);

		}

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(convenience, false);
			combi.setCriterionValue(competition, true);
			referenceUtilities.put(combi, 16);

		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(convenience, true);
			combi.setCriterionValue(competition, true);
			referenceUtilities.put(combi, 15);

		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(convenience, false);
			combi.setCriterionValue(competition, false);
			referenceUtilities.put(combi, 14);
		}
		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(convenience, false);
			combi.setCriterionValue(competition, true);
			referenceUtilities.put(combi, 13);
		}
		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(competition, false);
			referenceUtilities.put(combi, 12);

		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(competition, true);
			referenceUtilities.put(combi, 11);

		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(competition, false);
			referenceUtilities.put(combi, 10);

		}

		{
			combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(competition, true);
			referenceUtilities.put(combi, 9);

		}

		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(nbrAdjacentWall, 3);
			referenceUtilities.put(combi, 8);
		}

		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(nbrAdjacentWall, 2);
			referenceUtilities.put(combi, 7);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(time, true);
			combi.setCriterionValue(nbrAdjacentWall, 1);
			referenceUtilities.put(combi, 6);
		}

		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(nbrAdjacentWall, 3);
			referenceUtilities.put(combi, 5);
		}

		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(nbrAdjacentWall, 2);
			referenceUtilities.put(combi, 4);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(time, false);
			combi.setCriterionValue(nbrAdjacentWall, 1);
			referenceUtilities.put(combi, 3);
		}
		{
			combi1 = new AiUtilityCombination(case4);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 2);

		}
		{
			combi1 = new AiUtilityCombination(case4);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 1);

		}

		// ////////////////////////////////////ATTACK//////////////////////////////////////////////////
		// Case8 contains combinations used only in attack mode when we don't
		// have an enemy in the selected tiles.
		// That means they wont be used in the same time as the general
		// combinations.
		// That's why we gave them similar utility valors.
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 3);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 50);
		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 3);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 49);
		}

		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 2);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 40);
		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 2);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 39);
		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 1);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 30);
		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 1);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 27);
		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 6);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 29);
		}

		{
			combi1 = new AiUtilityCombination(case5);
			combi1.setCriterionValue(danger, true);
			referenceUtilities.put(combi1, 15);
		}

		{
			combi1 = new AiUtilityCombination(case5);
			combi1.setCriterionValue(danger, false);
			referenceUtilities.put(combi1, 14);
		}
		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, true);
			combi1.setCriterionValue(nbrAdjacentWall, 3);
			referenceUtilities.put(combi1, 13);
		}

		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, true);
			combi1.setCriterionValue(nbrAdjacentWall, 2);
			referenceUtilities.put(combi1, 12);
		}

		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, true);
			combi1.setCriterionValue(nbrAdjacentWall, 1);
			referenceUtilities.put(combi1, 11);

		}
		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, true);
			combi1.setCriterionValue(nbrAdjacentWall, 0);
			referenceUtilities.put(combi1, 10);

		}

		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, false);
			combi1.setCriterionValue(nbrAdjacentWall, 3);
			referenceUtilities.put(combi1, 9);

		}
		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, false);
			combi1.setCriterionValue(nbrAdjacentWall, 2);
			referenceUtilities.put(combi1, 8);

		}
		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, false);
			combi1.setCriterionValue(nbrAdjacentWall, 1);
			referenceUtilities.put(combi1, 7);

		}
		{
			combi1 = new AiUtilityCombination(case6);
			combi1.setCriterionValue(danger, false);
			combi1.setCriterionValue(nbrAdjacentWall, 0);
			referenceUtilities.put(combi1, 6);

		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 5);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 5);
		}
		{
			combi1 = new AiUtilityCombination(case7);
			combi1.setCriterionValue(time, true);
			referenceUtilities.put(combi1, 4);

		}
		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 6);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 3);
		}

		{
			combi1 = new AiUtilityCombination(case8);
			combi1.setCriterionValue(isenemyreachable, 5);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 2);
		}
		{
			combi1 = new AiUtilityCombination(case7);
			combi1.setCriterionValue(time, false);
			referenceUtilities.put(combi1, 1);

		}
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();

		AiUtilityCase result = null;

		Time time = new Time(ai);
		Convenience convenience = new Convenience(ai);
		Competition competition = new Competition(ai);
		NbrAdjacentWall nbradjacentwall = new NbrAdjacentWall(ai);
		IsEnemyReachable isenemyreachable = new IsEnemyReachable(ai);
		Danger danger = new Danger(ai);

		boolean herocontrol = true;
		// We control if we have enemies in the list of tiles returned by
		// SelectTiles.

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
		AiMode mode = this.ai.modeHandler.getMode();
		// Tile identification for collect mode

		if (mode.equals(AiMode.COLLECTING)) {
			// Visible item identification
			if (tile.getItems().contains(AiItemType.EXTRA_BOMB)
					|| tile.getItems().contains(AiItemType.EXTRA_FLAME)) {
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();

				criteria.add(time);
				criteria.add(convenience);
				criteria.add(competition);

				return result = new AiUtilityCase(caseName1, criteria);
			}

			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				AiTile currentNeighbor = tile.getNeighbor(direction);

				boolean value = this.ai.getWallInDanger(currentNeighbor);
				// Destructible wall in danger identification
				if (value) {
					Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
					criteria.add(time);
					criteria.add(competition);
					return result = new AiUtilityCase(caseName2, criteria);
				}

			}

			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				for (AiBlock currentBlock : tile.getNeighbor(direction)
						.getBlocks()) {
					ai.checkInterruption();
					// Destructible wall identification
					if (currentBlock.isDestructible()) {
						Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
						criteria = new TreeSet<AiUtilityCriterion<?>>();
						criteria.add(time);
						criteria.add(nbradjacentwall);
						return result = new AiUtilityCase(caseName3, criteria);
					}
				}
			}

			{
				// Dumb tile collect identification
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(time);
				return result = new AiUtilityCase(caseName4, criteria);
			}

		}

		// Tile identification for attack mode
		if (mode == AiMode.ATTACKING) {
			// Block enemy identification
			if (this.ai.simBlock(tile)) {
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(danger);
				return result = new AiUtilityCase(caseName5, criteria);
			}
			// Tile in range of an enemy identification
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (this.ai.getAnEnemyInMyRange(tile, direction, 0)) {
					Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
					criteria = new TreeSet<AiUtilityCriterion<?>>();
					criteria.add(nbradjacentwall);
					criteria.add(danger);
					return result = new AiUtilityCase(caseName6, criteria);
				}
			}

			// Tile Close to destructible wall close to enemy identification
			for (Direction direction : Direction.getPrimaryValues()) {
				ai.checkInterruption();
				if (herocontrol
						&& this.ai.controlOfDestructibleBlock(tile, direction) == true) {

					Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();

					criteria = new TreeSet<AiUtilityCriterion<?>>();
					criteria.add(isenemyreachable);
					criteria.add(time);
					return result = new AiUtilityCase(caseName8, criteria);
				}
			}
			// Dumb tile attack identification
			{
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(time);
				return result = new AiUtilityCase(caseName7, criteria);
			}
		}

		return result;
	}

	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();

	}
}
