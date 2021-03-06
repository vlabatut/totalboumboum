package org.totalboumboum.ai.v200910.ais.danesatir.v5c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * Class for Bonus Collect
 * 
 * @version 5.c
 * 
 * @author Levent Dane
 * @author Tolga Can Şatır
 *
 */
@SuppressWarnings("deprecation")
public class BonusCollector {
	
	/** */
	private DaneSatir ai;
	/** */
	private AiItem nearestItem;
	
	/**
	 * default constructor we need our instance for handling checkInterruption
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public BonusCollector(DaneSatir ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai=ai;
	}
	
	/**
	 * Return raw item list
	 * @return AiItem list
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public List<AiItem> getItems() throws StopRequestException {
		ai.checkInterruption();
		return ai.getPercepts().getItems();
	}
	
	/**
	 * Turn Item list to AiTile from AiItem
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	/**
	 * Find nearest item for hero with Astar (target tiles from getItemTiles)
	 * @param hero
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 * @throws NoWayException
	 * 		Description manquante !
	 */
	public void findNearestItem(AiHero hero) throws StopRequestException, NoWayException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		if(getItems().isEmpty())
			return;
		PathFinder p = this.ai.getPathFinder();
		try {
			//p.clearPath();
			p.calculPath(getItemsTiles());
			AiPath path = p.getPath();
			//GeneralFuncs.printLog(ai, path+"\n"+path.getLastTile(), VerboseLevel.MED);
			List<AiItem> items = path.getLastTile().getItems();
			if(items.size()==1)
				this.setNearestItem(items.get(0));
		} catch (NoWayException e) {
			//GeneralFuncs.printLog(ai, "Yol Yok", VerboseLevel.LOW);
			throw new NoWayException();
		}
		return;
	}
	/**
	 * Find Nearest Item for our hero
	 * @throws StopRequestException
	 * 		Description manquante !
	 * @throws NoWayException
	 * 		Description manquante !
	 */
	public void findNearestItem() throws StopRequestException, NoWayException {
		ai.checkInterruption();
		findNearestItem(ai.getOwnHero());
	}
	
	/**
	 * Find a wall to explode
	 * @return wall to explode
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile findWallToExplode() throws StopRequestException {
		ai.checkInterruption();
		List<AiTile> targets=getWallsTiles();
		AiHero hero=ai.getOwnHero();
		Astar astar = new Astar(ai,hero,
			new MyCost(new TimeMatrice(this.ai),ai), 
			new BasicHeuristicCalculator(),
			new WallSuccessor(ai)
			);
		AiPath path = astar.processShortestPath(hero.getTile(), targets);
		if(path.isEmpty())
			return null;
		/*GeneralFuncs.printLog(ai, "path->"+path, VerboseLevel.MED);
		GeneralFuncs.printLog(ai, "target->"+path.getTile(path.getLength()-2), VerboseLevel.MED);*/
		
		return path.getTile(path.getLength()-2);
	}
	
	/**
	 * 
	 * @param nearestItem
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setNearestItem(AiItem nearestItem) throws StopRequestException {
		ai.checkInterruption();
		this.nearestItem = nearestItem;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiItem getNearestItem() throws StopRequestException {
		ai.checkInterruption();
		return nearestItem;
	}
	
	/**
	 *  Find all destructible and not burning walls.
	 * @return list of walls' tiles
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiTile> getWallsTiles() throws StopRequestException {
		ai.checkInterruption();
		List<AiBlock> blocks=ai.getPercepts().getBlocks();
		ArrayList<AiTile> targets= new ArrayList<AiTile>();
		for(AiBlock i : blocks) {
			ai.checkInterruption();
			if(i.isDestructible() && i.getState().getName()!=AiStateName.BURNING && !i.hasEnded())
				targets.add(i.getTile());
		}
		return targets;
	}
}
