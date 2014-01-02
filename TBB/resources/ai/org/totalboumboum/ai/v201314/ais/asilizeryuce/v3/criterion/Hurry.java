/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.asilizeryuce.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v3.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette critere entier pour savoir que est-ce que la case est propre de tuer
 * l'ennemi, qu'on veut mettre une bombe
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class Hurry  extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "HURRY";
	
	/**avec cette boolean variable, on controle l'utilisation des variables
	 * bombTile et flammeTile.si on utilise, done vaut donne true. ü
	 */
	public boolean done = false;
	/** en posant une bombe dans cette case, on bloque l'ennemi qui nous approche */
	AiTile bombTile;
	/** en posant une bombe dans cette case on bloque la flamme et on gange 
	 * une possibilite de s'enfuire*/
	AiTile flammeTile;
	
	/**
	 * Crée un critère entier.
	 * 
	 * @param ai l'agent concerné.
	 */
	public Hurry(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}
	    // ///////////////////////////////////////////////////////////////
		// PROCESS /////////////////////////////////////
		// ///////////////////////////////////////////////////////////////
		@Override
		public Integer processValue(AiTile tile) {
			ai.checkInterruption();
			int result = 2;
			if(!done){
			AiHero hero = ai.getZone().getOwnHero();
			AiTile enemy=null;
			for(AiTile tiles : ai.enemyHandler.enemyTiles()){
				ai.checkInterruption();
			 if(tiles.getCol() == hero.getCol()) enemy=tiles;
			}		
   if (ai.tileHandler.simpleTileDistance(enemy,hero.getTile().getNeighbor(Direction.DOWN)) < 
		  ai.tileHandler.simpleTileDistance(enemy,hero.getTile().getNeighbor(Direction.UP)))
	   bombTile =hero.getTile().getNeighbor(Direction.DOWN);
				else bombTile =hero.getTile().getNeighbor(Direction.UP);
       
       for(AiTile tiles : ai.enemyHandler.enemyTiles()){
    	   ai.checkInterruption();
		    if(tiles.getRow() == hero.getRow()) enemy=tiles;
	     	}	
   
       if (ai.tileHandler.simpleTileDistance(enemy,hero.getTile().getNeighbor(Direction.LEFT)) > 
    			  ai.tileHandler.simpleTileDistance(enemy,hero.getTile().getNeighbor(Direction.RIGHT)))
    	   flammeTile =hero.getTile().getNeighbor(Direction.LEFT);
    					else flammeTile =hero.getTile().getNeighbor(Direction.RIGHT);
       
				done =true;
			}
		
			if(tile.equals(bombTile) && tile.getBombs().isEmpty()) return 0;
			
			if(tile.equals(flammeTile)&& tile.getBombs().isEmpty()) return 1;
			
			return result;
		}

}
