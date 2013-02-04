package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
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
public class PertinentCollecteForBonus extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "PertinentCollecteForBonus";
	
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
	public PertinentCollecteForBonus(CaliskanGeckalan ai) throws StopRequestException
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
	int nombreDeBomb = ownHero.getBombNumberCurrent();
	int nombreDeFlamme = ownHero.getBombRange();
	List<AiItem> items = tile.getItems();
	for(int i = 0; i<items.size(); i++) {
		ai.checkInterruption();
		AiItemType itemType = items.get(i).getType();
		if(itemType.equals(AiItemType.EXTRA_BOMB))
			if(nombreDeBomb < 2) {
				result = true;
				break;
			}
		else if(itemType.equals(AiItemType.EXTRA_FLAME))
			if(nombreDeFlamme < 3) {
				result = true;
				break;
			}
	}	
	return result;
	}
}
