package org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.ai.v201213.adapter.path.search.Astar;
import org.totalboumboum.ai.v201213.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201213.ais.caliskangeckalan.v2.CaliskanGeckalan;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 */
public class ConjonctionMurs extends AiUtilityCriterionBoolean<CaliskanGeckalan>
{	/** Nom de ce critère */
	public static final String NAME = "ConjonctionMurs";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	protected Astar astarPrecise = null;
	/** */
	protected Astar astarApproximation = null;
	/** */
	protected Dijkstra dijkstra = null;
	/** */
	protected AiPath indirectPath = null;
	/** */
	protected AiTile safeDestination = null;
	/** */
	protected boolean secondaryBombing = false;
	
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	public ConjonctionMurs(CaliskanGeckalan ai) throws StopRequestException
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
		int bombRange = ownHero.getBombRange();
		int count = 0;
		int col = tile.getCol();
		int row = tile.getRow();
		for(int i = 0; i< bombRange; i++) {
			ai.checkInterruption();
			if(zone.getHeight()>row+i) {
				AiTile nextTile = zone.getTile(row+i, col);
				if(nextTile.getBlocks().size()>0 && nextTile.getBlocks().get(0).isDestructible())
				{
					count++;
					break;
				}
			}
		}
		for(int i = 0; i< bombRange; i++) {
			ai.checkInterruption();
			if(zone.getWidth()>col+i) {
				AiTile nextTile = zone.getTile(row, col+i);
				if(nextTile.getBlocks().size()>0 && nextTile.getBlocks().get(0).isDestructible())
				{
					count++;
					break;
				}
			}
		}
		for(int i = 0; i< bombRange; i++) {
			ai.checkInterruption();
			if(row-i>=0) {
				AiTile nextTile = zone.getTile(row-i, col);
				if(nextTile.getBlocks().size()>0 && nextTile.getBlocks().get(0).isDestructible())
				{
					count++;
					break;
				}
			}
		}
		for(int i = 0; i< bombRange; i++) {
			ai.checkInterruption();
			if(col-i>=0) {
				AiTile nextTile = zone.getTile(row, col-i);
				if(nextTile.getBlocks().size()>0 && nextTile.getBlocks().get(0).isDestructible())
				{
					count++;
					break;
				}
			}
		}
		if(count>1)
			result = true;
		return result;
	}
}
