package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.comparator.BombComparator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.DistanceMatrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.Matrix;

/**
 * Class representing a mode that can be chosen given some perceptions.
 * 
 * @author Yasa Akbulut
 * 
 */
public abstract class Mode
{
	public final int BOMB_SAFETY = 100; // lets us be safe if a bomb gets past its explosion time
	public final int FLAME_SAFETY = 100; // approximate value of case travel time (in ms)
	public final int BLOCK = -1;
	public final int MIN_DISTANCE = 1;
	public Matrix interest;
	public AiZone zone;
	public AkbulutKupelioglu monIa;	
	public HashMap<AiHero, DistanceMatrix> distanceMatrices = new HashMap<AiHero, DistanceMatrix>();
	public boolean strategyChangeNeeded;
	
	/**
	 * Calculates the interest matrix for the corresponding mode.
	 * 
	 * @return the interest matrix of the corresponding mode.
	 * @throws StopRequestException
	 */
	public abstract Matrix calculateMatrix() throws StopRequestException;

	/**
	 * Calculates and returns the Manhattan Distance between two tiles in
	 * pixels.
	 * 
	 * @param tile1
	 *            The first tile
	 * @param tile2
	 *            The second tile
	 * @return the distance in pixels.
	 * @throws StopRequestException
	 */
	public double calculatePixelDistance(AiHero hero, AiTile tile1, AiTile tile2)
			throws StopRequestException
	{
		monIa.checkInterruption();
		return tile1.getSize()*calculateTileDistance(hero, tile1,tile2);
	}
	public double calculatePixelDistance(AiTile tile1, AiTile tile2)
			throws StopRequestException
	{
		monIa.checkInterruption();
		return calculatePixelDistance(zone.getOwnHero(), tile1, tile2);
	}
	
	public int calculateTileDistance(AiTile centerTile, AiTile tile2) throws StopRequestException
	{
		monIa.checkInterruption();
		return calculateTileDistance(zone.getOwnHero(), centerTile, tile2);
	}
	
	public int calculateTileDistance(AiHero hero, AiTile centerTile, AiTile tile) throws StopRequestException
	{
		monIa.checkInterruption();
		DistanceMatrix distanceMatrix = distanceMatrices.get(hero);
		if(distanceMatrix==null)
		{
			distanceMatrix = new DistanceMatrix(zone.getWidth(), zone.getHeight(), monIa);
			distanceMatrices.put(hero, distanceMatrix);
		}
		if(!centerTile.equals(distanceMatrix.getCenterTile()))
			distanceMatrix.recalculate(centerTile);
		return distanceMatrix.getElement(tile);
	}
	
	public void processIndestructibles(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiBlock> blocks = zone.getBlocks();
		for(AiBlock aiBlock : blocks)
		{
			monIa.checkInterruption();
			interest.setElement(aiBlock.getTile(), BLOCK);
		}
	}
	public void processFlames(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiFire> flames = zone.getFires();
		double timePerTile = 1000 * ownHero.getTile().getSize() / ownHero.getWalkingSpeed();
		for(AiFire flame : flames)
		{
			monIa.checkInterruption();
			double distance = Math.max(calculateTileDistance(ownHero.getTile(), flame.getTile()) - 1, 0); //why 1? because we can be at the two
			double timeOfArrival = distance * timePerTile;												//edges of a tile and still get the same distance.
			double matrixValue = Math.min(timeOfArrival - flame.getTime(), 0);
			interest.setElement(flame.getTile(), (int)matrixValue);
		}
	}
	public void processBombs(AiHero ownHero) throws StopRequestException
	{

		monIa.checkInterruption();
		double timePerTile = 1000*ownHero.getTile().getSize()/ownHero.getWalkingSpeed();
		List<AiBomb> bombs = zone.getBombs();
		//we sort the bombs by remaining time, ascending. this is crucial for chained explosion calculations. 
		Collections.sort(bombs, new BombComparator());
		
		HashMap<AiBomb, Double> bombTimes = new HashMap<AiBomb, Double>();
		//populate HashMap
		for(AiBomb bomb : bombs)
		{
			monIa.checkInterruption();
			double normalDuration = bomb.getNormalDuration();
			double bombTime = bomb.getTime();
			double timeRemaining = normalDuration - bombTime;
			if(timeRemaining<0)
			{
				if(bomb.getState().getName().equals(AiStateName.BURNING))//bomb is exploding
					timeRemaining = 0;
				else //bomb will explode.
					timeRemaining = BOMB_SAFETY;
			}
			
			
			bombTimes.put(bomb, timeRemaining);
		}
		//take chained explosions in consideration
		for(AiBomb bomb : bombs)
		{
			monIa.checkInterruption();
			List<AiTile> blast = bomb.getBlast();
			for(AiTile blastTile : blast)
			{
				monIa.checkInterruption();
				List<AiBomb> tileBombs = blastTile.getBombs();
				if(!tileBombs.isEmpty())
				{
					for(AiBomb triggeredBomb : tileBombs)
					{
						monIa.checkInterruption();
						if(!triggeredBomb.equals(bomb))
						{
							double newTimeRemaining = bombTimes.get(bomb)+triggeredBomb.getLatencyDuration();
							//we'll change only if the "triggered bomb" is going to explode later, this prevents us from changing a bomb that's already changed.
							if(bombTimes.get(triggeredBomb)>newTimeRemaining)
								bombTimes.put(triggeredBomb, newTimeRemaining);
						}
						
					}
				}
			}
		}
		//now we have a HashMap containing all the right values for remaining bomb times.
		
		for(AiBomb bomb : bombs)
		{
			monIa.checkInterruption();
			for(AiTile blastTile : bomb.getBlast())
			{
				monIa.checkInterruption();
				double distance = Math.max(calculateTileDistance(ownHero.getTile(), blastTile) - 1, 0); //why 1? because we can be at the two
				double timeOfArrival = distance * timePerTile;												//edges of a tile and still get the same distance.
				double matrixValue = Math.min(timeOfArrival - bombTimes.get(bomb), 0);
				interest.setElement(blastTile, (int)matrixValue);
			}
		}
		
	}

	
	public Matrix getCustom(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		Matrix copy = interest.getCopy();
		AiTile ownTile = ownHero.getTile();
		AiBomb bombPrototype = ownHero.getBombPrototype();
		List<AiTile> blast = bombPrototype.getBlast();
		blast.add(ownTile);
		double bombTimeRemaining = 0 - bombPrototype.getNormalDuration() - bombPrototype.getExplosionDuration();
		List<AiBomb> bombs = zone.getBombs();
		List<AiBomb> chainedBombs = new ArrayList<AiBomb>();
		//we'll check all the bombs to find out if we're going to have a chain reaction
		for(AiBomb bomb : bombs)
		{
			monIa.checkInterruption();
			List<AiTile> oldBombBlast = bomb.getBlast();
			if(oldBombBlast.contains(ownTile))
				if(!chainedBombs.contains(bomb))
					chainedBombs.add(bomb);
		}
		if(!chainedBombs.isEmpty())
		{
			Collections.sort(chainedBombs, new BombComparator());
			AiBomb firstBomb = chainedBombs.get(0); //the bomb that would trigger the explosion
			bombTimeRemaining = 0-firstBomb.getNormalDuration()-firstBomb.getExplosionDuration();
		}
		
		
		for(AiTile blastTile : blast)
		{
			monIa.checkInterruption();
			double timeOfArrival = 1000	* calculatePixelDistance(ownTile, blastTile) / ownHero.getWalkingSpeed();
			double matrixValue = Math.min(0, (int) Math.floor((bombTimeRemaining + timeOfArrival)) + BOMB_SAFETY);
			copy.setElement(blastTile, (int)matrixValue);
		}
		return copy;
	}
	public int getTileValue(AiTile tile, AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		if(!tile.getBlocks().isEmpty())
			return 0;
		int value = 4;
		for(AiTile neighbor : tile.getNeighbors())
		{
			monIa.checkInterruption();
			if(!neighbor.isCrossableBy(ownHero))
			{
				value--;
			}
		}
		return value;
	}
	
	public Matrix getInterest() throws StopRequestException
	{
		monIa.checkInterruption();
		return interest;
	}
	public void setInterest(Matrix m) throws StopRequestException
	{
		monIa.checkInterruption();
		interest = m;
	}
	public AiZone getZone() throws StopRequestException
	{
		monIa.checkInterruption();
		return zone;
	}
	public void setZone(AiZone zone) throws StopRequestException
	{
		monIa.checkInterruption();
		this.zone = zone;
	}
	public void resetMatrix() throws StopRequestException
	{
		monIa.checkInterruption();

		int height = zone.getHeight();
		int width = zone.getWidth();
		interest = new Matrix(width,height,monIa);
	}
	public abstract Strategy getNewStrategy() throws StopRequestException;
	
	public abstract boolean strategyChangeNeeded() throws StopRequestException;
	
	public void resetMatrix(int width, int height) throws StopRequestException
	{
		monIa.checkInterruption();
		interest = new Matrix(width, height, monIa);
		
	}

	public void setStrategyChangeNeeded(boolean b)
	{
		strategyChangeNeeded = b;		
	}
}
