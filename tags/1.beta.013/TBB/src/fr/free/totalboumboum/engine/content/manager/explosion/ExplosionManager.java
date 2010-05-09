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

import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
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
	
	public void putExplosion()
	{	Tile tile = sprite.getTile();
		// center
//NOTE tester l'autorisation d'apparition pour le centre comme on le fait pour les autres parties de l'explosion ?		
		Fire fire;
		if(flameRange==0)
			fire = explosion.makeFire("outside");
		else
			fire = explosion.makeFire("inside");
		Sprite owner;
		if(sprite.getOwner()==null)
			owner = sprite;
		else
			owner = sprite.getOwner();
		fire.setOwner(owner);
		fire.initGesture();
		tile.addSprite(fire);
		fire.setCurrentPosX(tile.getPosX());
		fire.setCurrentPosY(tile.getPosY());
		fire.setGesture(GestureName.BURNING, Direction.NONE, Direction.NONE, true);
		// branches
		if(flameRange>0)
		{	int hFlameRange = flameRange;
			int vFlameRange = flameRange;
			int levelWidth = GameVariables.level.getGlobalWidth();
			int levelHeight = GameVariables.level.getGlobalHeight();
			if(flameRange>=levelWidth)
				hFlameRange = levelWidth-1;
			if(flameRange>=levelHeight)
				vFlameRange = levelHeight-1;
			makeBranch(vFlameRange,Direction.DOWN);
			makeBranch(hFlameRange,Direction.LEFT);
			makeBranch(hFlameRange,Direction.RIGHT);
			makeBranch(vFlameRange,Direction.UP);
		}
	}	
	
	private void makeBranch(int power, Direction dir)
	{	Tile tile = sprite.getTile();
		int length = 1;
		boolean blocking;		
		Tile tileTemp = tile.getNeighbor(dir);
		SpecificAction specificAction;
		do
		{	Fire fire;
			if(length==power)
				fire = explosion.makeFire("outside"); //TODO remplacer ces chaines de caractères par des valeurs énumérées
			else
				fire = explosion.makeFire("inside");
			Sprite owner;
			if(sprite.getOwner()==null)
				owner = sprite;
			else
				owner = sprite.getOwner();
			fire.setOwner(owner);
			specificAction = new SpecificAppear(fire,tileTemp,dir);
			AbstractAbility ability = fire.modulateAction(specificAction);
			blocking = !ability.isActive();
			if(blocking)
				fire.consumeTile(tileTemp);
			else
			{	fire.initGesture();
				tileTemp.addSprite(fire);
				fire.setCurrentPosX(tileTemp.getPosX());
				fire.setCurrentPosY(tileTemp.getPosY());
				fire.setGesture(GestureName.BURNING,dir,dir, true);
				length++;
				tileTemp = tileTemp.getNeighbor(dir);
			}
		}
		while(!blocking && length<=power);
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
