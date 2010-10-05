package org.totalboumboum.ai.v200910.ais.danesatir.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * 
 * @version 3
 * 
 * @author Levent Dane
 * @author Tolga Can Satir
 *
 */
public class BonusCollector {
	
	private DaneSatir ai;
	private AiItem nearestItem;
	
	public BonusCollector(DaneSatir ai) {
		this.ai=ai;
	}
	
	public List<AiItem> getItems() {
		return ai.getPercepts().getItems();
	}
	
	public List<AiTile> getItemsTiles() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		List<AiTile> result=new ArrayList<AiTile>();
		for(AiItem i: getItems()) {
			ai.checkInterruption();
			result.add(i.getTile());
		}
		return result;
	}
	
	public void findNearestItem(AiHero hero) throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		if(getItems().isEmpty())
			return;
		PathFinder p = this.ai.getPathFinder();
		try {
			p.calculPath(getItemsTiles());
			AiPath path = p.getPath();
			GeneralFuncs.printLog(ai, path+"\n"+path.getLastTile(), VerboseLevel.MED);
			List<AiItem> items = path.getLastTile().getItems();
			if(items.size()==1)
				this.setNearestItem(items.get(0));
		} catch (NoWayException e) {
			GeneralFuncs.printLog(ai, "Yol Yok", VerboseLevel.LOW);
		}
		return;
	}
	
	public void findNearestItem() throws StopRequestException {
		findNearestItem(ai.getOwnHero());
	}
	/*public List<AiItem> getItemsWhichHaveWay(AiHero hero) {
		
	}*/

	public AiTile findWallToExplode() throws StopRequestException {
		List<AiTile> targets=getWallsTiles();
		AiHero hero=ai.getOwnHero();
		Astar astar = new Astar(ai,hero,
			new MyCost(), 
			new BasicHeuristicCalculator(),
			new WallSuccessor(ai)
			);
		AiPath path = astar.processShortestPath(hero.getTile(), targets);
		GeneralFuncs.printLog(ai, "path->"+path, VerboseLevel.MED);
		GeneralFuncs.printLog(ai, "target->"+path.getTile(path.getLength()-2), VerboseLevel.MED);
		return path.getTile(path.getLength()-2);
	}
	public void setNearestItem(AiItem nearestItem) {
		this.nearestItem = nearestItem;
	}

	public AiItem getNearestItem() {
		return nearestItem;
	}
	private List<AiTile> getWallsTiles() throws StopRequestException {
		ai.checkInterruption();
		List<AiBlock> blocks=ai.getPercepts().getBlocks();
		List<AiTile> targets= new ArrayList<AiTile>();
		for(AiBlock i : blocks) {
			ai.checkInterruption();
			if(i.isDestructible())
				targets.add(i.getTile());
		}
		return targets;
	}
}
