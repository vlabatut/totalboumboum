package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;

import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;

/**
 * Classe principale de notre agent
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
@SuppressWarnings("deprecation")
public class Agent extends ArtificialIntelligence {

	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();
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

		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;

		enemyHandler.verbose = false;
		collecteHandler.verbose = false;
		tileCalculationHandler.verbose = false;
		blockingHandler.verbose = false;
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

	/** Gestionnaire chargé de décider de l'ennemie cible de l'agent */
	public EnemyHandler enemyHandler;
	/** Gestionnaire chargé de décider blockage de l'ennemi cible */
	public BlockingHandler blockingHandler;
	/** Gestionnaire chargé de prendre decisions sur les cases */
	public TileCalculationHandler tileCalculationHandler;
	/** Gestionnaire chargé de décider de mode collecte de l'agent */
	public CollecteHandler collecteHandler;

	@Override
	protected void initHandlers() {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		// création des gestionnaires que nous avons ajouté
		enemyHandler = new EnemyHandler(this);
		blockingHandler = new BlockingHandler(this);
		tileCalculationHandler = new TileCalculationHandler(this);
		collecteHandler = new CollecteHandler(this);
		// initialisation des gestionnaires pour que nous puisse acceder ses
		// valeurs
		preferenceHandler.initHandler(this);
		blockingHandler.initHandler(this);
		bombHandler.initHandler(this);
		moveHandler.initHandler(this);
		modeHandler.initHandler(this);
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

		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();
		// le mode courant
		modeHandler.updateOutput();
		// bombHandler courant
		bombHandler.updateOutput();
		
		// l'ennemie cible courant
		enemyHandler.updateOutput();
		// les calculs associé à la mode collecte
		collecteHandler.updateOutput();
		// les calculs associé à la blockage de l'ennemie
		blockingHandler.updateOutput();
		// les calculs courant pour les cases
		tileCalculationHandler.updateOutput();
	}
}
