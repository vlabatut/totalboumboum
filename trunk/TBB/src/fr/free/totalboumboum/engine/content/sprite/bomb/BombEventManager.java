package fr.free.totalboumboum.engine.content.sprite.bomb;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.feature.action.detonate.SpecificDetonate;
import fr.free.totalboumboum.engine.content.feature.action.land.SpecificLand;
import fr.free.totalboumboum.engine.content.feature.action.punch.SpecificPunch;
import fr.free.totalboumboum.engine.content.feature.action.push.SpecificPush;
import fr.free.totalboumboum.engine.content.feature.action.trigger.SpecificTrigger;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class BombEventManager extends EventManager
{	
	HashMap<Direction,TreeSet<Sprite>> collidingSprites;
	
	public BombEventManager(Bomb sprite)
	{	super(sprite);
		collidingSprites = new HashMap<Direction, TreeSet<Sprite>>();
		collidingSprites.put(Direction.DOWN,new TreeSet<Sprite>());
		collidingSprites.put(Direction.LEFT,new TreeSet<Sprite>());
		collidingSprites.put(Direction.RIGHT,new TreeSet<Sprite>());
		collidingSprites.put(Direction.UP,new TreeSet<Sprite>());
	}

	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificConsume)
			actionConsume(event);
		else if(event.getAction() instanceof SpecificPunch)
			actionPunch(event);
		else if(event.getAction() instanceof SpecificTrigger)
			actionTrigger(event);
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.APPEARING)
			|| gesture.equals(GestureName.OSCILLATING) || gesture.equals(GestureName.OSCILLATING_FAILING) 
			|| gesture.equals(GestureName.SLIDING) || gesture.equals(GestureName.SLIDING) 
			|| gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.STANDING_FAILING))
		{	StateAbility a = sprite.modulateStateAbility(StateAbilityName.BOMB_EXPLOSION_LATENCY);
			if(a.isActive())
			{	double duration = a.getStrength();
				String delayName = DelayManager.DL_LATENCY;
				if(sprite.hasDelay(DelayManager.DL_LATENCY))
				{	double lat = sprite.getDelay(DelayManager.DL_LATENCY);	
					if(duration<lat)
						sprite.addDelay(delayName,duration);
				}
				else
					sprite.addDelay(delayName,duration);
			}
			else
			{	gesture = GestureName.BURNING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				sprite.putExplosion();
			}
		}
	}
	
	private void actionTrigger(ActionEvent event)
	{	if(gesture.equals(GestureName.OSCILLATING) || gesture.equals(GestureName.OSCILLATING_FAILING) 
			|| gesture.equals(GestureName.SLIDING) || gesture.equals(GestureName.SLIDING_FAILING) 
			|| gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.STANDING_FAILING))
		{	long r = Math.round(Math.random()*101);
			StateAbility b = sprite.modulateStateAbility(StateAbilityName.BOMB_FAILURE_PROBABILITY);
			if(b.isActive())
			{	double value = b.getStrength();
				// if there's a failure :  no explosion
				if(r<=value)
				{	b.setStrength(0);
					b.setFrame(true);
				}
			}
			else
			{	gesture = GestureName.BURNING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				sprite.putExplosion();							
			}
		}
	}
	
	private void actionPunch(ActionEvent event)
	{	if(gesture.equals(GestureName.OSCILLATING) || gesture.equals(GestureName.OSCILLATING_FAILING)
			|| gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.STANDING_FAILING)
			|| gesture.equals(GestureName.SLIDING) || gesture.equals(GestureName.SLIDING_FAILING)) 
		{	gesture = GestureName.PUNCHED;
			spriteDirection = event.getAction().getDirection();
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTROL EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ControlEvent event)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// ENGINE EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(EngineEvent event)
	{	if(event.getName().equals(EngineEvent.ANIME_OVER))
			engAnimeOver(event);
		else if(event.getName().equals(EngineEvent.COLLIDED_OFF))
			engCollidedOff(event);
		else if(event.getName().equals(EngineEvent.COLLIDED_ON))
			engCollidedOn(event);
		else if(event.getName().equals(EngineEvent.COLLIDING_ON))
			engCollidingOn(event);
		else if(event.getName().equals(EngineEvent.DELAY_OVER))
			engDelayOver(event);
		else if(event.getName().equals(EngineEvent.TRAJECTORY_OVER))
			engTrajectoryOver(event);
		else if(event.getName().equals(EngineEvent.ROUND_ENTER))
			engEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_START))
			engStart(event);
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.APPEARING))
		{	stand();
		}
		else if(gesture.equals(GestureName.BURNING))
		{	endSprite();
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void engCollidingOn(EngineEvent event)
	{	// bouncing : bouncing on the obstacle
		if(gesture.equals(GestureName.BOUNCING))
		{	spriteDirection = spriteDirection.getOpposite();
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
		}
		// sliding : the bomb stops in the center of the tile
		else if(gesture.equals(GestureName.SLIDING) || gesture.equals(GestureName.SLIDING_FAILING))
		{	gesture = GestureName.STANDING;
			spriteDirection = Direction.NONE;
			sprite.center();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			if(ability.isActive())
			{	double duration = ability.getStrength();
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
			}
			else
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
	
	private void engCollidedOff(EngineEvent event)
	{	// oscillating : the bomb stops oscillating
		if(gesture.equals(GestureName.OSCILLATING) || gesture.equals(GestureName.OSCILLATING_FAILING))
		{	Sprite actor = event.getSource();
			Direction dir = event.getDirection();
			collidingSprites.get(dir).remove(actor);
			// if no more sprites pushing the bomb in this direction
			if(collidingSprites.get(dir).size()==0)
			{	String delayName = DelayManager.DL_OSCILLATION+":"+dir;
				sprite.removeDelay(delayName);
				// if no more sprites pushing the at all
				if(collidingSprites.get(Direction.DOWN).size()==0
						&& collidingSprites.get(Direction.LEFT).size()==0
						&& collidingSprites.get(Direction.RIGHT).size()==0
						&& collidingSprites.get(Direction.UP).size()==0)
				{	if(gesture.equals(GestureName.OSCILLATING))
					{	gesture = GestureName.STANDING;
						StateAbility ability = sprite.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
						if(ability.isActive())
						{	double duration = ability.getStrength();
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
						}
						else
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
					}
					else if(gesture.equals(GestureName.OSCILLATING_FAILING))
					{	gesture = GestureName.STANDING_FAILING;
						sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
					}
				}
			}
		}		
	}

	private void engCollidedOn(EngineEvent event)
	{	// standing : the bomb starts oscillating
		if(gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.STANDING_FAILING)
				|| gesture.equals(GestureName.OSCILLATING) || gesture.equals(GestureName.OSCILLATING_FAILING))
		{	// check if the actor has the ability to push the bomb
			Sprite actor = event.getSource();
			Direction dir = event.getDirection();
			SpecificAction action = new SpecificPush(actor,sprite);
			ActionAbility a = actor.modulateAction(action);
			if(a.isActive())
			{	collidingSprites.get(dir).add(actor);
				String delayName = DelayManager.DL_OSCILLATION+":"+dir;
				StateAbility b = sprite.modulateStateAbility(StateAbilityName.BOMB_PUSH_LATENCY);
				double dur = b.getStrength();
				// check if the bomb was already collided in the same direction
				if(sprite.hasDelay(delayName))
				{	sprite.addDelay(delayName, dur);
					gesture = GestureName.OSCILLATING;
					spriteDirection = dir;
					sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
					StateAbility ability = sprite.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
					if(ability.isActive())
					{	double duration = ability.getStrength();
						sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
					}
					else
						sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				}
			}
		}		
	}

	private void engDelayOver(EngineEvent event)
	{	// could not enter at first, but can now appear (i.e. this happens after the begining of the round)
		if(gesture.equals(GestureName.NONE) && event.getStringParameter().equals(DelayManager.DL_ENTER))
		{	SpecificAction action = new SpecificAppear(sprite);
			ActionAbility actionAbility = sprite.modulateAction(action);
			// can appear >> appears
			if(actionAbility.isActive())
			{	appear();
			}
			// cannot appear >> wait for next iteration
			else
			{	sprite.addIterDelay(DelayManager.DL_ENTER,1);
			}
		}		
		// regular explosion
		else if(event.getStringParameter().equals(DelayManager.DL_EXPLOSION))
		{	SpecificAction action = new SpecificDetonate(sprite);
			ActionAbility ablt = sprite.modulateAction(action);
			if(ablt.isActive())
			{	// is it a time bomb ?
				StateAbility a = sprite.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
				if(a.isActive())
				{	// is the bomb working ?
					if(gesture.equals(GestureName.SLIDING) 
							|| gesture.equals(GestureName.STANDING) 
							|| gesture.equals(GestureName.OSCILLATING))
					{	// is there a failure ?
						long r = Math.round(Math.random()*101);
						StateAbility b = sprite.modulateStateAbility(StateAbilityName.BOMB_FAILURE_PROBABILITY);
						double value = b.getStrength();
						// if there's a failure :  no explosion
						if(r<=value)
						{	double regularTime = a.getStrength();
							//
							b = sprite.modulateStateAbility(StateAbilityName.BOMB_FAILURE_MINDURATION);
							double minD = b.getStrength();
							if(minD==0)
								minD = regularTime;
							//
							b = sprite.modulateStateAbility(StateAbilityName.BOMB_FAILURE_MAXDURATION);
							double maxD = b.getStrength();
							if(maxD==0)
								maxD = regularTime;
							//
							double failureDuration = Math.random()*(maxD-minD)+minD;
							sprite.addDelay(DelayManager.DL_EXPLOSION,failureDuration);
							gesture = GestureName.valueOf(gesture.toString()+"_FAILING");
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
						}
						// else : explosion
						else
						{	gesture = GestureName.BURNING;
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
							sprite.putExplosion();							
						}
					}
					// is the bomb failing ?
					else if(gesture.equals(GestureName.SLIDING_FAILING) || gesture.equals(GestureName.STANDING_FAILING) || gesture.equals(GestureName.OSCILLATING_FAILING))
					{	// explosion
						gesture = GestureName.BURNING;
						sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
						sprite.putExplosion();							
					}
				}
			}
			else
			{
				//NOTE pr�voir le cas o� la bombe ne peut pas p�ter, il faut le remettre au prochain instant
				// sauf que certains �tats pr�voient une r�init du timer au changement d'�tat (ex : bouncing)
				// >> en fait pas ds toutes les instances. -> mettre un param�tre (ability)
			}
		}
		// flame-caused explosion
		else if(event.getStringParameter().equals(DelayManager.DL_LATENCY))
		{	if(gesture.equals(GestureName.SLIDING) || gesture.equals(GestureName.OSCILLATING) || gesture.equals(GestureName.STANDING)
				|| gesture.equals(GestureName.SLIDING_FAILING) || gesture.equals(GestureName.OSCILLATING_FAILING) || gesture.equals(GestureName.STANDING_FAILING))
			{	SpecificAction action = new SpecificDetonate(sprite);
				ActionAbility ablt = sprite.modulateAction(action);
				if(ablt.isActive())
				{	gesture = GestureName.BURNING;
					sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
					sprite.putExplosion();							
				}
			}
		}
	}

	private void engTrajectoryOver(EngineEvent event)
	{	// the sprite is currently bouncing
		if(gesture.equals(GestureName.BOUNCING))
		{	SpecificAction specificAction = new SpecificLand(sprite);
			ActionAbility ability = sprite.modulateAction(specificAction);
			// the sprite is allowed to land
			if(ability.isActive())
			{	gesture = GestureName.LANDING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			// the sprite is not allowed to land
			else
			{	gesture = GestureName.BOUNCING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
		}
		// the sprite is landing
		else if(gesture.equals(GestureName.LANDING))
		{	//spriteDirection = Direction.NONE;
			stand();
		}
		// the sprite has been punched
		else if(gesture.equals(GestureName.PUNCHED))
		{	SpecificAction action = new SpecificLand(sprite);
			ActionAbility a = sprite.modulateAction(action);
			// the sprite is allowed to land
			if(a.isActive())
				gesture = GestureName.LANDING;
			else
				gesture = GestureName.BOUNCING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);			
		}
	}
	
	private void engEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE))
		{	spriteDirection = event.getDirection();
			SpecificAction action = new SpecificAppear(sprite);
			ActionAbility actionAbility = sprite.modulateAction(action);
			// can appear >> appears
			if(actionAbility.isActive())
			{	gesture = GestureName.ENTERING;
				StateAbility stateAbility = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
				double duration = stateAbility.getStrength();
				if(duration<=0)
					duration = 1;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
			}
			// cannot appear >> wait for next iteration
			else
			{	sprite.addIterDelay(DelayManager.DL_ENTER,1);		
			}
		}
	}
	
	private void engStart(EngineEvent event)
	{	if(gesture.equals(GestureName.PREPARED))
		{	stand();
		}
	}

	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/*
	 * action supposedly already tested
	 */
	private void appear()
	{	gesture = GestureName.APPEARING;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		EngineEvent event = new EngineEvent(EngineEvent.TILE_LOW_ENTER,sprite,null,sprite.getActualDirection()); //TODO to be changed by a GESTURE_CHANGE event (or equiv.)
		sprite.getTile().spreadEvent(event);
	}

	private void stand()
	{	gesture = GestureName.STANDING;
		StateAbility ability = sprite.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
		if(ability.isActive())
		{	double duration = ability.getStrength();
			sprite.addDelay(DelayManager.DL_EXPLOSION,duration);
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
		}
		else
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);		
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			// colliding sprites
			{	Iterator<Entry<Direction,TreeSet<Sprite>>> it = collidingSprites.entrySet().iterator();
				while(it.hasNext())
				{	Entry<Direction,TreeSet<Sprite>> t = it.next();
					TreeSet<Sprite> tree = t.getValue();
					tree.clear();
					it.remove();
				}
			}
		}
	}
}
