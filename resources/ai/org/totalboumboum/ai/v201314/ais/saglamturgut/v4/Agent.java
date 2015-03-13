package org.totalboumboum.ai.v201314.ais.saglamturgut.v4;

import org.totalboumboum.ai.v201314.adapter.agent.*;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

import java.util.LinkedList;

/**
 * The main class of the AI. includes needed methods and extra written
 * field/methods.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class Agent extends ArtificialIntelligence {
	/**
	 * Tile utility object
	 */
	private TileUtil tileUtil;

	/**
	 * Previous decision is used for being decisive, if the hero has the same
	 * minimum preference with previous decision, stay on the previous decision
	 */
	private int previousMinPref;

	/**
	 * Previous decision is used for being decisive, if the hero has the same
	 * minimum preference with previous decision, stay on the previous decision
	 */
	private AiTile previousDecision;

	/**
	 * Queue that holds last selected 50 tiles inside.
	 */
	private LimitedQueue<AiTile> lastTiles;

	/**
	 * Constructs the agent object.
	 */
	public Agent() {
		checkInterruption();
		tileUtil = new TileUtil(this);
		lastTiles = new LimitedQueue<AiTile>(this, 50);
	}

	/**
	 * initialises the internal objects of the class.
	 */
	@Override
	protected void initPercepts() {
		checkInterruption();
	}

	/**
	 * Updates the internal objects of the class.
	 */
	@Override
	protected void updatePercepts() {
		checkInterruption();

		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;
	}

	/**
	 * Handler for the mode decision of the agent.
	 */
	protected ModeHandler modeHandler;

	/**
	 * Handler for the preference decision (tile selection and category identification) of the agent.
	 */
	protected PreferenceHandler preferenceHandler;
	/**
	 * Handler for the bomb decision of the agent, the agent uses this handler to decide to put a bomb to its
	 * location or not.
	 */
	protected BombHandler bombHandler;
	/**
	 * Handler for the moving decisions of the agent. The agent selects a destination tile, calculates a path
	 * and decides a direction using this handler.
	 */
	protected MoveHandler moveHandler;

	/**
	 * Initialises all the handlers of the agent.
	 */
	@Override
	protected void initHandlers() {
		checkInterruption();

		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	}

	/**
	 * Method to get the mode handler of the agent.
	 * @return Mode handler of the agent.
	 */
	@Override
	protected AiModeHandler<Agent> getModeHandler() {
		checkInterruption();
		return modeHandler;
	}

	/**
	 * Method to get the preference handler of the agent.
	 * @return Preference handler of the agent.
	 */
	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler() {
		checkInterruption();
		return preferenceHandler;
	}

	/**
	 * Method to get the bomb handler of the agent.
	 * @return Bomb handler of the agent.
	 */
	@Override
	protected AiBombHandler<Agent> getBombHandler() {
		checkInterruption();
		return bombHandler;
	}

	/**
	 * Method to get the move handler of the agent.
	 * @return Move handler of the agent.
	 */
	@Override
	protected AiMoveHandler<Agent> getMoveHandler() {
		checkInterruption();
		return moveHandler;
	}

	/**
	 * Updates the console output of the agent.
	 */
	@Override
	protected void updateOutput() {
		checkInterruption();
		
		AiOutput output = getOutput();
		output.setTextSize(2);
 
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();
	}

	/**
	 * Returns the tile utility object.
	 * 
	 * @return The tile utility object.
	 */
	public TileUtil getTileUtil() {
		checkInterruption();
		return tileUtil;
	}

	/**
	 * Returns the previous minimum preference value.
	 * 
	 * @return The previous minimum preference value.
	 */
	public int getPreviousMinPref() {
		checkInterruption();
		return previousMinPref;
	}

	/**
	 * Sets the previous minimum preference value.
	 * 
	 * @param previousMinPref
	 *            The minimum preference value to set.
	 */
	public void setPreviousMinPref(int previousMinPref) {
		checkInterruption();
		this.previousMinPref = previousMinPref;
	}

	/**
	 * Returns the previous tile agent was on.
	 * 
	 * @return The previous tile agent was on
	 */
	public AiTile getPreviousDecision() {
		checkInterruption();
		return previousDecision;
	}

	/**
	 * Sets the previos tile decision.
	 * 
	 * @param previousDecision
	 *            The previous tile decision to set.
	 */
	public void setPreviousDecision(AiTile previousDecision) {
		checkInterruption();
		this.previousDecision = previousDecision;
	}

	/**
	 * Returns the last 50 decided tiles.
	 * 
	 * @return The last 50 decided tiles.
	 */
	public LinkedList<AiTile> getLastTiles() {
		checkInterruption();
		return lastTiles.getTiles();
	}

	/**
	 * Adds a tile to the decided tiles.
	 * 
	 * @param tile
	 *            Tile to add.
	 */
	public void addToLastTiles(AiTile tile) {
		checkInterruption();
		lastTiles.add(tile);
	}
}
