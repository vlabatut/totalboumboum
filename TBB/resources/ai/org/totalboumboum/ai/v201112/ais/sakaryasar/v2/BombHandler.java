package org.totalboumboum.ai.v201112.ais.sakaryasar.v2;

import java.util.HashMap;
import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<SakarYasar>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		verbose = false;
    	
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		
		HashMap<AiTile,Float> hm = ai.utilityHandler.getUtilitiesByTile();
		
		if(ai.modeHandler.getMode()==AiMode.COLLECTING){
			if(( hm.get(ownHero.getTile()) ==6 || hm.get(ownHero.getTile())==7 ) )
				if(isSafeToDropBomb(ownHero.getTile()))
					result =true;
		}
		if(ai.modeHandler.getMode()==AiMode.ATTACKING){
			if( ( hm.get(ownHero.getTile())>=5) )
				if(isSafeToDropBomb(ownHero.getTile()))
					result =true;
		}
		if(result && this.newDest!=null){
			ai.moveHandler.setCurrentDestination(newDest);
		}
		return result;
	}

	//////////////////////////////////////////////////////////////////
	// Own Methods 		//////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	private AiZone zone = ai.getZone();
	private AiHero ownHero = zone.getOwnHero();
	private AiTile newDest;
	/**
	 * method for checking safet of tiles in direction d 
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private boolean checkSafety(AiTile tile, Direction d) throws StopRequestException {
		ai.checkInterruption();

		boolean result = false;
		
		if ((d == Direction.LEFT || d == Direction.RIGHT)) {
			result = !hasBomb(tile, Direction.UP) || !hasBomb(tile, Direction.DOWN);
		} else {
			result = !hasBomb(tile, Direction.LEFT) || !hasBomb(tile, Direction.RIGHT);
		}
		
		return result;
	}
	/**
	 * method for checking safety of neighbors of a tile
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private boolean checkNeighbors(AiTile tile, Direction d) throws StopRequestException{
		ai.checkInterruption();
		boolean result = false;
		boolean cont = true;

		AiTile temp = tile.getNeighbor(d);
		while(!result && temp.getBlocks().isEmpty() && cont){	
			ai.checkInterruption();

			if(!temp.getBombs().isEmpty()){
				cont = false;
			}
			result = checkSafety(temp, d);
			temp = temp.getNeighbor(d);
		}
		return result;
	}
	/**
	 * method for testing if dropping a bomb is safe or not
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	private boolean isSafeToDropBomb(AiTile tile) throws StopRequestException{
		ai.checkInterruption();

		boolean result= false;

		result= checkNeighbors(tile, Direction.LEFT) || checkNeighbors(tile, Direction.RIGHT) 
				|| checkNeighbors(tile, Direction.UP) || checkNeighbors(tile, Direction.DOWN);

		return result;
	}
	/**
	 * check the d for possible bombs.
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private boolean hasBomb(AiTile tile, Direction d) throws StopRequestException{
		ai.checkInterruption();

		boolean result = false;
		
		AiTile temp = tile;
		int i=0;
		temp=temp.getNeighbor(d);
		
		while(temp.getBlocks().isEmpty() && temp.getBombs().isEmpty()){
			ai.checkInterruption();

			temp=temp.getNeighbor(d);
			i++;
		}
		if(!temp.getBombs().isEmpty()){
			if(temp.getBombs().get(0).getRange()>i){
				result = true;
			}
		}
		if(i==0){
			result = true;
		}
		
		return result;
	}
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
	}
}
