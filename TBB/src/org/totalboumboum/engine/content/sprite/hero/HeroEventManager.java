package org.totalboumboum.engine.content.sprite.hero;

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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.feature.action.cry.SpecificCry;
import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.feature.action.exult.SpecificExult;
import org.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import org.totalboumboum.engine.content.feature.action.jump.SpecificJump;
import org.totalboumboum.engine.content.feature.action.land.SpecificLand;
import org.totalboumboum.engine.content.feature.action.punch.SpecificPunch;
import org.totalboumboum.engine.content.feature.action.transmit.SpecificTransmit;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HeroEventManager extends EventManager
{	
	/** current direction blocked during some special gesture */
	protected Direction blockedDirection;
	/** current interactive move direction*/
	protected Direction controlDirection;
	
	protected String explosedBy = null;

	public HeroEventManager(Hero sprite)
	{	super(sprite);		
		controlDirection = Direction.NONE;
		blockedDirection = Direction.NONE;
		setWaitDelay();
	}

	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificConsume)
			actionConsume(event);
		else if(event.getAction() instanceof SpecificTransmit)
			actionTransmit(event);
	}
	
	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.PUNCHING) 
			|| gesture.equals(GestureName.PUSHING) 
			|| gesture.equals(GestureName.WAITING) 
			|| gesture.equals(GestureName.STANDING) 
			|| gesture.equals(GestureName.WALKING))
		{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_FIRE_PROTECTION);
			if(!ability.isActive())
			{	// explosed by
				if(explosedBy==null)
				{	Sprite spr = event.getAction().getActor();
					if(spr instanceof Fire)
					{	Fire temp = (Fire)spr;
						spr = temp.getOwner();
					}
					if(spr instanceof Bomb)
					{	Bomb temp = (Bomb)spr;
						spr = temp.getOwner();
					}
					if(spr instanceof Hero)
					{	Hero temp = (Hero)spr;
						explosedBy = temp.getPlayer().getId();
					}
//if(explosedBy==null)
//	System.out.println();
				}
				// stats
				StatisticAction statAction = StatisticAction.BOMB_PLAYER;
				long statTime = sprite.getLoopTime();
				String statActor = explosedBy;
				String statTarget = sprite.getPlayer().getId();
				StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
				sprite.addStatisticEvent(statEvent);
				// other lifes remaining?
				StateAbility stateAbility = sprite.modulateStateAbility(StateAbilityName.HERO_LIFE);
				if(stateAbility.isActive())
				{	// if the life ability is present, then its use is necessarily >0 (else the manager would've removed it
					sprite.modifyUse(stateAbility,-1);
				}
				else
				{	AbstractPlayer player = sprite.getPlayer(); 
					if(player!=null)
						player.setOut();
				}
				// state
				gesture = GestureName.BURNING;
				sprite.setGesture(gesture,spriteDirection,controlDirection,true);
			}
		}
	}

	private void actionTransmit(ActionEvent event)
	{	if(gesture.equals(GestureName.APPEARING) 
			|| gesture.equals(GestureName.BOUNCING) 
			|| gesture.equals(GestureName.CARRYING) 
			|| gesture.equals(GestureName.DISAPPEARING) 
			|| gesture.equals(GestureName.ENTERING)
			|| gesture.equals(GestureName.HOLDING)
			|| gesture.equals(GestureName.JUMPING)
			|| gesture.equals(GestureName.LANDING)
			|| gesture.equals(GestureName.PICKING)
			|| gesture.equals(GestureName.PREPARED)
			|| gesture.equals(GestureName.PUNCHING)
			|| gesture.equals(GestureName.PUSHING)
			|| gesture.equals(GestureName.STAGGERING)
			|| gesture.equals(GestureName.STANDING)
			|| gesture.equals(GestureName.THROWING)
			|| gesture.equals(GestureName.WAITING)
			|| gesture.equals(GestureName.WALKING))
		{	Item item = ((SpecificTransmit)event.getAction()).getObject();
			sprite.receiveItem(item);
		}
	}

	/////////////////////////////////////////////////////////////////
	// CONTROL EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ControlEvent event)
	{	
//System.out.println(event.getName()+","+event.getMode());
		
		
		if(event.getName().equals(ControlEvent.DOWN) || event.getName().equals(ControlEvent.LEFT) || event.getName().equals(ControlEvent.RIGHT) || event.getName().equals(ControlEvent.UP))
			controlDirection(event);
		else if(event.getName().equals(ControlEvent.DROPBOMB))
			controlDropBomb(event);
		else if(event.getName().equals(ControlEvent.JUMP))
			controlJump(event);
		else if(event.getName().equals(ControlEvent.PUNCHBOMB))
			controlPunchBomb(event);
		else if(event.getName().equals(ControlEvent.TRIGGERBOMB))
			controlTriggerBomb(event);
//System.out.println(spriteDirection+","+controlDirection);
//System.out.println(gesture);
//System.out.println();
	}

	private void controlDirection(ControlEvent event)
	{	// common
		Direction dir = Direction.valueOf(event.getName());
		// control inversion?
		StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_CONTROL_INVERSION);
		if(ability.isActive())
			dir = dir.getOpposite();
		// mode
		if(event.getMode())
			controlDirection = controlDirection.put(dir);
		else
			controlDirection = controlDirection.drop(dir);
		
		// gesture dependant
		if(gesture.equals(GestureName.JUMPING) || gesture.equals(GestureName.LANDING))
		{	//NOTE ici il faudra certainement distinguer ON et OFF
			spriteDirection = controlDirection;
			blockedDirection = controlDirection;
			sprite.setGesture(gesture,spriteDirection,controlDirection,false);
		}
		else if(gesture.equals(GestureName.PUSHING) || gesture.equals(GestureName.WALKING))
		{	if(controlDirection==Direction.NONE)
			{	setWaitDelay();
				gesture = GestureName.STANDING;
				sprite.setGesture(gesture, spriteDirection,controlDirection,true);			
			}
			else
			{	spriteDirection = controlDirection;
				sprite.setGesture(gesture, spriteDirection,controlDirection,false);
			}
		}
		else if(gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.WAITING))
		{	if(controlDirection!=Direction.NONE)
			{	spriteDirection = controlDirection;
				gesture = GestureName.WALKING;
				sprite.setGesture(gesture, spriteDirection,controlDirection,true);
			}
		}
	}
	
	private double lastDropTime = 0;
	private void controlDropBomb(ControlEvent event)
	{	
//System.out.println("enters ("+sprite.getCurrentPosX()+")");		
		if(event.getMode())
		{	if(gesture.equals(GestureName.PICKING) || gesture.equals(GestureName.THROWING)
				|| gesture.equals(GestureName.HOLDING) || gesture.equals(GestureName.CARRYING)
				|| gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.WALKING)
				|| gesture.equals(GestureName.PUNCHING) || gesture.equals(GestureName.PUSHING)
				|| gesture.equals(GestureName.HIDING) || gesture.equals(GestureName.WAITING))
			{	// enforcing drop bomb latency
				StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_DROP_LATENCY);
				double currentTime = RoundVariables.loop.getTotalGameTime();
				double elapsedTime = currentTime - lastDropTime;
				if(elapsedTime>ablt.getStrength())
				{	ablt = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_CONSTIPATION);
//System.out.println("constipation ("+sprite.getCurrentPosX()+"): "+ablt.isActive());		
					if(!ablt.isActive())
					{	Bomb bomb = sprite.makeBomb();
						SpecificDrop action = new SpecificDrop(sprite,bomb);
						ActionAbility ability = sprite.modulateAction(action);
//System.out.println("drop ("+sprite.getCurrentPosX()+"): "+ablt.isActive());
//if(!ablt.isActive())
//	System.out.println();
						if(ability.isActive())
						{	lastDropTime = currentTime;
							sprite.dropBomb(action);
							if(gesture.equals(GestureName.WAITING))
							{	setWaitDelay();
								gesture = GestureName.STANDING;
								sprite.setGesture(gesture,spriteDirection,controlDirection,true);					
							}
							else if(gesture.equals(GestureName.STANDING))
								setWaitDelay();
						}
					}
				}
			}
		}
	}
	
	private void controlJump(ControlEvent event)
	{	if((gesture.equals(GestureName.PUSHING) || gesture.equals(GestureName.STANDING)
			 || gesture.equals(GestureName.WAITING) || gesture.equals(GestureName.WALKING))
			 && event.getMode())
		{	SpecificAction action = new SpecificJump(sprite);
			ActionAbility ability = sprite.modulateAction(action);
			if(ability.isActive())
			{	gesture = GestureName.JUMPING;
				Direction cDirection = controlDirection.getHorizontalPrimary();
				Direction sDirection = spriteDirection.getHorizontalPrimary();
				sprite.setGesture(gesture, sDirection,cDirection,true);
				blockedDirection = cDirection;
			}
		}
	}
	
	private void controlPunchBomb(ControlEvent event)
	{	if((gesture.equals(GestureName.PUSHING) || gesture.equals(GestureName.STANDING)
			 || gesture.equals(GestureName.WAITING) || gesture.equals(GestureName.WALKING))
			 && event.getMode())
		{	// bomb
			Tile tile = sprite.getTile();
			Tile neighbor = tile.getNeighbor(spriteDirection);
			boolean found = false;
			// bomb in the same tile ?
			{	List<Bomb> bombs = tile.getBombs();
				Iterator<Bomb> i = bombs.iterator();
				while(!found && i.hasNext())
				{	Bomb bomb = i.next();
					SpecificAction action = new SpecificPunch(sprite,bomb);
					ActionAbility ability = sprite.modulateAction(action);
					if(ability.isActive())
					{	found = true;
						// gesture
						gesture = GestureName.PUNCHING;
						StateAbility a = sprite.modulateStateAbility(StateAbilityName.HERO_PUNCH_DURATION);
						double duration = a.getStrength();
						sprite.setGesture(gesture, spriteDirection,controlDirection,true,duration);
						// action
						ActionEvent e = new ActionEvent(action);
						bomb.processEvent(e);
					}
				}
			}
			// if not : in the neighbor tile ?
			if(!found)
			{	List<Bomb> bombs = neighbor.getBombs();
				Iterator<Bomb> i = bombs.iterator();
				while(!found && i.hasNext())
				{	Bomb bomb = i.next();
					SpecificAction action = new SpecificPunch(sprite,bomb);
					ActionAbility ability = sprite.modulateAction(action);
					if(ability.isActive())
					{	found = true;
						// gesture
						gesture = GestureName.PUNCHING;
						StateAbility a = sprite.modulateStateAbility(StateAbilityName.HERO_PUNCH_DURATION);
						double duration = a.getStrength();
						sprite.setGesture(gesture, spriteDirection,controlDirection,true,duration);
						// action
						ActionEvent e = new ActionEvent(action);
						bomb.processEvent(e);
					}
				}
			}
		}
	}
	
	private void controlTriggerBomb(ControlEvent event)
	{	//if(gesture.equals(GestureConstants.PUSHING) || gesture.equals(GestureConstants.STANDING)
		//	 || gesture.equals(GestureConstants.WAITING) || gesture.equals(GestureConstants.WALKING))
		if(event.getMode())
		{	// cette méthode se charge des controles n�cessaires
			sprite.triggerBomb(); 
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(EngineEvent event)
	{	
		
//System.out.println(event);		
		
		if(event.getName().equals(EngineEvent.ANIME_OVER))
			engAnimeOver(event);
		else if(event.getName().equals(EngineEvent.COLLIDED_OFF))
			engCollidingOff(event);
		else if(event.getName().equals(EngineEvent.COLLIDED_ON))
			engCollidingOn(event);
		else if(event.getName().equals(EngineEvent.CELEBRATION_DEFEAT))
			engDefeat(event);
		else if(event.getName().equals(EngineEvent.DELAY_OVER))
			engDelayOver(event);
		else if(event.getName().equals(EngineEvent.INTERSECTION_OFF))
			engIntersectionOff(event);
		else if(event.getName().equals(EngineEvent.INTERSECTION_ON))
			engIntersectionOn(event);
		else if(event.getName().equals(EngineEvent.TILE_LOW_ENTER))
			engTileLowEnter(event);
		else if(event.getName().equals(EngineEvent.TOUCH_GROUND))
			engTouchGround(event);
		else if(event.getName().equals(EngineEvent.TRAJECTORY_OVER))
			engTrajectoryOver(event);
		else if(event.getName().equals(EngineEvent.CELEBRATION_VICTORY))
			engVictory(event);
		else if(event.getName().equals(EngineEvent.ROUND_ENTER))
			engEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_START))
			engStart(event);
	}
	
	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.APPEARING))
		{	setWaitDelay();
			gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,controlDirection,true);
		}
		else if(gesture.equals(GestureName.BURNING))
		{	die();
		}
		else if(gesture.equals(GestureName.CRYING)
				|| gesture.equals(GestureName.EXULTING))
		{	//GameVariables.loop.celebrationOver();
			gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,controlDirection,true);
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.PUNCHING))
		{	setWaitDelay();
			gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,controlDirection,true);
		}
		else if(gesture.equals(GestureName.WAITING))
		{	setWaitDelay();
			gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,controlDirection,true);					
		}
	}
	
	private void engCollidingOff(EngineEvent event)
	{	//System.out.println(">>>SPR_COLLIDING_OFF with "+event.getSource());
		if(gesture.equals(GestureName.PUSHING))
		{	if(!sprite.isColliding())
			{	gesture = GestureName.WALKING;
				sprite.setGesture(gesture, spriteDirection,controlDirection,false);
			}
		}
	}
	
	private void engCollidingOn(EngineEvent event)
	{	//System.out.println(">>>SPR_COLLIDING_ON with "+event.getSource());
		if(gesture.equals(GestureName.BOUNCING))
		{	if(event.getSource()==sprite)
			{	blockedDirection = blockedDirection.getOpposite();
				sprite.setGesture(gesture,blockedDirection,blockedDirection,false);
			}
		}
		else if(gesture.equals(GestureName.WALKING))
		{	if(event.getSource()==sprite)
			{	gesture = GestureName.PUSHING;
				sprite.setGesture(gesture, spriteDirection,controlDirection,false);
			}
		}
	}
	
	private void engDelayOver(EngineEvent event)
	{	// wait delay (only in standing gesture)
		if(event.getStringParameter().equals(DelayManager.DL_WAIT))
		{	// the sprite is currently standing
			if(gesture.equals(GestureName.STANDING))
			{	gesture = GestureName.WAITING;
				sprite.setGesture(gesture,spriteDirection,controlDirection,true);
			}
		}
		// rebirth delay (only in hiding gesture)
		else if(event.getStringParameter().equals(DelayManager.DL_REBIRTH))
		{	// the sprite is currently hiding
			if(gesture.equals(GestureName.HIDING))
			{	// reinit the exploser
				explosedBy = null;
				// reinit the initial items
				sprite.reinitInitialItems();
				// put the protection on
				StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_REBIRTH_DURATION);
				double duration = ability.getStrength();
				ability = new StateAbility(StateAbilityName.HERO_FIRE_PROTECTION);
				ability.setStrength(-1);
				ability.setTime(duration);
				sprite.addDirectAbility(ability);
				ability = new StateAbility(StateAbilityName.SPRITE_TWINKLE);
				ability.setStrength(0);
				ability.setTime(duration);
				sprite.addDirectAbility(ability);
				// make the sprite to appear
				appear();
			}	
		}
		// victory delay: time to celebrate
		else if(event.getStringParameter().equals(DelayManager.DL_VICTORY))
		{	exult();			
		}
		// defeat delay: time to cry
		else if(event.getStringParameter().equals(DelayManager.DL_DEFEAT))
		{	cry();			
		}
		//
		else if(event.getStringParameter().equals(DelayManager.DL_START))
		{	if(gesture.equals(GestureName.PREPARED))
				start();
			else
				sprite.addIterDelay(DelayManager.DL_START,1);
		}
	}

	private void engIntersectionOff(EngineEvent event)
	{	//System.out.println(">>>SPR_INTERSECTION_OFF with"+event.getSource());

/*		Sprite intersected = event.getSource();
		if(intersected == sprite)
			intersected = event.getTarget();
		if(intersected instanceof Hero)
		{	System.out.println("Intersection OFF");
		
		}
*/		
	}

	private void engIntersectionOn(EngineEvent event)
	{	//System.out.println(">>>SPR_INTERSECTION_ON<"+sprite+"> with src="+event.getSource()+", trgt="+event.getTarget());
		Sprite source = event.getSource();
		Sprite target = event.getTarget();
		Sprite intersected = source;
		if(intersected==sprite)
			intersected = target;
		if(intersected instanceof Hero)
		{	SpecificTransmit transmitAction = new SpecificTransmit(sprite,intersected);
			ActionAbility ability = sprite.modulateAction(transmitAction);
			if(ability.isActive())
				sprite.transmitAllItems(intersected);
		}
	}

	private void engTileLowEnter(EngineEvent event)
	{	if(event.getSource()==sprite)
		{	if(gesture.equals(GestureName.PUSHING) || gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.WALKING))
			{	List<Item> items = new ArrayList<Item>(sprite.getTile().getItems());
				for(Item item: items)
				{	SpecificAction action = new SpecificGather(sprite,item);
					ActionAbility ability = sprite.modulateAction(action);
					if(ability.isActive())
					{	sprite.collectItem(item);
						ActionEvent evt = new ActionEvent(action);
						item.processEvent(evt);
					}
				}
			}
		}
	}
	
	private void engTouchGround(EngineEvent event)
	{	//if(gesture.equals(GestureConstants.LANDING))
		{	List<Item> items = sprite.getTile().getItems();
			for(Item item: items)
			{	SpecificAction action = new SpecificGather(sprite,item);
				ActionAbility ability = sprite.modulateAction(action);
				if(ability.isActive())
				{	sprite.collectItem(item);
					ActionEvent evt = new ActionEvent(action);
					item.processEvent(evt);
				}
			}
		}
	}
	
	private void engTrajectoryOver(EngineEvent event)
	{	// the sprite is currently bouncing/jumping
		if(gesture.equals(GestureName.BOUNCING))
		{	SpecificAction specificAction = new SpecificLand(sprite);
			ActionAbility ability = sprite.modulateAction(specificAction);
			// the sprite is allowed to land
			if(ability.isActive())
				gesture = GestureName.LANDING;
			// the sprite is not allowed to land
			else
				gesture = GestureName.BOUNCING;
			// si pas de direction bloqu�e, alors celles du sprite de du controle n'ont pas chang� (sont toujours correctes)
			if(blockedDirection==Direction.NONE)
				sprite.setGesture(gesture,spriteDirection,controlDirection,true);
			// sinon on prend celle qui est bloqu�e, car celles du controle/sprite ont pu changer
			else
				sprite.setGesture(gesture,blockedDirection,blockedDirection,true);
			// on met �ventuellement à jour pour le rebond 
			if(gesture.equals(GestureName.BOUNCING) && blockedDirection==Direction.NONE)
				blockedDirection = spriteDirection;											
		}
		
		// fin du saut
		else if(gesture.equals(GestureName.LANDING))
		{	if(controlDirection == Direction.NONE)
			{	if(blockedDirection!=Direction.NONE)
					spriteDirection = blockedDirection;
			}
			else
				spriteDirection = controlDirection;
			setWaitDelay();
			gesture = GestureName.STANDING;							
			sprite.setGesture(gesture,spriteDirection,controlDirection,true);
			blockedDirection = Direction.NONE;
		}
	}
	
	private void engVictory(EngineEvent event)
	{	exult();
	}
	
	private void engDefeat(EngineEvent event)
	{	cry();		
	}
	
	private void engEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE))
		{	gesture = GestureName.ENTERING;
			spriteDirection = event.getDirection();
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
			double duration = ability.getStrength();
			if(duration<=0)
				duration = 1;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);
		}
	}

	private void engStart(EngineEvent event)
	{	if(gesture.equals(GestureName.PREPARED))
		{	start();
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	sprite.addIterDelay(DelayManager.DL_START,1);
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
		sprite.setGesture(gesture,spriteDirection,controlDirection,true);
		EngineEvent event = new EngineEvent(EngineEvent.TILE_LOW_ENTER,sprite,null,sprite.getActualDirection()); //TODO to be changed by a GESTURE_CHANGE event (or equiv.)
		sprite.getTile().spreadEvent(event);
	}

	private void setWaitDelay()
	{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_WAIT_DURATION);
		double duration = ability.getStrength();
		sprite.addDelay(DelayManager.DL_WAIT,duration);
	}

	private void die()
	{	// manage the items and bombs
		sprite.spriteEnded();
		
		// the player is definitely out
		if(sprite.getPlayer().isOut())
		{	endSprite();
		}
		// the player still's got some lives
		else
		{	// put a delay before the rebirth
			StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_REBIRTH_DELAY);
			double duration = ability.getStrength();
			sprite.addDelay(DelayManager.DL_REBIRTH,duration);
			// hide the sprite
			gesture = GestureName.HIDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
	
	private void exult()
	{	SpecificAction specificAction = new SpecificExult(sprite);
		ActionAbility ability = sprite.modulateAction(specificAction);
		if(ability.isActive())
		{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
			double duration = ablt.getStrength();
			gesture = GestureName.EXULTING;							
			sprite.setGesture(gesture,spriteDirection,controlDirection,true,duration);
		}
		else
		{	// program the victory anime as soon as possible
			sprite.addIterDelay(DelayManager.DL_VICTORY,1);
		}
	}
	
	private void cry()
	{	SpecificAction specificAction = new SpecificCry(sprite);
		ActionAbility ability = sprite.modulateAction(specificAction);
		if(ability.isActive())
		{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
			double duration = ablt.getStrength();
			gesture = GestureName.CRYING;
			sprite.setGesture(gesture,spriteDirection,controlDirection,true,duration);
		}
		else
		{	// program the defeat anime as soon as possible
			sprite.addIterDelay(DelayManager.DL_DEFEAT,1);
		}		
	}
	
	private void start()	
	{	sprite.startItemManager();
		gesture = GestureName.STANDING;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public EventManager copy(Sprite sprite)
	{	EventManager result = new HeroEventManager((Hero)sprite);
		return result;
	}
}
