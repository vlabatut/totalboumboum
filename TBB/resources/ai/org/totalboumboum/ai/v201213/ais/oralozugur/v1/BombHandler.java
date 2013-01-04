package org.totalboumboum.ai.v201213.ais.oralozugur.v1;

import java.util.ArrayList;

import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */


public class BombHandler extends AiBombHandler<OralOzugur>
{	
	/**
}
	 * Range's bottom limit, used in determining the potentially dangerous tiles
	 * on bomb put.
	 */
	private final int			RANGE_BOTTOM_LIMIT		= 0;
	/**
	 * Distance's bottom limit, if distance drops to this limit, the fire's edge
	 * is reached in getDangerousTilesOnBombPut().
	 */
	
	private final int			DISTANCE_BOTTOM_LIMIT	= 0;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(OralOzugur ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		
		AiHero ourHero = ai.getZone().getOwnHero();
		AiTile currentTile =  ai.getZone().getOwnHero().getTile();
		//ai.setUtilityMap( ai.getUtilityHandler().getUtilitiesByTile() );
		if (!isHeroInDanger(ourHero) && canReachSafety(ourHero) )
		{	
			AiTile BiggestTile = ai.getBiggestTile();
			if (ai.getModeHandler().getMode().equals( AiMode.ATTACKING )) {
				float currentTileUtility = ai.getUtilityHandler().getUtilitiesByTile().get(currentTile);
				float biggestTileUtility = ai.getUtilityHandler().getUtilitiesByTile().get(BiggestTile);
				if (currentTileUtility == biggestTileUtility) {
					return true;
				} else if(ai.getNbMurDetruitofTile(currentTile) > 1){
					return true;
				}
				else return false;
			} else {
				//MODE COLLECTE
				
				if (ai.getPeutTuerEnnemiofTile(currentTile) == 2) {
					return true;
				} 
				else{
					int nbMurDetrui = ai.getNbMurDetruitofTile(currentTile);
					if (currentTile.equals( BiggestTile ) ){
						if ( nbMurDetrui > 0 ) {
							return true;
						}
						else return false;
					}
					else if (nbMurDetrui >1) {
						return true;
					}
				}
				
			}
		}
		return false;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
	
	//FUNCTIONS
	/**
	 * Checks a hero's danger situation. <br />
	 * (TESTED, WORKS)
	 * 
	 * @param givenHero
	 *            The hero to be checked.
	 * @return If given hero is in danger (in a blast range or in a flame) or
	 *         not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInDanger( AiHero givenHero ) throws StopRequestException
	{
		ai.checkInterruption();
		if ( getCurrentDangerousTiles().contains( givenHero.getTile() ) ) return true;
		return false;
	}
	/**
	 * Populates a list of dangerous tiles of this AI's zone. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return List of the dangerous tiles.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for ( AiBomb currentBomb : ai.getZone().getBombs() )
		{
			ai.checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				ai.checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : ai.getZone().getFires() )
		{
			ai.checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		return dangerousTiles;
	}
	
	/**
	 * Checks if the given hero can reach a safe tile if he puts a bomb to his
	 * tile. <br />
	 * (TESTED, WORKS)
	 * 
	 * @param givenHero
	 *            The hero to process.
	 * @return If given hero can access a safe tile or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean canReachSafety( AiHero givenHero ) throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> accessibleTiles = ai.getAccessibleTilesFrom( givenHero.getTile() );
		int safeTileCount = accessibleTiles.size();
		for ( AiTile currentTile : accessibleTiles )
		{
			ai.checkInterruption();
			if ( getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentTile ) || getCurrentDangerousTiles().contains( currentTile ) ) safeTileCount--;
		}
		return ( safeTileCount > 0 );
	}
	
	/**
	 * Populates a list of tiles which will become dangerous if a bomb is put on
	 * given tile. <br/>
	 * (TESTED, WORKS) <br/>
	 * At the beginning of the game, the ranges of the heroes can be
	 * uninitialized. So, be careful when giving range parameter from a hero's
	 * range. <br/>
	 * If range is 0, this method will return an empty list.
	 * 
	 * @param givenTile
	 *            The tile which will contain the potential bomb.
	 * @param range
	 *            The explosion range of the potential bomb.
	 * @return The list of potentially dangerous tiles on bomb put.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut( AiTile givenTile, int range ) throws StopRequestException
	{
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTilesOnBombPut = new ArrayList<AiTile>();
		if ( givenTile.isCrossableBy( ai.getZone().getOwnHero() ) && ( range > RANGE_BOTTOM_LIMIT ) )
		{
			dangerousTilesOnBombPut.add( givenTile );

			AiTile currentTile = givenTile.getNeighbor( Direction.LEFT );
			int distance = range;
			while ( currentTile.isCrossableBy(  ai.getZone().getOwnHero() ) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.LEFT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.RIGHT );
			distance = range;
			while ( currentTile.isCrossableBy(  ai.getZone().getOwnHero() ) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.RIGHT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.UP );
			distance = range;
			while ( currentTile.isCrossableBy(  ai.getZone().getOwnHero() ) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.UP );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.DOWN );
			distance = range;
			while ( currentTile.isCrossableBy(  ai.getZone().getOwnHero() ) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				ai.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.DOWN );
				distance--;
			}
		}

		return dangerousTilesOnBombPut;
	}


}
