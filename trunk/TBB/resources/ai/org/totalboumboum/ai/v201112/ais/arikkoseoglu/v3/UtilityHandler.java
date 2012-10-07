package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3;

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
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
//import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v1.criterion.ConcurenceCriter;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion.BonusCriter;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion.DistanceAdversaireCriter;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion.MenaceCriter;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion.MurDesCriter;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion.TempsCriter;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<ArikKoseoglu>
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
	protected UtilityHandler(ArikKoseoglu ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	
		//  à compléter
	}
	
	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String collecteModeCase = "COLLECTECASE";
	/** */
	private final String collecteModeCaseVisibleBonus = "COLLECTECASE_VISIBLE_BONUS";
	/** */
	private final String attaqueModeCaseVisibleAdv = "ATTAQUECASE_VISIBLE_ADV";
	/** */
	private final String attaqueModeCaseMur = "ATTAQUECASE_MUR";
	
	/** cases sélectionnées */
	public List<AiTile> selectedTiles= ai.getZone().getTiles(); 
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		selectedTiles.clear();
		AiZone gameZone= ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		List<AiTile> tiles = gameZone.getTiles();
		tiles.clear();
		this.possibleAAriveTiles(tiles, ownHero.getTile());
		this.safeTiles(tiles);
		result.addAll(tiles);
		selectedTiles.addAll(result);
		return result;
	}
	
	
	/**
	 * Method find tiles possibles d'arriver
	 * @param result
	 * @param tile
	 * @throws StopRequestException 
	 */	
	private void possibleAAriveTiles(List<AiTile> result,AiTile tile) throws StopRequestException
	{
		ai.checkInterruption();
		List<AiTile> neighbor = tile.getNeighbors();
		for(int i=0;i<neighbor.size();i++){
			ai.checkInterruption();
			AiTile n = neighbor.get(i);
			if(n.getBlocks().size() == 0 && n.getFires().size() ==0 && n.getBombs().size() ==0){
				if(!checkExisting(result,n)){
					result.add(n);
					this.possibleAAriveTiles(result, n);
				}
			}
		}
	}
	
	/**
	 * Method calcul unique tile in list 
	 * @param result 
	 * @param tile 
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	private boolean checkExisting(List<AiTile> result,AiTile tile)throws StopRequestException
	{
		ai.checkInterruption();
		for(int i=0;i<result.size();i++){
			ai.checkInterruption();
			if(tile.equals(result.get(i))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method calcul tiles sont dans un scope de bomb 
	 * @param tiles 
	 * @throws StopRequestException 
	 */
	private void safeTiles(List<AiTile> tiles)throws StopRequestException
	{
		ai.checkInterruption();
		AiZone gameArea = ai.getZone();
		List<AiBomb> bombs = gameArea.getBombs();
		for(int k = 0 ; k < tiles.size(); k++){
			ai.checkInterruption();
			AiTile tile = tiles.get(k);
			for(int i = 0 ; i< bombs.size(); i++){
				ai.checkInterruption();
				List<AiTile> scope = bombs.get(i).getBlast();
				if(scope.contains(tile)){
					tiles.remove(tile);
					k--;
					break;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param tiles
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private void setPossibleFuirTiles(List<AiTile> tiles) throws StopRequestException {
		ai.checkInterruption();
		AiZone gameArea = ai.getZone();
		AiHero ownHero = gameArea.getOwnHero();
		for(int i = 0; i<tiles.size(); i++){
			ai.checkInterruption();
			if(!possibleFuir(tiles.get(i), gameArea, ownHero)){
				tiles.remove(i);
				i--;
			}	
		}
		
	}
	
	/**
	* Method calculate possibilite de fuir
	 * @param center 
	 * @param gameArea 
	 * @param hero 
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	*/
	private boolean possibleFuir(AiTile center, AiZone gameArea, AiHero hero) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		List<AiTile> selectedTiles = ai.utilityHandler.selectedTiles;
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
		for(int i = 0 ; i<selectedTiles.size();i++){
			ai.checkInterruption();
			if(result.contains(selectedTiles.get(i)) || center.equals(selectedTiles.get(i))){
				selectedTiles.remove(i);
				i--;
			}
		}
		if(selectedTiles.size()>0){
			return true;
		}
		return false;
	}
	
	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		BonusCriter bonusCriter = new BonusCriter(ai);
		DistanceAdversaireCriter distanceCriter = new DistanceAdversaireCriter(ai);
		MenaceCriter menaceCriter = new MenaceCriter(ai);
		MurDesCriter murCriter = new MurDesCriter(ai);
		TempsCriter tempsCriter = new TempsCriter(ai);
		
		// on définit un premier cas utilisant les deux premiers critères
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();

		//Creer mon case 1 ---> pour collecte Mode
		//criteria.add(bonusCriter);
		criteria.add(tempsCriter);
		criteria.add(murCriter);
		AiUtilityCase collecteCase = new AiUtilityCase(collecteModeCase, criteria);
		
		//Creer mon case 1 ---> pour collecte Mode
		//criteria.add(bonusCriter);
		criteria.add(tempsCriter);
		criteria.add(bonusCriter);
		AiUtilityCase collecteCaseBonus = new AiUtilityCase(collecteModeCaseVisibleBonus, criteria);
		
		//Creer mon case 3 ---> pour attaque Mode si heros visible
		criteria= new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(menaceCriter);
		criteria.add(tempsCriter);
		//criteria.add(distanceADusman);
		AiUtilityCase attaqueCaseVisibleAdversaire = new AiUtilityCase(attaqueModeCaseVisibleAdv, criteria);
		
		//Creer mon case 4 ---> pour attaque Mode 
		criteria= new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(tempsCriter);
		criteria.add(murCriter);
		criteria.add(distanceCriter);
		AiUtilityCase attaqueCaseMur = new AiUtilityCase(attaqueModeCaseMur, criteria);
		
		// on met les cas dans la map prévue à cet effet
		// ceci permettra de les retrouver facilement plus tard,
		// en particulier dans la méthode identifyCase()
		cases.put(collecteModeCase, collecteCase);
		cases.put(attaqueModeCaseVisibleAdv, attaqueCaseVisibleAdversaire);
		cases.put(attaqueModeCaseMur, attaqueCaseMur);
		
		// on affecte les valeurs d'utilité
		int utility = 1;
		AiUtilityCombination combi;
		

		//Collecte Mode, pour bonus
		{
			for(int i = 0; i<= TempsCriter.TEMPS_LIMIT; i++){
				ai.checkInterruption();
				{
					ai.checkInterruption();
					combi = new AiUtilityCombination(collecteCaseBonus);
					combi.setCriterionValue(tempsCriter, i);
					combi.setCriterionValue(bonusCriter, true);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
		}
		{
			for(int i = 0; i<= TempsCriter.TEMPS_LIMIT; i++){
				ai.checkInterruption();
				{
					ai.checkInterruption();
					combi = new AiUtilityCombination(collecteCaseBonus);
					combi.setCriterionValue(tempsCriter, i);
					combi.setCriterionValue(bonusCriter, false);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
		}
		//Collecte Mode,bomb à Mur - bonus criter
		for(int j = MurDesCriter.MUR_LIMIT; j>=0; j--){
			ai.checkInterruption();
			for(int i = 0; i<= TempsCriter.TEMPS_LIMIT; i++){
				ai.checkInterruption();
				{
					ai.checkInterruption();
					combi = new AiUtilityCombination(collecteCase);
					combi.setCriterionValue(tempsCriter, i);
					combi.setCriterionValue(murCriter, j);
					referenceUtilities.put(combi,utility);
					utility++;
				}
				
			}
		}
		//////////---------------------------------------------------/////////////
		utility =1;
		//Attaque Mode
		{
			
			for(int i = 0; i<= TempsCriter.TEMPS_LIMIT; i++){
				ai.checkInterruption();
				{
					combi = new AiUtilityCombination(attaqueCaseVisibleAdversaire);
					combi.setCriterionValue(tempsCriter, i);
					combi.setCriterionValue(menaceCriter, true);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
			for(int i = 0; i<= TempsCriter.TEMPS_LIMIT; i++){
				ai.checkInterruption();
				{
					combi = new AiUtilityCombination(attaqueCaseVisibleAdversaire);
					combi.setCriterionValue(tempsCriter, i);
					combi.setCriterionValue(menaceCriter, false);
					referenceUtilities.put(combi,utility);
					utility++;
				}
			}
		}
		{
			for(int k = 0; k<= DistanceAdversaireCriter.DISTANCE_LIMIT; k++){
				ai.checkInterruption();
				for(int i = 0; i<= TempsCriter.TEMPS_LIMIT; i++){
					ai.checkInterruption();
					for(int j = MurDesCriter.MUR_LIMIT; j>=0; j--){
						ai.checkInterruption();
						{
							combi = new AiUtilityCombination(attaqueCaseMur);
							combi.setCriterionValue(distanceCriter,k);
							combi.setCriterionValue(tempsCriter, i);
							combi.setCriterionValue(murCriter, j);
							referenceUtilities.put(combi,utility);
							utility++;
						}
					}
				}
			}
		}
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		AiModeHandler<ArikKoseoglu> mode = ai.getModeHandler();
		AiMode mod = mode.getMode();
		if(mod== AiMode.ATTACKING){
			if(getPossibilityArriver()){
				result=cases.get(attaqueModeCaseVisibleAdv);
			}
			else{
				result=cases.get(attaqueModeCaseMur);
			}
		}
		else{//collecting
			if(getPossibilityRammaserBonus()){
				result=cases.get(collecteModeCaseVisibleBonus);
			}
			else{
				result=cases.get(collecteModeCase);	
			}
				
		}
		return result;
	}
	
	/**
	 * Method qui calculer possibilite d'arriver ue adversaire 
	 * 
	 * @return
	 * 		? 
	 * @throws StopRequestException 
	 */
	public boolean getPossibilityArriver() throws StopRequestException
	{	
		ai.checkInterruption();
		List<AiHero> heros = ai.getZone().getRemainingHeroes();
		heros.remove(ai.getZone().getOwnHero());
		for(int i = 0; i< heros.size();i++){
			ai.checkInterruption();
			if(selectedTiles.contains(heros.get(i).getTile())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method qui calculer possibilite de rammaser le bonus
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	private boolean getPossibilityRammaserBonus() throws StopRequestException
	{	
		ai.checkInterruption();
		List<AiItem> items = ai.getZone().getItems();
		for(int i = 0; i< items.size();i++){
			ai.checkInterruption();
			if(selectedTiles.contains(items.get(i).getTile())){
				return true;
			}
		}
		return false;
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
