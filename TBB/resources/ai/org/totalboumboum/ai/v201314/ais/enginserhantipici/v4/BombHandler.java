package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
//import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;

/**
 *	Class responsible of the decision to put a bomb
 * 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class BombHandler extends AiBombHandler<Agent>
{	
	
	/**
	 * construct a handler agent to pass a parameter
	 * 
	 * @param ai	
	 * 		the agent who is managed by this class
	 */
	protected BombHandler(Agent ai)
    {	super(ai);
    	ai.checkInterruption();
    	zone = ai.getZone();
    	ownHero = zone.getOwnHero();

    	
	}
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * it holds current game zone
	 */
	private AiZone zone = null;
	/**
	 * it holds our hero
	 */
	private AiHero ownHero = null;
	/**
	 *list of the dangerous tiles 
	 *this list will set by search simulation for future destinations
	 *then it will be used in moveHandler to decide where to go after putting a bomb
	 */
	public static ArrayList<AiTile> dangerousDestinations = null;
	/**
	 *At the end of this handler this variable will be set
	 *it shows wheter is a bomb put at last iteration or not
	 *it is necessary cause if a bomb is put after bombHandler we do not pass to moveHandler in same iteration
	 */
	public static volatile boolean aBombPutAtLastIteration = false;

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-access")
	@Override
	protected boolean considerBombing()
	{	ai.checkInterruption();
	
		ai.moveHandler.setFlagBombLastIt(aBombPutAtLastIteration);
		boolean result = false;
//		System.out.println("We are in BOMB handler and selected tile type = " + ai.preferenceHandler.selectedTileType);
//		System.out.println("We are still in bomb handler speedAttackTile = " + ai.preferenceHandler.speedAttackTile + " my hero tile = " + ai.getZone().getOwnHero().getTile());
		if(ai.preferenceHandler.selectedTileType == 8 
				&& ownHero.getTile().getBombs().isEmpty()
				&& (ai.preferenceHandler.speedAttackTile == ai.getZone().getOwnHero().getTile()) ){
			ai.preferenceHandler.speedAttack = false;
//			System.out.println("result  = true");
			result = true;
			
		}else{// all the other strategies
//			System.out.println("We are in else in bomb handler - speed attack");
			
		if(ai.preferenceHandler.selectedTileType == 2){//attack triangle
			
		if(!ownHero.getTile().getBombs().isEmpty()){
			result = false;	//if there is a bomb at current tile, do not put
		}else{
			if(ownHero.getBombNumberMax() < 3){
				result = false; // agent doesn't have the capacity of posing bomb for our strategy
			}else{
				if(ai.isPatternExist() && ai.getMyTiles().contains(ownHero.getTile())){			
					//we are in a condition where we are on a selected tile and pattern existe
					if(ai.getMyTiles().get(2) == ownHero.getTile()){
						//as the third element(last element) of the myTiles is showing the tile of "la case coude",
						//we are now on the middle tile "la case coude"
						if(!ai.getMyTiles().get(0).getBombs().isEmpty() || !ai.getMyTiles().get(1).getBombs().isEmpty() ){
							//there is a bomb at least one of the corners (first two element of the array "myTiles" are the corner tiles)
							result = true;
						}else{
							result = false;
						}	
					}else{
						//we are on one of the corner tiles "une case coin"
						boolean wCorner;
						if(ai.getMyTiles().get(0) == ownHero.getTile()){
							wCorner = true;
						}else{
							wCorner = false;//it means we are on the other corner - myTiles(1)
						}
						if(wCorner){
							if((!ai.getMyTiles().get(2).getBombs().isEmpty() && !ai.getMyTiles().get(1).getBombs().isEmpty()) || (ai.getMyTiles().get(2).getBombs().isEmpty() && ai.getMyTiles().get(1).getBombs().isEmpty())
									){
								result = true; // there are bombs on the other selected two tiles OR there are not any bombs on the other selected two tiles
							}else{
								result = false;	
							}
						}else if(ai.getMyTiles().get(1) == ownHero.getTile()){//!wCorner - myTiles(1)
							if((!ai.getMyTiles().get(2).getBombs().isEmpty() && !ai.getMyTiles().get(0).getBombs().isEmpty()) || (ai.getMyTiles().get(2).getBombs().isEmpty() && ai.getMyTiles().get(0).getBombs().isEmpty())){
								result = true; // there are bombs on the other selected two tiles OR there are not any bombs on the other selected two tiles
							}else{
								result = false;	
							}
						}
					}	
				}else{
					result=false;
				}
			}
		}
		}else if(ai.preferenceHandler.selectedTileType == 4){//Easy attack
			
			if(ownHero.getBombNumberCurrent() < 10  
					&& ownHero.getTile().getBombs().isEmpty()){
				
				for(AiTile tile : ownHero.getTile().getNeighbors()){
					ai.checkInterruption();
					if(!tile.getHeroes().isEmpty()){
						int i = 0;
						for(AiTile tileB : ownHero.getTile().getNeighbors()){
							ai.checkInterruption();
							if(tileB.getBombs().isEmpty()) i++; 
								if(i == 4) result = true; 
							
						}			//if target has enough bombs 
					}
				}
			}else{
				result = false;
			}
		}else if(ai.preferenceHandler.selectedTileType == 6){//category = SEARCH 
			if(ai.moveHandler.isDestructibleWallSearch() && ownHero.getTile().getBombs().isEmpty() && !aBombPutAtLastIteration ){
				 /*this is maybe not a good idea we cannot put 3 bomb at same time in search mode*/ //ownHero.getBombNumberCurrent() == 0 
					/*|| (ownHero.getBombNumberCurrent() == 1 && !ownHero.getTile().getNeighbors().contains(zone.getBombsByColor(ownHero.getColor()).get(0).getTile())))){*/
				result = true;
				ai.moveHandler.setDestructibleWallSearch(false);
				aBombPutAtLastIteration = false;
			}
		}else if(ai.preferenceHandler.selectedTileType == 7 && ownHero.getTile().getBombs().isEmpty()){//category = DEAD_END_ABS
//			System.out.println("before if, index of own hero = " +  ai.tilesInTunnel.indexOf(ai.getZone().getOwnHero().getTile()) + " size = " +ai.tilesInTunnel.size());
//			System.out.println("before if, tiles in tunnel --->>> " + ai.tilesInTunnel);
			if(ai.tilesInTunnel.contains(ownHero.getTile()) 
					&& ai.tilesInTunnel.size() > ai.tilesInTunnel.indexOf(ai.getZone().getOwnHero().getTile()) + 1)
			{//to check for not having exception
//			System.out.println("inside if, index of own hero = " +  ai.tilesInTunnel.indexOf(ai.getZone().getOwnHero().getTile()) + " size = " +ai.tilesInTunnel.size());
//			System.out.println("inside if, tiles in tunnel --->>> " + ai.tilesInTunnel);
				if(!ai.tilesInTunnel.get(((ai.tilesInTunnel.indexOf(ownHero.getTile()) + 1 /*+1*/ ))).getHeroes().isEmpty()
					|| ( ai.getTilesInTunnel().size() - ai.getTilesInTunnel().indexOf(ownHero.getTile()) 
							== ownHero.getBombRange() + 1)){
				result =true;
				ai.moveHandler.bombPosedForTunnel = true;
	//			System.out.println("BOMB POSED FOR DEAD END ATTACK");
			}else{

			result = false;
			}
			}else{//exception
				
				result = false;}
		}
		
		
		}//else of speed attack, for all strategies
		//for almost all the categories(6, 4, 7, 1) , we check if there is a place to escape, if we put a bomb to current tile//
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
//		if(ai.preferenceHandler.selectedTileType == 6 || ai.preferenceHandler.selectedTileType == 4 || ai.preferenceHandler.selectedTileType == 7 || ai.preferenceHandler.selectedTileType == 1){
		if(result == true && ai.preferenceHandler.selectedTileType != 8){
		int crossableCounter = 0;
		int noPlaceToEscapeCounter = 0;
		AiSimZone simZone = new AiSimZone(zone);
		AiSimZone tempSimZone = new AiSimZone(simZone);
		dangerousDestinations = new ArrayList<AiTile>();
		ArrayList<AiTile> blastTiles = new ArrayList<AiTile>();
		for(AiItem item : tempSimZone.getItems()){// let all the items on simZone burn in the hell :)
			ai.checkInterruption();
			simZone.removeSprite(simZone.getSpriteById(item));
		}
		
		for(AiTile tile : simZone.getOwnHero().getTile().getNeighbors()){
			ai.checkInterruption();
			if(tile.isCrossableBy(ownHero, false, false, false, true, true, true)) {
					crossableCounter++;
				AiSimHero simHero = null;
				simHero = simZone.createHero(tile, ownHero.getColor(), ownHero.getBombNumberCurrent(), ownHero.getBombRange(), false);
				
		/*      AiSimBomb simBomb = */ simZone.createBomb(ownHero.getTile(), ownHero.getBombRange(), ownHero.getBombDuration());
		
		//		System.out.println("SimHero = " + simHero +"---- real hero = " + ownHero);
		//		System.out.println("SIM BOMB = " + simBomb);
		//		System.out.println(simZone.toString());
				
				List<AiTile> listOfAcc = zone.getTiles();
				listOfAcc = new ArrayList<AiTile>(listOfAcc);
				listOfAcc.clear();
					listOfAcc = ai.findAccessibleTilesBySimHero(simHero, simZone);
				
				List<AiTile> tempListOfAcc = new ArrayList<AiTile>(listOfAcc);		
				
			//	System.out.println("BEFORE CLEANING temp list of acc " + tempListOfAcc);
			//	System.out.println("BLASTED TILES >>> " + simBomb.getBlast());
			//	System.out.println("----------------------------------------");
				
			//	System.out.println("BLASTED TILES >>> " + blastTiles);
				for(AiBomb bomb : simZone.getBombs()){
					ai.checkInterruption();
					blastTiles.addAll(bomb.getBlast());
				}
			//	System.out.println("BLASTED TILES >>> " + blastTiles);
			//	System.out.println("acc tile >>> " + listOfAcc);
				
				for(AiTile simTile : listOfAcc){
					ai.checkInterruption();
					if(blastTiles.contains(simTile))//simBomb.getBlast().contains(simTile))
						tempListOfAcc.remove(simTile);	
				}
		//		System.out.println("AFTER CLEANNING temp list of acc " + tempListOfAcc);
				
				if(tempListOfAcc.isEmpty()){
					dangerousDestinations.addAll(listOfAcc);
//					System.out.println("dangerous dest. ->>> " + dangerousDestinations);
					noPlaceToEscapeCounter++;
					}
			}
//			System.out.println("----------------------------------------");

		}
//			System.out.println("crossableCounter = " + crossableCounter + " " + "noPlaceToEscapeCounter = " + noPlaceToEscapeCounter);
		if(crossableCounter == noPlaceToEscapeCounter){
			result = false;
			dangerousDestinations.clear();
			}
		}
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
		
		if(result == true){
			aBombPutAtLastIteration = true;
			ai.moveHandler.setDestructibleWallSearch(false);
		}else{
			aBombPutAtLastIteration = false;
		}
		
//		System.out.println("BOMBHANDLER - A bomb put at last iteration at the end of the bomb handler ---->>" + aBombPutAtLastIteration);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		
	}
}
