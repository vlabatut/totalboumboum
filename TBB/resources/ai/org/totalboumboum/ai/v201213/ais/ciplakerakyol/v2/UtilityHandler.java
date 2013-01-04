package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion.CriterionConcurrence;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion.CriterionDuree;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion.CriterionItemPertinent;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion.CriterionMenace;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion.CriterionNombredeMur;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion.CriterionPertinenceAdv;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Utility Handler class de l'agent outremer
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class UtilityHandler extends AiUtilityHandler<CiplakErakyol>
{	
	
	/**
	 * The hero count at the initialisation.
	 */
	private final int	HERO_COUNT					= 0;


	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(CiplakErakyol ai) throws StopRequestException
    {	
		super(ai);
		ai.checkInterruption();
		verbose = true;
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * represent les case qui ont selectionné
	 */
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

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();
		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new CriterionNombredeMur(ai);
		new CriterionPertinenceAdv(ai);
		new CriterionDuree(ai);
		new CriterionMenace(ai);
		new CriterionItemPertinent(ai);
		new CriterionConcurrence(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * ItemVisible	, utilisé dans {@link #initCases}
	 * */
	private final String	ItemVisible			= "ItemVisible";
	/** 
	 * VoisinageMurDest, utilisé dans {@link #initCases}
	 * */
	private final String	VoisinageMurDest	= "VoisinageMurDest";
	/** 
	 * casNull, utilisé dans {@link #initCases}
	 * */
	private final String	casNull				= "casNull";
	/** 
	 * VoisinageAdversaire, utilisé dans {@link #initCases}
	 * */
	private final String	VoisinageAdversaire		= "VoisinageAdversaire";
	/** 
	 * AdverseireLoin, utilisé dans {@link #initCases}
	 * */
	private final String	AdverseireLoin		= "AdverseireLoin";
	

	@Override
	protected void initCases() throws StopRequestException {
		ai.checkInterruption();
		Set<AiUtilityCriterion<?, ?>> criteria;

		// on définit le cas "item visible" utilisant les quatre critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionItemPertinent.NAME));
		criteria.add(criterionMap.get(CriterionDuree.NAME));
		criteria.add(criterionMap.get(CriterionConcurrence.NAME));
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		new AiUtilityCase(ai,ItemVisible,criteria);
		
		// on définit le cas "Voisinage de murs dest." utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionDuree.NAME));
		criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		new AiUtilityCase(ai,VoisinageMurDest,criteria);

		// on définit le cas "null" utilisant une critère
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		new AiUtilityCase(ai,casNull,criteria);

		// on définit le cas "Voisinage de l'adversaire" utilisant les trois critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionPertinenceAdv.NAME));
		criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		new AiUtilityCase(ai,VoisinageAdversaire,criteria);
		
		// on définit le cas "l'adversaire loin" utilisant les quatre critères
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(CriterionDuree.NAME));
		criteria.add(criterionMap.get(CriterionConcurrence.NAME));
		criteria.add(criterionMap.get(CriterionMenace.NAME));
		new AiUtilityCase(ai,AdverseireLoin,criteria);

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		Set<AiUtilityCriterion<?, ?>> criteria;

		int count = HERO_COUNT;

		for ( AiBlock currentBlock : tile.getNeighbor( Direction.UP ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.DOWN ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.LEFT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.RIGHT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}

		// mode is collecting
		if ( ai.getModeHandler().getMode().equals( AiMode.COLLECTING ) )
		{
			if ( !tile.getItems().isEmpty() )
			{
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(CriterionItemPertinent.NAME));
				criteria.add(criterionMap.get(CriterionDuree.NAME));
				criteria.add(criterionMap.get(CriterionConcurrence.NAME));
				criteria.add(criterionMap.get(CriterionMenace.NAME));
				new AiUtilityCase(ai,ItemVisible,criteria);
			}

			else if ( count != HERO_COUNT )
			{
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(CriterionDuree.NAME));
				criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
				criteria.add(criterionMap.get(CriterionMenace.NAME));
				new AiUtilityCase(ai,VoisinageMurDest,criteria);
			}
			else
			{
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
				criteria.add(criterionMap.get(CriterionMenace.NAME));
				new AiUtilityCase(ai,casNull,criteria);
			}
		}
		else
		// mode is attacking
		{
			
			boolean rakip1 = false;
			boolean rakip2 = false;
			AiZone zone = ai.getZone();
			AiHero ownHero = zone.getOwnHero();
			List<AiTile> tilesN = tile.getNeighbors();
			
			for(int i=0;i<tilesN.size();i++)
			{
				ai.checkInterruption();
				if((tilesN.get(i).getHeroes().size()==1 && !tilesN.get(i).getHeroes().contains(ownHero)))
				{
					rakip1 = true;
				}
				else if(tilesN.get(i).getHeroes().size()>1 )
				{
					rakip2=true;
				}
			}

			if(rakip2){
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(CriterionPertinenceAdv.NAME));
				criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
				criteria.add(criterionMap.get(CriterionMenace.NAME));
				new AiUtilityCase(ai,VoisinageAdversaire,criteria);
			}
			else if(rakip1)
			{
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(CriterionDuree.NAME));
				criteria.add(criterionMap.get(CriterionConcurrence.NAME));
				criteria.add(criterionMap.get(CriterionMenace.NAME));
				new AiUtilityCase(ai,AdverseireLoin,criteria);
			}
			else
			{
				criteria = new TreeSet<AiUtilityCriterion<?,?>>();
				criteria.add(criterionMap.get(CriterionNombredeMur.NAME));
				criteria.add(criterionMap.get(CriterionMenace.NAME));
				new AiUtilityCase(ai,casNull,criteria);
			}
		}
		return result;
	}
	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException
	{	ai.checkInterruption();
	
		// on affecte les valeurs d'utilité
		int utility;
		AiUtilityCombination combiA,combiB,combiC;
		AiMode mode;

		// on commence avec le mode collecte
		{	
			mode = AiMode.COLLECTING;
			utility = 1;

			// le premier cas a trois critères binaire et un entier [0,1,2]
			// donc il y a 24 combinaisons possibles
			// mais on va calculer seulement les combinaisons sans menace. ça va etre 12
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(ItemVisible));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionItemPertinent)criterionMap.get(CriterionItemPertinent.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiA.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}

			
			// le deuxieme cas une critère binaire et deux entiers [0,1,2],[1,2,3]
			// donc il y a 18 combinaisons possibles
			// mais on va calculer seulement les combinaisons sans menace. ça va etre 9
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(VoisinageMurDest));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				combiB.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}

			// le troisieme cas a une critère binaire(T) et un entier [0]			
			{	
				combiC = new AiUtilityCombination(caseMap.get(casNull));
				combiC.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiC.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiC, utility);
				utility++;
			}
		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 1;
			
			// le premier cas a deux critères binaire et un entier [1,2,3]
			// donc il y a 12 combinaisons possibles
			// mais on va calculer seulement les combinaisons sans menace. ça va etre 6	
			{	
				combiA = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.TRUE);
				combiA.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),3);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),2);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			{	
				combiA = new AiUtilityCombination(caseMap.get(VoisinageAdversaire));
				combiA.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionPertinenceAdv)criterionMap.get(CriterionPertinenceAdv.NAME),Boolean.FALSE);
				combiA.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),1);
				defineUtilityValue(mode, combiA, utility);
				utility++;
			}
			
						
			// le deuxieme cas a deux critères binaire et un entier [0,1,2]
			// donc il y a 12 combinaisons possibles
			// mais on va calculer seulement les combinaisons sans menace. ça va etre 6	
			{	
				combiB = new AiUtilityCombination(caseMap.get(AdverseireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(AdverseireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(AdverseireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(AdverseireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),2);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(AdverseireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),1);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			{	
				combiB = new AiUtilityCombination(caseMap.get(AdverseireLoin));
				combiB.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.FALSE);
				combiB.setCriterionValue((CriterionConcurrence)criterionMap.get(CriterionConcurrence.NAME),Boolean.TRUE);
				combiB.setCriterionValue((CriterionDuree)criterionMap.get(CriterionDuree.NAME),0);
				defineUtilityValue(mode, combiB, utility);
				utility++;
			}
			
			// le troisieme cas a une critère binaire(T) et un entier [0]			
			{	
				combiC = new AiUtilityCombination(caseMap.get(casNull));
				combiC.setCriterionValue((CriterionMenace)criterionMap.get(CriterionMenace.NAME),Boolean.TRUE);
				combiC.setCriterionValue((CriterionNombredeMur)criterionMap.get(CriterionNombredeMur.NAME),0);
				defineUtilityValue(mode, combiC, utility);
				utility++;
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
		super.updateOutput();
	
	}
}
