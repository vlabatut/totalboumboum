package org.totalboumboum.ai.v200910.ais.adatepeozbek.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 2
 * 
 * @author Can Adatape
 * @author Sena Ozbek
 *
 */
public class AiEscape
{		
	private AdatepeOzbek ownAi;
	private AiZone zone;
	private List<AiBomb> bombs;
	private List<AiFire> fires;
	private List<AiHero> heroes;
	private List<AiItem> items;
	private List<AiBlock> blocks;
	private AiAction actionToDo = null;
	private double safeArray[][];
	private List<AiBlock> indestBlocks;
	private List<AiBlock> destBlocks;
	private double costArray[][];
	private static double SAFE = 10;
	private static double NOTSAFE = 0;
	@SuppressWarnings("unused")
	private static double INDESTRUCTIBLE = 0;
	@SuppressWarnings("unused")
	private static double DESTRUCTIBLE = 1;
	private static AiHero ownHero;
	private static AiTile currentTile;
	private AiPath path;
	
	public AiEscape(AdatepeOzbek ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		actionToDo = new AiAction(AiActionName.NONE);
		ownAi = ai;
		zone = ownAi.GetZone();
		
		safeArray = new double[zone.getHeight()][zone.getWidth()];
		destBlocks = new ArrayList<AiBlock>();
		indestBlocks = new ArrayList<AiBlock>();
		costArray = new double[zone.getHeight()][zone.getWidth()];
		initArrays();
		
		bombs = zone.getBombs();
		fires = zone.getFires();
		heroes = zone.getHeroes();
		items = zone.getItems();
		blocks = zone.getBlocks();
		
		ownHero = ai.GetOwnHero();
		currentTile = ownHero.getTile();

		for(AiBomb bomb : bombs)
		{ownAi.checkInterruption();
			List<AiTile> tilesToExplose = bomb.getBlast();
			for(AiTile tileToExplose : tilesToExplose)
			{ownAi.checkInterruption();
				if(!bomb.isWorking())
						continue;
				double level = (bomb.getNormalDuration() - bomb.getTime())/1000;
				if(safeArray[tileToExplose.getLine()][tileToExplose.getCol()] > level)
					safeArray[tileToExplose.getLine()][tileToExplose.getCol()] = level;				
			}
		}

		for(AiItem item : items)
		{ownAi.checkInterruption();
			AiTile itemTile = zone.getTile(item.getLine(), item.getCol());
			if(!isTileSafe(itemTile))
				continue;
			
			double level;
			if(item.getType() == AiItemType.EXTRA_FLAME)
				level = 3;
			else if(item.getType() == AiItemType.EXTRA_BOMB)
				level = 2;	
			else
				level = 1;
			
			if(isTileSafe(itemTile))
				safeArray[item.getLine()][item.getCol()] += level;
		}
		
		for(AiBlock block : blocks)
		{	ownAi.checkInterruption();
			if(!block.isDestructible())
				indestBlocks.add(block);
			else
				destBlocks.add(block);
		}
		
		for(AiFire fire : fires)
		{ownAi.checkInterruption();
			safeArray[fire.getLine()][fire.getCol()] = NOTSAFE;
		}
		
		updateCostArray();
		
		boolean found = false;
		path = ownAi.GetPath();
		
		AiTile nextTile = null;
		
		if(path != null && path.getTiles().size() > 0)
		{
			AiTile tile = path.getTile(0);
			if(currentTile == tile)
				path.removeTile(0);
			
			if(path.getTiles().size() > 0)
				nextTile = path.getTile(0);
			else
				path = null;
		}
		
		ownAi.SetPath(path);
		
		
		if(nextTile != null)
		{
			Debug.Writeln("END TILE NOT NULL CURRENT: " + currentTile.toString() + " NEXT : " + nextTile.toString() + " END : " + path.getLastTile());
			Direction dir = zone.getDirection(currentTile, nextTile);
			actionToDo = new AiAction(AiActionName.MOVE, dir);	
			ownAi.SetActionToDo(actionToDo);
		}
		
		else if(!isTileSafe(currentTile))
		{
			
			Debug.Writeln("TILE NOT SAFE CURRENT: " + currentTile.toString());
			List<AiTile> neighbors = currentTile.getNeighbors();
				
			for(AiTile neighbor : neighbors)
			{ownAi.checkInterruption();
				if(isTileSafe(neighbor) && neighbor.isCrossableBy(ownHero))
				{
					Direction dir = zone.getDirection(currentTile, neighbor);
					actionToDo = new AiAction(AiActionName.MOVE, dir);	
					found = true;
					ownAi.SetActionToDo(actionToDo);
					break;
				}
			}
			
			if(neighbors.size() == 0 || !found)
			{
				Astar astar = new Astar(ownAi,ownHero, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
				//AiTile endTile = GetShortestSafeTile();
				List<AiTile> safeTiles = getSafeTiles();
				AiPath localpath = astar.processShortestPath(currentTile, safeTiles);
					
				if(localpath.getTiles().size() > 0)
				{
					Direction dir = zone.getDirection(currentTile, localpath.getTile(0));
					actionToDo = new AiAction(AiActionName.MOVE, dir);	
					ownAi.SetActionToDo(actionToDo);
					ownAi.SetPath(localpath);
					path = localpath;
				}
			}
		}
		
		else
		{
			if(canTheyReachMe())
			{
				boolean amIStuck = StuckAlgorithm();
				if(amIStuck)
				{
					Debug.Writeln("I AM STUCK !");
				}
				else
					Debug.Writeln("I AM NOT STUCK !");
				
			}
		}
	}
	
	public boolean StuckAlgorithm() throws StopRequestException
	{
		Debug.Writeln("Current : " + currentTile.toString());
		int count = 0;
		List<AiTile> allPassedTiles = ownAi.GetPassedTiles();
		
		AiTile lastPassed = null;
		if(allPassedTiles.size() > 0)
			lastPassed = allPassedTiles.get(allPassedTiles.size()-1);
		
		for(AiTile neighbor : currentTile.getNeighbors())
		{ownAi.checkInterruption();
			if(isTileSafe(neighbor) && neighbor.isCrossableBy(ownHero) && currentTile != lastPassed)
			{
				count++;
			}
		}
		
		if(count > 1)
			return false;
		else if(count == 1)
		{
			Astar astar = new Astar(ownAi,ownHero, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
			
			List<AiTile> safeTiles = getSafeTiles();
			//AiTile tileToGo = GetShortestSafeTile();
			AiPath localpath = astar.processShortestPath(currentTile, safeTiles);
			Debug.Writeln("Current : " + currentTile.toString() + " End: " + localpath.getLastTile().toString());
			Debug.Writeln("Calculating stuck path...");
			Debug.Write("Path: ");
			for(AiTile tileToPass : localpath.getTiles())
			{
				ownAi.checkInterruption();
				Debug.Write(tileToPass.toString()+" ");
			}
			Debug.Writeln("");
			if(localpath.getTiles().size() > 0)
			{
				Debug.Writeln(currentTile.toString());
				Debug.Writeln(localpath.getLastTile().toString());
				Direction dir = zone.getDirection(currentTile, localpath.getTile(0));
				actionToDo = new AiAction(AiActionName.MOVE, dir);	
				ownAi.SetActionToDo(actionToDo);
				ownAi.SetPath(localpath);
				path = localpath;
				return true;
			}
			
			return false;
		}
		else
			return true;
	}
	
	public void initArrays() throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeight();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				safeArray[line][col] = SAFE;
			}
		}
	}
	
	public void updateCostArray() throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeight();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				costArray[line][col] = -safeArray[line][col];
			}
		}
	}
	
	public boolean isTileSafe(AiTile tile) throws StopRequestException
	{
		ownAi.checkInterruption();
		return safeArray[tile.getLine()][tile.getCol()] >= SAFE;
	}
	
	
	public AiTile GetShortestSafeTile() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		double dist = Double.MAX_VALUE;
		AiTile minTile = null;
		
		for(AiTile tile : getSafeTiles())
		{
			ownAi.checkInterruption();
			if(currentTile == tile)
				continue;
			double tempDist = zone.getPixelDistance(tile.getPosX(), tile.getPosY(), currentTile.getPosX(), currentTile.getPosY());
			if(dist > tempDist)
			{
				minTile = tile;
				dist = tempDist;
			}
			else if(dist == tempDist)
			{
				if(safeArray[tile.getLine()][tile.getCol()] < safeArray[currentTile.getLine()][currentTile.getCol()])
				{
					minTile = tile;
					dist = tempDist;
				}
			}
		}
		
		return minTile;
		
	}
	
	public AiTile GetLongestSafeTile() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		double dist = 0;
		AiTile maxTile = null;
		
		for(AiTile tile : getSafeTiles())
		{
			ownAi.checkInterruption();
			if(currentTile == tile)
				continue;
			double tempDist = zone.getPixelDistance(tile.getPosX(), tile.getPosY(), currentTile.getPosX(), currentTile.getPosY());
			if(dist < tempDist)
			{
				maxTile = tile;
				dist = tempDist;
			}
		}
		
		return maxTile;
	}
	
	public List<AiTile> getSafeTiles() throws StopRequestException
	{
		ownAi.checkInterruption();
		List<AiTile> safeTiles = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeight();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile TileToCheck = zone.getTile(line, col);
				if(isTileSafe(TileToCheck))
					safeTiles.add(TileToCheck);
			}
		}
		return safeTiles;
	}
	
	public void printTiles() throws StopRequestException
	{
		Debug.Writeln("LISTING MATRICE");
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeight();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			Debug.Writeln("");
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				Debug.Write(safeArray[line][col] + " ");
			}
		}
	}
	
	public boolean canTheyReachMe() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		Astar astar = new Astar(ownAi,ownHero, new BasicCostCalculator(),new BasicHeuristicCalculator());
		for(int i=0; i<heroes.size(); i++)
		{
			ownAi.checkInterruption();			
			AiTile heroPos = heroes.get(i).getTile();
			if(astar.processShortestPath(currentTile, heroPos) != null)
				return true;
		}
		
		return false;
	}
	
	public boolean IsThereBonusInTheTable() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		if(destBlocks.size() > 0 || items.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean IsPlayerStuck(AiHero hero) throws StopRequestException
	{
		ownAi.checkInterruption();
		AiTile heroPos = hero.getTile();
		
		int ct = 0;
		for(AiTile neighbor : heroPos.getNeighbors())
		{ownAi.checkInterruption();
			if(isTileSafe(neighbor) && neighbor.isCrossableBy(hero))
			{
				ct++;
			}
		}
		if(ct > 1)
			return false;
		else
			return true;
	}
	
	public AiItem GetBonusInArea(int len) throws StopRequestException
	{
		for(AiItem item : items)
		{ownAi.checkInterruption();
			if(zone.getTileDistance(currentTile.getLine(), currentTile.getCol(), item.getLine(), item.getCol()) <= len)
				return item;
		}
		
		return null;
	}
	
	public AiBlock GetDestBlockInMyRange() throws StopRequestException
	{
		int range = ownHero.getBombRange();
		for(AiBlock block : destBlocks)
		{ownAi.checkInterruption();
			int line = block.getLine();
			int col = block.getCol();
			if( line == currentTile.getLine())
			{
				if(col > currentTile.getCol() && col <= currentTile.getCol() + range)
					return block;
				else if(col < currentTile.getCol() && col >= currentTile.getCol() - range)
					return block;
			}
			else if( col == currentTile.getCol())
			{
				if(line > currentTile.getLine() && line <= currentTile.getLine() + range)
					return block;
				else if(line < currentTile.getLine() && line >= currentTile.getLine() - range)
					return block;
			}
		}
		
		return null;
	}
	
	public boolean GoToShortestDestBlock() throws StopRequestException
	{
		AiBlock minBlock = null;
		double minDistance = Double.MAX_VALUE;
		for(AiBlock block : destBlocks)
		{ownAi.checkInterruption();
			double dist = zone.getPixelDistance(currentTile.getPosX(), currentTile.getPosY(), block.getPosX(), block.getPosY());
			if( dist <= minDistance)
			{
				dist = minDistance;
				minBlock = block;
			}
		}
		
		if(minBlock == null)		
			return false;
		else if(!isTileSafe(minBlock.getTile()))
			return false;
		
		Astar astar = new Astar(ownAi,ownHero, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
		
		AiPath localpath = astar.processShortestPath(currentTile, minBlock.getTile());
			
		if(localpath.getTiles().size() > 0)
		{
			Direction dir = zone.getDirection(currentTile, localpath.getTile(0));
			actionToDo = new AiAction(AiActionName.MOVE, dir);	
			ownAi.SetActionToDo(actionToDo);
			ownAi.SetPath(localpath);
			path = localpath;
			return true;
		}
		
		return false;
	}
}
