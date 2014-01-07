/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3;

//import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * C'est une classe qui aide aux calcules qu'on fait pour trouver des tiles
 * qu'on peut acceder directement. On calcule aussi les selections des cases ici .
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
@SuppressWarnings("deprecation")
public class AccessibleTilesFinder {
	
	/**
	 * On definit un objet ai de la classe agent pour qu'on l'utilise dans des autres classes
	 */
	Agent ai;

	/**	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * @param ai
	 * l'agent que cette classe doit gérer.
	 */
	
	public AccessibleTilesFinder(Agent ai)
	{
		ai.checkInterruption();
		this.ai=ai;
	}
	
	
	/**
	 * Recursive method to fill a list of accessible tiles.
	 * @param Hero
	 *  		  Pour determiner laquelle hero on va utiliser. (Nous ou l'adversaire)
	 * @param sourceTile
	 *            The tile to start looking from. If not crossable, list will
	 *            not be populated.
	 */
	public void fillAccess(AiTile sourceTile, AiHero Hero) {
		ai.checkInterruption();

			ai.access.add(sourceTile);
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy( Hero ) && !ai.access.contains( sourceTile.getNeighbor( Direction.UP ) ) )
				fillAccess( sourceTile.getNeighbor( Direction.UP ),Hero );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy( Hero ) && !ai.access.contains( sourceTile.getNeighbor( Direction.DOWN ) ) )
				fillAccess( sourceTile.getNeighbor( Direction.DOWN ),Hero );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy( Hero ) && !ai.access.contains( sourceTile.getNeighbor( Direction.LEFT ) ) ) 
				fillAccess( sourceTile.getNeighbor( Direction.LEFT ),Hero );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy( Hero ) && !ai.access.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) )
				fillAccess( sourceTile.getNeighbor( Direction.RIGHT ),Hero );	
		
	}
	
	
	/**
	 * On prend les murs qu'on peut accceder. 
	 */
	
	public void fillAccessWall(){
		ai.checkInterruption();
		for(AiTile tile:ai.access){
			ai.checkInterruption();
			for(AiTile t:tile.getNeighbors()){
				ai.checkInterruption();
				if(!t.getBlocks().isEmpty()){
						for(AiBlock mur:t.getBlocks()){
			    			ai.checkInterruption();

							if(mur.isDestructible() && !ai.accessWall.contains(mur)){
								ai.accessWall.add(mur);
							}
					}
				}
			}			
		}
	}
	
    /**
	 * Controle si un ennemi est accesible ou pas
	 * 
	 * @return vrai, s'il y a un adversaire accesible dans la zone
	 */
	public boolean isEnemiesAccessible()  {
		ai.checkInterruption();
		Boolean result=false;
		for (AiHero hero : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if (ai.access.contains(hero.getTile())) 
				result=true;
		}
		return result;

	}

}
