package org.totalboumboum.ai.v201112.ais.balcetin.v3;

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
{	/** */
	public AiHero ownHero ;
	/** */
	public AiTile currentTile;
	
	@Override
	protected void init() throws StopRequestException
	{	checkInterruption();
		
		super.init();
		
		//no text output on the console
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
	protected ModeHandler modeHandler;
	protected UtilityHandler utilityHandler;
	protected BombHandler bombHandler;
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
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

			moveHandler.updateOutput();
			
	}

	




}
