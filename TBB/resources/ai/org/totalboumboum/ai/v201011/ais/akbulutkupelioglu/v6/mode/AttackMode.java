package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.mode;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.strategy.AttackStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.strategy.StrategyStep;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.strategy.SurviveStrategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util.Coordinate;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util.Matrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util.PathUtility;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util.astar.enemysearch.DestructibleCostCalculator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.util.astar.enemysearch.DestructibleSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * The class representing the attack mode. This class is responsible for matrix
 * calculation in attack mode, as well as the strategies.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
@SuppressWarnings("deprecation")
public class AttackMode extends Mode
{

	private final int ENEMY_VALUE = 50;
	private final int UNREACHABLE_ENEMY_VALUE = 200;

	private static AttackMode instance = null;
	
	private AttackMode(AkbulutKupelioglu ia) throws StopRequestException
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
	 * @return The AttackMode instance.
	 * @throws StopRequestException
	 */
	public static AttackMode getInstance(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		if(instance==null)
		{
			instance = new AttackMode(ia);
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
		AiHero ownHero = zone.getOwnHero();

		resetMatrix();

		 //Indestructibles
		 processIndestructibles(ownHero);
				
		 //Destructibles
		 processDestructibles(ownHero);
				
		 //Bonus
		 processBonus(ownHero);
				
		 //Enemies
		 processEnemies(ownHero);
				
		 //Bombs
		 processBombs(ownHero);
		 //Flames
		 processFlames(ownHero);

		return getInterest();
	}

	private void processDestructibles(AiHero ownHero)
			throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiBlock> blocks = zone.getBlocks();
		for(AiBlock block : blocks)
		{
			monIa.checkInterruption();
			List<AiTile> neighbors = block.getTile().getNeighbors();
			for(AiTile neighbor : neighbors)
			{
				monIa.checkInterruption();
				if(neighbor.isCrossableBy(ownHero))
				{
					getInterest().addToElement(neighbor, 10);
				}
			}
		}
	}

	private void processBonus(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		for(AiItem bonus : zone.getItems())
		{
			monIa.checkInterruption();
			double distance = calculatePixelDistance(ownHero.getTile(), bonus
					.getTile());
			double timeOfArrival = 0.02;
			if(distance>0)
			timeOfArrival = ownHero.getWalkingSpeed() / distance;
			double timeFactor = 500 * timeOfArrival;
			getInterest().setElement(bonus.getTile(), (int) timeFactor);
		}
	}

	private void processEnemies(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		AiTile ownTile = ownHero.getTile();
		double speed = ownHero.getWalkingSpeed();

		List<AiHero> enemies = zone.getRemainingHeroes();
		enemies.remove(ownHero);

		for(AiHero enemy : enemies)
		{
			monIa.checkInterruption();
			AiTile enemyTile = enemy.getTile();
			double enemyDistance = calculatePixelDistance(ownTile, enemyTile);
			if(enemyDistance > 0)
			{
				if(enemy.getState().getDirection() != Direction.NONE)// enemy is
																		// moving
				{
					double distance = calculateTileDistance(ownHero.getTile(),
							enemyTile);
					distance = Math.max(distance, MIN_DISTANCE);
					double value = ENEMY_VALUE * speed / distance;
					Direction[] directions = enemy.getState().getDirection()
							.getPrimaries();
					for(Direction direction : directions)
					{
						monIa.checkInterruption();
						AiTile neighbor = enemyTile.getNeighbor(direction);
						if(neighbor != null)// there is a neighbor
							if(neighbor.getBlocks().isEmpty())// it is not a
																// wall
								if(getTileValue(neighbor, ownHero) == 3)// it is
																		// an
																		// immediate
																		// dead-end
									getInterest().setElement(enemyTile,
											(int) value);
								else
									// it's not an immediate dead-end
									getInterest().setElement(neighbor,
											(int) value);
					}
				}else
				// enemy is not moving
				{
					double distance = calculatePixelDistance(ownTile, enemyTile);
					distance = Math.max(distance, MIN_DISTANCE);
					double timeFactor = ENEMY_VALUE * speed / distance;
					for(Direction direction : Direction.getPrimaryValues())// we'll
																			// look
																			// at
																			// the
																			// neighbors
					{
						monIa.checkInterruption();
						AiTile firstNeighborTile = enemyTile
								.getNeighbor(direction);
						int firstNeighborValue = 0;
						int secondNeighborValue = 0;
						if(firstNeighborTile.getBlocks().isEmpty()) // if the
																	// direct
																	// neighbor
																	// is not a
																	// block
						{
							firstNeighborValue = getTileValue(
									firstNeighborTile, ownHero);
							for(AiTile secondNeighborTile : firstNeighborTile
									.getNeighbors()) // we'll evaluate its
														// neighbors
							{
								monIa.checkInterruption();
								secondNeighborValue += getTileValue(
										secondNeighborTile, ownHero);
							}
						}
						double value = (Math.abs(firstNeighborValue) + Math
								.abs(secondNeighborValue))
								* timeFactor; // may need a recalculation
						if(enemyTile.getNeighbor(direction).isCrossableBy(
								ownHero))
							getInterest().setElement(
									enemyTile.getNeighbor(direction),
									(int) value);
					}
				}
			}else//we don't have access to enemy
			{
				getInterest().setElement(enemyTile, UNREACHABLE_ENEMY_VALUE);
			}
		}
	}


	@Override
	public Strategy getNewStrategy() throws StopRequestException
	{
		monIa.checkInterruption();
		AiHero ownHero = zone.getOwnHero();
		AiTile ownTile = ownHero.getTile();
		List<Coordinate> maxElements = interest.getMax(20, distanceMatrices.get(ownHero));
		Astar astar = new Astar(monIa, ownHero, new DestructibleCostCalculator(monIa), new BasicHeuristicCalculator(), new DestructibleSuccessorCalculator(monIa));
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
		List<AiTile> waypoints = PathUtility.getDestructibleNeighboursOnPath(path, monIa);
		
		waypoints.add(path.getLastTile());
		Strategy result = new AttackStrategy(monIa);
		for(int i=waypoints.size(); i>0; i--)
		{
			monIa.checkInterruption();
			StrategyStep strategyStep = new StrategyStep(waypoints.get(i-1), new AiAction(AiActionName.DROP_BOMB), monIa);
			strategyStep.setValue(interest.getElement(waypoints.get(i-1)));
			result.getWaypoints().push(strategyStep);
		}

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
