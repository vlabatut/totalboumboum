package org.totalboumboum.ai.v201112.ais.kayukataskin.v3;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.ais._simplet.CommonTools;
import org.totalboumboum.ai.v201112.ais.kayukataskin.v3.criterion.CriterionFirst;
import org.totalboumboum.ai.v201112.ais.kayukataskin.v3.criterion.CriterionSecond;
import org.totalboumboum.ai.v201112.ais.kayukataskin.v3.criterion.CriterionThird;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * @author Pol Kayuka
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<KayukaTaskin>
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
	protected UtilityHandler(KayukaTaskin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
	
		initData();
	}

	/** */
	private AiZone zone = null;
	/** */
	private AiHero ownHero = null;
	/** */
	@SuppressWarnings("unused")
	private Dijkstra dijkstra = null;
	/** */
	@SuppressWarnings("unused")
	private CommonTools commonTools;
	/** */
	protected HashMap<AiTile,Boolean> bombTiles = new HashMap<AiTile,Boolean>();
	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	@SuppressWarnings("unused")
	private final String caseName1 = "CAS1";
	/** */
	@SuppressWarnings("unused")
	private final String caseName2 = "CAS2";
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	private void initData() throws StopRequestException
	{	ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		commonTools = ai.commonTools;
		

		CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		dijkstra = new Dijkstra(ai,ownHero,costCalculator,successorCalculator);
	}
	
	@Override
	protected void resetData() throws StopRequestException
	{	ai.checkInterruption();
		super.resetData();
		
		bombTiles.clear();
	}
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		boolean collecte=ai.modeHandler.isCollectPossible();
		if(collecte){
			List<AiItem> bonus=zone.getItems();
			List<AiBlock> wall=zone.getDestructibleBlocks();
			
			if(!bonus.isEmpty()){
				for(AiItem item: bonus)
				{	ai.checkInterruption();	
					AiTile tile = item.getTile();
					result.add(tile);
				}
			}
			
			if(!wall.isEmpty()){
			
				for(AiBlock block: wall)
				{	ai.checkInterruption();	
					AiTile tile = block.getTile();
					result.addAll(tile.getNeighbors());
				}
			}
		}
		else{
			List<AiHero> heros=zone.getHeroes();
			heros.remove(ownHero);
			int range=ownHero.getBombRange();
			
			for(AiHero hero:heros){
				ai.checkInterruption();
				AiTile up=hero.getTile().getNeighbor(Direction.UP);
				AiTile down=hero.getTile().getNeighbor(Direction.DOWN);
				AiTile left=hero.getTile().getNeighbor(Direction.LEFT);
				AiTile right=hero.getTile().getNeighbor(Direction.RIGHT);
				
				for(int i=0;i<range;i++){
					ai.checkInterruption();
					result.add(up);
					up=up.getNeighbor(Direction.UP);
				}
				
				for(int i=0;i<range;i++){
					ai.checkInterruption();
					result.add(down);
					down=down.getNeighbor(Direction.DOWN);
				}
				
				for(int i=0;i<range;i++){
					ai.checkInterruption();
					result.add(left);
					left=left.getNeighbor(Direction.LEFT);
				}
				
				for(int i=0;i<range;i++){
					ai.checkInterruption();
					result.add(right);
					right=right.getNeighbor(Direction.RIGHT);
				}
			}
		}
		
		if(result.isEmpty()){
			AiTile current=ownHero.getTile();
			result.add(current);
			
			bombTiles.put(current, false);
		}
		
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		//  à compléter afin d'initialiser les critères 
		// et les cas. la méthode est appelée une seule fois
		
		//  le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un 
		// exemple, vous devez définir vos propres critères !
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode

		// on définit les critères
		CriterionFirst criterionFirst = new CriterionFirst(ai);
		CriterionSecond criterionSecond = new CriterionSecond(ai);
		CriterionThird criterionThird = new CriterionThird(ai);
		
		AiUtilityCase caseCollectBonus;
		AiUtilityCase caseCollectWall;
		AiUtilityCase caseAttack;
		
		{
			Set<AiUtilityCriterion<?>> criteria= new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(criterionFirst);
criteria.add(criterionSecond);
			caseCollectBonus=new AiUtilityCase("CASE_BONUS",criteria);
			cases.put("CASE_BONUS",caseCollectBonus);
		}
		
		{
			Set<AiUtilityCriterion<?>> criteria= new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(criterionSecond);
			caseCollectWall=new AiUtilityCase("CASE_WALL",criteria);
			cases.put("CASE_WALL",caseCollectWall);
		}
		
		{
			Set<AiUtilityCriterion<?>> criteria= new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(criterionThird);
			caseAttack=new AiUtilityCase("CASE_ATTACK",criteria);
			cases.put("CASE_ATTACK",caseAttack);
		}
		
		// on affecte les valeurs d'utilité
		int utility = 1;
		AiUtilityCombination combi;
		// le premier cas a un critère binaire et un entier [1,3],
		// donc il y a 6 combinaisons possibles
		// ici, pour l'exemple, on leur affecte des utilités
		// en respectant l'ordre des valeurs, mais bien sûr
		// ce n'est pas du tout obligatoire
		{	// on crée la combinaison (vide pour l'instant)
			combi = new AiUtilityCombination(caseCollectBonus);
			// on affecte les valeurs de chaque critère
			combi.setCriterionValue(criterionFirst,false);
			combi.setCriterionValue(criterionSecond,1);
			// on rajoute la combinaison dans la map, avec son utilité
			referenceUtilities.put(combi,utility);
			// on incrémente l'utilité pour la combinaison suivante
			utility++;
		}
		{	combi = new AiUtilityCombination(caseCollectBonus);
			combi.setCriterionValue(criterionFirst,false);
			combi.setCriterionValue(criterionSecond,2);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(caseCollectBonus);
			combi.setCriterionValue(criterionFirst,false);
			combi.setCriterionValue(criterionSecond,3);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(caseCollectBonus);
			combi.setCriterionValue(criterionFirst,true);
			combi.setCriterionValue(criterionSecond,1);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(caseCollectBonus);
			combi.setCriterionValue(criterionFirst,true);
			combi.setCriterionValue(criterionSecond,2);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		{	combi = new AiUtilityCombination(caseCollectBonus);
			combi.setCriterionValue(criterionFirst,true);
			combi.setCriterionValue(criterionSecond,3);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		
		// le deuxième cas a un critère entier [1,3] 
		// et un critère chaine de caractères à 5 valeurs
		// possibles, donc ça faiut 15 combinaisons au total
		// la définition de l'utilité de ces combinaisons
		// se fait de la même façon que ci dessus
		{	combi = new AiUtilityCombination(caseAttack);
//			combi = new AiUtilityCombination(caseCollectBonus);
//			combi.setCriterionValue(criterionSecond,1);
			combi.setCriterionValue(criterionThird,CriterionThird.VALUE1);
			referenceUtilities.put(combi,utility);
			utility++;
		}
		// etc.
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		
		//  à compléter pour identifier le cas associé
		// à la case passée en paramètre
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
		
		return result;
	}
	
	/**
	 * 
	 * @param center
	 * 		description manquante !
	 * @param hero
	 * 		description manquante !
	 * @return
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public Set<AiTile> getTilesForRadius(AiTile center, AiHero hero) throws StopRequestException
	{	ai.checkInterruption();
		// init
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		
		// on ne teste pas la case de la cible, on la considère comme ok
		
		// par contre, on teste celles situées à porté de bombes
		for(Direction d: Direction.getPrimaryValues())
		{	ai.checkInterruption();
			AiTile neighbor = center;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				if(neighbor.isCrossableBy(fire))
					result.add(neighbor);
				else
					blocked = true;
				i++;
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		//  à redéfinir, si vous voulez afficher d'autres informations
	}
}
