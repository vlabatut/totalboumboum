package org.totalboumboum.engine.content.manager.ability;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
public class EmptyAbilityManager extends AbilityManager
{	
	public EmptyAbilityManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// DIRECT ABILITIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void addDirectAbilities(List<AbstractAbility> abilities)
	{	
		// useless here
	}
	
	@Override
	public void addDirectAbility(AbstractAbility ability)
	{	
		// useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT ABILITIES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public StateAbility getAbility(String name)
	{	StateAbility result = new StateAbility(name);
		return result;
	}

	@Override
	public List<StateAbility> getAbilitiesStartingWith(String name)
	{	List<StateAbility> result = new ArrayList<StateAbility>();
		return result;
	}
	
	@Override
	public ActionAbility getAbility(SpecificAction action)
	{	ActionAbility result = new ActionAbility(action);
		return result;
	}
	
	@Override
	public ActionAbility getAbility(GeneralAction action)
	{	ActionAbility result = new ActionAbility(action);
		return result;
	}

	@Override
	public void update()
	{	
		// useless here
	}
	
	@Override
	public void modifyUse(AbstractAbility ability, int delta)
	{	
		// useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AbilityManager copy(Sprite sprite)
	{	AbilityManager result = new EmptyAbilityManager(sprite);
		return result;
	}
}
