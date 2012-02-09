package org.totalboumboum.ai.v201112.ais.balcetin.v2;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion.AcceptableDistance;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion.CollectRelevance;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion.Competition;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion.Threat;
import org.totalboumboum.ai.v201112.ais.balcetin.v2.criterion.WallCount;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<BalCetin> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(BalCetin ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		//  à compléter
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String Collect_Visible_Item = "VISIBLE_ITEM";
	private final String Collect_Wall_Neighbor = "WALL_NEIGHBOR";

	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		TileProcess tp = new TileProcess(this.ai);
		result.addAll(tp.getwalkableTiles());
		for (AiTile aiTile : result) {
			ai.checkInterruption();
			System.out.println("\nselecttilesresult : " + aiTile);
		}

		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();
		
		//Defining the criterions
		AcceptableDistance acceptableDistance = new AcceptableDistance(ai);
		CollectRelevance collectRelevance = new CollectRelevance(ai);
		Competition competition = new Competition(ai);
		Threat threat = new Threat(ai);
		WallCount wallCount = new WallCount(ai);

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

		// on met les cas dans la map prévue à cet effet
		// ceci permettra de les retrouver facilement plus tard,
		// en particulier dans la méthode identifyCase()
		cases.put(Collect_Visible_Item, caseCollectVisibleItem);
		cases.put(Collect_Wall_Neighbor, caseWallNeighbor);

		// Affecting the utility values.

		AiUtilityCombination CollectComb;
		//there are 4 criterions binary.So 16 different possibilities.
		
		{ //creating empty combination.
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			// Affecting the values for each criterion
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			//adding combination in the map with its utility value
			referenceUtilities.put(CollectComb, 1);
			

		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 2);

		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 3);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 4);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 5);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 6);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 7);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, false);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 8);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 9);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 10);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 11);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 12);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 13);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 14);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, false);
			referenceUtilities.put(CollectComb, 15);
		}
		{
			CollectComb = new AiUtilityCombination(caseCollectVisibleItem);
			CollectComb.setCriterionValue(collectRelevance, true);
			CollectComb.setCriterionValue(competition, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(acceptableDistance, true);
			referenceUtilities.put(CollectComb, 16);
		}

		////////////////////////////////////////////////////////////////////////////////////////////
		// wallCount has a domain [0,3]
		// case WallNeighbor --> collect mode
		//there are 2 binary criterions and a criterion which has a domain of [0,3] , so 16 different combinations.
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 17);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 18);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 19);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 20);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 21);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 22);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 23);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, false);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 24);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 25);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 26);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 27);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, false);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 28);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 0);
			referenceUtilities.put(CollectComb, 29);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 1);
			referenceUtilities.put(CollectComb, 30);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 2);
			referenceUtilities.put(CollectComb, 31);
		}
		{
			CollectComb = new AiUtilityCombination(caseWallNeighbor);
			CollectComb.setCriterionValue(acceptableDistance, true);
			CollectComb.setCriterionValue(threat, true);
			CollectComb.setCriterionValue(wallCount, 3);
			referenceUtilities.put(CollectComb, 32);
		}

		
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCase result = null;
		AiMode mode = ai.modeHandler.getMode();
		
		//if mode is collect.
		if (mode == AiMode.COLLECTING) {
			if (tile.getItems().isEmpty())
				return this.cases.get(Collect_Wall_Neighbor); //if the tile has no bonus,then returns WallNeighbor utilities.
			else
				return this.cases.get(Collect_Visible_Item);//if the tile has bonus,then returns visible Item utilities.
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		
	}
}
