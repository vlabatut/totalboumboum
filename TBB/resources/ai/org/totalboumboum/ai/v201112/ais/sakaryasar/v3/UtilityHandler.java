package org.totalboumboum.ai.v201112.ais.sakaryasar.v3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion.CriterionChaineReaction;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion.CriterionDanger;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion.CriterionDestructibleWalls;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion.CriterionEnemies;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion.CriterionEnemyDirection;
import org.totalboumboum.ai.v201112.ais.sakaryasar.v3.criterion.CriterionPertinance;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<SakarYasar>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		verbose = false;	
	}
	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String caseName1 = "CAS1";
	private final String caseName2 = "CAS2";
	private final String caseName3 = "CAS3";

	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();

		Set<AiTile> result = new TreeSet<AiTile>();
		
		result.add(ai.getZone().getOwnHero().getTile());

		AiTile left , up, right, down;
		Queue<AiTile> qTile = new LinkedList<AiTile>();
		
		qTile.add(ai.getZone().getOwnHero().getTile());

		while(!qTile.isEmpty()){
			ai.checkInterruption();
			if(!result.contains(qTile.element()))
				result.add(qTile.element());

			left = qTile.element().getNeighbor(Direction.LEFT);
			right = qTile.element().getNeighbor(Direction.RIGHT);
			up = qTile.element().getNeighbor(Direction.UP);
			down =qTile.element().getNeighbor(Direction.DOWN);
			
			if(up.getBlocks().isEmpty() && up.getBombs().isEmpty() && !result.contains(up))
				qTile.add(up);
			if(down.getBlocks().isEmpty() && down.getBombs().isEmpty() && !result.contains(down))
				qTile.add(down);
			if(left.getBlocks().isEmpty() && left.getBombs().isEmpty() && !result.contains(left))
				qTile.add(left);
			if(right.getBlocks().isEmpty() && right.getBombs().isEmpty() &&!result.contains(right))
				qTile.add(right);
			
			qTile.remove();
			
		}
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();

		// on définit les critères
		CriterionPertinance crPertinance = new CriterionPertinance(ai);
		CriterionChaineReaction crReaction = new CriterionChaineReaction(ai);
		CriterionDanger crDanger = new CriterionDanger(ai);
		CriterionDestructibleWalls crWalls = new CriterionDestructibleWalls(ai);
		CriterionEnemies  crEnemies = new CriterionEnemies(ai);
		CriterionEnemyDirection crEnemyDirection = new CriterionEnemyDirection(ai);
		
		
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(crDanger);
		criteria.add(crPertinance);
		criteria.add(crEnemies);
		AiUtilityCase case1 = new AiUtilityCase(caseName1,criteria);

		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(crDanger);
		criteria.add(crWalls);
		AiUtilityCase case2 = new AiUtilityCase(caseName2,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(crDanger);
		criteria.add(crReaction);
		criteria.add(crEnemyDirection);
		criteria.add(crWalls);
		AiUtilityCase case3 = new AiUtilityCase(caseName3,criteria);

		cases.put(caseName1,case1);
		
		int utility = 10;
		AiUtilityCombination combi;

		{
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}

		{	
			combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,true);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,false);
			referenceUtilities.put(combi,utility);
			utility++;
		}

		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,true);
			referenceUtilities.put(combi,0);
		}

		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,true);
			referenceUtilities.put(combi,0);
		}
		
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,false);
			combi.setCriterionValue(crEnemies,false);
			referenceUtilities.put(combi,0);
		}
		
		{	combi = new AiUtilityCombination(case1);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crPertinance,true);
			combi.setCriterionValue(crEnemies,false);
			referenceUtilities.put(combi,0);
		}

		////////////////////////////////////////////////////////
		//cas 2		////////////////////////////////////////////
		////////////////////////////////////////////////////////
		utility = 5;
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crWalls,0);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crWalls,1);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crWalls,2);
			referenceUtilities.put(combi,utility);
			utility++;
		}

		
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crWalls,0);
			referenceUtilities.put(combi,0);
		}
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crWalls,1);
			referenceUtilities.put(combi,0);
		}
		{	combi = new AiUtilityCombination(case2);
			combi.setCriterionValue(crDanger,true);
			combi.setCriterionValue(crWalls,2);
			referenceUtilities.put(combi,0);
		}

		utility = 4;
		////////////////////////////////////////////////////////
		//cas 3		////////////////////////////////////////////
		////////////////////////////////////////////////////////
		//4
		{	combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crReaction,false);
			combi.setCriterionValue(crEnemyDirection,false);
			combi.setCriterionValue(crWalls,0);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		//5
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//6
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//7
		{	combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger,false);
			combi.setCriterionValue(crReaction,true);
			combi.setCriterionValue(crEnemyDirection,false);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		//8
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//9
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		
		//10
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//11
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//12
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, utility);
			utility++;
		}

		//13

		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//14
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		//15
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, false);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, utility);
			utility++;
		}
		
		//0
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, 0);
		}

		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, false);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, 0);
		}
		
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, false);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, 0);
		}
		
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 0);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 1);
			referenceUtilities.put(combi, 0);
		}
		{
			combi = new AiUtilityCombination(case3);
			combi.setCriterionValue(crDanger, true);
			combi.setCriterionValue(crReaction, true);
			combi.setCriterionValue(crEnemyDirection, true);
			combi.setCriterionValue(crWalls, 2);
			referenceUtilities.put(combi, 0);
		}	
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();

		AiUtilityCase result ;
		

		CriterionPertinance crPertinance = new CriterionPertinance(ai);
		CriterionChaineReaction crReaction = new CriterionChaineReaction(ai);
		CriterionDanger crDanger = new CriterionDanger(ai);
		CriterionDestructibleWalls crWalls = new CriterionDestructibleWalls(ai);
		CriterionEnemies  crEnemies = new CriterionEnemies(ai);
		CriterionEnemyDirection crEnemyDirection = new CriterionEnemyDirection(ai);
		
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		if(ai.getModeHandler().getMode() == AiMode.COLLECTING){

			if(!tile.getItems().isEmpty()){
				criteria.add(crDanger);
				criteria.add(crPertinance);
				criteria.add(crEnemies);
				result = new AiUtilityCase(caseName1,criteria);
			}
			else{
				criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(crDanger);
				criteria.add(crWalls);
				result = new AiUtilityCase(caseName2,criteria);
			}
		}
		else{
			criteria = new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(crDanger);
			criteria.add(crReaction);
			criteria.add(crEnemyDirection);
			criteria.add(crWalls);
			result = new AiUtilityCase(caseName3,criteria);
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
