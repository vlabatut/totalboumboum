package org.totalboumboum.ai.v201112.ais.demirsazan.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * @author Serdil Demir
 * @author Gökhan Sazan
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<DemirSazan>
{	
	protected ModeHandler(DemirSazan ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
		AiZone gameZone =ai.getZone();

		AiHero ownHero = gameZone.getOwnHero();
		int bombNum = ownHero.getBombNumberMax();
		if(bombNum <2){
			return false;
		}
		return true;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		AiZone gameArea =ai.getZone();
		int itemsSize= gameArea.getItems().size();
		int  heroSize= gameArea.getRemainingHeroes().size();
		if((itemsSize / heroSize )>1){
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
	}
}
