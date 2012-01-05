package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import java.util.ArrayList;
import java.util.Collections;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Our move handler class.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class MoveHandler extends AiMoveHandler<CoskunOzdemir>
{
	/**
	 * Radius to search for search tiles, with center as our hero's current tile.
	 */
	private final int	SAFE_TILE_SEARCH_RADIUS	= 6;
	/**
	 * Represents the first element of an array-list etc.
	 */
	private final int	FIRST					= 0;

	/**
	 * Constructs a handler for the agent passed as a parameter.
	 * 
	 * @param ai
	 *            The agent that the class will handle.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected MoveHandler( CoskunOzdemir ai ) throws StopRequestException
	{
		super( ai );
		ai.checkInterruption();
		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{
		ai.checkInterruption();
		Direction result = Direction.NONE;

		if ( this.ai.getHo().isHeroInDanger() )
		{

			// Think twice before setting check radius' value
			result = this.ai.getPo().getNextDirectionOnPath( this.ai.getTo().getClosestSafeTile( SAFE_TILE_SEARCH_RADIUS ) );

		}
		else
		{
			this.ai.setUtilityMap( this.ai.getUtilityHandler().getUtilitiesByTile() );
			AiTile biggestTile = this.ai.getTileWithBiggestUtility();
			this.ai.addToLastUtilityTiles( biggestTile );

			// Here we check if our her is stuck between two utility tiles.
			// If so, he decides to go to a specific one. (Decision maker, break
			// dance breaker)
			if ( this.ai.getHo().heroInEndlessLoop() && this.ai.getZone().getBombsByColor( PredefinedColor.BLACK ).isEmpty() )
			{
				ArrayList<AiTile> a = new ArrayList<AiTile>( this.ai.getStuckUtilityTiles() );
				Collections.sort( a );
				biggestTile = a.get( FIRST );
			}

			if ( biggestTile.equals( this.ai.getHero().getTile() ) )
			{
				result = Direction.NONE;
			}
			else
			{
				result = this.ai.getPo().getNextDirectionOnPath( biggestTile );
			}
		}

		// This checks if the result is dangerous. If so, hero stays where he
		// is.
		if ( !this.ai.getTo().getCurrentDangerousTiles().contains( this.ai.getHero().getTile() ) && this.ai.getTo().getCurrentDangerousTiles().contains( this.ai.getHero().getTile().getNeighbor( result ) ) ) result = Direction.NONE;

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{
		ai.checkInterruption();
		super.updateOutput();
	}
}
