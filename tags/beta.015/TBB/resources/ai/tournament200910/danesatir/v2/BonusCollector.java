package tournament200910.danesatir.v2;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiItem;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;

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
		ArrayList<AiTile> list = new ArrayList<AiTile>();
		for(AiItem i: items) {
			list.add(i.getTile());
		}
		Astar a = new Astar(ai,ai.getOwnHero(), new SafeCostCalculator(this.ai,this.ai.getTimeMatrice()), new BasicHeuristicCalculator());
		AiPath path = a.processShortestPath(source, list);
		path.removeTile(source);
		return path.getTile(0);
	}
}
