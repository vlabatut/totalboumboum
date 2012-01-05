package org.totalboumboum.ai.v201112.ais.coskunozdemir.v3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * The main class of our AI. includes needed methods and our field/methods.
 * 
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class CoskunOzdemir extends ArtificialIntelligence
{
	/**
	 * Stores the utility map.
	 */
	private HashMap<AiTile, Float>	utilityMap;
	/**
	 * Stores last {@link #STORED_TILE_COUNT} utility tiles.
	 */
	private Queue<AiTile>			lastUtilityTiles;
	/**
	 * HashSet of utility tiles that our AI is stuck between.
	 */
	private HashSet<AiTile>			stuckUtilityTiles;
	/**
	 * Upper limit of the tile count of the last utility tiles. If reached, new
	 * values added to the queue will cause a pull (from tail) from the queue.
	 */
	private final int				STORED_TILE_COUNT	= 200;

	/**
	 * Represents this AI's hero.
	 */
	private AiHero					hero;

	/**
	 * Does tile operations.
	 */
	private TileOperation			to;
	/**
	 * Does hero operations.
	 */
	private HeroOperation			ho;
	/**
	 * Does path operations.
	 */
	private PathOperation			po;

	@Override
	protected void init() throws StopRequestException
	{
		checkInterruption();
		super.init();
		verbose = false;

		this.lastUtilityTiles = new LinkedList<AiTile>();
		this.stuckUtilityTiles = new HashSet<AiTile>();
		hero = this.getZone().getOwnHero();
		this.to = new TileOperation( this );
		this.ho = new HeroOperation( this );
		this.po = new PathOperation( this );
	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{
		checkInterruption();
	}

	@Override
	protected void updatePercepts() throws StopRequestException
	{
		checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler		modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler	utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler		bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler		moveHandler;

	@Override
	protected void initHandlers() throws StopRequestException
	{
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler( this );
		utilityHandler = new UtilityHandler( this );
		bombHandler = new BombHandler( this );
		moveHandler = new MoveHandler( this );

	}

	@Override
	protected AiModeHandler<CoskunOzdemir> getModeHandler() throws StopRequestException
	{
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<CoskunOzdemir> getUtilityHandler() throws StopRequestException
	{
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<CoskunOzdemir> getBombHandler() throws StopRequestException
	{
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<CoskunOzdemir> getMoveHandler() throws StopRequestException
	{
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{
		checkInterruption();
		moveHandler.updateOutput();
		utilityHandler.updateOutput();
		modeHandler.updateOutput();
	}

	// OUR METHODS

	/**
	 * Returns the tile which has the biggest utility value.
	 * 
	 * @return Returns the AiTile with the biggest utility value.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected AiTile getTileWithBiggestUtility() throws StopRequestException
	{
		checkInterruption();
		AiTile result = this.getZone().getOwnHero().getTile();
		for ( AiTile currentTile : utilityMap.keySet() )
		{
			checkInterruption();
			if ( utilityMap.get( currentTile ) > utilityMap.get( result ) )
			{
				result = currentTile;
			}
		}
		return result;
	}

	/**
	 * Adds the given tile to the last visited tiles lastTiles.
	 * 
	 * @param toAdd
	 *            Tile to add.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected void addToLastUtilityTiles( AiTile toAdd ) throws StopRequestException
	{
		checkInterruption();
		this.lastUtilityTiles.add( toAdd );
		if ( this.lastUtilityTiles.size() > STORED_TILE_COUNT )
		{
			this.lastUtilityTiles.remove();
		}
	}

	// GETTERS

	/**
	 * Method to get Utility map.
	 * 
	 * @return Utility map. Can be empty at start of the game.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected HashMap<AiTile, Float> getUtilityMap() throws StopRequestException
	{
		checkInterruption();
		return utilityMap;
	}

	/**
	 * Method to get the last N calculated utility tiles.
	 * 
	 * @return The last N calculated utility tiles.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected Queue<AiTile> getLastUtilityTiles() throws StopRequestException
	{
		checkInterruption();
		return this.lastUtilityTiles;
	}

	/**
	 * If the hero is in endless loop, this method can be called to see which
	 * utility tiles he is trying to reach. If the count is 2, hero is probably
	 * stuck. This method returns data similar to {@link #getLastUtilityTiles()}
	 * , with duplicate data removed.
	 * 
	 * @return The utility tiles the hero stuck between.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected HashSet<AiTile> getStuckUtilityTiles() throws StopRequestException
	{
		checkInterruption();
		return stuckUtilityTiles;
	}

	/**
	 * Method to get this AI's hero.
	 * 
	 * @return this AI's hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiHero getHero() throws StopRequestException
	{
		checkInterruption();
		return hero;
	}

	/**
	 * Method to get the tile operation object of the current hero.
	 * 
	 * @return the tile operation object.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public TileOperation getTo() throws StopRequestException
	{
		checkInterruption();
		return to;
	}

	/**
	 * Method to get the hero operation object of the current hero.
	 * 
	 * @return the hero operation object.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public HeroOperation getHo() throws StopRequestException
	{
		checkInterruption();
		return ho;
	}

	/**
	 * Method to get the path operation object of the current hero.
	 * 
	 * @return the path operation object.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public PathOperation getPo() throws StopRequestException
	{
		checkInterruption();
		return po;
	}

	// SETTERS

	/**
	 * Sets utility map to the field.
	 * 
	 * @param utilityMap
	 *            The given utility map.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected void setUtilityMap( HashMap<AiTile, Float> utilityMap ) throws StopRequestException
	{
		checkInterruption();
		this.utilityMap = utilityMap;
	}

	/**
	 * Method to set the stuck utility tiles.
	 * 
	 * @param stuckTiles
	 *            HashSet of the stuck tiles to set.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected void setStuckUtilityTiles( HashSet<AiTile> stuckTiles ) throws StopRequestException
	{
		checkInterruption();
		this.stuckUtilityTiles = stuckTiles;
	}

}
