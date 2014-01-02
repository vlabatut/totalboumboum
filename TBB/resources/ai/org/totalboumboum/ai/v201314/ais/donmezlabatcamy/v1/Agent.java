package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;

/**
 * Classe principale de notre agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class Agent extends ArtificialIntelligence {
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();
	}

    /**
     * Méthode permettant de faire une initialisation supplémentaire.
     */
	@Override
	protected void initOthers() {
		checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
    /**
     * Méthode permettant d'initilaiser les percepts de l'agent c'est à dire les 
     * différents objets stockés en interne dans ces classes.
     */
	@Override
	protected void initPercepts() {
		checkInterruption();

	}
    
    /**
     * Méthode permettant de mettre à jour les percepts de l'agent c'est à dire les
     * différents objets stockés en interne dans ces classes.
     */
	@Override
	protected void updatePercepts() {
		checkInterruption();

		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = true;
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

    /**
     * Cette méthode a pour but d'initialiser les gestionnaires.
     */
	@Override
	protected void initHandlers() {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
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
    
    /** 
     * Méthode de permettant de mettre à jour les sorties graphiques de l'agent.
     */
	@Override
	protected void updateOutput() {
		checkInterruption();

		AiOutput output = getOutput();
		output.setTextSize(2);


		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();

	}
}
