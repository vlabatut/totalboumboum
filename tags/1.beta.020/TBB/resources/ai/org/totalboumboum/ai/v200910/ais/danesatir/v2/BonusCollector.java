package org.totalboumboum.ai.v200910.ais.danesatir.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * 
 * @version 2
 * 
 * @author Levent Dane
 * @author Tolga Can Satir
 *
 */
public class BonusCollector {
	private List<AiItem> items;
	private DaneSatir ai;
	
	public BonusCollector(DaneSatir ai) {
		this.ai=ai;
		this.items=this.ai.getPercepts().getItems();
	}
	
	public AiTile findNearBonus(AiTile source) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		//if(items.size());
		List<AiTile> list = new ArrayList<AiTile>();
		for(AiItem i: items) {
			list.add(i.getTile());
		}
		Astar a = new Astar(ai,ai.getOwnHero(), new SafeCostCalculator(this.ai,this.ai.getTimeMatrice()), new BasicHeuristicCalculator());
		AiPath path = a.processShortestPath(source, list);
		path.removeTile(source);
		return path.getTile(0);
	}
}
