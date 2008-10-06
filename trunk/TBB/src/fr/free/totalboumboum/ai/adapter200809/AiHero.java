package fr.free.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;

public class AiHero extends AiSprite<Hero>
{
	AiHero(AiTile tile, Hero sprite)
	{	super(tile,sprite);
		initColor();
		updateBombParam();
	}
	
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateBombParam();
	}

	@Override
	void finish()
	{	super.finish();
	}

	/////////////////////////////////////////////////////////////////
	// BOMB PARAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int bombRange;
	private int bombNumber;
	private int bombCount;
	
	public int getBombRange()
	{	return bombRange;
	}
	public int getBombNumber()
	{	return bombNumber;
	}
	public int getBombCount()
	{	return bombCount;
	}
	
	private void updateBombParam()
	{	Hero sprite = getSprite();
		// bomb range
		StateAbility ab = sprite.computeCapacity(StateAbility.BOMB_RANGE);
        bombRange = (int)ab.getStrength();
		// max number of simultaneous bombs
    	ab = sprite.computeCapacity(StateAbility.BOMB_NUMBER);
    	bombNumber = (int)ab.getStrength();
        // number of bombs currently dropped
    	bombCount = sprite.getDroppedBombs().size();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;
	
	public PredefinedColor getColor()
	{	return color;	
	}
	private void initColor()
	{	Hero sprite = getSprite();
		color = sprite.getColor();	
	}
}
