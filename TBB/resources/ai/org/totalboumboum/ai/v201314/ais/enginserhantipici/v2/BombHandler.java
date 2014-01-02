package org.totalboumboum.ai.v201314.ais.enginserhantipici.v2;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;


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
	@Override
	protected boolean considerBombing()
	{	ai.checkInterruption();
		boolean result = false;
	
		if(!ai.getZone().getOwnHero().getTile().getBombs().isEmpty()){
			result = false;	//if there is a bomb at current tile, dont put
		}else{
			if(ai.getZone().getOwnHero().getBombNumberMax() < 3){
				result = false; // agent doesn't have the capacity of posing bomb for our strategy
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
					result = false;
				}
			}
		}
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
