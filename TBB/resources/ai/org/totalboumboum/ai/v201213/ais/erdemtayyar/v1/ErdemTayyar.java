package org.totalboumboum.ai.v201213.ais.erdemtayyar.v1;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.Heros;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.Paths;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.Tiles;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * The main class of our AI. includes needed methods and our field/methods.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class ErdemTayyar extends ArtificialIntelligence {
	/**
	 * Stores the utility map.
	 */
	private Map<AiTile, Float> utilityMap;
	/**
	 * Stores last {@link #STORED_TILE_COUNT} utility tiles.
	 */
	private Queue<AiTile> lastUtilityTiles;
	/**
	 * HashSet of utility tiles that our AI is stuck between.
	 */
	private HashSet<AiTile> stuckUtilityTiles;

	/**
	 * Represents this AI's hero.
	 */
	private AiHero hero;

	/**
	 * Upper limit of the tile count of the last utility tiles. If reached, new
	 * values added to the queue will cause a pull (from tail) from the queue.
	 */
	private final int STORED_TILE_COUNT = 200;

	/**
	 * tile operations.
	 */
	private Tiles ts;
	/**
	 * hero operations.
	 */
	private Heros hs;
	/**
	 * path operations.
	 */
	private Paths ps;

	/**
	 * Instancie la classe principale de l'agent.
	 */
	public ErdemTayyar() {
		// active/désactive la sortie texte
		verbose = false;

	}

	@Override
	protected void initOthers() throws StopRequestException {
		checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException {
		checkInterruption();
		this.ts = new Tiles(this);
		this.hs = new Heros(this);
		this.ps = new Paths(this);

		this.lastUtilityTiles = new LinkedList<AiTile>();
		this.stuckUtilityTiles = new HashSet<AiTile>();
		hero = this.getZone().getOwnHero();
	}

	@Override
	protected void updatePercepts() throws StopRequestException {
		checkInterruption();
		this.ts = new Tiles(this);
		this.hs = new Heros(this);
		this.ps = new Paths(this);

		this.lastUtilityTiles = new LinkedList<AiTile>();
		this.stuckUtilityTiles = new HashSet<AiTile>();
		hero = this.getZone().getOwnHero();
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() throws StopRequestException {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);

	}

	
	@Override
	
	protected AiModeHandler<ErdemTayyar> getModeHandler()
			throws StopRequestException {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<ErdemTayyar> getUtilityHandler()
			throws StopRequestException {
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<ErdemTayyar> getBombHandler()
			throws StopRequestException {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<ErdemTayyar> getMoveHandler()
			throws StopRequestException {
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException {
		checkInterruption();
		modeHandler.updateOutput();
		
		utilityHandler.updateOutput();
		
		bombHandler.updateOutput();
		
		moveHandler.updateOutput();

		

		

	}

	// OUR METHODS

	/**
	 * Method to get this AI's hero.
	 * 
	 * @return this AI's hero.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public AiHero getHero() throws StopRequestException {
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
	public Tiles getTs() throws StopRequestException {
		checkInterruption();
		return ts;
	}

	/**
	 * Method to get the hero operation object of the current hero.
	 * 
	 * @return the hero operation object.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public Heros getHs() throws StopRequestException {
		checkInterruption();
		return hs;
	}

	/**
	 * Method to get the path operation object of the current hero.
	 * 
	 * @return the path operation object.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public Paths getPs() throws StopRequestException {
		checkInterruption();
		return ps;
	}

	/**
	 * Returns the tile which has the biggest utility value.
	 * 
	 * @return Returns the AiTile with the biggest utility value.
	 * 
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected AiTile getTileWithBiggestUtility() throws StopRequestException {
		checkInterruption();
		AiTile result = this.getZone().getOwnHero().getTile();
		for (AiTile currentTile : utilityMap.keySet()) {
			checkInterruption();
			if (utilityMap.get(currentTile) > utilityMap.get(result)) {
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
	protected void addToLastUtilityTiles(AiTile toAdd)
			throws StopRequestException {
		checkInterruption();
		this.lastUtilityTiles.add(toAdd);
		if (this.lastUtilityTiles.size() > STORED_TILE_COUNT) {
			this.lastUtilityTiles.remove();
		}
	}

	/**
	 * Method to get the last N calculated utility tiles.
	 * 
	 * @return The last N calculated utility tiles.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected Queue<AiTile> getLastUtilityTiles() throws StopRequestException {
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
	protected HashSet<AiTile> getStuckUtilityTiles()
			throws StopRequestException {
		checkInterruption();
		return stuckUtilityTiles;
	}

	/**
	 * Sets utility map to the field.
	 * 
	 * @param utilityMap
	 *            The given utility map.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected void setUtilityMap(Map<AiTile, Float> utilityMap)
			throws StopRequestException {
		checkInterruption();
		this.utilityMap = utilityMap;
	}

	/**
	 * Method to get Utility map.
	 * 
	 * @return Utility map. Can be empty at start of the game.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected Map<AiTile, Float> getUtilityMap()
			throws StopRequestException {
		checkInterruption();
		return utilityMap;
	}

	/**
	 * Method to set the stuck utility tiles.
	 * 
	 * @param stuckTiles
	 *            HashSet of the stuck tiles to set.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	protected void setStuckUtilityTiles(HashSet<AiTile> stuckTiles)
			throws StopRequestException {
		checkInterruption();
		this.stuckUtilityTiles = stuckTiles;
	}
}
