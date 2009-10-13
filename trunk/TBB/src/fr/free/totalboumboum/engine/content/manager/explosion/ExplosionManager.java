package fr.free.totalboumboum.engine.content.manager.explosion;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.configuration.RoundVariables;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.action.detonate.SpecificDetonate;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

public class ExplosionManager
{	
	public ExplosionManager(Sprite sprite)
	{	this.sprite = sprite;
		explosion = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLAME RANGE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected int flameRange;
	
	public int getFlameRange()
	{	return flameRange;
	}
	
	public void setFlameRange(int flameRange)
	{	this.flameRange = flameRange;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Explosion explosion;
	
	public void setExplosion(Explosion explosion)
	{	this.explosion = explosion;	
	}
	
	public Explosion getExplosion()
	{	return explosion;	
	}
	
	/**
	 * create the explosion and returns the list of concerned tiles
	 * 
	 * @param fake	false if one just wants the tile list and not the actual explosion
	 * @return	the list of tiles to be put on fire
	 */
	public List<Tile> makeExplosion(boolean fake)
	{	// init
		List<Tile> result = new ArrayList<Tile>();
		Tile tile = sprite.getTile();
		Sprite owner;
		if(sprite.getOwner()==null)
			owner = sprite;
		else
			owner = sprite.getOwner();
		
		// center
		{	Fire fire;
			if(flameRange==0)
				fire = explosion.makeFire("outside",tile);
			else
				fire = explosion.makeFire("inside",tile);
			fire.setOwner(owner);
			SpecificAction specificAction = new SpecificAppear(fire,Direction.NONE);
			AbstractAbility ability = fire.modulateAction(specificAction);
			if(!fake)
			{	if(!ability.isActive())
				{	fire.consumeTile(tile);
				}
				else
				{	RoundVariables.level.insertSpriteTile(fire);
					SpecificDetonate detonateAction = new SpecificDetonate(sprite,Direction.NONE);
					ActionEvent evt = new ActionEvent(detonateAction);
					fire.processEvent(evt);
				}
			}
			result.add(tile);
		}
		
		// branches
		{	boolean blocked[] = {false,false,false,false};
			Tile tiles[] = {tile,tile,tile,tile};
			Direction directions[] = {Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP};
			ArrayList<Tile> processed = new ArrayList<Tile>();
			processed.add(tile);
			boolean goOn = true;
			int length = 1;
			while(goOn && length<=flameRange)
			{	goOn = false;
				// increase the explosion
				for(int i=0;i<directions.length;i++)
				{	if(!blocked[i])
					{	// get the tile
						Direction direction = directions[i];
						Tile tempTile = tiles[i].getNeighbor(direction);
						tiles[i] = tempTile;
						if(!processed.contains(tempTile))
						{	processed.add(tempTile);
							Fire fire;
							if(length==flameRange)
								fire = explosion.makeFire("outside",tempTile); //TODO remplacer ces chaines de caractères par des valeurs énumérées
							else
								fire = explosion.makeFire("inside",tempTile);
							fire.setOwner(owner);
							SpecificAction specificAction = new SpecificAppear(fire,direction);
							AbstractAbility ability = fire.modulateAction(specificAction);
							blocked[i] = !ability.isActive();
							if(!fake)
							{	if(blocked[i])
								{	fire.consumeTile(tempTile);
									//blocked[i] = true;
								}
								else
								{	goOn = true;
									RoundVariables.level.insertSpriteTile(fire);
									SpecificDetonate detonateAction = new SpecificDetonate(sprite,direction);
									ActionEvent evt = new ActionEvent(detonateAction);
									fire.processEvent(evt);
								}
							}
							else
							{	goOn = goOn || !blocked[i];
							}
							result.add(tempTile);
						}
					}
				}
				length++;
			}
		}
		
		return result;
	}	
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// explosion
if(explosion==null)
	System.out.println(sprite.getName());
			explosion.finish();
			explosion = null;
			// misc
			sprite = null;
		}
	}
}
