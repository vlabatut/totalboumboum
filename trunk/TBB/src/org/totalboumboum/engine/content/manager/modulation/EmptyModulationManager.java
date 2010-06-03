package org.totalboumboum.engine.content.manager.modulation;

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

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import org.totalboumboum.engine.content.sprite.Sprite;

public class EmptyModulationManager extends ModulationManager
{	
	public EmptyModulationManager(Sprite sprite)
	{	super(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// OTHER MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public OtherModulation getOtherModulation(String name, Sprite modulated)
	{	OtherModulation result = null;
		if(result==null)
			result = new OtherModulation(name);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ACTOR MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ActorModulation getActorModulation(SpecificAction action)
	{	ActorModulation result = null;
		if(result==null)
			result = new ActorModulation(action);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TARGET MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public TargetModulation getTargetModulation(SpecificAction action)
	{	TargetModulation result = null;
		if(result==null)
			result = new TargetModulation(action);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// THIRD MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ThirdModulation getThirdModulation(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	ThirdModulation result = null;
		if(result==null)
			result = new ThirdModulation(action);
		return result;
	}
	
	@Override
	public ThirdModulation getThirdModulation(GeneralAction action, ArrayList<AbstractAbility> actorProperties, ArrayList<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	ThirdModulation result = null;
		if(result==null)
			result = new ThirdModulation(action);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ActionAbility modulateAction(SpecificAction action)
	{	//ActionAbility result = sprite.getAbility(action);
ActionAbility result = new ActionAbility(action);
		return result;
	}
	
	/**
	 * check if this sprite modulation is preventing the specified action to happen
	 */
	@Override
	public boolean isThirdPreventing(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	boolean result = false;
		return result;
	}
	
	@Override
	public boolean isThirdPreventing(GeneralAction action, ArrayList<AbstractAbility> actorProperties, ArrayList<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	boolean result = false;
		return result;
	}
	
	@Override
	public boolean isTargetPreventing(SpecificAction action)
	{	boolean result = false;
		return result;
	}
	
	public StateAbility modulateStateAbility(String name)
	{	//StateAbility result = sprite.getAbility(name);
StateAbility result = new StateAbility(name);
		return result;
	}	

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ModulationManager copy(Sprite sprite)
	{	ModulationManager result = new EmptyModulationManager(sprite);
		return result;
	}
}
