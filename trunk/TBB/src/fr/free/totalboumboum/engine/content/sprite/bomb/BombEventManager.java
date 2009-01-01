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
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.manager.DelayManager;
import fr.free.totalboumboum.engine.content.manager.EventManager;
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
	
	public void initGesture()
	{	gesture = GestureConstants.STANDING;
		spriteDirection = Direction.NONE;
		StateAbility ability = sprite.computeAbility(StateAbility.BOMB_TRIGGER_TIMER);
		if(ability.isActive())
		{	double duration = ability.getStrength();
			sprite.addDelay(DelayManager.DL_EXPLOSION,duration);
			sprite.setGesture(gesture, spriteDirection, Direction.NONE, true, duration);
		}
		else
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			
	}

/*
 * *****************************************************************
 * ACTION EVENTS
 * *****************************************************************
 */	
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction().getName().equals(AbstractAction.CONSUME))
			actionConsume(event);
		else if(event.getAction().getName().equals(AbstractAction.PUNCH))
			actionPunch(event);
		else if(event.getAction().getName().equals(AbstractAction.TRIGGER))
			actionTrigger(event);
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureConstants.OSCILLATING) || gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.STANDING)
			|| gesture.equals(GestureConstants.OSCILLATING_FAILING) || gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.STANDING_FAILING))
		{	StateAbility a = sprite.computeAbility(StateAbility.BOMB_EXPLOSION_LATENCY);
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
			{	gesture = GestureConstants.BURNING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				sprite.putExplosion();							
			}
		}
	}
	
	private void actionTrigger(ActionEvent event)
	{	if(gesture.equals(GestureConstants.OSCILLATING) || gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.STANDING)
			|| gesture.equals(GestureConstants.OSCILLATING_FAILING) || gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.STANDING_FAILING))
		{	long r = Math.round(Math.random()*101);
			StateAbility b = sprite.computeAbility(StateAbility.BOMB_FAILURE_PROBABILITY);
			if(b.isActive())
			{	double value = b.getStrength();
				// if there's a failure :  no explosion
				if(r<=value)
				{	b.setStrength(0);
					b.setFrame(true);
				}
			}
			else
			{	gesture = GestureConstants.BURNING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
				sprite.putExplosion();							
			}
		}
	}
	
	private void actionPunch(ActionEvent event)
	{	if(gesture.equals(GestureConstants.OSCILLATING) || gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.STANDING)
			|| gesture.equals(GestureConstants.OSCILLATING_FAILING) || gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.STANDING_FAILING))
		{	gesture = GestureConstants.PUNCHED;
			spriteDirection = event.getAction().getDirection();
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
	
/*
 * *****************************************************************
 * CONTROL EVENTS
 * *****************************************************************
 */
	@Override
	public void processEvent(ControlEvent event)
	{	
	}

/*
 * *****************************************************************
 * ENGINE EVENTS
 * *****************************************************************
 */
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
		
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureConstants.BURNING))
		{	gesture = GestureConstants.ENDED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			sprite.endSprite();
		}
	}

	private void engCollidingOn(EngineEvent event)
	{	// bouncing : bouncing on the obstacle
		if(gesture.equals(GestureConstants.BOUNCING))
		{	spriteDirection = spriteDirection.getOpposite();
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
		}
		// sliding : the bomb stops in the center of the tile
		else if(gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.SLIDING_FAILING))
		{	gesture = GestureConstants.STANDING;
			spriteDirection = Direction.NONE;
			sprite.center();
			StateAbility ability = sprite.computeAbility(StateAbility.BOMB_TRIGGER_TIMER);
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
		if(gesture.equals(GestureConstants.OSCILLATING) || gesture.equals(GestureConstants.OSCILLATING_FAILING))
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
				{	if(gesture.equals(GestureConstants.OSCILLATING))
					{	gesture = GestureConstants.STANDING;
						StateAbility ability = sprite.computeAbility(StateAbility.BOMB_TRIGGER_TIMER);
						if(ability.isActive())
						{	double duration = ability.getStrength();
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
						}
						else
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
					}
					else if(gesture.equals(GestureConstants.OSCILLATING_FAILING))
					{	gesture = GestureConstants.STANDING_FAILING;
						sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
					}
				}
			}
		}		
	}

	private void engCollidedOn(EngineEvent event)
	{	// standing : the bomb starts oscillating
		if(gesture.equals(GestureConstants.STANDING) || gesture.equals(GestureConstants.STANDING_FAILING)
				|| gesture.equals(GestureConstants.OSCILLATING) || gesture.equals(GestureConstants.OSCILLATING_FAILING))
		{	// check if the actor has the ability to push the bomb
			Sprite actor = event.getSource();
			Direction dir = event.getDirection();
			SpecificAction action = new SpecificAction(AbstractAction.PUSH,actor,sprite,dir);
			ActionAbility a = actor.computeAbility(action);
			if(a.isActive())
			{	collidingSprites.get(dir).add(actor);
				String delayName = DelayManager.DL_OSCILLATION+":"+dir;
				StateAbility b = sprite.computeAbility(StateAbility.BOMB_PUSH_LATENCY);
				double dur = b.getStrength();
				// check if the bomb was already collided in the same direction
				if(sprite.hasDelay(delayName))
				{	sprite.addDelay(delayName, dur);
					gesture = GestureConstants.OSCILLATING;
					spriteDirection = dir;
					sprite.setGesture(gesture,spriteDirection,Direction.NONE,false);
					StateAbility ability = sprite.computeAbility(StateAbility.BOMB_TRIGGER_TIMER);
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
	{	// regular explosion
		if(event.getStringParameter().equals(DelayManager.DL_EXPLOSION))
		{	SpecificAction action = new SpecificAction(AbstractAction.DETONATE,sprite,null,Direction.NONE);
			ActionAbility ablt = sprite.computeAbility(action);
			if(ablt.isActive())
			{	// is it a time bomb ?
				StateAbility a = sprite.computeAbility(StateAbility.BOMB_TRIGGER_TIMER);
				if(a.isActive())
				{	// is the bomb working ?
					if(gesture.equals(GestureConstants.SLIDING) 
							|| gesture.equals(GestureConstants.STANDING) 
							|| gesture.equals(GestureConstants.OSCILLATING))
					{	// is there a failure ?
						long r = Math.round(Math.random()*101);
						StateAbility b = sprite.computeAbility(StateAbility.BOMB_FAILURE_PROBABILITY);
						double value = b.getStrength();
						// if there's a failure :  no explosion
						if(r<=value)
						{	double regularTime = a.getStrength();
							//
							b = sprite.computeAbility(StateAbility.BOMB_FAILURE_MINDURATION);
							double minD = b.getStrength();
							if(minD==0)
								minD = regularTime;
							//
							b = sprite.computeAbility(StateAbility.BOMB_FAILURE_MAXDURATION);
							double maxD = b.getStrength();
							if(maxD==0)
								maxD = regularTime;
							//
							double failureDuration = Math.random()*(maxD-minD)+minD;
							sprite.addDelay(DelayManager.DL_EXPLOSION,failureDuration);
							gesture = gesture+"-failing";
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
						}
						// else : explosion
						else
						{	gesture = GestureConstants.BURNING;
							sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
							sprite.putExplosion();							
						}
					}
					// is the bomb failing ?
					else if(gesture.equals(GestureConstants.SLIDING_FAILING) || gesture.equals(GestureConstants.STANDING_FAILING) || gesture.equals(GestureConstants.OSCILLATING_FAILING))
					{	// explosion
						gesture = GestureConstants.BURNING;
						sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
						sprite.putExplosion();							
					}
				}
			}
			else
			{
				//NOTE prévoir le cas où la bombe ne peut pas péter, il faut le remettre au prochain instant
				// sauf que certains états prévoient une réinit du timer au changement d'état (ex : bouncing)
			}
		}
		// explosion-caused explosion
		else if(event.getStringParameter().equals(DelayManager.DL_LATENCY))
		{	if(gesture.equals(GestureConstants.SLIDING) || gesture.equals(GestureConstants.OSCILLATING) || gesture.equals(GestureConstants.STANDING)
				|| gesture.equals(GestureConstants.SLIDING_FAILING) || gesture.equals(GestureConstants.OSCILLATING_FAILING) || gesture.equals(GestureConstants.STANDING_FAILING))
			{	SpecificAction action = new SpecificAction(AbstractAction.DETONATE,sprite,null,Direction.NONE);
				ActionAbility ablt = sprite.computeAbility(action);
				if(ablt.isActive())
				{	gesture = GestureConstants.BURNING;
					sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
					sprite.putExplosion();							
				}
			}
		}
	}

	private void engTrajectoryOver(EngineEvent event)
	{	// the sprite is currently bouncing
		if(gesture.equals(GestureConstants.BOUNCING))
		{	SpecificAction specificAction = new SpecificAction(AbstractAction.LAND,sprite,null,Direction.NONE);
			ActionAbility ability = sprite.computeAbility(specificAction);
			// the sprite is allowed to land
			if(ability.isActive())
			{	gesture = GestureConstants.LANDING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			// the sprite is not allowed to land
			else
			{	gesture = GestureConstants.BOUNCING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
		}
		// the sprite is landing
		else if(gesture.equals(GestureConstants.LANDING))
		{	// gesture/direction
			gesture = GestureConstants.STANDING;
			spriteDirection = Direction.NONE;
			// normal delay
			StateAbility ability = sprite.computeAbility(StateAbility.BOMB_TRIGGER_TIMER);
			if(ability.isActive())
			{	double duration = ability.getStrength();
				sprite.addDelay(DelayManager.DL_EXPLOSION,duration);
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
			}
			else
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);			
		}
		// the sprite has been punched
		else if(gesture.equals(GestureConstants.PUNCHED))
		{	SpecificAction action = new SpecificAction(AbstractAction.LAND,sprite,null,Direction.NONE);
			ActionAbility a = sprite.computeAbility(action);
			// the sprite is allowed to land
			if(a.isActive())
				gesture = GestureConstants.LANDING;
			else
				gesture = GestureConstants.BOUNCING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);			
		}
	}

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
