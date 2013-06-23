package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.mode.CollectMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.mode.Mode;


/**
 * Decides whether or not it's profitable to drop a bomb.
 * @author yasa
 *
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
	
	/**
	 * Gets the BombDecider instance.
	 * @param myZone The zone.
	 * @param ia The AkbulutKupeloglu using this.
	 * @return The BombDecider instance.
	 * @throws StopRequestException
	 */
	public static BombDecider getInstance(AiZone myZone, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIA = ia;
		zone = myZone;
		if(instance==null)
			instance=new BombDecider();
		return instance;
	}
	
	/**
	 * Decides whether or not the AI would want to drop a bomb.
	 * @return The decision to drop a bomb.
	 * @throws StopRequestException
	 */
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
