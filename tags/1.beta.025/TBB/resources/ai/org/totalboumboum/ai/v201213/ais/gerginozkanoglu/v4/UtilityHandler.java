
package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion.Competition;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion.Distance;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion.NumberOfDestructibleWalls;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion.Pertinence;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion.Security;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v4.criterion.Time;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 *
 * 
 * @author Tuğçe Gergin
 * @author Seçil Özkanoğlu
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<GerginOzkanoglu> {
	
	/**
	 * For case "Neigborhood of destructible wall(s)", this is the base.
	 */
	private static int DESTWALL_COUNT = 0;


	/**
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(GerginOzkanoglu ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException {
		ai.checkInterruption();

		
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		TileCalculation calculate =new TileCalculation(this.ai);
        Set <AiTile>  result =new TreeSet<AiTile>();
        result.addAll(calculate.allAccesibleTiles(this.ai.getZone().getOwnHero().getTile()));
        return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();

		new Security(ai);
		new Time(ai);
		new Pertinence(ai);
		new Competition(ai);
		new NumberOfDestructibleWalls(ai);
		new Distance(ai);
		
		

	}

	// ///////////////////////////////////////////////////////////////
	// CASE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// Collecting Mode
	/**
	 * Visible item
	 */
	private final String VISIBLE_ITEM = "VisibleItem";

	/**
	 * Neighborhood to an enemy
	 */
	private final String NEIGHBORHOOD_WALL = "NeigborhoodWall";

	/**
	 * Case null of collecting mode
	 */
	private final String C_CASE_NULL = "CollectCaseNull";
	// Attack Mode
	/**
	 * Blocking an enemy
	 */
	private final String BLOCKING_ENEMY = "BlockingEnemy";

	/**
	 * Closest enemy to destructible walls or visible items.
	 */
	private final String CLOSEST_ENEMY = "ClosestEnemy";
	
	/**
	 * when there is no enemy in all accessible tiles we will 
	 * use this case in order to access the enemy
	 */
	private final String UNREACHABLE_ENEMY = "UnreachableEnemy";
	/**
	 * Case null of attack mode.
	 */
	private final String A_CASE_NULL = "AttackCaseNull";

	@Override
	protected void initCases() throws StopRequestException {
		ai.checkInterruption();
		Set<AiUtilityCriterion<?, ?>> criteria;

		// Collecting mode
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Time.NAME));
		criteria.add(criterionMap.get(Competition.NAME));
		criteria.add(criterionMap.get(Pertinence.NAME));
		new AiUtilityCase(ai, VISIBLE_ITEM, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Time.NAME));
		criteria.add(criterionMap.get(NumberOfDestructibleWalls.NAME));
		new AiUtilityCase(ai, NEIGHBORHOOD_WALL, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Time.NAME));
		new AiUtilityCase(ai, C_CASE_NULL, criteria);

		// Attack Mode
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Time.NAME));
		new AiUtilityCase(ai, BLOCKING_ENEMY, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Time.NAME));
		new AiUtilityCase(ai, CLOSEST_ENEMY, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Distance.NAME));
		new AiUtilityCase(ai, UNREACHABLE_ENEMY, criteria);

		
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Security.NAME));
		criteria.add(criterionMap.get(Time.NAME));
		new AiUtilityCase(ai, A_CASE_NULL, criteria);

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCase result = null;
		Security security = (Security) (criterionMap.get(Security.NAME));
		Time time =(Time) (criterionMap.get(Time.NAME));
		Pertinence pertinence =(Pertinence) (criterionMap.get(Pertinence.NAME));
		Competition competition = (Competition) (criterionMap.get(Competition.NAME));
		NumberOfDestructibleWalls numberOfDestructibleWalls =(NumberOfDestructibleWalls) (criterionMap.get(NumberOfDestructibleWalls.NAME));
		Distance distance = (Distance) (criterionMap.get(Distance.NAME));
		TileCalculation calculate = new TileCalculation(this.ai);
		//our agent's mode is collecting.
		if(this.ai.getModeHandler().getMode().equals(AiMode.COLLECTING))
		{
			if(!tile.getItems().isEmpty()) // criteria is visible item
			{
             Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
             criterion.add(security);
             criterion.add(time);
             criterion.add(pertinence);
             criterion.add(competition);
             result = new AiUtilityCase(this.ai,VISIBLE_ITEM, criterion);
			}
			else if(calculate.numberOfDestructibleWalls(tile) > DESTWALL_COUNT)
			{
				//then we are in the case "Neighborhood of dest wall"
				 Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
				 criterion.add(security);
				 criterion.add(time);
				 criterion.add(numberOfDestructibleWalls);
				 result = new AiUtilityCase(this.ai, NEIGHBORHOOD_WALL, criterion);
			}
			else
			{
				Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
				criterion.add(security);
				criterion.add(time);
				result =  new AiUtilityCase(this.ai, C_CASE_NULL, criterion);
				
			}
		}
		else // mode is attacking.
		{
			if(calculate.blockingEnemyIsPossible(tile))
			   {
			    Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
			    criterion.add(security);
			    criterion.add(time);
			    result =  new AiUtilityCase(this.ai,BLOCKING_ENEMY, criterion);
			   }
			else 
			{
//				//closest enemy
				if(calculate.threatenEnemy(tile, this.ai.getZone().getOwnHero().getBombRange()))
				{
					Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
					criterion.add(security);
					criterion.add(time);
					result =  new AiUtilityCase(this.ai,CLOSEST_ENEMY, criterion);
					
				}
				else if(!calculate.isThereEnemyInAllAccessibleTiles(this.ai.getZone().getOwnHero().getTile()))
				{
					Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
					criterion.add(security);
					criterion.add(distance);
					result =  new AiUtilityCase(this.ai,UNREACHABLE_ENEMY, criterion);
				}
				else
				{
					Set<AiUtilityCriterion<?,?>> criterion = new TreeSet<AiUtilityCriterion<?, ?>>();
					criterion.add(security);
					criterion.add(time);
					result =  new AiUtilityCase(this.ai,A_CASE_NULL, criterion);
					
				}
				
			}
			
			
		}
       
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// REFERENCE /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException {
		ai.checkInterruption();

		int utility;
		AiUtilityCombination combi;
		AiMode mode;

		{
			mode = AiMode.COLLECTING;
			utility = 0;
			{ 
				combi = new AiUtilityCombination(caseMap.get(C_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{ 
				combi = new AiUtilityCombination(caseMap.get(C_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{ 
				combi = new AiUtilityCombination(caseMap.get(C_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{ 
				combi = new AiUtilityCombination(caseMap.get(C_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(
						caseMap.get(NEIGHBORHOOD_WALL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(NumberOfDestructibleWalls) criterionMap
								.get(NumberOfDestructibleWalls.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(VISIBLE_ITEM));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Competition) criterionMap.get(Competition.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME), 2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}

		}

		// on traite maintenant le mode attaque
		{
			mode = AiMode.ATTACKING;
			utility = 0;

			{
				combi = new AiUtilityCombination(caseMap.get(A_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CLOSEST_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(BLOCKING_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(UNREACHABLE_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Distance) criterionMap.get(Distance.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(UNREACHABLE_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Distance) criterionMap.get(Distance.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(UNREACHABLE_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Distance) criterionMap.get(Distance.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(UNREACHABLE_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Distance) criterionMap.get(Distance.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(A_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(A_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(A_CASE_NULL));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(BLOCKING_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
	
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CLOSEST_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.FALSE);
	
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CLOSEST_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(BLOCKING_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CLOSEST_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(BLOCKING_ENEMY));
				combi.setCriterionValue(
						(Security) criterionMap.get(Security.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Time) criterionMap.get(Time.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			
		}
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
