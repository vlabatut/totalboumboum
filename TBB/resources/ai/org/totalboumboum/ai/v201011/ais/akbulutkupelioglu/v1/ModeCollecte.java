package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v1;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

import java.util.Iterator;
import java.util.List;

public class ModeCollecte extends Mode {

	private Matrix interet;
	private static AiZone zone;
	private static AkbulutKupelioglu monIa;
	private static ModeCollecte instance = null;
	
	
	private static final int BONUS_CONST = 900;
	@SuppressWarnings("unused")
	private static final int MUR_DESTR = 200;
	
	private ModeCollecte(AiZone zone) throws StopRequestException
	{
		monIa.checkInterruption();
		
		int height = zone.getHeight();
		int width = zone.getWidth();
		
		interet = new Matrix(width, height, monIa);
	}
	
	public static ModeCollecte getInstance(AiZone myZone, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		zone = myZone;		
		if(instance==null)
			instance = new ModeCollecte(myZone);
		return instance;
			
	}
	
	public Matrix calculateMatrix() throws StopRequestException
	{
		monIa.checkInterruption();		
		
		//place the bonus
		List<AiItem> bonus = zone.getItems();
		for (Iterator<AiItem> iterator = bonus.iterator(); iterator.hasNext();) {
			monIa.checkInterruption();
			
			AiItem aiItem = (AiItem) iterator.next();
			int x = aiItem.getLine();
			int y = aiItem.getCol();
			int bonusPenalty = 0;
			//TODO: Review distances
			for (AiHero hero : zone.getRemainingHeroes()) {
				double distance = zone.getPixelDistance(hero, aiItem);
				if(hero==zone.getOwnHero())
					bonusPenalty += distance;
				else
				{
					double distance2 = zone.getPixelDistance(zone.getOwnHero(), aiItem);
					if (distance<distance2) {
						bonusPenalty += distance2-distance;
					}
				}
			}
			interet.setElement(x, y, BONUS_CONST-bonusPenalty);
			
		}
		return interet;
	}


		
	
	
}
