package org.totalboumboum.ai.v201112.ais.balcetin.v2;

import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;


import org.totalboumboum.engine.content.feature.Direction;

/**
 * Move Handler class to decide where AI moves.
 * 
 * @author Adnan Bal
 * @author Özcan Çetin
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<BalCetin>
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
	protected MoveHandler(BalCetin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
	
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		TileProcess tp = new TileProcess(this.ai);
		
		System.out.println("\nDangerous tiles : " + tp.getDangerousTiles());
		System.out.println("\nsafe tiles : " + tp.getsafeTiles());
		System.out.println("\nwalkable tiles : " + tp.getwalkableTiles());
		System.out.println("\ncan reach tiles : " + tp.getcanReachTiles());
		for (AiTile walkableTile : tp.getwalkableTiles().subList(2, 3)) {
			ai.checkInterruption();
			System.out.println("\nwalkabletile : " + walkableTile);
				return this.ai.getZone().getDirection(this.ai.getZone().getOwnHero().getTile(), walkableTile);

		}
		

	
		
		return Direction.NONE;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	
	}
}
