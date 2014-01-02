package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;

import org.totalboumboum.ai.v201314.adapter.data.AiTile;


/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class BombHandler extends AiBombHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai)
    {	super(ai);
    	ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-access")
	@Override
	protected boolean considerBombing()
	{	ai.checkInterruption();
		boolean result = false;
//		System.out.println("We are in BOMB handler and selected tile type = " + ai.preferenceHandler.selectedTileType);
//		System.out.println("We are still in bomb handler speedAttackTile = " + ai.preferenceHandler.speedAttackTile + " my hero tile = " + ai.getZone().getOwnHero().getTile());
		if(ai.preferenceHandler.selectedTileType == 8 
				&& ai.getZone().getOwnHero().getTile().getBombs().isEmpty()
				&& (ai.preferenceHandler.speedAttackTile == ai.getZone().getOwnHero().getTile()) ){
			ai.preferenceHandler.speedAttack = false;
//			System.out.println("result  = true");
			result = true;
			
		}else{// all the other strategies
	//		System.out.println("We are in else in bomb handler - speed attack");
			
		if(ai.preferenceHandler.selectedTileType == 2){//attack triangle
			
		if(!ai.getZone().getOwnHero().getTile().getBombs().isEmpty()){
			result = false;	//if there is a bomb at current tile, dont put
		}else{
			if(ai.getZone().getOwnHero().getBombNumberMax() < 3){
				result = false; // agent doesn't have the capacity of posing bomb for our strategy
//it need to change in move handler			ai.preferenceHandler.selectedTileType = 4;//turn category to easy_attack
			}else{
				if(ai.isPatternExist() && ai.getMyTiles().contains(ai.getZone().getOwnHero().getTile())){			
					//we are in a condition where we are on a selected tile and pattern existe
					if(ai.getMyTiles().get(2) == ai.getZone().getOwnHero().getTile()){
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
						if(ai.getMyTiles().get(0) == ai.getZone().getOwnHero().getTile()){
							wCorner = true;
						}else{
							wCorner = false;//it means we are on the other corner - myTiles(1)
						}
						if(wCorner){
							if((!ai.getMyTiles().get(2).getBombs().isEmpty() && !ai.getMyTiles().get(1).getBombs().isEmpty()) || (ai.getMyTiles().get(2).getBombs().isEmpty() && ai.getMyTiles().get(1).getBombs().isEmpty())){
								result = true; // there are bombs on the other selected two tiles OR there are not any bombs on the other selected two tiles
							}else{
								result = false;	
							}
						}else{//!wCorner - myTiles(1)
							if((!ai.getMyTiles().get(2).getBombs().isEmpty() && !ai.getMyTiles().get(0).getBombs().isEmpty()) || (ai.getMyTiles().get(0).getBombs().isEmpty() && ai.getMyTiles().get(1).getBombs().isEmpty())){
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
			
			if(/*!ai.preferenceHandler.isTargetHasEnoughBombs() && */ 
					ai.getZone().getOwnHero().getBombNumberCurrent() < 4  
					&& ai.getZone().getOwnHero().getTile().getBombs().isEmpty()){
				
				for(AiTile tile : ai.getZone().getOwnHero().getTile().getNeighbors()){
					ai.checkInterruption();
					if(!tile.getHeroes().isEmpty()){
						int i = 0;
						for(AiTile tileB : ai.getZone().getOwnHero().getTile().getNeighbors()){
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
			if(ai.moveHandler.destructibleWallSearch){
				result = true;
				ai.moveHandler.setDestructibleWallSearch(false);
			}
		}else if(ai.preferenceHandler.selectedTileType == 7){//category = DEAD_END_ABS
			
			if(ai.tilesInTunnel.size() >= ai.tilesInTunnel.indexOf(ai.getZone().getOwnHero().getTile()) + 1){//to check for not having exception
			
				if(!ai.tilesInTunnel.get(((ai.tilesInTunnel.indexOf(ai.getZone().getOwnHero().getTile()) + 1))).getHeroes().isEmpty()
					|| ( ai.getTilesInTunnel().size() - ai.getTilesInTunnel().indexOf(ai.getZone().getOwnHero().getTile()) 
							== ai.getZone().getOwnHero().getBombRange() + 1)){
				result =true;
				ai.moveHandler.bombPosedForTunnel = true;
			}else{
			
			
	/*		int diff = ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),ai.theEnemyInsideTheTunnel.getTile()) - 
					ai.getZone().getOwnHero().getBombRange();
			
			if(ai.getZone().getOwnHero().getTile().getBombs().isEmpty()
					&& diff == 0){
				//ai.preferenceHandler.selectedTileType = -1;
				result = true;
			}else{
				result = false;
			}
		*/	
			result = false;
			}
			}else{result = false;}
		}
		
		}//else of speed attack, for all strategies
	
		//for all the categories
/*		int c = 0;
		for(AiTile tile : ai.getZone().getOwnHero().getTile().getNeighbors()){
			if(!tile.isCrossableBy(ai.getZone().getOwnHero(), false, false, false, true, true, true)) 
				c++;
		}
		if(c==4){//agent is stuck
			result = false;
		}
		
	*/	
		
		
		return result && ai.canEscapeFromOwnBomb();
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
