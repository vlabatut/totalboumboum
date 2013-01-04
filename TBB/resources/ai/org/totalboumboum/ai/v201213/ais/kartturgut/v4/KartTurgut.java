package org.totalboumboum.ai.v201213.ais.kartturgut.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 
 * @author Yunus Kart
 * @author Siyabend Turgut
 */
public class KartTurgut extends ArtificialIntelligence
{
	
	/** */
	public AiHero notreHero ;
	/** */
	public AiTile notreTile;
	/** */
	public KartTurgut()
	{	
		verbose = false;
	}
	
	
	
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
	this.notreHero = this.getZone().getOwnHero();
	this.notreTile = this.notreHero.getTile();
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		
	this.notreHero = this.getZone().getOwnHero();
	this.notreTile = this.notreHero.getTile();
	}
	
	
	/** */
	protected ModeHandler modeHandler;
	/** */
	protected UtilityHandler utilityHandler;
	/** */
	protected BombHandler bombHandler;
	/** */
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
	protected AiModeHandler<KartTurgut> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<KartTurgut> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<KartTurgut> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<KartTurgut> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		
			moveHandler.updateOutput();
			
			utilityHandler.updateOutput();
	
		
	}


}