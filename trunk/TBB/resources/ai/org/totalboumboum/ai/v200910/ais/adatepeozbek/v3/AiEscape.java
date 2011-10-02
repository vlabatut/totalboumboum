package org.totalboumboum.ai.v200910.ais.adatepeozbek.v3;

import java.util.ArrayList;
import java.util.Collection;
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
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 3
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
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
	private static double NOT_SAFE = 0;
	private static double STUCK = 7.5;
	private static AiHero OWN_HERO;
	private static AiTile CURRENT_TILE;
	private AiPath path;
	
	public AiEscape(AdatepeOzbek ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		actionToDo = new AiAction(AiActionName.NONE);
		ownAi = ai;
		zone = ownAi.getZone();
		
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
		
		OWN_HERO = ai.getOwnHero();
		CURRENT_TILE = OWN_HERO.getTile();

		
		for(AiBomb bomb : bombs)
		{
			ownAi.checkInterruption();
		
			List<AiTile> tilesToExplose = new ArrayList<AiTile>();
			List<AiBomb> inBombs = new ArrayList<AiBomb>();
			getBlast(bomb,tilesToExplose,inBombs);
			double level = SAFE;
			
			for(AiBomb bmb : inBombs)
			{
				ownAi.checkInterruption();
				double inlevel = (bmb.getNormalDuration() - bmb.getTime())/1000;
				if(inlevel < level)
					level = inlevel;
			}
			
			for(AiTile tileToExplose : tilesToExplose)
			{
				ownAi.checkInterruption();
				//if(!bomb.isWorking())
				//		continue;
				if(safeArray[tileToExplose.getLine()][tileToExplose.getCol()] > level)
					safeArray[tileToExplose.getLine()][tileToExplose.getCol()] = level;				
			}
		}
		
		//printTiles();
		
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
			else if(item.getType() == AiItemType.MALUS)
				level = -3;
			else
				level = 1;
			
			if(isTileSafe(itemTile))
				safeArray[item.getLine()][item.getCol()] += level;
		}
		
		for(AiBlock block : blocks)
		{	ownAi.checkInterruption();
			if(block.getState().getName() == AiStateName.BURNING)
			{
				safeArray[block.getLine()][block.getCol()] = NOT_SAFE;
			}
			if(!block.isDestructible())
			{
				safeArray[block.getLine()][block.getCol()] = NOT_SAFE;
				indestBlocks.add(block);
			}
			else
			{
				safeArray[block.getLine()][block.getCol()] = NOT_SAFE;
				destBlocks.add(block);
			}
		}
		
		for(AiFire fire : fires)
		{ownAi.checkInterruption();
			safeArray[fire.getLine()][fire.getCol()] = NOT_SAFE;
		}
		
		
		for(int line=0;line<zone.getHeight();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				if(safeArray[line][col] == SAFE)
					safeArray[line][col] = stuckCheck(zone.getTile(line, col));
			}
		}
		
		updateCostArray();
		
		path = ownAi.getPath();
		
		AiTile nextTile = null;
		
		if(path != null && path.getTiles().size() > 0)
		{
			AiTile tile = path.getTile(0);
			if(CURRENT_TILE == tile)
				path.removeTile(0);
			
			if(path.getTiles().size() > 0)
				nextTile = path.getTile(0);
			else
				path = null;
		}
		
		ownAi.setPath(path);
		
		
		if(nextTile != null)
		{
			if(!checkNextTileIsCrossable(nextTile))
			{
				calculateNewPath();
				return;
			}
			
			/* IMPLEMENTATION N'EST PAS ENCORE FINIE
			 * 
			else if(isTileSafe(CURRENT_TILE) && !checkPathSafety(path))
			{
				path = null;
				ownAi.setPath(path);
				return;
			}*/

			Debug.writeln("END TILE NOT NULL CURRENT: " + CURRENT_TILE.toString() + " NEXT : " + nextTile.toString() + " END : " + path.getLastTile());
			Direction dir = zone.getDirection(CURRENT_TILE, nextTile);
			actionToDo = new AiAction(AiActionName.MOVE, dir);	
			ownAi.setActionToDo(actionToDo);
		}
		
		else if(!isTileSafe(CURRENT_TILE))
		{
			
			Debug.writeln("TILE NOT SAFE CURRENT: " + CURRENT_TILE.toString());
			calculateNewPath();
		}
		
		else
		{
			
			if(canTheyReachMe())
			{	
				boolean amIStuck = stuckAlgorithm();
				
				if(amIStuck)
				{
					Debug.writeln("I AM STUCK !");
					return;
				}
			}
				
			
			Debug.writeln("I AM NOT STUCK !");
				
				if(isThereBonusInTheTable())
				{
					Debug.writeln("THERE ARE BONUS IN AREA");
					
					AiPath pathToBonus = getReachableBonusInArea(10);
					if(pathToBonus != null)
					{
						Debug.writeln("GOING TO BONUS IN AREA !");
						Direction dir = zone.getDirection(CURRENT_TILE, pathToBonus.getTile(0));
						actionToDo = new AiAction(AiActionName.MOVE, dir);	
						ownAi.setActionToDo(actionToDo);
						ownAi.setPath(pathToBonus);
						path = pathToBonus;
						return;
					}
					else
					{
						Debug.writeln("SEARCHING FOR DEST BLOCKS !");
						AiBlock destBlock = getDestBlockInMyRange();
						if(destBlock != null)
						{
							Debug.writeln("BLOCK DEST FOUND : " + destBlock.toString());
							boolean canIEscape = canIEscapeIfIBomb();

							if(!canIEscape)
							{
								Debug.writeln("I CANNOT ESCAPE MOVING TO BLOCK FOUND");
								//goToShortestDestBlock();
								Direction dir = zone.getDirection(CURRENT_TILE, destBlock.getTile());
								actionToDo = new AiAction(AiActionName.MOVE, dir);	
								ownAi.setActionToDo(actionToDo);
							}
							else
							{
								Debug.writeln("ESCAPING");
							}
							return;
						}
						else
						{
							Debug.writeln("BLOCK NOT FOUND IN RANGE MOVING TO SHORTEST");
							goToShortestDestBlock();
						}
					}
				}
				else
					Debug.writeln("Nothing to do");
		}
	}

	public double stuckCheck(AiTile tile) throws StopRequestException
	{
		ownAi.checkInterruption();
		int count = 0;
		
		/*
		List<AiTile> allPassedTiles = ownAi.getPassedTiles();
		
		AiTile lastPassed = null;
		if(allPassedTiles.size() > 0)
			lastPassed = allPassedTiles.get(allPassedTiles.size()-1);
		*/
		
		for(AiTile neighbor : tile.getNeighbors())
		{ownAi.checkInterruption();
			if(isTileSafe(neighbor) && neighbor.isCrossableBy(OWN_HERO))
			{
				count++;
			}
		}
		
		if(count > 1)
			return SAFE;
		else if(count == 1)
		{
			return STUCK;
		}
		else
			return NOT_SAFE;
	}
	
	public boolean stuckAlgorithm() throws StopRequestException
	{
		ownAi.checkInterruption();
		Debug.writeln("Current : " + CURRENT_TILE.toString());
		int count = 0;
		
		/*
		List<AiTile> allPassedTiles = ownAi.getPassedTiles();
		
		AiTile lastPassed = null;
		if(allPassedTiles.size() > 0)
			lastPassed = allPassedTiles.get(allPassedTiles.size()-1);
		*/
		
		for(AiTile neighbor : CURRENT_TILE.getNeighbors())
		{ownAi.checkInterruption();
			if(isTileSafe(neighbor) && neighbor.isCrossableBy(OWN_HERO) ) //&& CURRENT_TILE != lastPassed)
			{
				count++;
			}
		}
		
		if(count > 1)
			return false;
		else if(count == 1)
		{
			return calculateNewPath();
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
		return safeArray[tile.getLine()][tile.getCol()] >= STUCK;
	}
	
	public AiTile getShortestSafeTile() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		double dist = Double.MAX_VALUE;
		AiTile minTile = null;
		
		for(AiTile tile : getSafeTiles())
		{
			ownAi.checkInterruption();

			double tempDist = zone.getPixelDistance(tile.getPosX(), tile.getPosY(), CURRENT_TILE.getPosX(), CURRENT_TILE.getPosY());
			if(dist > tempDist)
			{
				minTile = tile;
				dist = tempDist;
			}
			else if(dist == tempDist)
			{
				if(safeArray[tile.getLine()][tile.getCol()] < safeArray[CURRENT_TILE.getLine()][CURRENT_TILE.getCol()])
				{
					minTile = tile;
					dist = tempDist;
				}
			}
		}
		
		return minTile;
		
	}
	
	public AiTile getLongestSafeTile() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		double dist = 0;
		AiTile maxTile = null;
		
		for(AiTile tile : getSafeTiles())
		{
			ownAi.checkInterruption();

			double tempDist = zone.getPixelDistance(tile.getPosX(), tile.getPosY(), CURRENT_TILE.getPosX(), CURRENT_TILE.getPosY());
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
				if(CURRENT_TILE == TileToCheck)
					continue;
				if(isTileSafe(TileToCheck))
					safeTiles.add(TileToCheck);
			}
		}
		return safeTiles;
	}
	
	public void printTiles() throws StopRequestException
	{
		Debug.writeln("LISTING MATRICE");
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeight();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			Debug.writeln("");
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				Debug.write(safeArray[line][col] + " ");
			}
		}
	}
	
	public void printTiles(double matrice[][]) throws StopRequestException
	{
		Debug.writeln("LISTING MATRICE");
		ownAi.checkInterruption();
		for(int line=0;line<matrice.length;line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			Debug.writeln("");
			for(int col=0;col<matrice[line].length;col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				Debug.write(matrice[line][col] + " ");
			}
		}
	}
	
	public boolean canTheyReachMe() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		if(ownAi.canTheyReachMe == true)
			return true;
		
		for(int i=0; i<heroes.size(); i++)
		{
			ownAi.checkInterruption();
			
			AiHero hero = heroes.get(i);
			if(hero == OWN_HERO)
				continue;
						
			AiTile heroPos = hero.getTile();
			AiPath possiblePath = calculateNewPath(heroPos);
			if(possiblePath != null)
			{
				ownAi.canTheyReachMe = true;
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isThereBonusInTheTable() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		if(destBlocks.size() > 0 || items.size() > 0)
			return true;
		else
			return false;
	}
	
	public boolean canIEscapeIfIBomb() throws StopRequestException
	{
		ownAi.checkInterruption();
		
		if(OWN_HERO.getBombCount() > 1)
			return false;
		double tempArray[][] = new double[zone.getHeight()][zone.getWidth()];
		
		tempArray = safeArray.clone();
		
		int range = OWN_HERO.getBombRange();
		
		AiTile tempTile = CURRENT_TILE;
		
		double bombExplosionTime = 3;
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.LEFT);
			
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
		}
		tempTile = CURRENT_TILE;
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.RIGHT);
			
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
		}
		
		tempTile = CURRENT_TILE;
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.UP);
			
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
		}
		
		tempTile = CURRENT_TILE;
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.DOWN);
			
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
		}
		
		if(safeArray[CURRENT_TILE.getLine()][CURRENT_TILE.getCol()] > bombExplosionTime)
			safeArray[CURRENT_TILE.getLine()][CURRENT_TILE.getCol()] = bombExplosionTime;
				
		// On met la bombe car on sait qu'on peut s'enfuir
		
		printTiles();
		if(calculateNewPath() == true)
		{
			safeArray = tempArray.clone();
			actionToDo = new AiAction(AiActionName.DROP_BOMB);	
			ownAi.setActionToDo(actionToDo);
			return true;
		}
		
		// On remet safeArray a sa place
		safeArray = tempArray.clone();
		return false;
	}
	
	public boolean isPlayerStuck(AiHero hero) throws StopRequestException
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
	
	public AiPath getReachableBonusInArea(int len) throws StopRequestException
	{
		ownAi.checkInterruption();
		AiPath minItemPath = null;
		int distance = Integer.MAX_VALUE;
		for(AiItem item : items)
		{
			ownAi.checkInterruption();
			if(item.getType() == AiItemType.MALUS)
				continue;
			
			AiTile tileToGo = item.getTile();
			AiPath newpath = calculateNewPath(tileToGo);
			if(newpath == null)
				continue; // path unreachable
			int _dist = zone.getTileDistance(CURRENT_TILE.getLine(), CURRENT_TILE.getCol(), item.getLine(), item.getCol());
			if(_dist < distance)
			{
				distance = _dist;
				minItemPath = newpath;
			}
		}
		
		if(distance > len)
			minItemPath = null;
		
		return minItemPath;
	}
	
	public AiBlock getDestBlockInMyRange() throws StopRequestException
	{
		ownAi.checkInterruption();
		int range = OWN_HERO.getBombRange();
		int ct = 0;
		Direction dir = Direction.LEFT;
		while(ct < 4)
		{
			ct++;
			if(ct == 2)
				dir = Direction.RIGHT;
			else if(ct == 3)
				dir = Direction.UP;
			else if(ct == 4)
				dir = Direction.DOWN;
			AiTile tempTile = CURRENT_TILE;
			for(int i=0; i<range; i++)
			{
				tempTile = tempTile.getNeighbor(dir);
				List<AiBlock> n_blocks = tempTile.getBlocks();
				if(n_blocks.size() == 0)
					continue;
				for(int j=0; j< n_blocks.size();)
				{
					AiBlock nBlock = n_blocks.get(j);
					if(nBlock.isDestructible())
						return nBlock;			
					else
						break;
				}
			}
		}
		
		return null;
	}
	
	public boolean goToShortestDestBlock() throws StopRequestException
	{
		ownAi.checkInterruption();
		AiBlock minBlock = null;
		AiPath minPath = null;
		double minDistance = Double.MAX_VALUE;
		for(AiBlock block : destBlocks)
		{
			ownAi.checkInterruption();
			AiTile tileToGo = null;
			for(AiTile neigh : block.getTile().getNeighbors())
			{
				if(isTileSafe(neigh))
				{
					tileToGo = neigh;
					break;
				}
			}
			if(tileToGo == null)
				continue;
			AiPath newpath = calculateNewPath(tileToGo);
			if(newpath == null)
				continue; // path unreachable
			double dist = zone.getPixelDistance(CURRENT_TILE.getPosX(), CURRENT_TILE.getPosY(), block.getPosX(), block.getPosY());
			if( dist < minDistance)
			{
				minPath = newpath;
				dist = minDistance;
				minBlock = block;
			}
		}
		
		if(minBlock == null)		
			return false;
			
		if(minPath != null)
		{
			Debug.writeln("GOING TO SHORTEST BLOCK " + minBlock.getTile().toString());
			Direction dir = zone.getDirection(CURRENT_TILE, minPath.getTile(0));
			actionToDo = new AiAction(AiActionName.MOVE, dir);	
			ownAi.setActionToDo(actionToDo);
			ownAi.setPath(minPath);
			path = minPath;
			return true;
		}
		
		return false;
	}
	
	public AiPath calculateNewPath(AiTile tileToGo) throws StopRequestException
	{
		ownAi.checkInterruption();
		Astar astar = new Astar(ownAi,OWN_HERO, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
		
		AiPath localpath = astar.processShortestPath(CURRENT_TILE, tileToGo);
		for(AiTile tileToPass : localpath.getTiles())
		{
			ownAi.checkInterruption();
			Debug.write(tileToPass.toString()+" ");
		}

		if(localpath.getTiles().size() > 0)
		{
			return localpath;
		}	
		return null;
	}
	
	public boolean checkNextTileIsCrossable(AiTile tile) throws StopRequestException
	{
		ownAi.checkInterruption();
		if(!tile.isCrossableBy(OWN_HERO))
			return false;
		else
			return true;
	}
	
	public boolean checkPathSafety(AiPath path)
	{
		if(path.getTiles().size() > 1)
		{
			AiTile tileToGo = path.getTile(1);
			double distance = zone.getPixelDistance(CURRENT_TILE.getPosX(), CURRENT_TILE.getPosY(), tileToGo.getPosX(), tileToGo.getPosY());
			AiTile tileBetween = path.getTile(0);
			if(distance / OWN_HERO.getWalkingSpeed() < safeArray[tileBetween.getLine()][tileBetween.getCol()] - 1 )
				return false;
		}
		return true;
	}
	
	public boolean calculateNewPath() throws StopRequestException
	{
		ownAi.checkInterruption();
		Astar astar = new Astar(ownAi,OWN_HERO, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
		
		List<AiTile> safeTiles = getSafeTiles();
		AiPath localpath = astar.processShortestPath(CURRENT_TILE, safeTiles);
		for(AiTile tileToPass : localpath.getTiles())
		{
			ownAi.checkInterruption();
			Debug.write(tileToPass.toString()+" ");
		}

		if(localpath.getTiles().size() > 0)
		{
			Debug.writeln(CURRENT_TILE.toString());
			Debug.writeln(localpath.getLastTile().toString());
			Direction dir = zone.getDirection(CURRENT_TILE, localpath.getTile(0));
			actionToDo = new AiAction(AiActionName.MOVE, dir);	
			ownAi.setActionToDo(actionToDo);
			ownAi.setPath(localpath);
			path = localpath;
			return true;
		}	
		return false;
	}
	
	/**
	 * calcule une liste de cases correspondant au souffle indirect de la bombe
	 * passée en paramètre. Le terme "indirect" signifie que la fonction est récursive : 
	 * si une case à portée de souffle contient une bombe, le souffle de cette bombe est rajouté
	 * dans la liste blast, et la bombe est rajoutée dans la liste bombs.
	 * Par Vincent Labatut
	 */
	private List<AiTile> getBlast(AiBomb bomb, List<AiTile> blast, List<AiBomb> bombs) throws StopRequestException
	{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
	
		if(!bombs.contains(bomb))
		{	bombs.add(bomb);
		
			// on récupére le souffle
			List<AiTile> tempBlast = bomb.getBlast();
			blast.addAll(tempBlast);
			
			// bombs
			for(AiTile tile: tempBlast)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				
				Collection<AiBomb> bList = tile.getBombs();
				if(bList.size()>0)
				{	AiBomb b = bList.iterator().next();
					getBlast(b,blast,bombs);
				}
			}
		}
		
		return blast;
	}	
}
