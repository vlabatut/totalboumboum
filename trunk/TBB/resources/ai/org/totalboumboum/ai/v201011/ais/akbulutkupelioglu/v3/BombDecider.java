package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.CollectMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.Mode;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class BombDecider
{

	private static BombDecider instance = null;
	private static AkbulutKupelioglu monIA = null; 
	private static AiZone zone = null;
	
	
	private BombDecider() throws StopRequestException
	{
		monIA.checkInterruption();
	}
	
	public static BombDecider getInstance(AiZone myZone, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIA = ia;
		zone = myZone;
		if(instance==null)
			instance=new BombDecider();
		return instance;
	}
	
	public boolean willBomb() throws StopRequestException
	{
		monIA.checkInterruption();
		Mode mode = monIA.getMode();
		boolean intention = false;
		List<AiHero> enemies = zone.getHeroes();
		AiHero ownHero = zone.getOwnHero();
		enemies.remove(ownHero);
		
		
		//if we have a bomb on our tile, or cannot drop any more bombs, we won't attempt to.
		if(ownHero.getBombNumberCurrent()<ownHero.getBombNumberMax()&&ownHero.getTile().getBombs().size()==0)
		{
			
			List<AiTile> bombBlast = ownHero.getBombPrototype().getBlast();
	
			//if there's a destructible within range, we'd want to drop a bomb, if we are in collect mode
			if(mode instanceof CollectMode)
			{
				for(AiTile aiTile : bombBlast)
				{
					monIA.checkInterruption();
					if(!aiTile.getBlocks().isEmpty())
					{
						intention = true;
					}
				}
			}
			
			//however, if there's a bonus within range, we wouldn't want to destroy it, if we are in collect mode
			if(mode instanceof CollectMode)
			{
				for(AiTile aiTile : bombBlast)
				{
					monIA.checkInterruption();
					if(!aiTile.getItems().isEmpty())
					{
						intention = false;
					}
				}
			}
			
			//but, if there's an enemy within range, we would want to threaten it (with a bomb of course)
			ArrayList<AiHero> enemiesWithinRange = new ArrayList<AiHero>();
			for(AiTile aiTile : bombBlast)
			{
				monIA.checkInterruption();
				for(AiHero hero: aiTile.getHeroes())
				{
					monIA.checkInterruption();
					if(!(enemiesWithinRange.contains(hero)))
						enemiesWithinRange.add(hero);
				}
			}
			enemiesWithinRange.remove(ownHero);
			if(!enemiesWithinRange.isEmpty())
				intention = true;
		}
		return intention;
	}


	
}
