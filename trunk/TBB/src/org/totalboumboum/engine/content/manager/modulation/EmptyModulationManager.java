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

import java.util.List;

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.GeneralAppear;
import org.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EmptyModulationManager extends ModulationManager
{	
	public EmptyModulationManager(Sprite sprite)
	{	super(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// OTHER MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final OtherModulation otherModulation = new OtherModulation("");

	@Override
	public OtherModulation getOtherModulation(String name, Sprite modulated)
	{	return otherModulation;
	}

	/////////////////////////////////////////////////////////////////
	// ACTOR MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ActorModulation actorModulation = new ActorModulation(new GeneralAppear());

	@Override
	public ActorModulation getActorModulation(SpecificAction action)
	{	return actorModulation;
	}
	
	/////////////////////////////////////////////////////////////////
	// TARGET MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final TargetModulation targetModulation = new TargetModulation(new GeneralAppear());

	@Override
	public TargetModulation getTargetModulation(SpecificAction action)
	{	return targetModulation;
	}
	
	/////////////////////////////////////////////////////////////////
	// THIRD MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ThirdModulation thirdModulation = new ThirdModulation(new GeneralAppear());
	
	@Override
	public ThirdModulation getThirdModulation(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	return thirdModulation;
	}
	
	@Override
	public ThirdModulation getThirdModulation(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	return thirdModulation;
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ActionAbility actionAbility = new ActionAbility(new GeneralAppear());
	private final StateAbility stateAbility = new StateAbility("");
	
	@Override
	public ActionAbility modulateAction(SpecificAction action)
	{	//ActionAbility result = sprite.getAbility(action);
		return actionAbility;
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
	public boolean isThirdPreventing(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
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
		return stateAbility;
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
