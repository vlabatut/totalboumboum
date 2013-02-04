package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
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
public class DistanceTemps extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCETEMPS";
	
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
	public DistanceTemps(CaliskanGeckalan ai) throws StopRequestException
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
	{	ai.checkInterruption();
	boolean result = false;
	double far = zone.getTileDistance(new AiLocation(tile), new AiLocation(ai.getZone().getOwnHero().getTile()));
	if(far<4)
		result = true;
	
	return result;
	}
}
