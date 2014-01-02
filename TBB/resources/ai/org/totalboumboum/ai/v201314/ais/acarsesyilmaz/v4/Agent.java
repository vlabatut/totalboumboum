package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;

/**
 * Classe principale de notre agent.
 *
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class Agent extends ArtificialIntelligence
{
	
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	
		checkInterruption();
	}
	
	/**
	 * Initialise autre choses que les percepts et gestionnaires si nécessaire
	 */
	@Override
	protected void initOthers()
	{	
		checkInterruption();
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initialise respectivement les percepts
	 */
	@Override
	protected void initPercepts()
	{	
	checkInterruption();
	}
	
	/**
	 * Met à jour respectivement les percepts
	 */
	@Override
	protected void updatePercepts()
	{	
		checkInterruption();		
		//this.securityHandler.updateAccessibleTiles(); 
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;	
	/**Instancie la classe secondaire concernant l'attaque de l'agent.*/
	public AttackHandler attackHandler;	
	/**Instancie la classe secondaire concernant la sécurité de l'agent.*/
	public SecurityHandler securityHandler;	
	/**Instancie la classe secondaire concernant les items de l'agent.*/
	public ItemHandler itemHandler;
	
	/**
	 * Initialise respectivement les gestionnaires
	 */
	@Override
	protected void initHandlers()
	{	
		checkInterruption();		
		attackHandler = new AttackHandler(this);
		securityHandler = new SecurityHandler(this);
		itemHandler = new ItemHandler(this);
		
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);		
		
		verbose = false;
		modeHandler.verbose = false;		
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
	}

	/**
	 * Renvoie le gestionnaire de préférence de cet agent.
	 * 
	 * @return
	 * 		Le gestionnaire de mode de cet agent.
	 */
	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	checkInterruption();
		return modeHandler;
	}

	/**
	 * Renvoie le gestionnaire de posage de bombe de cet agent.
	 * 
	 * @return
	 * 		Le gestionnaire de préférence de cet agent.
	 */
	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	checkInterruption();
		return preferenceHandler;
	}

	/**
	 * Renvoie le gestionnaire de déplacement de cet agent.
	 * 
	 * @return
	 * 		Le gestionnaire de posage de bombe de cet agent.
	 */
	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	checkInterruption();
		return bombHandler;
	}

	/**
	 * Indique si l'agent a déjà été initialisé ou pas
	 * 
	 * @return
	 * 		Le gestionnaire de déplacement de cet agent.
	 */
	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	
	/**
	 * Met à jour la sortie graphique.
	 */
	@Override
	protected void updateOutput()
	{	
		checkInterruption();
		AiOutput output = getOutput();
		output.setTextSize(2);
		moveHandler.updateOutput();
		preferenceHandler.updateOutput();
	}
}
