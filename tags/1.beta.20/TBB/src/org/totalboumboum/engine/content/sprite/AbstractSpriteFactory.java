package org.totalboumboum.engine.content.sprite;

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

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.AbstractGesturePack;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractSpriteFactory<T extends Sprite, U extends AbstractGesturePack<?>>
{	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String name;

	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// GESTURE PACK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected U gesturePack;

	public U getGesturePack()
	{	return gesturePack;
	}
	
	public void setGesturePack(U gesturePack)
	{	this.gesturePack = gesturePack;
	}
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<AbstractAbility> abilities = new ArrayList<AbstractAbility>();

	public List<AbstractAbility> getAbilities()
	{	return abilities;
	}
	
	public void setAbilities(List<AbstractAbility> abilities)
	{	this.abilities = abilities;
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String explosionName;

	public String getExplosionName()
	{	return explosionName;
	}
	
	public void setExplosionName(String explosionName)
	{	this.explosionName = explosionName;
	}
}
