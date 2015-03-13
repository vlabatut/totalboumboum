package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.criterion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v3.CaliskanGeckalanSeven;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Mustafa Çalışkan
 * @author Gökhan Geçkalan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class Rivals extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "RIVALS";
	
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Rivals(CaliskanGeckalanSeven ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected CaliskanGeckalanSeven ai;
	
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

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		Collection<AiBlock> blocks = zone.getBlocks();
		List<AiTile> heroesTiles = new ArrayList<AiTile>();
		List<AiHero> remainingHeroes = zone.getRemainingOpponents();
		Iterator<AiHero> it = remainingHeroes.iterator();
		while(it.hasNext()) {
			ai.checkInterruption();
			AiHero target = it.next();
			if(!target.equals(ownHero))
				heroesTiles.add(target.getTile());
		}
		if(heroesTiles.size()>0)  {
			if(heroesTiles.contains(tile))
				return true;
			int v = 0;
			int k = ownHero.getBombRange();
			while (v<heroesTiles.size()) {
				ai.checkInterruption();
				AiTile tempTile = heroesTiles.get(v);
				AiTile tempTile2 = tempTile;
				int i = 0;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile2 = tempTile.getNeighbor(Direction.DOWN);
					tempTile = tile2;
					if (!blocks.contains(tile2)) {
						if(!heroesTiles.contains(tile2))
							heroesTiles.add(tile2);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tempTile2;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile2 = tempTile.getNeighbor(Direction.UP);
					tempTile = tile2;
					if (!blocks.contains(tile2)) {
						if(!heroesTiles.contains(tile2))
							heroesTiles.add(tile2);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tempTile2;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile2 = tempTile.getNeighbor(Direction.RIGHT);
					tempTile = tile2;
					if (!blocks.contains(tile2)) {
						if(!heroesTiles.contains(tile2))
							heroesTiles.add(tile2);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tempTile2;
				while (i < k && !blocks.contains(tempTile)) {
					ai.checkInterruption();
					AiTile tile2 = tempTile.getNeighbor(Direction.LEFT);
					tempTile = tile2;
					if (!blocks.contains(tile2)) {
						if(!heroesTiles.contains(tile2))
							heroesTiles.add(tile2);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tempTile2;		
			}
			if(heroesTiles.contains(tile))
				return true;
		}
		
		return result;
	}
}
