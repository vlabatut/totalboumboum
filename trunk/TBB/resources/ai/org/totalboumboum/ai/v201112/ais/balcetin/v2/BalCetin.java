package org.totalboumboum.ai.v201112.ais.balcetin.v2;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Our principal class of our AI 
 *
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class BalCetin extends ArtificialIntelligence
{
	/** */
	public AiHero ownHero ;
	/** */
	public AiTile currentTile;
	
	@Override
	protected void init() throws StopRequestException
	{	checkInterruption();
		
		super.init();
		verbose = false;
		
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
	
		this.ownHero = this.getZone().getOwnHero();
		this.currentTile = this.ownHero.getTile();
		
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		
	
		this.ownHero = this.getZone().getOwnHero();
		this.currentTile = this.ownHero.getTile();	
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
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		
		
	
	}

	@Override
	protected AiModeHandler<BalCetin> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<BalCetin> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<BalCetin> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<BalCetin> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
	
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}

	




}
