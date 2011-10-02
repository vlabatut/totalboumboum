package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util.Matrix;

import java.util.HashMap;
import java.util.List;

/**
 * The class representing the collect mode. This class is responsible for matrix
 * calculation in collect mode.
 * 
 * @author Yasa Akbulut
 * 
 */
@SuppressWarnings("deprecation")
public class CollectMode extends Mode
{
	//gözden geçir, ben atladım bunu

	private static CollectMode instance = null;
	private static AiZone zone;

	private CollectMode() throws StopRequestException
	{
		monIa.checkInterruption();

		int height = getZone().getHeight();
		int width = getZone().getWidth();
		setInterest(new Matrix(width, height, monIa));
	}

	/**
	 * Returns the CollectMode instance. If an instance does not exist, a new
	 * one will be created, if an instance exists it will be returned. This is
	 * the singleton pattern. This method requires two parameters for
	 * initialization.
	 * 
	 * @param myZone
	 *            the AiZone containing perception information.
	 * @param ia
	 *            the AkbulutKupelioglu who owns this CollectMode. (for
	 *            checkInterruption())
	 * @return CollectMode instance.
	 * @throws StopRequestException
	 */
	public static CollectMode getInstance(AiZone myZone, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		setZone(myZone);
		zone = getZone();
		if(instance == null)
			instance = new CollectMode();
		return instance;

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







}
