package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;

import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;



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
	 * hold the tile for speed attack where own hero is going to put a bomb
	 */
	public static volatile AiTile speedAttackTile = null;
	
	/**
	 * current game zone
	 */
	private AiZone zone = null;
	
	/**
	 * if speed attack category is selected or not
	 */
	public static volatile boolean speedAttack = false; 
	
	/**
	 * if our hero is in the tunnel for dead end attack
	 */
	public static boolean ownHeroInTunnel;
	


	
//	private boolean underAttack;
	
	/**
	 * 
	 */
//	private Map<AiTile, List<AiTile>> mapOfDeadEnds;
//	private boolean targetHasEnoughBombs;
	
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
	
	zone = ai.getZone();
	AiHero myOwnHero = zone.getOwnHero();
	List<AiHero> myEnemies = zone.getRemainingOpponents();
	myEnemies = new ArrayList<AiHero>(myEnemies);
	
	List<AiTile> myEnemyTiles = zone.getTiles();
	myEnemyTiles= new ArrayList<AiTile>(myEnemyTiles);
	myEnemyTiles.clear();
	
	for(AiHero hero : myEnemies){
		ai.checkInterruption();
		myEnemyTiles.add(hero.getTile());
	}
	
	List<AiTile> myTiles = zone.getTiles();
	myTiles = new ArrayList<AiTile>(myTiles);
	myTiles.clear();
	
	selectedTileType = -1;

//%%%%%%%%%%%%%%%%%%%%%%%%%%%% SPEED ATTACK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
	//			System.out.println("my en basta enemy tiles---> " + myEnemyTiles.get(0));
			if(myEnemyTiles.contains(myOwnHero.getTile())){
//				System.out.println("my enemy tiles---> " + myEnemyTiles.indexOf(0));
				int cntr = 0;
				int fireCntr = 0;
				AiTile fireTile = null;
				for(AiTile tile : myOwnHero.getTile().getNeighbors()){
					ai.checkInterruption();
					if(!tile.isCrossableBy(myOwnHero, false,false,false,true,true,true))
						cntr++;
					if(!tile.getFires().isEmpty()){
						fireCntr++;
						fireTile = tile;
					}
	//				System.out.println("speed attack inside for ---> cntr = " + cntr + " fireCntr = " + fireCntr + " tile = " + tile);
				}
				if(cntr == 4 && fireCntr == 1){
					selectedTileType = 8;
					speedAttack = true;
					speedAttackTile = fireTile;
					myTiles.add(fireTile);
				}
			}
		if(speedAttack){
			selectedTileType = 8;
			if(!myTiles.contains(speedAttackTile))
				myTiles.add(speedAttackTile);
		}
//	if(selectedTileType != 8 && !ai.moveHandler.isSpeedAttackFinished() ) selectedTileType = 8;
//			System.out.println("selected tile type = " + selectedTileType);
	if(selectedTileType != 8){// all the other strategies,even security
	
	
//	List<AiTile> accesibleTiles = ai.getAccesibles();
//	accesibleTiles = new ArrayList<>(accesibleTiles);
	
	List<AiTile> accTiles = ai.getAcc();
	accTiles = new ArrayList<AiTile>(accTiles);
//	boolean patternDoesntExiste = false;
	

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SECURITY %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//	
/*garip
	if(  ai.DangerDegree(myOwnHero.getTile()) == 1){
		this.underAttack = true;
		for(AiTile tile : accTiles){
			ai.checkInterruption();
			if(tile.isCrossableBy(myOwnHero, false, false, false, true, true, true)){
				if(ai.getZone().getTileDistance(tile, myOwnHero.getTile()) < 4){
					myTiles.add(tile);
				}
			}
		}	
		selectedTileType = 1;
	}
*/
	if(!zone.getBombs().isEmpty()){	
		if(ai.isInDanger(myOwnHero.getTile())){
			myTiles.clear();	
			for(AiTile tile: zone.getTiles()){
				ai.checkInterruption();
				if(accTiles.contains(tile) && 4 > zone.getTileDistance(myOwnHero.getTile(), tile)){
					if(tile.isCrossableBy(myOwnHero,false, false, false, true, true, true)){
						myTiles.add(tile);
					}
				}
			}
			selectedTileType = 1;
		}
	}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
	int c = 0;//how many heroes exist in our accessible space
	for(AiHero hero : myEnemies){
		ai.checkInterruption();
		if (accTiles.contains(hero.getTile())) c++;	
	}
	if(c == 0 && selectedTileType != 1 /*myTiles.isEmpty()*/
	){
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SEARCH %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//		
		selectedTileType = 6;
		for(AiHero hero : myEnemies){
			ai.checkInterruption();
			result.add(hero.getTile());
		}	

	}
	if(selectedTileType != 6 && selectedTileType != 1){
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ATTACK (TRIANGLE) %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//		
		for(AiHero enemy : myEnemies){
			ai.checkInterruption();
			if(zone.getTileDistance(enemy, myOwnHero) < 6){
				PatternHandler  myPattern = new PatternHandler(ai, enemy);
				if(myPattern.isPatternExist){ //PATTERN FOUND!!!
					selectedTileType = 2;
				
					ai.setPatternExist(true);
					myTiles.add(zone.getTile(myPattern.tilesXY[1], myPattern.tilesXY[0]));
					myTiles.add(zone.getTile(myPattern.tilesXY[3], myPattern.tilesXY[2]));
					myTiles.add(zone.getTile(myPattern.tilesXY[5], myPattern.tilesXY[4]));
					ai.setMyTiles(myTiles);
					
					if(myPattern.isThereATileNearByEnemy){
						ai.setB(true);//change the variable names!!!!!!
					}else{
						ai.setB(false);
						}
				}else{//PATTERN NOT FOUND!!!
//					patternDoesntExiste = true;
				}
			}
		}
	}
	if(selectedTileType != 6 && selectedTileType != 1 && selectedTileType != 2){
	//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% DEAD_END_ABS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//	
		Map<AiTile, List<AiTile>> mapOfDeadEnds = new HashMap<AiTile, List<AiTile>>(ai.getDeadEnds());
		boolean forBreaker = false;
		boolean forBreaker1 = false;
	
		
		List<AiTile> tempControlTiles = myOwnHero.getTile().getNeighbors();
		tempControlTiles = new ArrayList<AiTile>(tempControlTiles);
		tempControlTiles.add(myOwnHero.getTile());
		
		
			if(ownHeroInTunnel){
				for(AiTile k : mapOfDeadEnds.keySet()){
					ai.checkInterruption();
					for(AiTile j : mapOfDeadEnds.get(k)){
						ai.checkInterruption();
					if(j == myOwnHero.getTile()){
						tempControlTiles.clear();
						tempControlTiles.add(k);
						forBreaker1 = true;
						break;
					}if(forBreaker1) break;
					}
				}
			}
	//	System.out.println("ownHeroInTunnel = " + ownHeroInTunnel + " tempControlTiles ----> " 
	//		+ tempControlTiles );
			
		
		for(AiTile tile : tempControlTiles){
			ai.checkInterruption();
					
		if(mapOfDeadEnds.keySet().contains(tile)){
			
			/*		if(ownHeroInTunnel){
						for(AiTile k : mapOfDeadEnds.keySet()){
							for(AiTile j : mapOfDeadEnds.get(k)){
							if(j == myOwnHero.getTile()){
								tile = k;
								forBreaker1 = true;
								break;
							}if(forBreaker1) break;
						}
					}
					}	*/
					
						for(AiTile tileIn : mapOfDeadEnds.get(tile)){
							ai.checkInterruption();
							if(myEnemyTiles.contains(tileIn)){
								selectedTileType = 7;
								ownHeroInTunnel = true;
								
								myTiles.clear();// unnecessary myTiles is already empty but...	
							//	myTiles.add(tile);
								
								if (1 == ai.tunnelType(mapOfDeadEnds.get(tile).get(0))){
									myTiles.add(mapOfDeadEnds.get(tile).get(0).getNeighbor(Direction.DOWN));
									myTiles.add(mapOfDeadEnds.get(tile).get(0).getNeighbor(Direction.UP));
								}else if(0 == ai.tunnelType(mapOfDeadEnds.get(tile).get(0))){
									myTiles.add(mapOfDeadEnds.get(tile).get(0).getNeighbor(Direction.LEFT));
							//		myTiles.add(mapOfDeadEnds.get(tile).get(0).getNeighbor(Direction.RIGHT));
								}
									
								myTiles.addAll(mapOfDeadEnds.get(tile));
							
								//myTiles.add(tileIn);
								if(myOwnHero != myEnemyTiles.get(myEnemyTiles.indexOf(tileIn)).getHeroes().get(0)){
								ai.setTheEnemyInsideTheTunnel(myEnemyTiles.get(myEnemyTiles.indexOf(tileIn)).getHeroes().get(0));
								}else{
									ai.setTheEnemyInsideTheTunnel(myEnemyTiles.get(myEnemyTiles.indexOf(tileIn)).getHeroes().get(1));
								}
								forBreaker = true;// if there is an enemy inside of a tunnel, don't look for other enemies
								break;
							}
						}
						if(forBreaker) break;
			}
			
		} 
		if(!myTiles.contains(myOwnHero.getTile())) ownHeroInTunnel = false; 
		ai.setTilesInTunnel(myTiles);
	}
	
	if(selectedTileType != 6 && selectedTileType != 1 && selectedTileType != 2 && selectedTileType != 7 ){
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% EASY ATTACK %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//	
			///////////garip////////
/*			selectedTileType = 4;	
			Map<AiTile, List<AiTile>> map = new HashMap<AiTile, List<AiTile>>(ai.getDeadEnds());
				for(AiTile tile : map.keySet()){
					ai.checkInterruption();
					if(ai.DangerDegree(tile) != 1){
						result.add(tile);
					}
				}		
*/		////////////garip///////
			
			//find the closest enemy
			selectedTileType = 4;
			AiHero closestEnemy = myEnemies.get(0);
			for (AiHero myEnemy :myEnemies){
				ai.checkInterruption();
				if(zone.getTileDistance(myEnemy.getTile(), myOwnHero.getTile()) < zone.getTileDistance(closestEnemy.getTile(), myOwnHero.getTile())  ){
					closestEnemy = myEnemy;
				}
			}	
			for(AiTile tile: ai.getZone().getTiles()){
				ai.checkInterruption();
				if(3 > zone.getTileDistance(closestEnemy.getTile(), tile)){
					if(tile.isCrossableBy(myOwnHero, false, false, false, true, true, true) 
							&& accTiles.contains(tile)){
						myTiles.add(tile);
					}
				}
				}
		
//case selection part for collecting mode, we don't still have a collecting strategy
//it's just for showing that the mode selection is working 		
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% MODE COLLECT %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
//		System.out.println("modehandler mode =  " + ai.modeHandler.getMode().name());
		if(ai.modeHandler.getMode() == AiMode.COLLECTING){
			myTiles.clear();
			myTiles.addAll(accTiles);
	//		System.out.println("modehandler mode =  " + ai.modeHandler.getMode().name());
		}
			
			
			
		}//end of else EASY_ATTACK
		//if(myTiles.isEmpty()) when there is no danger we are inside of this block
	
	}//end of all attacks, speed attack

// SECURITY category contains the layer of mode collect
//		result.clear();	
//		result.addAll(accesibleTiles);	
//		result.clear();
//		result.addAll(ai.getAcc());

/*	
	myTiles.clear();
	Map<AiTile, List<AiTile>> mapOfDeadEnds = new HashMap<AiTile, List<AiTile>>(ai.getDeadEnds());
	
//for(AiTile tileZ : ai.getZone().getTiles()){
		for(AiTile tile : mapOfDeadEnds.keySet()){
			myTiles.add(tile);
			for(AiTile tileIn : mapOfDeadEnds.get(tile)){
			myTiles.add(tileIn);
			}
		}
//	}
	
*/	
	
//	System.out.println("We are in preference handler and selected tile type = " + selectedTileType);
		result.addAll(myTiles);	
		
		return result;
		
	}
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% END OF %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
//%%%%%%%%%%%%%%%%%%%%%%%%%%% SELECT TILES %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
	
	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "SECURITY";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "ATTACK";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_3 = "COLLECT";
	/** Nom de la 4ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_4 = "EASY_ATTACK";
	/** Nom de la 5ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_5 = "DEAD_END";
	/** Nom de la 6ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_6 = "SEARCH";
	/** Nom de la 7ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_7 = "DEAD_END_ABS";
	/** Nom de la 8ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_8 = "SPEED_ATTACK";
	
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
		if(this.selectedTileType == 1){
			result = getCategory(CAT_NAME_1);
		}else if(this.selectedTileType == 2){
			result = getCategory(CAT_NAME_2);
		}else if(this.selectedTileType == 5){
			result = getCategory(CAT_NAME_5);
		}else if(this.selectedTileType == 4){
			result = getCategory(CAT_NAME_4);	
		}else if(this.selectedTileType == 6){
			result = getCategory(CAT_NAME_6);	
		}else if(this.selectedTileType == 7){
			result = getCategory(CAT_NAME_7);	
		}else if(this.selectedTileType == 8){
			result = getCategory(CAT_NAME_8);
		}}
		
//		System.out.println("SELECTED CATEGORY is " + result.getName() + " For The Tile ---> (" + tile.getRow() + "," + tile.getCol() + ")");
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
/*
	public boolean isTargetHasEnoughBombs() {
		return targetHasEnoughBombs;
	}

	public void setTargetHasEnoughBombs(boolean targetHasEnoughBombs) {
		this.targetHasEnoughBombs = targetHasEnoughBombs;
	}
*/
}