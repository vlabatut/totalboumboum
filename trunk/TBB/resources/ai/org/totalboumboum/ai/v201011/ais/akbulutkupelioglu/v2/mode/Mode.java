package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.BombDecider;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.comparator.BombComparator;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.DistanceMatrix;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Matrix;

/**
 * Class representing a mode that can be chosen given some perceptions.
 * 
 * @author Yasa Akbulut
 * 
 */
@SuppressWarnings("deprecation")
public abstract class Mode
{
	public final int BOMB_SAFETY = -100; // lets us be safe if a bomb gets past its explosion time
	public final int FLAME_SAFETY = -400; // approximate value of case travel time (in ms)
	public final int BLOCK = -1;
	public final int MIN_DISTANCE = 1;
	private Matrix interest;
	private static AiZone zone;
	public static AkbulutKupelioglu monIa;	
	public HashMap<AiHero, DistanceMatrix> distanceMatrices = new HashMap<AiHero, DistanceMatrix>();
	
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
			double timeOfArrival = calculateTileDistance(ownHero.getTile(), flame.getTile())*timePerTile;
			interest.setElement(flame.getTile(), Math.min(0, (int) Math
					.floor((timeOfArrival))
					+ FLAME_SAFETY));
		}
	}
	public void processBombs(AiHero ownHero) throws StopRequestException
	{
		//burdan basla
		monIa.checkInterruption();
		List<AiBomb> bombs = zone.getBombs();
		Collections.sort(bombs, new BombComparator()); //we sort the bombs by remaining time, ascending 
		double timePerTile = 1000*ownHero.getTile().getSize()/ownHero.getWalkingSpeed();
		for(AiBomb bomb : bombs)
		{
			monIa.checkInterruption();
			List<AiTile> blast = bomb.getBlast();
			double normalDuration = bomb.getNormalDuration();
			double timeRemaining = bomb.getTime() - normalDuration	- bomb.getExplosionDuration();
			if(bomb.getTime()>normalDuration)
				timeRemaining = -bomb.getExplosionDuration();
			
			for(AiTile blastTile : blast)
			{
				monIa.checkInterruption();
				double distance = calculateTileDistance(ownHero.getTile(), blastTile);
				
				double matrixValue = timeRemaining + (distance*timePerTile);
				if(interest.getElement(bomb.getTile())<matrixValue)
					matrixValue = interest.getElement(bomb.getTile()); //use min() maybe?
				interest.setElement(blastTile, (int)matrixValue);

			}
		}
	}
	public void processBreadthFirstDistance() throws StopRequestException
	{
		monIa.checkInterruption();
		for(AiFloor floor : zone.getFloors())
		{
			double dist = calculatePixelDistance(zone.getOwnHero().getTile(), floor.getTile());
			interest.setElement(floor.getTile(), (int)dist);
		}
		
		
	}
	
	public void processBombDistances(AiHero ownHero) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiBomb> bombs = zone.getBombs();
		Collections.sort(bombs, new BombComparator());
		for(AiBomb bomb : bombs)
		{
			monIa.checkInterruption();
			List<AiTile> blast = bomb.getBlast();
			double normalDuration = bomb.getNormalDuration();
			double timeRemaining = bomb.getTime() - normalDuration	- bomb.getExplosionDuration();
			if(bomb.getTime()>normalDuration)
				timeRemaining = -bomb.getExplosionDuration();
			
			for(AiTile blastTile : blast)
			{
				monIa.checkInterruption();

				double timeOfArrival = 1000
						* calculatePixelDistance(blastTile, ownHero.getTile())
						/ ownHero.getWalkingSpeed();
				double matrixValue = Math.min(0, (int) Math.floor((timeRemaining + timeOfArrival)) + BOMB_SAFETY);
				if(interest.getElement(bomb.getTile())<matrixValue)
					matrixValue = interest.getElement(bomb.getTile()); //use min() maybe?
				interest.setElement(blastTile, (int)matrixValue);

			}
		}
	}

	
	public Matrix getCustom(AiHero ownHero, double normalDuration, double explosionDuration) throws StopRequestException
	{
		monIa.checkInterruption();
		Matrix copy = interest.getCopy();
		AiTile ownTile = ownHero.getTile();
		List<AiTile> blast = BombDecider.getBombRange(true, ownHero);
		blast.add(ownTile);
		double timeRemaining = 0 - normalDuration - explosionDuration;
		for(AiTile blastTile : blast)
		{
			monIa.checkInterruption();
			double timeOfArrival = 1000	* calculatePixelDistance(ownTile, blastTile) / ownHero.getWalkingSpeed();
			double matrixValue = Math.min(0, (int) Math.floor((timeRemaining + timeOfArrival)) + BOMB_SAFETY);
			if(copy.getElement(blastTile)<matrixValue)
				matrixValue = copy.getElement(blastTile); //use min() maybe?
				
			copy.setElement(blastTile, (int)matrixValue);
		}
		return copy;
	}
	public static int getTileValue(AiTile tile, AiHero ownHero) throws StopRequestException
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
	public static AiZone getZone() throws StopRequestException
	{
		monIa.checkInterruption();
		return zone;
	}
	public static void setZone(AiZone zone) throws StopRequestException
	{
		monIa.checkInterruption();
		Mode.zone = zone;
	}
	public void resetMatrix() throws StopRequestException
	{
		monIa.checkInterruption();
		interest.resetMatrix(monIa);
	}
}
