package tournament200910.danesatir.v5;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiItem;
import fr.free.totalboumboum.ai.adapter200910.data.AiStateName;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
/**
 * Class for Bonus Collect
 * @author limon
 *
 */
public class BonusCollector {
	
	private DaneSatir ai;
	private AiItem nearestItem;
	
	/**
	 * default constructor we need our instance for handling checkInterruption
	 * @param ai
	 */
	public BonusCollector(DaneSatir ai) {
		this.ai=ai;
	}
	
	/**
	 * Return raw item list
	 * @return AiItem list
	 */
	public List<AiItem> getItems() {
		return ai.getPercepts().getItems();
	}
	
	/**
	 * Turn Item list to AiTile from AiItem
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> getItemsTiles() throws StopRequestException {
		// avant tout : test d'interruption
		ai.checkInterruption();
		ArrayList<AiTile> result=new ArrayList<AiTile>();
		for(AiItem i: getItems()) {
			ai.checkInterruption();
			result.add(i.getTile());
		}
		return result;
	}
	/**
	 * Find nearest item for hero with Astar (target tiles from getItemTiles)
	 * @param hero
	 * @throws StopRequestException
	 * @throws NoWayException
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
	 * @throws NoWayException
	 */
	public void findNearestItem() throws StopRequestException, NoWayException {
		findNearestItem(ai.getOwnHero());
	}
	
	/**
	 * Find a wall to explode
	 * @return wall to explode
	 * @throws StopRequestException
	 */
	public AiTile findWallToExplode() throws StopRequestException {
		List<AiTile> targets=getWallsTiles();
		AiHero hero=ai.getOwnHero();
		Astar astar = new Astar(ai,hero,
			new MyCost(new TimeMatrice(this.ai)), 
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
	public void setNearestItem(AiItem nearestItem) {
		this.nearestItem = nearestItem;
	}

	public AiItem getNearestItem() {
		return nearestItem;
	}
	
	/**
	 *  Find all destructible and not burning walls.
	 * @return list of walls' tiles
	 * @throws StopRequestException
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
