package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201213.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201213.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
@SuppressWarnings("deprecation")
public class NombreMursAttack extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "NOMBREMURS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** initialize the zone	 */
	protected AiZone zone = null;
	/** initialize the ownHero	 */
	protected AiHero ownHero = null;
	/** initialize the tile of ownHero	 */
	protected AiTile currentTile = null;
	/** initialize the astar for direct path */
	protected Astar astarPrecise = null;
	/** initialize the astar for indirect path */
	protected Astar astarApproximation = null;
	/** initialize the dijkstra for all tiles*/
	protected Dijkstra dijkstra = null;
	/** initialize a path for indirect*/
	protected AiPath indirectPath = null;
	/** initialize a tile for escape*/
	protected AiTile safeDestination = null;
	/** initialize a boolean for dropping bomb*/
	protected boolean secondaryBombing = false;
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	public NombreMursAttack(CaliskanGeckalan ai) throws StopRequestException
	{	// init nom
		super(ai,NAME);
		ai.checkInterruption();
		
		// init agent
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = true;
		int count = 0;
		AiPath path = null;
		
		ApproximateCostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
		astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
	
		Astar astar = new Astar(ai, ai.getZone().getOwnHero(), costCalculator, heuristicCalculator, successorCalculator);
		try {
			path = astar.startProcess(new AiLocation(currentTile), tile);
		} catch (LimitReachedException e) {
			e.printStackTrace();
		}
		if(path!=null) {
			for(int i=0; i< path.getLength(); i++) {
				ai.checkInterruption();
				AiTile tilePath = path.getLocation(i).getTile();
				List<AiBlock> blocks = tilePath.getBlocks();
				if(!blocks.isEmpty()) {
					for(int j = 0; j<blocks.size(); j++ ) {
						ai.checkInterruption();
						if(blocks.get(j).isDestructible())
							count++;
					}
				}
			}
			if(count>1) 
				result = false;
		}
		return result;
	}
}
