package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3;

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
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.comparator.TileComparator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.Mode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy.StrategyStep;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy.SurviveStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.Matrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.astar.AdvancedSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class ActionDecider
{
	private static ActionDecider instance = null;
	private static AkbulutKupelioglu monIa = null;
	private static AiZone zone = null;
	private static Matrix interest = null;
	private static Mode mode = null;

	private ActionDecider() throws StopRequestException
	{
		monIa.checkInterruption();
	}

	public static ActionDecider getInstance(Matrix matrix, AiZone myZone,
			AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		zone = myZone;
		interest = matrix;
		mode = ia.getMode();
		if(instance == null)
			instance = new ActionDecider();
		return instance;
	}

	public AiAction decide(boolean bombIntention) throws StopRequestException
	{
		monIa.checkInterruption();
		Mode currentMode = monIa.getMode();
		AiHero ownHero = zone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		Strategy previousStrategy = monIa.getStrategy();
		AiAction result = new AiAction(AiActionName.NONE);
		Astar astar = new Astar(monIa, ownHero, new BasicCostCalculator(), new BasicHeuristicCalculator(), new AdvancedSuccessorCalculator(interest, monIa));
		AiPath path = new AiPath();
		if(previousStrategy == null)
		{
			previousStrategy = currentMode.getNewStrategy();
			monIa.setStrategy(previousStrategy);
		}
		if(interest.getElement(ownTile)<0)
		{
			if(previousStrategy instanceof SurviveStrategy)
			{
				//continue strategy
				AiTile safeTile = previousStrategy.getWaypoints().peek().getTile();
				try
				{
					path = astar.processShortestPath(ownTile, safeTile);
					path.checkStartingPoint();
				}catch(LimitReachedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(path.getLength()>1)
				{
					Direction direction = zone.getDirection(ownHero, path.getTile(1));
					result=new AiAction(AiActionName.MOVE,direction);
				}
			}else
			{
				//new survive strategy
				List<AiTile> safeTiles = getSafeTiles(ownHero);
				if(safeTiles.isEmpty())
				{
					//dead? not necessarily. we'll pick a random direction and just run like hell.
					//in the next version, of course.
				}else
				{
					try
					{
						path = astar.processShortestPath(ownTile, safeTiles);
						path.checkStartingPoint();
					}catch(LimitReachedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(path.getLength()<=1)
					{
						//this should not be
					}else
					{
						SurviveStrategy surviveStrategy = new SurviveStrategy(monIa);
						surviveStrategy.setLastStrategy(previousStrategy);
						StrategyStep step = new StrategyStep(path.getLastTile(), new  AiAction(AiActionName.NONE), monIa);
						step.setValue(Integer.MAX_VALUE);
						surviveStrategy.getWaypoints().add(step);
						monIa.setStrategy(surviveStrategy);
						result = getActionFromStrategy(surviveStrategy);
					}
				}
			}			
		}else
		{
			if(previousStrategy==null)
			{
				Strategy newStrategy = mode.getNewStrategy();
				monIa.setStrategy(newStrategy);
				result = getActionFromStrategy(newStrategy);
			}else
			{
				if(previousStrategy.waypoints.isEmpty())
				{
					if(previousStrategy instanceof SurviveStrategy)
					{
						Strategy newStrategy = ((SurviveStrategy)previousStrategy).getLastStrategy();
						monIa.setStrategy(newStrategy);
						result = getActionFromStrategy(newStrategy);
					}else
					{
						//new strategy
						Strategy newStrategy = mode.getNewStrategy();
						monIa.setStrategy(newStrategy);
						result = getActionFromStrategy(newStrategy);
					}
				}else
				{
					StrategyStep step = previousStrategy.waypoints.peek();
					if(step.getTile().equals(ownTile))
					{
						//goal!
						if(!ownTile.getItems().isEmpty())
							result = new AiAction(AiActionName.NONE);
						else
						{
							StrategyStep currentStep = previousStrategy.waypoints.pop();
							result = currentStep.getAction();
						}
					}else
					{
						//continue strategy
						result = getActionFromStrategy(previousStrategy);
					}
				}
			}
		}
		
		//if we have intention to drop a bomb, we'll drop it with chance of 20%
		if(bombIntention)
		{
			if(Math.random()>0.8)
				result = new AiAction(AiActionName.DROP_BOMB);
		}
		
		if(result.getName().equals(AiActionName.DROP_BOMB))
		{
			//let's find a safe tile before we drop a bomb
			List<AiTile> safeTiles = getSafeTilesWithSimulatedBomb(ownHero);
			if(safeTiles.isEmpty())
			{
				result = new AiAction(AiActionName.NONE);
			}else
			{
				try
				{
					path = astar.processShortestPath(ownHero.getTile(), safeTiles);
					path.checkStartingPoint();
				}catch(LimitReachedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(path.getLength()<=1)
				{
					result = new AiAction(AiActionName.NONE);
				}else
				{
					SurviveStrategy surviveStrategy = new SurviveStrategy(monIa);
					StrategyStep step1 = new StrategyStep(path.getLastTile(), new AiAction(AiActionName.NONE), monIa);
					step1.setValue(Integer.MAX_VALUE);
					//StrategyStep step2 = new StrategyStep(ownTile, new AiAction(AiActionName.DROP_BOMB), monIa);
					//step2.setValue(interest.getElement(ownTile));
					surviveStrategy.getWaypoints().push(step1);
					//surviveStrategy.getWaypoints().push(step2);
					surviveStrategy.setLastStrategy(previousStrategy);
					monIa.setStrategy(surviveStrategy);
					result = new AiAction(AiActionName.DROP_BOMB);
				}
			}
		}
		
		return result;
	}

	

	private AiAction getActionFromStrategy(Strategy newStrategy) throws StopRequestException
	{
		monIa.checkInterruption();
		Astar astar = new Astar(monIa, zone.getOwnHero(), new BasicCostCalculator(), new BasicHeuristicCalculator(), new AdvancedSuccessorCalculator(interest, monIa));
		AiPath path = new AiPath();
		if(newStrategy.waypoints.isEmpty())
			return new AiAction(AiActionName.NONE); 
		AiTile endTile = newStrategy.waypoints.peek().getTile();
		try
		{
			path = astar.processShortestPath(zone.getOwnHero().getTile(), endTile);
			path.checkStartingPoint();
		}catch(LimitReachedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(path.getLength()<=1)
			return new AiAction(AiActionName.NONE);
		Direction direction = zone.getDirection(zone.getOwnHero(), path.getTile(1));
		if(interest.getElement(zone.getOwnHero().getTile())>0&&interest.getElement(path.getTile(1))<0)
			return new AiAction(AiActionName.NONE);
		return new AiAction(AiActionName.MOVE, direction);
	}

	private List<AiTile> getSafeTilesCustom(AiTile tile, AiHero ownHero, Direction direction, Matrix matrix) throws StopRequestException
	{
		monIa.checkInterruption();
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
			monIa.checkInterruption();
			List<AiTile> safeTiles = getSafeTilesCustom(neighbor, ownHero, zone.getDirection(tile, neighbor), matrix); 
			if(safeTiles!=null)
			{
				for(AiTile safeTile : safeTiles)
				{
					monIa.checkInterruption();
					temp.add(safeTile);
				}
			}
		}
		Collections.sort(temp, new TileComparator(tile,monIa));
		result = temp;
		return result;
	}
	
	private List<AiTile> getSafeTiles(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();	
		return getSafeTilesCustom(ownHero.getTile(), ownHero, null, interest);
	}
	
	private List<AiTile> getSafeTilesWithSimulatedBomb(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		Matrix customMatrix = mode.getCustom(ownHero); //TODO: MOFO duzgun. v2: oldu gibi?
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
