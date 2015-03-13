package org.totalboumboum.ai.v201112.ais.demirsazan.v3;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion.Bonus;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion.DestructibleMur;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion.DistanceCible;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion.DistanceMenace;
import org.totalboumboum.ai.v201112.ais.demirsazan.v3.criterion.Menace;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * @author Serdil Demir
 * @author Gökhan Sazan
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<DemirSazan>
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
	protected UtilityHandler(DemirSazan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String caseCollectePourBonus = "C_MUR";
	/** */
	private final String caseCollectePourMur = "C_BONUS";
	/** */
	private final String caseAttaquePourAdversaire = "A_ADVERSAIRE";
	/** */
	private final String caseAttaquePourMur = "A_MUR";
	/** */
	public List<AiTile> selectedTiles= ai.getZone().getTiles(); 
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		CommonTools commonTools = new CommonTools(ai);
		commonTools.toString();
		AiZone gameZone= ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		List<AiTile> tiles = gameZone.getTiles();
		
		tiles.clear();
		commonTools.possibleTiles(tiles, ownHero.getTile());
		commonTools.safeTiles(tiles);
		result.addAll(tiles);

		selectedTiles.clear();
		selectedTiles.addAll(result);
		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		
		//initialize Criter
		DistanceCible criterDeDistanceCible = new DistanceCible(ai);
		DestructibleMur criterDeMur = new DestructibleMur(ai);
		Bonus criterDeBonus= new Bonus(ai);
		Menace criterDeMenace = new Menace(ai);
		DistanceMenace criterDeMenaceDistance = new DistanceMenace(ai);
		
		// on définit un premier cas utilisant les deux premiers critères
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();

		/////////////////////------------------------Criters-----------------///////////////////////////////
		
		////////////////////----First Criter---/////////////////////:
		criteria.add(criterDeDistanceCible);
		criteria.add(criterDeMur);
		AiUtilityCase caseCollecte_Mur = new AiUtilityCase(caseCollectePourMur, criteria);
		////////////////////----First Criter---/////////////////////
		
		
		////////////////////----Second Criter---/////////////////////
		criteria= new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterDeDistanceCible);
		criteria.add(criterDeBonus);
		AiUtilityCase caseCollecte_Bonus = new AiUtilityCase(caseCollectePourBonus, criteria);
		////////////////////----Second Criter---/////////////////////
		
		
		////////////////////----Third Criter---/////////////////////
		criteria= new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterDeDistanceCible);
		criteria.add(criterDeMenace);
		AiUtilityCase caseAttaque_Adversaire = new AiUtilityCase(caseAttaquePourAdversaire, criteria);
		////////////////////----Third Criter---/////////////////////

		
		////////////////////----Fourth Criter---/////////////////////
		criteria= new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(criterDeDistanceCible);
		criteria.add(criterDeMur);
		criteria.add(criterDeMenaceDistance);
		AiUtilityCase caseAttaque_Mur = new AiUtilityCase(caseAttaquePourMur, criteria);
		////////////////////----Fourth Criter---/////////////////////
		
		//////////////////////////************************end of criters******************/////////////////////////////////////
		
		
		// Creation of cases
		cases.put(caseCollectePourBonus,caseCollecte_Bonus);
		cases.put(caseAttaquePourAdversaire, caseAttaque_Adversaire);
		cases.put(caseAttaquePourMur, caseAttaque_Mur);
		cases.put(caseCollectePourMur, caseCollecte_Mur);
		
			
		//////////////////////////////////////---COMBINATION DE VALEUR UTILITY ----/////////////////////////////////////////
		// on affecte les valeurs d'utilité
		int utility = 1;
		AiUtilityCombination combi;
		
		//Attaque mode
		{//si adv possible
			for(int i = 0; i<= DistanceCible.DISTANCE_LIMIT; i++){
				ai.checkInterruption();
				{
					combi = new AiUtilityCombination(caseAttaque_Adversaire);
					combi.setCriterionValue(criterDeDistanceCible, i);
					combi.setCriterionValue(criterDeMenace, true);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
			for(int i = 0; i<= DistanceCible.DISTANCE_LIMIT; i++){
				ai.checkInterruption();
				{
					combi = new AiUtilityCombination(caseAttaque_Adversaire);
					combi.setCriterionValue(criterDeDistanceCible, i);
					combi.setCriterionValue(criterDeMenace, false);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
		}
		{//ou non pas
			for(int k = 0; k<= DistanceMenace.DISTANCE_LIMIT; k++){
				ai.checkInterruption();
				for(int i = 0; i<= DistanceCible.DISTANCE_LIMIT; i++){
					ai.checkInterruption();
					for(int j = DestructibleMur.MUR_LIMIT; j>=0; j--){
						ai.checkInterruption();
						{
							combi = new AiUtilityCombination(caseAttaque_Mur);
							combi.setCriterionValue(criterDeMenaceDistance,k);
							combi.setCriterionValue(criterDeDistanceCible, i);
							combi.setCriterionValue(criterDeMur, j);
							referenceUtilities.put(combi,utility);
							utility++;
						}
					}
				}
			}
		}
		
		//Collecte Mode
		utility =1;
		{//il y a des bonus
			ai.checkInterruption();
			for(int i = 0; i<= DistanceCible.DISTANCE_LIMIT; i++){
				ai.checkInterruption();
				{
					ai.checkInterruption();
					combi = new AiUtilityCombination(caseCollecte_Bonus);
					combi.setCriterionValue(criterDeDistanceCible, i);
					combi.setCriterionValue(criterDeBonus, true);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
		}
		{
			for(int i = 0; i<= DistanceCible.DISTANCE_LIMIT; i++){
				ai.checkInterruption();
				{
					ai.checkInterruption();
					combi = new AiUtilityCombination(caseCollecte_Bonus);
					combi.setCriterionValue(criterDeDistanceCible, i);
					combi.setCriterionValue(criterDeBonus, false);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
		}//ou pas
		for(int j = DestructibleMur.MUR_LIMIT; j>=0; j--){
			ai.checkInterruption();
			for(int i = 0; i<= DistanceCible.DISTANCE_LIMIT; i++){
				ai.checkInterruption();
				{
					ai.checkInterruption();
					combi = new AiUtilityCombination(caseCollecte_Mur);
					combi.setCriterionValue(criterDeDistanceCible, i);
					combi.setCriterionValue(criterDeMur, j);
					referenceUtilities.put(combi,utility);
					utility++;
				}
				
			}
		}
		//////////////////////////////////////********end of valeur utility/////////////////////////////////////////
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	
		CommonTools commonTools = new CommonTools(ai);
		AiUtilityCase result = null;
		AiModeHandler<DemirSazan> mode = ai.getModeHandler();
		AiMode mod = mode.getMode();
		if(mod== AiMode.ATTACKING){
			if(commonTools.isPossibleArriver()){
				result=cases.get(caseAttaquePourAdversaire);
			}
			else{
				result=cases.get(caseAttaquePourMur);
			}
		}
		else{
			if(commonTools.isPossibleRammaserBonus()){
				result=cases.get(caseCollectePourBonus);
			}
			else{
				result=cases.get(caseCollectePourMur);	
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
