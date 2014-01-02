package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v3.util.LimitedQueue;

/**
 * The main class of the AI. includes needed methods and extra written
 * field/methods.
 * 
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
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
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();
		tileUtil = new TileUtil(this);
		lastTiles = new LimitedQueue<AiTile>(this, 50);
	}

	@Override
	protected void initOthers() {
		checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() {
		checkInterruption();
	}

	@Override
	protected void updatePercepts() {
		checkInterruption();

		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Gestionnaire chargé de calculer le mode de l'agent
	 */
	protected ModeHandler modeHandler;
	/**
	 * Gestionnaire chargé de calculer les valeurs de préférence de l'agent
	 */
	protected PreferenceHandler preferenceHandler;
	/**
	 * Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas
	 */
	protected BombHandler bombHandler;
	/**
	 * Gestionnaire chargé de décidé de la direction de déplacement de l'agent
	 */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() {
		checkInterruption();

		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	}

	@Override
	protected AiModeHandler<Agent> getModeHandler() {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler() {
		checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler() {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler() {
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
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
	public LimitedQueue<AiTile> getLastTiles() {
		checkInterruption();
		return lastTiles;
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
