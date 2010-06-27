package org.totalboumboum.engine.content.manager.ability;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbilityManager
{	
	public AbilityManager(Sprite sprite)
	{	this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}

/*	
	public boolean hasAbility(AbstractAbility ability)
	{	return dynamicAbilities.contains(ability);
	}
*/
	/////////////////////////////////////////////////////////////////
	// DIRECT ABILITIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilities provided before the begining of the round */
	protected final List<AbstractAbility> directAbilities = new ArrayList<AbstractAbility>();
	
	public abstract void addDirectAbilities(List<AbstractAbility> abilities);
	
	public abstract void addDirectAbility(AbstractAbility ability);
	
	public List<AbstractAbility> getDirectAbilities()
	{	return directAbilities;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT ABILITIES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** all abilities (direct and items) */
	protected final List<AbstractAbility> currentAbilities = new ArrayList<AbstractAbility>();
	
	public abstract StateAbility getAbility(String name);
	
/*	public StateAbility getAbility(StateAbility ability)
	{	return getAbility(ability.getName());
	}
*/
	
	public abstract ActionAbility getAbility(SpecificAction action);
	
	public abstract ActionAbility getAbility(GeneralAction action);

	/**
	 *  current abilities :
	 *  	- directAbilities
	 *  	- itemAbilities
	 */
	public abstract void update();
	
	public abstract void modifyUse(AbstractAbility ability, int delta);

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract AbilityManager copy(Sprite sprite);
}
