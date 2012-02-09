package org.totalboumboum.ai.v201112.ais.balcetin.v3;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.AcceptableDistance;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.AttackMostRelevantHero;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.CollectRelevance;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.Competition;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.PossibleDeadOpponentCount;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.Threat;
import org.totalboumboum.ai.v201112.ais.balcetin.v3.criterion.WallCount;

/**
 * Class that calcuklates utility values for selected tiles.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<BalCetin> {
	
	protected UtilityHandler(BalCetin ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		//no text output on the console
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	
	// Case Names
	private final String Collect_Visible_Item = "VISIBLE_ITEM";
	private final String Collect_Wall_Neighbor = "WALL_NEIGHBOR";
	private final String Attack_Attack = "ATTACK";

	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();

		//we are selecting just tiles which our hero can pass by walking.
		Set<AiTile> result = new TreeSet<AiTile>();
		TileProcess tp = new TileProcess(this.ai);
		result.addAll(tp.getwalkableTiles());

		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();

		// Defining the criterions
		AcceptableDistance acceptableDistance = new AcceptableDistance(ai);
		CollectRelevance collectRelevance = new CollectRelevance(ai);
		Competition competition = new Competition(ai);
		Threat threat = new Threat(ai);
		WallCount wallCount = new WallCount(ai);
		AttackMostRelevantHero mostRelevantHero = new AttackMostRelevantHero(ai);
		PossibleDeadOpponentCount possibleDeadOpponentCount = new PossibleDeadOpponentCount(
				ai);

		// Defining first case using 4 criterions
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(collectRelevance);
		criteria.add(competition);
		criteria.add(threat);
		criteria.add(acceptableDistance);
		AiUtilityCase caseCollectVisibleItem = new AiUtilityCase(
				Collect_Visible_Item, criteria);

		// Defining second case using 3 criterions
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(acceptableDistance);
		criteria.add(threat);
		criteria.add(wallCount);
		AiUtilityCase caseWallNeighbor = new AiUtilityCase(
				Collect_Wall_Neighbor, criteria);

		// Defining "attack case" using 4 criterions
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(acceptableDistance);
		criteria.add(threat);
		criteria.add(mostRelevantHero);
		criteria.add(possibleDeadOpponentCount);
		AiUtilityCase caseAttack = new AiUtilityCase(Attack_Attack, criteria);

		
		cases.put(Collect_Visible_Item, caseCollectVisibleItem);
		cases.put(Collect_Wall_Neighbor, caseWallNeighbor);
		cases.put(Attack_Attack, caseAttack);

		// Affecting the utility values.

		AiUtilityCombination CollectComb;
		// there are 4 criterions binary.So 16 different possibilities for
		// VisibleItem.
		// //////////////////////////////////////////////////////////////////////////////////////////
		// wallCount has a domain [0,3]
		// case WallNeighbor --> collect mode
		// there are 2 binary criterions and a criterion which has a domain of
		// [0,3] , so 16 different combinations.

		// here's the order of the combinations
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 32);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 31);

		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 30);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 29);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 28);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 27);
		}

		{ // creating empty combination.
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			// Affecting the values for each criterion
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			// adding combination in the map with its utility value
			referenceUtilities.put(CollectComb, 26);

		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 25);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 24);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 23);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 22);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 21);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 20);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 19);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 18);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 17);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 16);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 15);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 14);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 13);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 12);
		}

		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 11);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 10);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 9);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 8);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 7);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 6);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 5);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 4);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 3);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 2);
		}

		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 1);
		}

		//Creating and ordering Attack Combinations.
		//Attack mode has 3 binary criterions.
		//and a integer criterion has a domain of [0,3]. So 32 different combinations.

		AiUtilityCombination AttackComb;

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 32);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 31);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 30);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 29);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 28);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 27);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 26);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 25);
		}
		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 24);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 23);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 22);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 21);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 20);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 19);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 18);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, false);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 17);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 16);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 15);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 14);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 13);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 12);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 11);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 10);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 9);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 8);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 3);
			referenceUtilities.put(AttackComb, 7);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 2);
			referenceUtilities.put(AttackComb, 6);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 1);
			referenceUtilities.put(AttackComb, 5);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 4);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, true);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 3);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, true);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 2);
		}

		{
			AttackComb = new AiUtilityCombination(caseAttack);
			AttackComb.setCriterionValue(acceptableDistance, false);
			AttackComb.setCriterionValue(threat, true);
			AttackComb.setCriterionValue(mostRelevantHero, false);
			AttackComb.setCriterionValue(possibleDeadOpponentCount, 0);
			referenceUtilities.put(AttackComb, 1);
		}

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		AiMode mode = ai.modeHandler.getMode();
		// if mode is collect.
		if (mode == AiMode.COLLECTING) {
			if (tile.getItems().isEmpty())
				return this.cases.get(Collect_Wall_Neighbor); // if the tile has
																// no bonus,then
																// returns
																// WallNeighbor
																// utilities.
			else
				return this.cases.get(Collect_Visible_Item);// if the tile has
															// bonus,then
															// returns visible
															// Item utilities.
		} else
			//returns Attack Mode.
			return this.cases.get(Attack_Attack);
	

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();
		super.updateOutput();

	}
}
