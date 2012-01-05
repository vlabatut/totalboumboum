package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import java.util.ArrayList;
import java.util.HashSet;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * This class is aimed to contain useful hero operations. This class's methods
 * need an AI to operate (get zone etc.).
 * 
 * @author Utku Ozdemir
 * @author Doruk Coskun
 */
public class HeroOperation
{
	// FIELD
	/**
	 * The AI.
	 */
	private CoskunOzdemir	ai;
	/**
	 * If reached and the AI's still trying to reach
	 * {@link #STUCK_UTILITY_TILE_COUNT} utility tiles, AI is in endless loop.
	 */
	private final int		ENDLESS_LOOP_LIMIT			= 190;
	/**
	 * Used to determine if hero can reach safety if he puts a bomb on his
	 * location. IF safe tile count drops to this limit, there is nowhere to
	 * run.
	 */
	private final int		SAFETY_TILE_COUNT_LIMIT		= 0;
	/**
	 * If the AI cannot decide between this amount of tiles, he may be in
	 * endless loop.
	 */
	private final int		STUCK_UTILITY_TILE_COUNT	= 2;

	// CONSTRUCTOR
	/**
	 * The AI must be given to this class to operate properly.
	 * 
	 * @param ai
	 *            The AI to give.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected HeroOperation( CoskunOzdemir ai ) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;

	}

	// METHODS
	/**
	 * Checks the danger situation of this AI's hero. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return If this AI's hero is in danger (in a blast range or in a flame)
	 *         or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInDanger() throws StopRequestException
	{
		ai.checkInterruption();
		TileOperation to = new TileOperation( this.ai );
		if ( to.getCurrentDangerousTiles().contains( this.ai.getHero().getTile() ) ) return true;
		return false;
	}

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
		TileOperation to = new TileOperation( this.ai );
		if ( to.getCurrentDangerousTiles().contains( givenHero.getTile() ) ) return true;
		return false;
	}

	/**
	 * Returns enemy situation in the range of this AI's own hero. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return If there's another hero in the range or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInRange() throws StopRequestException
	{
		ai.checkInterruption();

		return isHeroInRange( this.ai.getHero() );
	}

	/**
	 * Returns enemy situation in the range of the given hero. <br />
	 * (TESTED, WORKS)
	 * 
	 * @param givenHero
	 *            The hero to do the operation for.
	 * @return If there's another hero in the range or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean isHeroInRange( AiHero givenHero ) throws StopRequestException
	{
		ai.checkInterruption();

		for ( AiHero currentHero : this.ai.getZone().getRemainingHeroes() )
		{
			ai.checkInterruption();
			if ( !currentHero.equals( givenHero ) )
			{
				TileOperation to = new TileOperation( this.ai );
				if ( to.getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentHero.getTile() ) ) return true;
			}
		}
		return false;
	}

	/**
	 * Checks if this AI's hero can reach a safe tile if he puts a bomb to his
	 * tile. <br />
	 * (TESTED, WORKS)
	 * 
	 * @return If given hero can access a safe tile or not.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean canReachSafety() throws StopRequestException
	{
		ai.checkInterruption();
		return this.canReachSafety( this.ai.getHero() );
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
		TileOperation to = new TileOperation( this.ai );
		ArrayList<AiTile> accessibleTiles = to.getAccessibleTiles( givenHero.getTile() );
		int safeTileCount = accessibleTiles.size();
		for ( AiTile currentTile : accessibleTiles )
		{
			ai.checkInterruption();
			if ( to.getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentTile ) || to.getCurrentDangerousTiles().contains( currentTile ) ) safeTileCount--;
		}
		return ( safeTileCount > SAFETY_TILE_COUNT_LIMIT );
	}

	/**
	 * Checks if this AI's hero is in endless loop between two tiles with
	 * biggest utility.
	 * 
	 * @return Returns true if hero is in endless loop.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected boolean heroInEndlessLoop() throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = false;
		if ( this.ai.getLastUtilityTiles().size() > ENDLESS_LOOP_LIMIT )
		{
			HashSet<AiTile> set = new HashSet<AiTile>( this.ai.getLastUtilityTiles() );
			if ( set.size() == STUCK_UTILITY_TILE_COUNT )
			{
				result = true;
				this.ai.setStuckUtilityTiles( set );
			}
		}
		return result;
	}

}
