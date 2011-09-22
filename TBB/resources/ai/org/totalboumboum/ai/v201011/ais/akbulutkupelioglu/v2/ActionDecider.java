package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.comparator.TileComparator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode.AttackMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode.CollectMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode.Mode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy.AttackStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy.CollectStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy.SurviveStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Coordinate;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.DistanceMatrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Matrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.PathUtility;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.astar.AdvancedSuccessorCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.astar.enemysearch.DestructibleCostCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.astar.enemysearch.DestructibleSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class ActionDecider
{
	private static ActionDecider instance = null;
	private static AkbulutKupelioglu monIA = null;
	private static AiZone zone = null;
	private static Matrix interest = null;
	private static Mode mode = null;

	private ActionDecider() throws StopRequestException
	{
		monIA.checkInterruption();
	}

	public static ActionDecider getInstance(Matrix matrix, AiZone myZone,
			AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIA = ia;
		zone = myZone;
		interest = matrix;
		mode = ia.getMode();
		if(instance == null)
			instance = new ActionDecider();
		return instance;
	}

	public AiAction decide(boolean bombIntention) throws StopRequestException
	{
		monIA.checkInterruption();
		AiHero ownHero = zone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		AiAction action = new AiAction(AiActionName.NONE);
		Astar astar = new Astar(monIA, ownHero, new BasicCostCalculator(), new BasicHeuristicCalculator(), new AdvancedSuccessorCalculator(interest, monIA));
		Strategy previousStrategy = monIA.getStrategy();
		
		boolean willBomb = false;
		if(bombIntention)
		{
			List<AiTile> safeTiles = getSafeTilesWithSimulatedBomb(ownHero);
			if(safeTiles.size()>0)
			{
				AiPath path = new AiPath();
				int i=0;
				while(path.getLength()<=1&&i<safeTiles.size())
				{
					try
					{
						path = astar.processShortestPath(ownTile, safeTiles.get(i));
					}catch(LimitReachedException e)
					{
						//  Auto-generated catch block
						e.printStackTrace();
					}
					i++;					
				}
				if(path.getLength()>1)
				{
					willBomb = true;
					Strategy strategy = new SurviveStrategy(monIA);
					strategy.setGoalAction(new AiAction(AiActionName.NONE));
					strategy.setGoalCase(path.getLastTile());
					strategy.setGoalValue(Integer.MAX_VALUE);
					monIA.setStrategy(strategy);
				}
			}
			
		}
		if(!willBomb)
		{
			if(previousStrategy!=null)
			{
				//goalCase e geldik mi?
				if(ownTile==previousStrategy.getGoalCase())
				{
					if(previousStrategy.getGoalAction()!=null)
						action = previousStrategy.getGoalAction();
					monIA.setStrategy(null);
				}
				else
				{
					//eski stratejiyi degistirmeye gerek var mi?
					boolean changeStrategy = false;
					//tehlikede miyiz?
					if(interest.getElement(ownTile)<0)
					{
						if(!(previousStrategy instanceof SurviveStrategy))
							changeStrategy = true;
					}
						//komsu caselerde bonus var mi
						// implement
						//arkamizdan gelen var mi
						//mal durumda dusman var mi
						//karar al gerek var mi?
					
					if(changeStrategy)//evet:yeni strateji belirle
					{
						action = getNewStrategy(ownTile, ownHero, astar);
					}else//hayir:stratejiye devam
					{
						try
						{
							AiPath path = astar.processShortestPath(ownTile, previousStrategy.getGoalCase());
							if(path.getLength()>1)
							{
								Direction direction = zone.getDirection(ownTile, path.getTile(1));
								action = new AiAction(AiActionName.MOVE, direction);
							}else
							{
								action = getNewStrategy(ownTile, ownHero, astar);
							}
							
						}catch(LimitReachedException e)
						{
							System.out.println("Limit reached. (ActionDecider.decide(): continuing previous strategy)");
							e.printStackTrace();
						}
					}
									
				}
	
			}else
			{	
				action = getNewStrategy(ownTile, ownHero, astar);
			}
		}else
		{
			action = new AiAction(AiActionName.DROP_BOMB);
		}
		//return new AiAction(AiActionName.NONE);
		return action;
	}

	private AiAction getNewStrategy(AiTile ownTile, AiHero ownHero, Astar astar) throws StopRequestException
	{
		monIA.checkInterruption();
		AiAction action = new AiAction(AiActionName.NONE);
		//yeni strateji belirle
		Strategy strategy = null;
		//tehlikede miyiz?
		if(interest.getElement(ownTile)<0)
		{
			//tehlikedeyiz? olmayabilir. gittigimiz yonde devam edebilecek zamanimiz var ise devam edebiliriz.
			//en yakın safe case'leri bul.
			List<AiTile> safeTiles = getSafeTiles(ownHero);
			if(!safeTiles.isEmpty())
			{
				AiPath path = new AiPath();
				int i=0;
				do
				{
					monIA.checkInterruption();
					try
					{
						path = astar.processShortestPath(ownTile, safeTiles.get(i));
					}catch(LimitReachedException e)
					{
						System.out.println("Limit reached. (ActionDecider.decide(): no previous strategy, in danger, safe cases present)");
						e.printStackTrace();
					}
				}while(path.getLength()<=1);
				//şimdi oraya gitmek için bir survivalStrategy çak
				strategy = new SurviveStrategy(monIA);
				strategy.setGoalCase(path.getLastTile());
				strategy.setGoalValue(Integer.MAX_VALUE);
				strategy.setGoalAction(new AiAction(AiActionName.NONE));
				monIA.setStrategy(strategy);
				Direction direction = zone.getDirection(ownTile, path.getTile(1));
				action = new AiAction(AiActionName.MOVE, direction);
			}//else kolay gelsin
			else
			{
				System.out.println("I'm in danger but i cannot find any way to escape. Goodbye.");
				action = new AiAction(AiActionName.NONE);
			}
		}else //tehlikede değiliz
		{
			//hangi moddayiz?
			Mode mode = monIA.getMode();
			boolean wait = true;
			for(AiTile neighbor : ownTile.getNeighbors())
			{
				if(interest.getElement(neighbor)>0)
					wait=false;
			}
			if(wait) //nothing to do, except wait.
				return new AiAction(AiActionName.NONE);
			//nereye gidelim?
			List<Coordinate> interestingPlaces = interest.getMax(20); // bunu bi sekilde breadth-first search ile birlestir = win!
			//bundan asagisi hic de mantikli degil. bi yerde feci bir design flaw var. strategy olayini yeniden dusunmek gerekebilir.
			List<AiTile> possiblePlaces = new ArrayList<AiTile>();
			for(Coordinate coordinate : interestingPlaces)
			{
				AiTile possibleInteresting = zone.getTile(coordinate.x, coordinate.y);
				if(Mode.getTileValue(possibleInteresting, ownHero)>0)
					possiblePlaces.add(zone.getTile(coordinate.x, coordinate.y));
			}
			AiPath path = new AiPath();
			AiTile goalTile;
			int i = 0;
			
			while(path.getLength()<=1&&i<possiblePlaces.size())
			{
				AiTile endTile = zone.getTile(interestingPlaces.get(i).x, interestingPlaces.get(i).y);
				try
				{
					path = astar.processShortestPath(ownTile, endTile);
				}catch(LimitReachedException e)
				{
					System.out.println("Limit reached. (ActionDecider.decide(): no previous strategy, not in danger)");
					e.printStackTrace();
				}
				i++;
			}
			goalTile = zone.getTile(interestingPlaces.get(i).x, interestingPlaces.get(i).y);
			//gecici dirty hack
			AiAction goalAction = null;
			if(path.getLength()<=1)
			{
				if(mode instanceof CollectMode)
				{
					DistanceMatrix dm = monIA.getMode().distanceMatrices.get(ownHero);
					List<Coordinate> cs = dm.getMax(20);
	
					for(int j = cs.size(); j >= 0&&path.getLength()<=1; j--)
					{
						Coordinate possibleGoal = cs.get(j);
						AiTile endTile = zone.getTile(possibleGoal.x,possibleGoal.y);
						try
						{
							path = astar.processShortestPath(ownTile, endTile);
						}catch(LimitReachedException e)
						{
							System.out.println("Limit reached. (ActionDecider.decide(): no previous strategy, not in danger)");
							e.printStackTrace();
						}					
					}
				}else
				{
					
					Astar customAstar = new Astar(monIA, ownHero, new DestructibleCostCalculator(), new BasicHeuristicCalculator(),
							new DestructibleSuccessorCalculator());
					AiPath customPath = new AiPath();
					int k=0;
					while(path.getLength()<=1)
					{
						try
						{
							customPath = customAstar.processShortestPath(ownTile, possiblePlaces.get(k));
						}catch(LimitReachedException e)
						{
							System.out.println("Limit reached. (ActionDecider.decide(): no previous strategy, not in danger)");
							e.printStackTrace();
						}	
						k++;
					}
					goalTile = PathUtility.getFirstDestructibleOnPath(customPath, monIA);
					goalAction = new AiAction(AiActionName.DROP_BOMB);
					if(goalTile==null)
						System.out.println("Impossible bug occured. Stay calm and wait for the universe to collapse. LOLJK");
				}
			}
			Strategy newStrategy = null;
			if(monIA.getMode() instanceof AttackMode)
			{
				newStrategy = new AttackStrategy(monIA);
				newStrategy.setGoalCase(goalTile);
				if(goalAction==null)
					goalAction = new AiAction(AiActionName.NONE);
				newStrategy.setGoalAction(goalAction);
				newStrategy.setGoalValue(interestingPlaces.get(i-1).value);
			}else
			{
				newStrategy = new CollectStrategy(monIA);
				newStrategy.setGoalCase(goalTile);
				newStrategy.setGoalAction(new AiAction(AiActionName.DROP_BOMB));
				newStrategy.setGoalValue(interestingPlaces.get(i-1).value);
			}
			
			//stratejiyi yazmayi unutma
			monIA.setStrategy(newStrategy);
			if(path.getLength()>1)
			{
				Direction direction = zone.getDirection(ownTile, path.getTile(1));
				action = new AiAction(AiActionName.MOVE, direction);
			}
		
		}
		return action;
	}

	

	private List<AiTile> getSafeTilesCustom(AiTile tile, AiHero ownHero, Direction direction, Matrix matrix) throws StopRequestException
	{
		monIA.checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		if(!tile.isCrossableBy(ownHero)) //no dice.
			return null;
		if(matrix.getElement(tile)>=0)// safe! yay!
		{
			result.add(tile);
			return result;
		}
		//not safe. we'll consider the neighbors.
		List<AiTile> temp = new ArrayList<AiTile>();
		List<AiTile> neighbors = tile.getNeighbors();
		if(direction!=null)
			neighbors.remove(tile.getNeighbor(direction.getOpposite()));
		for(AiTile neighbor : neighbors)
		{
			monIA.checkInterruption();
			List<AiTile> safeTiles = getSafeTilesCustom(neighbor, ownHero, zone.getDirection(tile, neighbor), matrix); 
			if(safeTiles!=null)
			{
				for(AiTile safeTile : safeTiles)
				{
					monIA.checkInterruption();
					temp.add(safeTile);
				}
			}
		}
		Collections.sort(temp, new TileComparator(tile,monIA));
		result = temp;
		return result;
	}
	
	private List<AiTile> getSafeTiles(AiHero ownHero) throws StopRequestException
	{
		monIA.checkInterruption();	
		return getSafeTilesCustom(ownHero.getTile(), ownHero, null, interest);
	}
	
	private List<AiTile> getSafeTilesWithSimulatedBomb(AiHero ownHero) throws StopRequestException
	{
		monIA.checkInterruption();
		Matrix customMatrix = mode.getCustom(ownHero, 3000, 1000); // MOFO duzgun
		List<AiTile> result = getSafeTilesCustom(ownHero.getTile(), ownHero, null, customMatrix);
		if(result!=null)
		{
			return result;
		}else
		{
			return new ArrayList<AiTile>();
		}
	}
	
	
	
}
