package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5c;

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
 * @version 5.c
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
		safeArray = new double[zone.getHeigh()][zone.getWidth()];
		destBlocks = new ArrayList<AiBlock>();
		indestBlocks = new ArrayList<AiBlock>();
		costArray = new double[zone.getHeigh()][zone.getWidth()];
		initArrays();
		
		bombs = zone.getBombs();
		fires = zone.getFires();
		heroes = zone.getRemainingHeroes();
		items = zone.getItems();
		blocks = zone.getBlocks();
		
		OWN_HERO = ai.getOwnHero();
		CURRENT_TILE = OWN_HERO.getTile();
		
		if(ownAi.lastBombedTile != null)
		{
			ownAi.lastBombedTile = zone.getTile(ownAi.lastBombedTile.getLine(), ownAi.lastBombedTile.getCol());
			if(ownAi.lastBombedTile.getBombs().size() == 0)
				ownAi.lastBombedTile = null;
		}
		
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
		
		
		for(int line=0;line<zone.getHeigh();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				if(safeArray[line][col] == SAFE)
					safeArray[line][col] = stuckCheck(zone.getTile(line, col));
			}
		}
		
		updateCostArray();
		evaluateOpposites();

		AiPath pathBackup = ownAi.getPath();
		
		if(ownAi.urgentBombs <= 0 && isHeroInRange())
		{
			Debug.writeln("URGENT BOMB ADDED",ownAi);
			ownAi.urgentBombs = 1;
			
		}
		
		if(ownAi.urgentBombs > 0 && isHeroInRange())
		{
			Debug.writeln("URGENT BOMBS " + ownAi.urgentBombs,ownAi);
			Debug.writeln(CURRENT_TILE.toString(),ownAi);
			if(CURRENT_TILE.getBombs().size() == 0 && (ownAi.lastBombedTile == null || !CURRENT_TILE.getNeighbors().contains(ownAi.lastBombedTile)))
			{
				Debug.writeln("Can I escape",ownAi);
				boolean escape = canIEscapeIfIBomb();
				
				Debug.writeln("URGENT BOMB " + escape + " URGENT COUNT " + ownAi.urgentBombs + " CURRENT TILE " + CURRENT_TILE.toString(),ownAi);
				if(escape)
				{
					Debug.writeln("I can",ownAi);
					ownAi.urgentBombs--;
					return;
				}
				else if(isTileSafe(CURRENT_TILE) && ((path != null && path.getLength() == 1) || path == null))
				{
					Debug.writeln("I cant",ownAi);
					AiPath pth = findPathToGo();
					if(pth != null)
					{
						path = pth;
						ownAi.setPath(pth);
						return;
					}
				}
			}
		}
		
		ownAi.setPath(pathBackup);
		path = pathBackup;
		updatePath();
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
			
			
			else if(isTileSafe(CURRENT_TILE) && !isTileSafe(nextTile))
			{
				Debug.writeln("NEXT TILE IS NOT SAFE ",ownAi);
				return;
			}		

			Debug.writeln("END TILE NOT NULL CURRENT: " + CURRENT_TILE.toString() + " NEXT : " + nextTile.toString() + " END : " + path.getLastTile(),ownAi);
			Direction dir = zone.getDirection(CURRENT_TILE, nextTile);
			actionToDo = new AiAction(AiActionName.MOVE, dir);	
			ownAi.setActionToDo(actionToDo);
			
			if(path != null && !isTileSafe(path.getLastTile()))
			{
				calculateNewPath();
				return;
			}
		}		
		else
		{
			
			if(canTheyReachMe())
			{	
				boolean easy = true;
				for(int i=0; i<heroes.size(); i++)
				{ownAi.checkInterruption();
					if(heroes.get(i) == OWN_HERO)
						continue;
					if(getHeroType(heroes.get(i)) != EnemyTypes.FOLLOW && getHeroType(heroes.get(i)) != EnemyTypes.EASY)
					{
						easy = false;
						break;
					}
				}
				AiHero enemyChosen = null;
				if(easy)
				{
					enemyChosen = getHeroByType(EnemyTypes.FOLLOW);
					
					ownAi.privateRang = 1;
					if(enemyChosen == null)
						enemyChosen = getHeroByType(EnemyTypes.EASY);
				}	
				if(enemyChosen == null)
				{
					enemyChosen = getHeroByType(EnemyTypes.MEDIUM);
					ownAi.privateRang = 5;
				}
				if(enemyChosen == null)
				{
					ownAi.privateRang = 0;
					enemyChosen = getHeroByType(EnemyTypes.HARD);
					if(enemyChosen != null && !enoughArmed())
						enemyChosen = null;
				}
				if(enemyChosen != null)
				{
					AiPath pth = calculateNewPath(enemyChosen.getTile());
						
					if(pth != null)
					{
						Debug.writeln("GOING TO ENEMY TILE " + enemyChosen.getTile().toString(),ownAi);
						
						int dist = zone.getTileDistance(OWN_HERO.getTile(), enemyChosen.getTile());
						
						Debug.writeln("Tile dist: "+ dist,ownAi);
						if(dist <= 2)
						{
							ownAi.urgentBombs = 1;
							return;
						}
						if(pth.getLength() > 4)
						{
							for(int i=pth.getLength()-1; i>3; i--)
							{ownAi.checkInterruption();
								pth.removeTile(i);
							}
						}
						ownAi.setPath(pth);
						path = pth;
						return;
					}
					
					ownAi.privateRang = 0;
					return;
				}					

				boolean amIStuck = stuckAlgorithm();
				
				if(amIStuck)
				{
					Debug.writeln("I AM STUCK !",ownAi);
					return;
				}
			}
				
			
			Debug.writeln("I AM NOT STUCK !",ownAi);
			
			Debug.writeln("Range: " + OWN_HERO.getBombRange() + " Bomb: " + OWN_HERO.getBombNumber(),ownAi);
			
				if(isThereBonusInTheTable())
				{
					Debug.writeln("THERE ARE BONUS IN AREA",ownAi);
					if(!enoughArmed())
					{
						AiPath pathToBonus = getReachableBonusInArea(10);
						if(pathToBonus != null)
						{
							Debug.writeln("GOING TO BONUS IN AREA !",ownAi);
							ownAi.setPath(pathToBonus);
							path = pathToBonus;
							return;
						}
					}
					Debug.writeln("SEARCHING FOR DEST BLOCKS !",ownAi);
					AiBlock destBlock = getDestBlockInMyRange();
					if(destBlock != null)
					{
						Debug.writeln("BLOCK DEST FOUND : " + destBlock.toString(),ownAi);
						boolean canIEscape = canIEscapeIfIBomb();

						if(!canIEscape)
						{
							Debug.writeln("I CANNOT ESCAPE MOVING TO BLOCK FOUND",ownAi);
							
							AiPath pathToGo = calculateNewPath(destBlock.getTile());
							
							if(destBlock.getTile().getNeighbors().contains(CURRENT_TILE))
							{
								ownAi.triedTiles.visited.add(CURRENT_TILE);
								goToShortestDestBlock();
								return;
							}
							
							if(pathToGo != null)
								Debug.writeln("Moving to " + pathToGo.toString(),ownAi);
							
							ownAi.setPath(pathToGo);
							path = pathToGo;
							
						}
						else
						{
							Debug.writeln("ESCAPING",ownAi);
						}
						return;
					}
					else
					{
						Debug.writeln("BLOCK NOT FOUND IN RANGE MOVING TO SHORTEST",ownAi);
						goToShortestDestBlock();
					}
				}
				else
				{
					
					AiHero hr = getCloserHero();
					AiPath pt = calculateNewPath(hr.getTile());
					ownAi.setPath(pt);
					
					return;	
				}
		}
	}

	public boolean isHeroInRange(AiHero hero) throws StopRequestException
	{ownAi.checkInterruption();
	
		int range = OWN_HERO.getBombRange();
		int ct = 0;
		Direction dir = Direction.LEFT;
		if(CURRENT_TILE.getHeroes().contains(hero))
			return true;
		while(ct < 4)
		{	ownAi.checkInterruption();
			ct++;
			if(ct == 2)
				dir = Direction.RIGHT;
			else if(ct == 3)
				dir = Direction.UP;
			else if(ct == 4)
				dir = Direction.DOWN;
			AiTile tempTile = CURRENT_TILE;
			for(int i=0; i<range-1; i++)
			{ownAi.checkInterruption();
				tempTile = tempTile.getNeighbor(dir);
				if(tempTile.getHeroes().contains(hero))
					return true;
				else if(tempTile.getBlocks().size() > 0 || tempTile.getItems().size() > 0)
					break;
			}
		}
		
		return false;
	}
	
	public boolean isHeroInRange() throws StopRequestException
	{ownAi.checkInterruption();
		int range = OWN_HERO.getBombRange();
		if(ownAi.privateRang > 0)
			range = Math.min(ownAi.privateRang, range);
		int ct = 0;
		Direction dir = Direction.LEFT;
		if(CURRENT_TILE.getHeroes().size() > 1 || ( CURRENT_TILE.getHeroes().size() == 1 && !CURRENT_TILE.getHeroes().contains(OWN_HERO)))
			return true;
		
		while(ct < 4)
		{	ownAi.checkInterruption();
			ct++;
			if(ct == 2)
				dir = Direction.RIGHT;
			else if(ct == 3)
				dir = Direction.UP;
			else if(ct == 4)
				dir = Direction.DOWN;
			AiTile tempTile = CURRENT_TILE;
			for(int i=0; i<range-1; i++)
			{ownAi.checkInterruption();
				tempTile = tempTile.getNeighbor(dir);
				if(tempTile.getHeroes().size()>0)
					return true;
				else if(tempTile.getBlocks().size() > 0 || tempTile.getItems().size() > 0)
					break;
			}
		}
		
		return false;
	}
	
	public boolean enoughArmed() throws StopRequestException
	{ownAi.checkInterruption();
		if(OWN_HERO.getBombNumber() >= 5 && OWN_HERO.getBombRange() >= 5)
			return true;
		else
			return false;		
	}
	
	public AiHero getHeroByType(EnemyTypes type) throws StopRequestException
	{ownAi.checkInterruption();
		for(int j=0; j<ownAi.enemies.size(); j++)
		{ownAi.checkInterruption();
			Enemy enm = ownAi.enemies.get(j);
			if(enm.getType() == type)
			{
				return enm.getHero();
			}
		}
		return null;
	}
	
	public EnemyTypes getHeroType(AiHero hero) throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int j=0; j<ownAi.enemies.size(); j++)
		{ownAi.checkInterruption();
			Enemy enm = ownAi.enemies.get(j);
			if(enm.getHero().getColor() == hero.getColor())
			{
				return enm.getType();
			}
		}
		return EnemyTypes.UNKNOWN;
	}
	
	public Enemy getEnemy(AiHero hero) throws StopRequestException
	{ownAi.checkInterruption();
		for(int j=0; j<ownAi.enemies.size(); j++)
		{ownAi.checkInterruption();
			Enemy enm = ownAi.enemies.get(j);
			
			if(enm.getHero().getColor() == hero.getColor())
			{
				return enm;
			}
		}
		return null;
	}
	
	public void updatePath() throws StopRequestException
	{ownAi.checkInterruption();
		if(path == null)
			return;
		for(int i=0; i<path.getLength(); i++)
		{ownAi.checkInterruption();
			AiTile pthTile = path.getTile(i);
			pthTile = zone.getTile(pthTile.getLine(), pthTile.getCol());
			path.setTile(i, pthTile);
		}
		
	}
	public void evaluateOpposites() throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int i=0; i<heroes.size(); i++)
		{ownAi.checkInterruption();
			AiHero hero = heroes.get(i);
			
			for(int j=0; j<ownAi.enemies.size(); j++)
			{ownAi.checkInterruption();
				Enemy enm = ownAi.enemies.get(j);
				
				if(enm.getHero().getColor() == hero.getColor())
				{
					enm.updateHero(hero);
					
					if(zone.getPixelDistance(OWN_HERO.getPosX(), OWN_HERO.getPosY(), enm.getHero().getPosX(), enm.getHero().getPosY()) <= 40)
						enm.increaseSameTileCount();
					
					if(hero.getBombCount() > 0)
						enm.increasePosedBombs(hero.getBombCount());
				}
			}
		}
		
		if(heroes.size() != ownAi.enemies.size())
		{
			for(int j=ownAi.enemies.size()-1; j>=0; j--)
			{ownAi.checkInterruption();
				boolean found = false;
				for(int i=0; i<heroes.size(); i++)
				{ownAi.checkInterruption();
					AiHero hero = heroes.get(i);
					
					if(hero.getColor() == ownAi.enemies.get(j).getHero().getColor())
					{
						found = true;
						break;
					}
				}
				
				if(!found)
				{
					ownAi.enemies.remove(j);
				}
			}
		}
		
			for(int i=0 ; i<ownAi.enemies.size(); i++)
			{ownAi.checkInterruption();
				Enemy enm = ownAi.enemies.get(i);
				
				if(zone.getTotalTime()-enm.getLastCheck() < 1000)
					continue;
				else if(enm.getType() == EnemyTypes.HARD || enm.getType() == EnemyTypes.FOLLOW)
					continue;
				
				boolean reach = canHeReachMe(enm.getHero());

				enm.setLastCheck(zone.getTotalTime());
				if(enm.getPosedBombs() == 0 && enm.getSameTileCount() >= 300 && reach)
				{
					enm.setType(EnemyTypes.FOLLOW);
					Debug.writeln(enm.getHero().toString() + " type is " + enm.getType().toString() + " " + enm.toString(),ownAi);
				}
				else if(enm.getPosedBombs() == 0)
				{
					enm.setType(EnemyTypes.EASY);
					Debug.writeln(enm.getHero().toString() + " type is " + enm.getType().toString() + " " + enm.toString(),ownAi);
				}
				else if(enm.getPosedBombs() > 0 && enm.getPosedBombs() <= 200)
				{
					enm.setType(EnemyTypes.MEDIUM);
					Debug.writeln(enm.getHero().toString() + " type is " + enm.getType().toString() + " " + enm.toString(),ownAi);
				}
				else if(enm.getPosedBombs() > 200)
				{
					enm.setType(EnemyTypes.HARD);
					Debug.writeln(enm.getHero().toString() + " type is " + enm.getType().toString() + " " + enm.toString(),ownAi);
				}			
			}
	}
	
	public AiHero getCloserHero() throws StopRequestException
	{ownAi.checkInterruption();
		double minDist = Double.MAX_VALUE;
		AiHero minHero = null;
		for(int i =0 ; i<heroes.size(); i++)
		{ownAi.checkInterruption();
			AiHero tempHero = heroes.get(i);
			if(tempHero != OWN_HERO)
			{
				double dist = zone.getPixelDistance(tempHero.getPosX(), tempHero.getPosY(), OWN_HERO.getPosX(), OWN_HERO.getPosY());
				if(dist < minDist)
				{
					minDist = dist;
					minHero = tempHero;
				}
			}
		}
		
		return minHero;		
		
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
		Debug.writeln("Current : " + CURRENT_TILE.toString(),ownAi);
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
		else
		{
			for(int j=0; j<6; j++)
			{ownAi.checkInterruption();
				calculateNewPath();
				if(path == null)
					continue;
				for(int i=0; i<path.getLength(); i++ )
				{ownAi.checkInterruption();
					if(i == path.getLength()-1)
						break;
					AiTile nextToGo = path.getTile(i);
					
					double time = (i+1)*CURRENT_TILE.getSize() / OWN_HERO.getWalkingSpeed();
						
					if( (time) > (safeArray[nextToGo.getLine()][nextToGo.getCol()] + 0.5) )
					{
						path = null;
						ownAi.setPath(null);
						Debug.writeln("I CANT ESCAPE IF I START RISKY!",ownAi);
						break;
					}
				}
			}
			Debug.writeln("New path found for stuck",ownAi);
			return true;
		}
	}
	
	public void initArrays() throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeigh();line++)
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
		for(int line=0;line<zone.getHeigh();line++)
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
			if(tile == CURRENT_TILE)
				continue;
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
	
	public AiPath findPathToGo() throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeigh();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile TileToCheck = zone.getTile(line, col);
				if(CURRENT_TILE == TileToCheck)
					continue;
				if(safeArray[line][col] >= STUCK)
				{
					AiPath pth = calculateNewPath(zone.getTile(line, col));
					if(pth != null && pth.getLength() > 3)
						return pth;
				}
				
			}
		}
		return null;
	}
	
	public List<AiTile> getSafeTiles() throws StopRequestException
	{
		ownAi.checkInterruption();
		List<AiTile> safeTiles = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeigh();line++)
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
	
	public double[][] copyTiles(double[][] from) throws StopRequestException
	{	ownAi.checkInterruption();
		double[][] to = new double[zone.getHeigh()][zone.getWidth()];
		for(int line=0;line<zone.getHeigh();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				to[line][col] = from[line][col];
			}
		}
		return to;
	}
	
	public AiTile findStuckTile() throws StopRequestException
	{
		ownAi.checkInterruption();
		for(int line=0;line<zone.getHeigh();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			Debug.writeln("",ownAi);
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				if(safeArray[line][col] == STUCK)
				{
					AiTile stTile = zone.getTile(line, col);
					AiTile tempTile = stTile;
					int dirCt = 0;
					int stCt = 0;
					while(dirCt < 4)
					{	ownAi.checkInterruption();
						Direction dir = null;
						if(dirCt == 0)
							dir = Direction.UP;
						else if(dirCt == 1)
							dir = Direction.DOWN;
						else if(dirCt == 3)
							dir = Direction.LEFT;
						else
							dir = Direction.RIGHT;
						AiTile neiTile = tempTile.getNeighbor(dir);
						if(neiTile.isCrossableBy(OWN_HERO))
						{
							int i=0;
							for(i=0; i<3; i++)
							{	ownAi.checkInterruption();
								stCt = 0;
								for(int j=0; j<neiTile.getNeighbors().size(); j++)
								{	ownAi.checkInterruption();
									if(neiTile.getNeighbors().get(j).isCrossableBy(OWN_HERO))
										stCt++;
								}
								if(stCt > 2)
									break;
								
								neiTile = tempTile.getNeighbor(dir);
							}
							if(i==3)
								return zone.getTile(line, col);
						}			
					}
				}
			}
		}
		return null;
	}
	
	public void printTiles() throws StopRequestException
	{
		ownAi.checkInterruption();
		Debug.writeln("LISTING MATRICE",ownAi);
		for(int line=0;line<zone.getHeigh();line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			Debug.writeln("",ownAi);
			for(int col=0;col<zone.getWidth();col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				Debug.write(safeArray[line][col] + " ",ownAi);
			}
		}
	}
	
	public void printTiles(double matrice[][]) throws StopRequestException
	{
		ownAi.checkInterruption();
		Debug.writeln("LISTING MATRICE",ownAi);
		for(int line=0;line<matrice.length;line++)
		{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
			Debug.writeln("",ownAi);
			for(int col=0;col<matrice[line].length;col++)
			{	ownAi.checkInterruption(); //APPEL OBLIGATOIRE
				Debug.write(matrice[line][col] + " ",ownAi);
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
	
	public boolean canHeReachMe(AiHero hero) throws StopRequestException
	{
		ownAi.checkInterruption();
		if(hero == OWN_HERO)
			return false;
					
		AiPath possiblePath = calculateNewPath(hero.getTile());
		if(possiblePath != null)
		{
			return true;
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
		
		double tempArray[][] = new double[zone.getHeigh()][zone.getWidth()];
		
		tempArray = copyTiles(safeArray);
		
		for(int i=0; i<heroes.size(); i++)
		{ownAi.checkInterruption();
			AiHero hr = heroes.get(i);
			if(getHeroType(hr) != EnemyTypes.EASY && getHeroType(hr) != EnemyTypes.FOLLOW)
			{
				if(hr.getTile() != CURRENT_TILE)
				{
					safeArray[hr.getTile().getLine()][hr.getTile().getCol()] = NOT_SAFE;
				}
			}
		}
		
		int range = OWN_HERO.getBombRange();
		
		AiTile tempTile = CURRENT_TILE;
		
		double bombExplosionTime = Math.min(1.5, safeArray[CURRENT_TILE.getLine()][CURRENT_TILE.getCol()]);
		
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.LEFT);
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
			
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
		}
		tempTile = CURRENT_TILE;
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.RIGHT);
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
			
		}
		
		tempTile = CURRENT_TILE;
		for(int i=0; i<range;i++)
		{
			ownAi.checkInterruption();
			tempTile = tempTile.getNeighbor(Direction.UP);
			
			if(!tempTile.isCrossableBy(OWN_HERO))
				break;
			
			// forme une bombe irreelle avec un temps d'explosion bombExplosionTime
			if(safeArray[tempTile.getLine()][tempTile.getCol()] > bombExplosionTime)
				safeArray[tempTile.getLine()][tempTile.getCol()] = bombExplosionTime;
			
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
			for(int i=0; i<path.getLength(); i++ )
			{ownAi.checkInterruption();
				if(i == path.getLength()-1)
					break;
				AiTile nextToGo = path.getTile(i);
				
				double time = (i+1)*CURRENT_TILE.getSize() / OWN_HERO.getWalkingSpeed();
					
				if((time) > (safeArray[nextToGo.getLine()][nextToGo.getCol()] + 0.5) )
				{
					path = null;
					ownAi.setPath(null);
					safeArray = tempArray;
					Debug.writeln("I CANT ESCAPE IF I BOMB IT'S RISKY!",ownAi);
					return false;
				}
			}
			Debug.writeln("I CAN ESCAPE, BOMBING",ownAi);
			Debug.writeln(path.toString(),ownAi);
			safeArray = tempArray;
			actionToDo = new AiAction(AiActionName.DROP_BOMB);	
			ownAi.setActionToDo(actionToDo);
			ownAi.lastBombedTile = CURRENT_TILE;
			return true;
		}
		
		// On remet safeArray a sa place
		safeArray = tempArray;
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
			if(stuckCheck(tileToGo) == STUCK)
			{
				for(int i=0; i<heroes.size();i++)
				{ownAi.checkInterruption();
					AiHero hr = heroes.get(i);
					if(zone.getTileDistance(hr.getTile(), tileToGo) < 4+zone.getTileDistance(OWN_HERO.getTile(), tileToGo))
						continue;
				}
			}
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
		{	ownAi.checkInterruption();
			ct++;
			if(ct == 2)
				dir = Direction.RIGHT;
			else if(ct == 3)
				dir = Direction.UP;
			else if(ct == 4)
				dir = Direction.DOWN;
			AiTile tempTile = CURRENT_TILE;
			boolean res = true;
			for(int i=0; i<range-1; i++)
			{ownAi.checkInterruption();
				if(!res)
					break;
				tempTile = tempTile.getNeighbor(dir);

				List<AiBlock> n_blocks = tempTile.getBlocks();
				if(n_blocks.size() == 0)
					continue;
				for(int j=0; j< n_blocks.size();)
				{ownAi.checkInterruption();
					AiBlock nBlock = n_blocks.get(j);
					if(nBlock.isDestructible())
						return nBlock;		
					else if(!nBlock.isDestructible())
					{
						res = false;
						break;
					}
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
		//int maxDest = 0;
		for(AiBlock block : destBlocks)
		{
			ownAi.checkInterruption();
			AiTile tileToGo = null;
			for(AiTile neigh : block.getTile().getNeighbors())
			{ownAi.checkInterruption();
				if(isTileSafe(neigh) && neigh.isCrossableBy(OWN_HERO))
				{
					if(ownAi.triedTiles.visited.contains(neigh))
					{
						Debug.writeln("TILE ALREADY TRIED",ownAi);
						continue;
					}
					
					if(stuckCheck(neigh) == STUCK)
					{
						Debug.writeln(neigh.toString() + " added ",ownAi);
						ownAi.triedTiles.visited.add(neigh);
					}
					
					tileToGo = neigh;
					AiPath newpath = calculateNewPath(tileToGo);
					if(newpath == null)
						continue; // path unreachable
					/*
					int totDest = 0;
					for(int i=0; i<neigh.getNeighbors().size(); i++)
					{
						for(int j=0; j< neigh.getNeighbors().get(i).getBlocks().size(); j++)
						{
							if(neigh.getNeighbors().get(i).getBlocks().get(j).isDestructible())
								totDest +=1;
						}
					}*/
					
					double dist = zone.getPixelDistance(CURRENT_TILE.getPosX(), CURRENT_TILE.getPosY(), block.getPosX(), block.getPosY());
					if(dist < minDistance)//if( totDest > maxDest || (totDest == maxDest && dist < minDistance))
					{
						minPath = newpath;
						dist = minDistance;
						minBlock = block;
						//maxDest = totDest;
					}
				}
			}
			
			if(minPath == null)
				ownAi.triedTiles.reset();
		}
		
		if(minBlock == null)		
			return false;
			
		if(minPath != null)
		{
			Debug.writeln("GOING TO SHORTEST BLOCK " + minBlock.getTile().toString(),ownAi);
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
			Debug.write(tileToPass.toString()+" ",ownAi);
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
	
	public boolean calculateNewPath() throws StopRequestException
	{
		ownAi.checkInterruption();
		Astar astar = new Astar(ownAi,OWN_HERO, new MatrixCostCalculator(costArray),new BasicHeuristicCalculator());
		
		List<AiTile> safeTiles = getSafeTiles();
		AiPath localpath = astar.processShortestPath(CURRENT_TILE, safeTiles);
		for(AiTile tileToPass : localpath.getTiles())
		{
			ownAi.checkInterruption();
			Debug.write(tileToPass.toString()+" ",ownAi);
		}

		if(localpath.getTiles().size() > 0)
		{
			Debug.writeln(CURRENT_TILE.toString(),ownAi);
			Debug.writeln(localpath.getLastTile().toString(),ownAi);
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
		
			// on récupère le souffle
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
