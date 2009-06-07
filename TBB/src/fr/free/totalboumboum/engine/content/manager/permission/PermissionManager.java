package fr.free.totalboumboum.engine.content.manager.permission;

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
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ActorPermission;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.StateModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.TargetPermission;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ThirdPermission;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class PermissionManager
{	private Sprite sprite;
	private String currentGesture;
	@SuppressWarnings("unused")
	private Direction currentDirection;
	private PermissionPack permissions;
	private ArrayList<AbstractAbility> modulationAbilities;
	
	public PermissionManager(Sprite sprite)
	{	this.sprite = sprite;
		permissions = new PermissionPack();
		currentGesture = GestureName.NONE;
		currentDirection = Direction.NONE;
		modulationAbilities = new ArrayList<AbstractAbility>();
	}
	
	public Level getLevel()
	{	return sprite.getLevel();
	}
	
	public void setGesture(String gesture, Direction spriteDirection)
	{	if(!currentGesture.equals(gesture))
			updateModulations(gesture);
		currentGesture = gesture;
		currentDirection = spriteDirection;
	}
	
	public void setPermissionPack(PermissionPack permissions)
	{	this.permissions = permissions;
		permissions.setSprite(sprite);
		updateModulations(currentGesture);
	}
	
	public StateModulation getModulation(StateModulation modulation)
	{	StateModulation result = permissions.getModulation(currentGesture, modulation);
		return result;
	}
	public ActorPermission getActorPermission(SpecificAction action)
	{	ActorPermission result = permissions.getActorPermission(currentGesture, action);
		return result;
	}
	public TargetPermission getTargetPermission(SpecificAction action)
	{	TargetPermission result = permissions.getTargetPermission(currentGesture, action);
		return result;
	}
	public ThirdPermission getThirdPermission(SpecificAction action)
	{	ThirdPermission result = permissions.getThirdPermission(currentGesture, action);
		return result;
	}
	
	/**
	 * calcule l'ability totale pour ce sprite s'il tente d'effectuer
	 * l'action passée en paramètre, en tenant compte de :
	 * 	- ses capacités
	 * 	- son auto-permission (actor)
	 * 	- la permission de la cible (target)
	 * 	- la permission de l'environnement (third, ici : la case ou les cases)
	 */
	public ActionAbility computeAbility(SpecificAction action)
	{	ActionAbility result = new ActionAbility(action.getGeneralAction(),getLevel());
//		AbstractSprite actor = action.getActor();
		Sprite target = action.getTarget();
		// capacity of the actor
		ActionAbility capacityAbility = computeCapacity(action);
		result = (ActionAbility)capacityAbility.copy();
		// permission of the actor
		if(result.isActive())
			combineActorPermission(action,result);
		// permission of the target (if there's one!)
		if(result.isActive() && target!=null && target.getCurrentGesture()!=null)
			combineTargetPermission(action,result);
		// permission of the environement
		if(result.isActive())
			combineThirdPermission(action,result);
		return result;
	}
	
	public ActionAbility computeCapacity(AbstractAction action)
	{	ActionAbility result;
		result = sprite.getAbility(action);
		return result;
	}
	
	public void combineActorPermission(SpecificAction specificAction, ActionAbility ability)
	{	GeneralAction action = specificAction.getGeneralAction();
		ActionAbility result = new ActionAbility(action,getLevel());
		Sprite actor = specificAction.getActor();
		ActorPermission actorPermission = actor.getActorPermission(specificAction);
		if(actorPermission==null)
		{	result.setStrength(0);
			result.setFrame(true);			
		}
		else
		{	result.setStrength(actorPermission.getStrength());
			result.setFrame(actorPermission.getFrame());
		}
		ability.combine(result);
	}

	public void combineTargetPermission(SpecificAction specificAction, ActionAbility ability)
	{	GeneralAction action = specificAction.getGeneralAction();
		ActionAbility result = new ActionAbility(action,getLevel());
		Sprite target = specificAction.getTarget();
		TargetPermission targetPermission = target.getTargetPermission(specificAction);
		if(targetPermission==null)
		{	result.setStrength(0);
			result.setFrame(true);			
		}
		else
		{	result.setStrength(targetPermission.getStrength());
			result.setFrame(targetPermission.getFrame());
		}
		ability.combine(result);
	}

	/**
	 * on se retreint aux cases contenant l'acteur et la cible, et on teste 
	 * chaque sprite.
	 */
	public void combineThirdPermission(SpecificAction specificAction, ActionAbility ability)
	{	Sprite actor = specificAction.getActor();
		Sprite target = specificAction.getTarget();
		// intransitive action
		if(target==null)
		{	Tile place = actor.getTile();
			if(place!=null)
				combineTilePermission(specificAction,ability,place);
		}
		// transitive action
		else
		{	Tile tileA = actor.getTile();
			if(tileA!=null) 
				combineTilePermission(specificAction,ability,tileA);
			Tile tileT = target.getTile();
			if(tileT!=null && tileT!=tileA && ability.isActive())
				combineTilePermission(specificAction,ability,target.getTile());
		}
	}

	/**
	 * calcule les permissions third au niveau d'une case donnée
	 */
	public void combineTilePermission(SpecificAction specificAction, ActionAbility ability, Tile tile)
	{	GeneralAction action = specificAction.getGeneralAction();
		ArrayList<Sprite> sprites = tile.getSprites();
		Iterator<Sprite> i = sprites.iterator();
		while(i.hasNext() && ability.isActive())
		{	Sprite tempSprite = i.next();
			if(tempSprite!=null && tempSprite!=specificAction.getActor() && tempSprite!=specificAction.getTarget())
			{	ActionAbility result = new ActionAbility(action,getLevel());
				ThirdPermission thirdPermission = tempSprite.getThirdPermission(specificAction);
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
	
	public StateAbility computeAbility(String name)
	{	StateAbility result = new StateAbility(name,getLevel());
		// capacity of the actor
		StateAbility capacityAbility = computeCapacity(name);
		result = (StateAbility)capacityAbility.copy();
		// modification du to the environement
		if(result.isActive())
			combineStateModulation(name,result);
		return result;
	}
	
	public StateAbility computeCapacity(String name)
	{	StateAbility result = sprite.getAbility(name);
		return result;
	}

	public void combineStateModulation(String name, StateAbility ability)
	{	Tile place = sprite.getTile();
		if(place!=null)
		{	ArrayList<Sprite> sprites = place.getSprites();
			Iterator<Sprite> i = sprites.iterator();
			while(i.hasNext() && ability.isActive())
			{	Sprite tempSprite = i.next();
				if(tempSprite!=null && tempSprite!=sprite)
				{	StateAbility result = new StateAbility(name,getLevel());
					StateModulation stateModulation = new StateModulation(name);
					stateModulation = tempSprite.getModulation(stateModulation);
					if(stateModulation!=null)
					{	result.setStrength(stateModulation.getStrength());
						result.setFrame(stateModulation.getFrame());
					}
					ability.combine(result);
				}
			}
		}
	}
	
	public void updateModulations(String newGesture)
	{	
/*		
if(sprite instanceof Item && sprite.getCurrentGesture()!=null && sprite.getCurrentGesture().equals("burning"))
	System.out.println();
*/		
		ArrayList<StateModulation> modulations = permissions.getModulations(newGesture);
		modulationAbilities.clear();
		Iterator<StateModulation> i = modulations.iterator();
		while(i.hasNext())
		{	StateModulation temp = i.next();
			StateAbility ab = new StateAbility(temp.getName(),getLevel());
			ab.setFrame(temp.getFrame());
			ab.setStrength(temp.getStrength());
			modulationAbilities.add(ab);
		}
	}
	public ArrayList<AbstractAbility> getModulationAbilities()
	{	return modulationAbilities;		
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// modulation abilities
			{	Iterator<AbstractAbility> it = modulationAbilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// permission pack
			permissions.finish();
			permissions = null;
			// misc
			currentDirection = null;
			sprite = null;
		}
	}
}
