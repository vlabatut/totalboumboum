package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode.CollectMode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.mode.Mode;
import org.totalboumboum.engine.content.feature.Direction;


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
	
			List<AiTile> rangeTiles = getBombRange(true, ownHero); 
			List<AiTile> rangeTilesDestructible = getBombRange(false, ownHero);
	
			//show destructible range
			for(AiTile aiTile : rangeTilesDestructible)
			{
				monIA.checkInterruption();
				monIA.getOutput().setTileColor(aiTile, Color.ORANGE);
			}
			//show floor range
			for(AiTile aiTile : rangeTiles)
			{
				monIA.checkInterruption();
				monIA.getOutput().setTileColor(aiTile, Color.RED);
			}
	
			//if there's a destructible within range, we'd want to drop a bomb, if we are in collect mode
			//if(mode instanceof CollectMode)
			//{
				for(AiTile aiTile : rangeTilesDestructible)
				{
					monIA.checkInterruption();
					if(!aiTile.getBlocks().isEmpty())
					{
						intention = true;
					}
				}
			//}
			
			//however, if there's a bonus within range, we wouldn't want to destroy it, if we are in collect mode
			if(mode instanceof CollectMode)
			{
				for(AiTile aiTile : rangeTilesDestructible)
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
			for(AiTile aiTile : rangeTiles)
			{
				monIA.checkInterruption();
				for(AiHero hero: aiTile.getHeroes())
					if(!(enemiesWithinRange.contains(hero)||hero.equals(ownHero)))
						enemiesWithinRange.add(hero);
			}
			if(!enemiesWithinRange.isEmpty())
				intention = true;
		}
		return intention;
	}

	public static List<AiTile> getBombRange(boolean walls, AiHero ownHero) throws StopRequestException
	{
		//TODO:gözden geçir ki, bonus falan geldiğinde düzgün işlesin
		monIA.checkInterruption();
		int range = ownHero.getBombRange();
		AiTile ownTile = ownHero.getTile();
		List<AiTile> rangeTiles = new ArrayList<AiTile>();
		rangeTiles.add(ownTile);
		Direction[] directions = {Direction.DOWN, Direction.RIGHT, Direction.UP, Direction.LEFT};
		
		for(int i = 0; i<4; i++)
		{
			monIA.checkInterruption();
			AiTile directNeighbor = ownTile.getNeighbor(directions[i]);
			if(directNeighbor.isCrossableBy(ownHero))
			{
				for(int j=0; j<range-1; j++)
				{
					monIA.checkInterruption();
					AiTile neighbor = directNeighbor.getNeighbor(directions[i]);
					if(walls)
					{
						if(!neighbor.isCrossableBy(ownHero))
							j=range;
						else
							rangeTiles.add(neighbor);
					}else
					{
						if(neighbor.isCrossableBy(ownHero))
						{
							rangeTiles.add(neighbor);
						}else
						{
							if(!neighbor.getBlocks().isEmpty())
							{
								if(neighbor.getBlocks().get(0).isDestructible())
									rangeTiles.add(neighbor);
								else
									j=range;
							}else
							{
								j=range;
							}
						}
					}
				}
				rangeTiles.add(directNeighbor);
			}else
			{
				if(directNeighbor.getBlocks().size()>0)
					if(directNeighbor.getBlocks().get(0).isDestructible()&&!walls)
						rangeTiles.add(directNeighbor);
						
			}
		}
		return rangeTiles;
	}



	
}
