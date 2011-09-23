package org.totalboumboum.engine.content.manager.explosion;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import org.totalboumboum.engine.content.feature.action.detonate.SpecificDetonate;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FullExplosionManager extends ExplosionManager
{	
	public FullExplosionManager(Sprite sprite)
	{	super(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public long getExplosionDuration()
	{	long result;
		Tile tile = sprite.getTile();
		Fire fire = explosion.makeFire(null,tile);
		Gesture gesture = fire.getGesturePack().getGesture(GestureName.BURNING);
		AnimeDirection anime = gesture.getAnimeDirection(Direction.LEFT);
		result = anime.getTotalDuration();
		return result;
	}
	
	/**
	 * create the explosion and returns the list of concerned tiles
	 * 
	 * @param fake	false if one just wants the tile list and not the actual explosion
	 * @return	the list of tiles to be put on fire
	 */
	@Override
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
			List<Tile> processed = new ArrayList<Tile>();
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
	
	// NOTE workaround needed by the AI API
	public boolean isPenetrating()
	{	boolean result = false;
		Tile tile = sprite.getTile();
		Fire fire = explosion.makeFire("outside",tile);
		fire.getAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ExplosionManager copy(Sprite sprite)
	{	ExplosionManager result = new FullExplosionManager(sprite);
		result.explosion = explosion;
		return result;
	}
}
