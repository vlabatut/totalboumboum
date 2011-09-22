package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.mode;

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
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.comparator.BombComparator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.DistanceMatrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.util.Matrix;

/**
 * Class representing a mode that can be chosen given some perceptions.
 * 
 * @author Yasa Akbulut
 * 
 */
/**
 * Represents a mode the artificial intelligence can be in. All the modes the IA can use
 * should extend this class.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 *
 */
public abstract class Mode
{
	/**
	 * If a bomb gets past its explosion time, this value is used as its remaining time, allowing the AI to be safe.
	 */
	public final int BOMB_SAFETY = 100;
	/**
	 * This value is used to ensure a case with a flame in it is not selected to travel to.  
	 */
	public final int FLAME_SAFETY = 100; // may be replaced with approximate value of case travel time (in ms)
	/**
	 * The value of an indestructible block.
	 */
	public final int BLOCK = -1;
	/**
	 * The minimum distance (in pixels) the AI prefers to keep with an enemy, to avoid lining ups.
	 */
	public final int MIN_DISTANCE = 1; //may or may not need a revision.
	/**
	 * The interest matrix.
	 */
	public Matrix interest;
	/**
	 * The perceptions.
	 */
	public AiZone zone;
	/**
	 *  The AkbulutKupelioglu using this.
	 */
	public AkbulutKupelioglu monIa;	
	/**
	 * A distance matrix for each hero in the game.
	 */
	public HashMap<AiHero, DistanceMatrix> distanceMatrices = new HashMap<AiHero, DistanceMatrix>();
	/**
	 * Strategy change decision override flag.
	 */
	public boolean strategyChangeNeeded;
	
	/**
	 * Calculates the interest matrix for the corresponding mode.
	 * 
	 * @return the interest matrix of the corresponding mode.
	 * @throws StopRequestException
	 */
	public abstract Matrix calculateMatrix() throws StopRequestException;

	/**
	 * Calculates and returns the distance between two tiles for a given hero in
	 * pixels. Note that the distance is first calculated in tiles, then converted
	 * to pixels, so the result is always quantized. 
	 * 
	 * @param hero The hero who is at the center of a distance matrix.
	 * @param tile1 The first tile.
	 * @param tile2 The second tile.
	 * @return the distance in pixels.
	 * @throws StopRequestException
	 */
	public double calculatePixelDistance(AiHero hero, AiTile tile1, AiTile tile2)
			throws StopRequestException
	{
		monIa.checkInterruption();
		return tile1.getSize()*calculateTileDistance(hero, tile1,tile2);
	}
	/**
	 * Calculates and returns the distance between two tiles for own hero in
	 * pixels. Note that the distance is first calculated in tiles, then converted
	 * to pixels, so the result is always quantized. 
	 * @param tile1 The first tile.
	 * @param tile2 The second tile.
	 * @return The pixel distance between the tiles.
	 * @throws StopRequestException
	 */
	public double calculatePixelDistance(AiTile tile1, AiTile tile2)
			throws StopRequestException
	{
		monIa.checkInterruption();
		return calculatePixelDistance(zone.getOwnHero(), tile1, tile2);
	}
	
	/**
	 * Calculates and returns the distance between two tiles for own hero in
	 * tiles.
	 * @param centerTile The first tile.
	 * @param tile2 The second tile.
	 * @return The pixel distance between the tiles.
	 * @throws StopRequestException
	 */
	public int calculateTileDistance(AiTile centerTile, AiTile tile2) throws StopRequestException
	{
		monIa.checkInterruption();
		return calculateTileDistance(zone.getOwnHero(), centerTile, tile2);
	}
	
	/**
	 * Calculates and returns the distance between two tiles for a given hero in
	 * tiles. The distance is fetched from the distance matrix, if a recalculation is needed
	 * (i.e. the player has moved, or the distance matrix does not exist for that player)
	 * the matrix is recalculated.
	 * 
	 * @param hero The hero for which the distance should be calculated.
	 * @param centerTile The center tile of the distance matrix.
	 * @param tile The tile to which the distance should be calculated.
	 * @return The pixel distance between the tiles.
	 * @throws StopRequestException
	 */
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
	
	/**
	 * Processes the tiles with hard walls in the level, and sets the corresponding element
	 * in the interest matrix accordingly. 
	 * @param ownHero The hero using this.
	 * @throws StopRequestException
	 */
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
	/**
	 * Processes the tiles with soft walls in the level, and sets the corresponding element
	 * in the interest matrix accordingly. 
	 * @param ownHero The hero using this.
	 * @throws StopRequestException
	 */
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
	/**
	 * Processes the tiles with bombs in the level, and sets the corresponding element
	 * in the interest matrix accordingly. Chained bombs are also taken in consideration.
	 * @param ownHero The hero using this.
	 * @throws StopRequestException
	 */
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

	
	/**
	 * Calculates a custom interest matrix, used for simulating a bomb in the current case.
	 * @param ownHero The hero using this.
	 * @return A custom interest matrix.
	 * @throws StopRequestException
	 */
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
	/**
	 * Gets the value of a certain tile. The value corresponds to the tile's "openness", which means
	 * the number of tiles one can travel starting from this tile.
	 *  
	 * @param tile The tile of which the value should be calculated. 
	 * @param ownHero The hero using this.
	 * @return A value, between 0 and 4, indicating the "openness" of a tile.
	 * @throws StopRequestException
	 */
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
	
	/**
	 * Returns the interest matrix.
	 * @return The interest matrix.
	 * @throws StopRequestException
	 */
	public Matrix getInterest() throws StopRequestException
	{
		monIa.checkInterruption();
		return interest;
	}
	/**
	 * Sets the interest matrix.
	 * @param m The interest matrix.
	 * @throws StopRequestException
	 */
	public void setInterest(Matrix m) throws StopRequestException
	{
		monIa.checkInterruption();
		interest = m;
	}
	/**
	 * Gets the zone associated with this mode.
	 * @return The associated zone.
	 * @throws StopRequestException
	 */
	public AiZone getZone() throws StopRequestException
	{
		monIa.checkInterruption();
		return zone;
	}
	/**
	 * Sets the zone associated with this mode.
	 * @param zone The zone to be set.
	 * @throws StopRequestException
	 */
	public void setZone(AiZone zone) throws StopRequestException
	{
		monIa.checkInterruption();
		this.zone = zone;
	}
	/**
	 * Initializes the interest matrix.
	 * @throws StopRequestException
	 */
	public void resetMatrix() throws StopRequestException
	{
		monIa.checkInterruption();

		int height = zone.getHeight();
		int width = zone.getWidth();
		interest = new Matrix(width,height,monIa);
	}
	/**
	 * Resets the matrix with the given dimensions.
	 * @param width The width of the new matrix.
	 * @param height The height of the new matrix.
	 * @throws StopRequestException
	 */
	public void resetMatrix(int width, int height) throws StopRequestException
	{
		monIa.checkInterruption();
		interest = new Matrix(width, height, monIa);
		
	}
	/**
	 * Gets a new strategy, regarding the current state of the game.
	 * @return The new strategy.
	 * @throws StopRequestException
	 */
	public abstract Strategy getNewStrategy() throws StopRequestException;
	
	/**
	 * Decides whether or not a strategy change is needed, regarding the current state of the game.
	 * @return The decision.
	 * @throws StopRequestException
	 */
	public abstract boolean strategyChangeNeeded() throws StopRequestException;
	

	
	/**
	 * Sets the strategy change flag, making it possible to override the decision to change strategy.
	 * @param b The value to be set.
	 * @throws StopRequestException 
	 */
	public void setStrategyChangeNeeded(boolean b) throws StopRequestException
	{
		monIa.checkInterruption();
		strategyChangeNeeded = b;		
	}
}
