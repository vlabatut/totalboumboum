package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v3;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu Chaque agent doit hériter de cette classe. La méthode
 *         processAction est la méthode appelée par le gestionnaire d'agents
 *         pour obtenir la prochaine action à effectuer. Cette méthode
 *         implémente l'algorithme général spécifié en cours et ne doit être ni
 *         modifiée ni surchargée.
 */

public class Agent extends ArtificialIntelligence {
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();
	}

	/** Pour les cases qui est traversable par nous directement et secure. */
	public Set<AiTile> acces;
	/** Pour les cases secure */
	public Set<AiTile> safeTiles;
	/**
	 * value boolean pour adversaire est traversable directement par notre agent
	 */
	public boolean isEnnemyAccesible;
	/** Pour les cases dans fires, blasts ou bombes */
	public ArrayList<AiTile> dangerTiles;

	@Override
	/**
	 Méthode permettant de faire une initialisation supplémentaire. Elle est automatiquement appelée à la 
	fin de init(). Par défaut, cette méthode ne fait rien, mais elle peut être surchargée si nécessaire. Comme 
	elle est faite après l'initialisation des gestionnaires, il est possible d'y utiliser les gestionnaires, ou des 
	objets qu'ils ont créés (ce qui n'est pas le cas de initPercepts(), qui est elle appelée avant la création des 
	gestionnaires). **/
	protected void initOthers() {
		checkInterruption();
		safeTiles = new TreeSet<AiTile>();
		dangerTiles = new ArrayList<AiTile>();
		isEnnemyAccesible = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	@Override
	/**
	 * Méthode permettant d'initialiser les percepts de l'agent, c'est-à-dire
	 * les différents objets stockés en interne dans ses classes.
	 **/
	protected void initPercepts() {
		checkInterruption();
		acces = new TreeSet<AiTile>();

	}

	@Override
	/**
	Méthode permettant de mettre à jour les percepts de l'agent, c'est-à-dire les différents objets stockés 
	en interne dans ses classes.**/
	protected void updatePercepts() {
		checkInterruption();

		acces.clear();
		acces = this.preferenceHandler.selectTiles();

		isEnnemyAccesible = this.tileHandler.isEnnemyAccesible();

		dangerTiles.clear();
		dangerTiles = this.tileHandler.dangerTiles();

		safeTiles.clear();
		safeTiles = this.tileHandler.safeTiles();

		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	/** Gestionnaire chargé de calculer des cases */
	public TileHandler tileHandler;

	@Override
	/**Cette méthode a pour but d'initialiser les gestionnaires. Elle doit obligatoirement être surchargée.*/
	protected void initHandlers() {
		checkInterruption();

		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		tileHandler = new TileHandler(this);

	}

	@Override
	/**Renvoie le gestionnaire de mode de cet agent. Il doit avoir d'abord été créé dans la méthode 
	initHandlers().**/
	protected AiModeHandler<Agent> getModeHandler() {
		checkInterruption();
		return modeHandler;
	}

	@Override
	/**

	Renvoie le gestionnaire de préférence de cet agent. Il doit avoir d'abord été créé dans la 
	méthode initHandlers().
	 **/
	protected AiPreferenceHandler<Agent> getPreferenceHandler() {
		checkInterruption();
		return preferenceHandler;
	}

	@Override
	/**

	Renvoie le gestionnaire de préférence de cet agent. Il doit avoir d'abord été créé dans la 
	méthode initHandlers().
	 **/
	protected AiBombHandler<Agent> getBombHandler() {
		checkInterruption();
		return bombHandler;
	}

	@Override
	/**
	Renvoie le gestionnaire de déplacement de cet agent. Il doit avoir d'abord été créé dans la méthode 
	initHandlers().**/
	protected AiMoveHandler<Agent> getMoveHandler() {
		checkInterruption();
		return moveHandler;
	}

	/**
	 * Renvoie le gestionnaire de cases de cet agent.
	 * 
	 * @return tileHandler
	 */
	protected TileHandler getTileHandler() {
		checkInterruption();
		return tileHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Méthode permettant de mettre à jour les sorties graphiques de l'agent. 
	 */
	protected void updateOutput() {
		checkInterruption();

		AiOutput output = getOutput();
		output.setTextSize(2);

		moveHandler.updateOutput();

		preferenceHandler.updateOutput();

	}
}
