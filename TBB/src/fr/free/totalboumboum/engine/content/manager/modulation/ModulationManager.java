package fr.free.totalboumboum.engine.content.manager.modulation;

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
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.gesture.Gesture;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ActorModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.SelfModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class ModulationManager
{	
	public ModulationManager(Sprite sprite)
	{	this.sprite = sprite;
		currentGesture = null;
		currentDirection = Direction.NONE;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	public Level getLevel()
	{	return sprite.getLevel();
	}
	
	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Gesture currentGesture;
	@SuppressWarnings("unused")
	private Direction currentDirection;
	
	public void updateGesture(Gesture gesture, Direction spriteDirection)
	{	
//		if(!currentGesture.equals(gesture))
//			updateStateAbilities();
		currentGesture = gesture;
		currentDirection = spriteDirection;
	}
	
	/////////////////////////////////////////////////////////////////
	// SELF MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the self-modulation corresponding to the specified StateAbilityName.
	 * If the sprite has no gesture, or no modulation for this StateAbility
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	private SelfModulation getSelfModulation(StateAbilityName name)
	{	SelfModulation result = null;
		if(currentGesture!=null)
			result = currentGesture.getSelfModulation(name);
		if(result==null)
			result = new SelfModulation(name);
		return result;
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
	public OtherModulation getOtherModulation(StateAbilityName name, Sprite modulated)
	{	OtherModulation result = null;
		if(currentGesture!=null)
			result = currentGesture.getOtherModulation(name,sprite,modulated);
		if(result==null)
			result = new OtherModulation(name);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ACTOR MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the modulation corresponding to the specified SpecificAction.
	 * If the sprite has no gesture, or no modulation for this action
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public ActorModulation getActorModulation(SpecificAction action)
	{	ActorModulation result = null;
		if(currentGesture!=null)
			result = currentGesture.getActorModulation(action);
		if(result==null)
			result = new ActorModulation(action);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TARGET MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the modulation corresponding to the specified SpecificAction.
	 * If the sprite has no gesture, or no modulation for this action
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public TargetModulation getTargetModulation(SpecificAction action)
	{	TargetModulation result = null;
		if(currentGesture!=null)
			result = currentGesture.getTargetModulation(action);
		if(result==null)
			result = new TargetModulation(action);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// THIRD MODULATIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the modulation corresponding to the specified SpecificAction.
	 * If the sprite has no gesture, or no modulation for this action
	 * then a neutral modulation is returned.
	 * This method always returns a modulation.
	 */
	public ThirdModulation getThirdModulation(SpecificAction action)
	{	ThirdModulation result = null;
		if(currentGesture!=null)
			result = currentGesture.getThirdModulation(action);
		if(result==null)
			result = new ThirdModulation(action);
		return result;
	}
	
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
	public ActionAbility modulateAction(SpecificAction action)
	{	// actor original ability 
		//Sprite actor = action.getActor();
		ActionAbility result = sprite.getAbility(action);
		// actor modulation
		result = combineActorModulation(action,result);
		// target modulation (if there's one!)
		result = combineTargetModulation(action,result);
		// environement modulation
		result = combineThirdModulation(action,result);
		return result;
	}
	
	private ActionAbility combineActorModulation(SpecificAction action, ActionAbility ability)
	{	ActionAbility result = ability;
		//Sprite actor = action.getActor();
		if(result.isActive())
		{	ActorModulation actorModulation = getActorModulation(action);
			result = actorModulation.modulate(result);		
		}
		return result;
	}

	private ActionAbility combineTargetModulation(SpecificAction action, ActionAbility ability)
	{	ActionAbility result = ability;
		Sprite target = action.getTarget();
		if(result.isActive() && target!=null)
		{	TargetModulation targetModulation = target.getTargetModulation(action);
			result = targetModulation.modulate(result); 		
		}
		return result;
	}

	/**
	 * on se retreint aux cases contenant l'acteur et la cible, et on teste 
	 * chaque sprite.
	 */
	private ActionAbility combineThirdModulation(SpecificAction action, ActionAbility ability)
	{	ActionAbility result = ability;
		//Sprite actor = action.getActor();
		Sprite target = action.getTarget();
		if(result.isActive())
		{	// list of the involved sprites
			ArrayList<Sprite> sprites = new ArrayList<Sprite>();
			Tile tileA = sprite.getTile();
			if(tileA!=null)
			{	for(Sprite s: tileA.getSprites())
				{	if(s!=target && s!=sprite)
						sprites.add(s);					
				}
			}
			if(target!=null)
			{	Tile tileT = target.getTile();
				if(tileT!=null)
				{	for(Sprite s: tileT.getSprites())
					{	if(!sprites.contains(s) && s!=target && s!=sprite)
							sprites.add(s);					
					}
				}
			}
			// check each one of them
			Iterator<Sprite> i = sprites.iterator();
			while(i.hasNext() && result.isActive())
			{	Sprite tempSprite = i.next();
				ThirdModulation thirdModulation = tempSprite.getThirdModulation(action);
				result = thirdModulation.modulate(result); 		
			}
		}
		return result;
/*
		// intransitive action
		if(target==null)
		{	Tile place = actor.getTile();
			if(place!=null)
				combineTileModulation(specificAction,ability,place);
		}
		// transitive action
		else
		{	Tile tileA = actor.getTile();
			if(tileA!=null) 
				combineTileModulation(specificAction,ability,tileA);
			Tile tileT = target.getTile();
			if(tileT!=null && tileT!=tileA && ability.isActive())
				combineTileModulation(specificAction,ability,target.getTile());
		}
*/		
	}

	/**
	 * calcule les permissions third au niveau d'une case donnée
	 */
/*	public void combineTileModulation(SpecificAction specificAction, ActionAbility ability, Tile tile)
	{	GeneralAction action = specificAction.getGeneralAction();
		ArrayList<Sprite> sprites = tile.getSprites();
		Iterator<Sprite> i = sprites.iterator();
		while(i.hasNext() && ability.isActive())
		{	Sprite tempSprite = i.next();
			if(tempSprite!=null && tempSprite!=specificAction.getActor() && tempSprite!=specificAction.getTarget())
			{	ActionAbility result = new ActionAbility(action,getLevel());
				ThirdModulation thirdPermission = tempSprite.getThirdModulation(specificAction);
				if(thirdPermission==null)
				{	result.setStrength(0);
					result.setFrame(true);			
				}
				else
				{	result.setStrength(thirdPermission.getStrength());
					result.setFrame(thirdPermission.getFrame());
				}
				ability.combine(result);
			}
		}
	}
*/
	
	public StateAbility modulateStateAbility(StateAbilityName name)
	{	// original ability
		StateAbility result = sprite.getAbility(name);
		// self modulation
		result = combineSelfModulation(result);
		// environement modulation
		result = combineOtherModulation(result);
		return result;
	}	
		
	private StateAbility combineSelfModulation(StateAbility ability)
	{	StateAbility result = ability;
		if(result.isActive())
		{	SelfModulation selfModulation = getSelfModulation(ability.getName());
			result = selfModulation.modulate(result);		
		}
		return result;
	}
		
	/**
	 * on se retreint aux cases contenant l'acteur et la cible, et on teste 
	 * chaque sprite.
	 */
	private StateAbility combineOtherModulation(StateAbility ability)
	{	StateAbility result = ability;
		if(result.isActive())
		{	// list of the involved sprites
			ArrayList<Sprite> sprites = new ArrayList<Sprite>();
			Tile tile = sprite.getTile();
			if(tile!=null)
			{	for(Sprite s: tile.getSprites())
				{	if(s!=sprite)
						sprites.add(s);					
				}
			}
			// check each one of them
			Iterator<Sprite> i = sprites.iterator();
			while(i.hasNext() && result.isActive())
			{	Sprite tempSprite = i.next();
				OtherModulation otherModulation = tempSprite.getOtherModulation(ability.getName(),sprite);
				result = otherModulation.modulate(result); 		
			}
		}
		return result;
	}
	
/*	
	public StateAbility computeCapacity(String name)
	{	StateAbility result = sprite.getAbility(name);
		return result;
	}
*/
/*	private void combineStateModulation(String name, StateAbility ability)
	{	Tile place = sprite.getTile();
		if(place!=null)
		{	ArrayList<Sprite> sprites = place.getSprites();
			Iterator<Sprite> i = sprites.iterator();
			while(i.hasNext() && ability.isActive())
			{	Sprite tempSprite = i.next();
				if(tempSprite!=null && tempSprite!=sprite)
				{	StateAbility result = new StateAbility(name,getLevel());
					StateModulation stateModulation = new StateModulation(name);
					stateModulation = tempSprite.getStateModulation(stateModulation);
					if(stateModulation!=null)
					{	result.setStrength(stateModulation.getStrength());
						result.setFrame(stateModulation.getFrame());
					}
					ability.combine(result);
				}
			}
		}
	}
*/	
	/////////////////////////////////////////////////////////////////
	// MODULATION ABILITIES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** list of state abilities corresponding to state modulations, to be used by the ability manager */
//	private ArrayList<AbstractAbility> stateAbilities = new ArrayList<AbstractAbility>();

//	public void updateStateAbilities()
//	{	
/*		
if(sprite instanceof Item && sprite.getCurrentGesture()!=null && sprite.getCurrentGesture().equals("burning"))
	System.out.println();
*/		
//		ArrayList<StateModulation> modulations = currentGesture.getStateModulations();
//		stateAbilities.clear();
//		Iterator<StateModulation> i = modulations.iterator();
//		while(i.hasNext())
//		{	StateModulation temp = i.next();
//			StateAbility ab = new StateAbility(temp.getName(),getLevel());
//			ab.setFrame(temp.getFrame());
//			ab.setStrength(temp.getStrength());
//			stateAbilities.add(ab);
//		}
//	}
//	public ArrayList<AbstractAbility> getModulationStateAbilities()
//	{	return stateAbilities;		
//	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// modulation abilities
//			{	Iterator<AbstractAbility> it = stateAbilities.iterator();
//				while(it.hasNext())
//				{	AbstractAbility temp = it.next();
//					temp.finish();
//					it.remove();
//				}
//			}
			// misc
			currentDirection = null;
			sprite = null;
		}
	}
}
