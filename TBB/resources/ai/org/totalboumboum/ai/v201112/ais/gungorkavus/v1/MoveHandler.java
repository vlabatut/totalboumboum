package org.totalboumboum.ai.v201112.ais.gungorkavus.v1;

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
public class MoveHandler extends AiMoveHandler<GungorKavus>
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
	@SuppressWarnings({ "unused", "null" })
	protected MoveHandler(GungorKavus ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
		
		// TODO à compléter
		
		AiZone zone = ai.getZone();
		AiTile ownTile = zone.getOwnHero().getTile();
		HashMap<AiTile,Float> utility = ai.utilityHandler.getUtilitiesByTile();
		
		HashMap<Float, List<AiTile>> utility2 = ai.utilityHandler.getUtilitiesByValue();

		
		
		
		
		
		
		Astar astar = null;
		AiPath path ;
		try {
			path = astar.processShortestPath(currentDestination);
		} catch (LimitReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
	
		AiTile cTile = null;
		Direction cDirection = Direction.UP;
		

		if(ownHero!=null)
		{
			cTile=ownHero.getTile(); 
		}
		
		if(currentDirection == Direction.DOWN && cTile.getNeighbor(Direction.DOWN).isCrossableBy(ownHero, false, false, false, false, true, true)){
			cDirection=Direction.DOWN;
		}
		if(currentDirection == Direction.LEFT && cTile.getNeighbor(Direction.LEFT).isCrossableBy(ownHero, false, false, false, false, true, true)){
			cDirection=Direction.LEFT;
		}
		if(currentDirection == Direction.RIGHT && cTile.getNeighbor(Direction.RIGHT).isCrossableBy(ownHero, false, false, false, false, true, true)){
			cDirection=Direction.RIGHT;
		}
		if(currentDirection == Direction.UP && cTile.getNeighbor(Direction.UP).isCrossableBy(ownHero, false, false, false, false, true, true)){
			cDirection=Direction.UP;
		}
		
		
		
		// TODO à compléter
		
		return cDirection;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
