package org.totalboumboum.ai.v201213.ais.besnilikangal.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;

/**
 * La class main de notre IA.
 * 
 * @author Mustafa Besnili
 * @author Doruk Kangal
 */
public class BesniliKangal extends ArtificialIntelligence
{
	/** Represents this AI's hero */
	public AiHero ownHero;
	/** Faire l'operation des cases */
	public TileOperation tileOperation;
	/** Faire l'operation des chemins */
	public PathOperation pathOperation;
	/** Faire l'operation des agents */
	public HeroOperation heroOperation;

	/**
	 * Instancie la classe principale de l'agent.
	 */
	public BesniliKangal()
	{	// active/désactive la sortie texte
		verbose = false;
	}

	@Override
	protected void initOthers() throws StopRequestException
	{	// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
		checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
		checkInterruption();
		ownHero = getZone().getOwnHero();
		tileOperation = new TileOperation( this );
		pathOperation = new PathOperation( this );
		heroOperation = new HeroOperation( this );
	}

	@Override
	protected void updatePercepts() throws StopRequestException
	{	// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
		checkInterruption();
		tileOperation.update();
	}

	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;

	@Override
	protected void initHandlers() throws StopRequestException
	{	// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
		checkInterruption();
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		modeHandler.verbose = verbose;

		utilityHandler = new UtilityHandler(this);
		utilityHandler.verbose = verbose;

		bombHandler = new BombHandler(this);
		bombHandler.verbose = verbose;

		moveHandler = new MoveHandler(this);
		moveHandler.verbose = verbose;
	}

	@Override
	protected AiModeHandler<BesniliKangal> getModeHandler() throws StopRequestException
	{
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<BesniliKangal> getUtilityHandler() throws StopRequestException
	{
		checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<BesniliKangal> getBombHandler() throws StopRequestException
	{
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<BesniliKangal> getMoveHandler() throws StopRequestException
	{
		checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();
		utilityHandler.updateOutput();
		moveHandler.updateOutput();
		modeHandler.updateOutput();
	}
}
