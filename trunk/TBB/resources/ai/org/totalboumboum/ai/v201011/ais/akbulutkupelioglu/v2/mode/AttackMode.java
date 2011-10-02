package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode;

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Matrix;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public class AttackMode extends Mode
{
	private static AiZone zone;
	private static AttackMode instance = null;

	private final int ENEMY_VALUE = 50;
	private final int UNREACHABLE_ENEMY_VALUE = 200;

	private AttackMode() throws StopRequestException
	{
		monIa.checkInterruption();
		int height = zone.getHeight();
		int width = zone.getWidth();
		setInterest(new Matrix(width, height, monIa));
	}

	public static AttackMode getInstance(AiZone myZone, AkbulutKupelioglu ia)
			throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		setZone(myZone);
		zone = getZone();
		if(instance == null)
			instance = new AttackMode();
		return instance;
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
				
		// //Bombs
		processBombs(ownHero);
		//Flames
		processFlames(ownHero);

		// processBreadthFirstDistance();
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
			double distance = calculatePixelDistance(ownHero.getTile(), bonus
					.getTile());
			double timeOfArrival = ownHero.getWalkingSpeed() / distance;
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

}
