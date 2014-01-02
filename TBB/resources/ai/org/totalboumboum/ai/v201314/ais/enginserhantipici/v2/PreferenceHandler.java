package org.totalboumboum.ai.v201314.ais.enginserhantipici.v2;

import java.util.ArrayList;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;



/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails.
 * 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */


public class PreferenceHandler extends AiPreferenceHandler<Agent>
{
	/**
	 * selectedTileType holds 2 for selected tiles for Attack, 1 for Escape and -1 for an error
	 */
	public int selectedTileType = -1;
	
	/**
	 * isThereATileNearByEnemy is true if the pattern exist and  if there is an enemy near a selected tile
	 * it uses the variable which has also the same name in "Patterns" class for having its value
	 */

	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();

		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles()
	{	ai.checkInterruption();
	Set<AiTile> result = new TreeSet<AiTile>();
	
	AiHero myOwnHero = ai.getZone().getOwnHero();
	List<AiHero> myAdversaires = ai.getZone().getRemainingOpponents();
	myAdversaires = new ArrayList<AiHero>(myAdversaires);
	
	List<AiTile> myTiles = ai.getZone().getTiles();
	myTiles = new ArrayList<AiTile>(myTiles);
	myTiles.clear();
	
	
	if(!ai.getZone().getBombs().isEmpty()){	
		if(ai.isInDanger(myOwnHero.getTile())){
			myTiles.clear();
			for(AiTile tile: ai.getZone().getTiles()){
				ai.checkInterruption();
				if(4 > ai.getZone().getTileDistance(myOwnHero.getTile(), tile)){
					if(tile.isCrossableBy(myOwnHero, false, false, false, true, true, true)){
						myTiles.add(tile);
					}
				}
			}
			selectedTileType = 1;
		}
	}

	if(myTiles.isEmpty()){
		selectedTileType = 2;
		for (AiHero enemy : myAdversaires){
			ai.checkInterruption();
				int yAdv = enemy.getTile().getRow();
				int xAdv = enemy.getTile().getCol();
				int y = yAdv - 3;
				int[] myCurrentCliche = new int[25];
				int k = 0;
				
		for(int i = 0; i < 5; i++){
			ai.checkInterruption();
			y++;
			// after first 5 values of x determined, we restart x from the beginning of the next row
			int x = xAdv - 3;
			for(int j = 0; j < 5; j++){
				ai.checkInterruption();
				x++;
				myCurrentCliche[k] = 2;
				//if the current coordinate is outside of the zone then consider that element as a wall
				//it should be change when there is a passage at the zone!
				if(y < 0 || x < 0 || x + 1 > ai.getZone().getWidth() || y + 1 > ai.getZone().getHeight() ) {
					myCurrentCliche[k] = 1; 
					//when there is a zone with noborders we have to write a control here, for not having "array out bound" exception
				}else if (!ai.getZone().getTile(y,x).getBlocks().isEmpty()){
					myCurrentCliche[k] = 1; 
				}	
				k++;
			}
		}  
		
		Patterns myPattern = new Patterns(myCurrentCliche, enemy.getTile().getCol(), enemy.getTile().getRow(),ai);
		
		if(myPattern.isPatternExist){
			//PATTERN FOUND!!!
			ai.setPatternExist(true);
			myTiles.add(ai.getZone().getTile(myPattern.tilesXY[1], myPattern.tilesXY[0]));
			myTiles.add(ai.getZone().getTile(myPattern.tilesXY[3], myPattern.tilesXY[2]));
			myTiles.add(ai.getZone().getTile(myPattern.tilesXY[5], myPattern.tilesXY[4]));
			ai.setMyTiles(myTiles);
			
			if(myPattern.isThereATileNearByEnemy){
				ai.setB(true);
			}else{
				ai.setB(false);
				}
		}else{
			//PATTERN NOT FOUND!!!	
		}
		}// for AiHero enemy : myAdversaires
			
		
//		Collections.shuffle(tiles);		
		
	}//if(myTiles.isEmpty()) when there is no danger we are inside of this block
	
		result.addAll(myTiles);
	
		//case selection part for collecting mode, we don't still have a collecting strategy
		//it's just for showing that the mode selection is working 	
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		if(mode==AiMode.COLLECTING){
			result.clear();
			result.add(ai.getZone().getOwnHero().getTile());
		}
			
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "ECHAPE";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "ATTACK";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_3 = "COLLECT";
	


	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		if(mode==AiMode.COLLECTING){
			//we don't still have a category for collect mode
			result = getCategory(CAT_NAME_3);	
		}else{
		if(selectedTileType == 1){
			result = getCategory(CAT_NAME_1);
		}else if(selectedTileType == 2){
			result = getCategory(CAT_NAME_2);	
		}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		// pour ne pas afficher la préférence des cases non-sélectionnées
		outputWorstPref = false;
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		
		
	}
}
