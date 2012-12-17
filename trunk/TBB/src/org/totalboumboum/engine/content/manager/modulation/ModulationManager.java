package org.totalboumboum.engine.content.manager.modulation;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
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
public abstract class ModulationManager
{	
	public ModulationManager(Sprite sprite)
	{	this.sprite = sprite;
		currentGesture = null;
		currentDirection = Direction.NONE;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;
	
	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Gesture currentGesture;
	protected Direction currentDirection;
	
	public void updateGesture(Gesture gesture, Direction spriteDirection)
	{	
//		if(!currentGesture.equals(gesture))
//			updateStateAbilities();
		currentGesture = gesture;
		currentDirection = spriteDirection;
	}
	
	/////////////////////////////////////////////////////////////////
	// OTHER MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the state ability modulation corresponding to the specified StateAbilityName.
	 * If the sprite has no gesture, or no modulation for this StateAbility
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public abstract OtherModulation getOtherModulation(String name, Sprite modulated);

	/////////////////////////////////////////////////////////////////
	// ACTOR MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the modulation corresponding to the specified SpecificAction.
	 * If the sprite has no gesture, or no modulation for this action
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public abstract ActorModulation getActorModulation(SpecificAction action);
	
	/////////////////////////////////////////////////////////////////
	// TARGET MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the modulation corresponding to the specified SpecificAction.
	 * If the sprite has no gesture, or no modulation for this action
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public abstract TargetModulation getTargetModulation(SpecificAction action);
	
	/**
	 * Like {@link #getTargetModulation(SpecificAction)}, but for a
	 * gesture which is not necessarily the current one.
	 */
	public abstract TargetModulation getTargetModulation(SpecificAction action, GestureName gestureName);

	/////////////////////////////////////////////////////////////////
	// THIRD MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the modulation corresponding to the specified SpecificAction.
	 * If the sprite has no gesture, or no modulation for this action
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public abstract ThirdModulation getThirdModulation(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances);

	public abstract ThirdModulation getThirdModulation(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances);

	public abstract ThirdModulation getThirdModulation(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances, GestureName gestureName);

	/////////////////////////////////////////////////////////////////
	// ABILITIES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * process the total ability for this action, considering:
	 * 	- actor original ability
	 * 	- actor modulation (depending on its current gesture)
	 * 	- target modulation (same thing, and only if the target exists)
	 * 	- environment modulation (considering all sprites in the actor and target tiles) 
	 */
	public abstract ActionAbility modulateAction(SpecificAction action);
	
	/**
	 * check if this sprite modulation is preventing the specified action to happen
	 */
	public abstract boolean isThirdPreventing(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances);

	public abstract boolean isThirdPreventing(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances);

	public abstract boolean wouldThirdPreventing(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances, GestureName gestureName);
	
	public abstract boolean isTargetPreventing(SpecificAction action);
	
	public abstract boolean wouldTargetPreventing(SpecificAction action, GestureName gestureName);
	
	public abstract StateAbility modulateStateAbility(String name);

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract ModulationManager copy(Sprite sprite);
}
