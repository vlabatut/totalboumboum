package org.totalboumboum.ai.v201112.ais.coskunozdemir.v2;

import java.util.ArrayList;
import java.util.Collections;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Our move handler class.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<CoskunOzdemir>
{
	/**
	 * Radius to search for search tiles, with ceter as our hero's current tile.
	 */
	private final int	SAFE_TILE_SEARCH_RADIUS	= 4;
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
		TileOperation to = new TileOperation( this.ai );
		HeroOperation ho = new HeroOperation( this.ai );
		PathOperation po = new PathOperation( this.ai );

		if ( ho.isHeroInDanger() )
		{
			try
			{
				// Think twice before setting check radius' value
				result = po.getNextDirectionOnPath( to.getClosestSafeTile( SAFE_TILE_SEARCH_RADIUS ) );
			}
			catch ( LimitReachedException e )
			{
				e.printStackTrace();
			}
		}
		else
		{
			this.ai.setUtilityMap( this.ai.getUtilityHandler().getUtilitiesByTile() );
			AiTile biggestTile = this.ai.getTileWithBiggestUtility();
			this.ai.addToLastUtilityTiles( biggestTile );

			// Here we check if our her is stuck between two utility tiles.
			// If so, he decides to go to a specific one. (Decision maker, break
			// dance breaker)
			if ( ho.heroInEndlessLoop() && this.ai.getZone().getBombsByColor( PredefinedColor.BLACK ).isEmpty() )
			{
				ArrayList<AiTile> a = new ArrayList<AiTile>( this.ai.getStuckUtilityTiles() );
				Collections.sort( a );
				biggestTile = a.get( FIRST );
			}

			if ( biggestTile.equals( this.ai.getZone().getOwnHero().getTile() ) )
			{
				result = Direction.NONE;
			}
			else
			{
				try
				{
					result = po.getNextDirectionOnPath( biggestTile );
				}
				catch ( LimitReachedException e )
				{
					e.printStackTrace();
				}

			}
		}

		// This checks if the result is dangerous. If so, hero stays where he
		// is.
		if ( !to.getCurrentDangerousTiles().contains( this.ai.getZone().getOwnHero().getTile() ) && to.getCurrentDangerousTiles().contains( this.ai.getZone().getOwnHero().getTile().getNeighbor( result ) ) ) result = Direction.NONE;

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
