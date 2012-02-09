package org.totalboumboum.ai.v201112.ais.balcetin.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Bomb Handler class to drop bomb action.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<BalCetin> {

	protected BombHandler(BalCetin ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		verbose = true;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException {
		ai.checkInterruption();
		AiMode mode = ai.modeHandler.getMode();

		TileProcess tp = new TileProcess(this.ai);
	if(mode == AiMode.COLLECTING){
		if ((tp.isTileDangerousOnBombDrop() == false)
				&& (ai.getZone().getOwnHero().getTile() == tp.getBestTile()) && (tp.getBestTile().getItems().size() == 0)) {

			return true;
		} 
		else 
			return false;
	}	
	else  //mode is attack, no need to check if there is an item on the tile.	if we reached to Best tile, than drop.
		{
		if ((tp.isTileDangerousOnBombDrop() == false)
				&& (ai.getZone().getOwnHero().getTile() == tp.getBestTile()))
				return true;
		else 
			return false;
		}
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException {
		ai.checkInterruption();

	}
}
