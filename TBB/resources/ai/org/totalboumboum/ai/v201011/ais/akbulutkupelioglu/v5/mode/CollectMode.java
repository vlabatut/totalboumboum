package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.mode;

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.strategy.CollectStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.strategy.StrategyStep;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.strategy.SurviveStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.Coordinate;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.Matrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.astar.AdvancedSuccessorCalculator;

/**
 * The class representing the collect mode. This class is responsible for matrix
 * calculation in collect mode, as well as the strategies.
 * 
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 * 
 */
public class CollectMode extends Mode
{

	private static CollectMode instance = null;
	
	private CollectMode(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		zone = ia.getPercepts();
		int height = zone.getHeight();
		int width = zone.getWidth();
		interest = new Matrix(width, height, monIa);
	}

	/**
	 * Gets the instance of this mode. The instance is created if not already created.
	 * @param ia AkbulutKupelioglu using this.
	 * @return The CollectMode instance.
	 * @throws StopRequestException
	 */
	public static CollectMode getInstance(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		if(instance==null)
		{
			instance = new CollectMode(ia);
		}
		return instance;
	}
	/**
	 * Resets this mode, destroying the instance.
	 * @param ia AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public static void reset(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		instance = null;
	}
	
	@Override
	public Matrix calculateMatrix() throws StopRequestException
	{
		monIa.checkInterruption();

		resetMatrix();
		
		AiHero ownHero = zone.getOwnHero();

		// process the bonus
		processBonus(ownHero);

		//process the indestructibles
		processIndestructibles(ownHero);
		
		// process the destructibles
		processDestructibles(ownHero);

		// process the bombs
		processBombs(ownHero);

		// process the flames
		processFlames(ownHero);

		
		return getInterest();
	}


	private void processBonus(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiItem> items = zone.getItems();
		for(AiItem item : items)
		{
			monIa.checkInterruption();
			AiTile itemTile = item.getTile();
			HashMap<AiHero, Double> pixelDistances = new HashMap<AiHero, Double>();
			for(AiHero hero : zone.getRemainingHeroes())
			{
				monIa.checkInterruption();
				pixelDistances.put(hero, calculatePixelDistance(hero, hero.getTile(), itemTile));
			}
			double ownDistance = pixelDistances.get(ownHero);
			if((ownDistance>0)) //if we have access to bonus's area
			{
				double ownTimeOfArrival = ownHero.getWalkingSpeed() / ownDistance;
				double timeFactor = 1000 * ownTimeOfArrival;
				double correctiveForEnemyDistance = 0;
				for(AiHero hero : zone.getRemainingHeroes())
				{
					monIa.checkInterruption();
					if(!(hero.equals(ownHero)))
					{
						if(ownDistance>pixelDistances.get(hero))
						{
							timeFactor = 0; //we have no interest as bonus is closer to an enemy
						}else
						{
							double difference = pixelDistances.get(hero)-ownDistance;
							correctiveForEnemyDistance = (1000* ownHero.getWalkingSpeed() / difference)/(zone.getRemainingHeroes().size()-1);
						}
					}
				}
				if(timeFactor>0)
					timeFactor+=correctiveForEnemyDistance;
				getInterest().addToElement(item.getTile(), (int) timeFactor);
			}
		}
	}

	private void processDestructibles(AiHero ownHero)
			throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiBlock> blocks = zone.getBlocks();
		for(AiBlock aiTile : blocks)
		{
			monIa.checkInterruption();

			if(aiTile.isDestructible())
			{
				List<AiTile> neighbors = aiTile.getTile().getNeighbors();
				for(AiTile neighbor : neighbors)
				{
					monIa.checkInterruption();

					if(neighbor.isCrossableBy(ownHero))
					{
						int distance = calculateTileDistance(ownHero.getTile(), neighbor);
						// double pixelDistance =
						// calculatePixelDistance(neighbor,ownHero.getTile());
						double timeFactor = ownHero.getWalkingSpeed()
								/ distance;
						// double timeOfArrival = pixelDistance /
						// ownHero.getWalkingSpeed() * 1000;
						getInterest().addToElement(neighbor, (int) timeFactor);
					}
				}
			}
		}
	}

	public Strategy getNewStrategy() throws StopRequestException
	{
		monIa.checkInterruption();
		AiHero ownHero = zone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		List<Coordinate> maxElements = interest.getMax(20, distanceMatrices.get(ownHero));
		Astar astar = new Astar(monIa, ownHero, new BasicCostCalculator(), new BasicHeuristicCalculator(), new AdvancedSuccessorCalculator(interest, monIa));
		AiPath path = new AiPath();
		for(Coordinate coordinate : maxElements)
		{
			monIa.checkInterruption();
			if(path.getLength()<=1)
			{
				try
				{
					path = astar.processShortestPath(ownTile, zone.getTile(coordinate.x, coordinate.y));
					path.checkStartingPoint();
				}catch(LimitReachedException e)
				{
					// 
					e.printStackTrace();
				}
			}
		}
		if(path.getLength()<=1)
		{
			//System.out.println("Path length <= 1 in getNewStrategy");
		}
		CollectStrategy result = new CollectStrategy(monIa);
		
		AiTile goalTile = path.getLastTile();
		StrategyStep strategyStep = new StrategyStep(goalTile, new AiAction(AiActionName.NONE), monIa);
		strategyStep.setValue(interest.getElement(goalTile));
		if(goalTile.getItems().isEmpty())
			strategyStep.setAction(new AiAction(AiActionName.DROP_BOMB));
		result.getWaypoints().push(strategyStep);
		return result;
	}

	@Override
	public boolean strategyChangeNeeded() throws StopRequestException
	{
		monIa.checkInterruption();
		Strategy previousStrategy = null;
		previousStrategy = monIa.getStrategy();
		if(previousStrategy==null)
			return true;
		if(previousStrategy.getWaypoints().size()==0)
			return true;
		if(previousStrategy instanceof SurviveStrategy && !previousStrategy.getWaypoints().peek().getTile().equals(zone.getOwnHero().getTile()))
			return false;
		return false;
	}






}
