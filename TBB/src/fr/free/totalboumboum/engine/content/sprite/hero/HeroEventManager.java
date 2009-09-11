package fr.free.totalboumboum.engine.content.sprite.hero;

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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.feature.action.cry.SpecificCry;
import fr.free.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import fr.free.totalboumboum.engine.content.feature.action.exult.SpecificExult;
import fr.free.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import fr.free.totalboumboum.engine.content.feature.action.jump.SpecificJump;
import fr.free.totalboumboum.engine.content.feature.action.land.SpecificLand;
import fr.free.totalboumboum.engine.content.feature.action.punch.SpecificPunch;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.statistics.StatisticAction;
import fr.free.totalboumboum.game.statistics.StatisticEvent;

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
						explosedBy = temp.getPlayer().getFileName();
					}
//if(explosedBy==null)
//	System.out.println();
				}
				// stats
				StatisticAction statAction = StatisticAction.BOMB_PLAYER;
				long statTime = sprite.getLoopTime();
				String statActor = explosedBy;
				String statTarget = sprite.getPlayer().getFileName();
				StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
				sprite.addStatisticEvent(statEvent);
				// other lifes remaining?
				StateAbility stateAbility = sprite.modulateStateAbility(StateAbilityName.HERO_LIFE);
				if(stateAbility.isActive())
				{	// if the life ability is present, then its use is necessarily >0 (else the manager would've removed it
					sprite.modifyUse(stateAbility,-1);
				}
				else
				{	Player player = sprite.getPlayer(); 
					if(player!=null)
						player.setOut();
				}
				// state
				gesture = GestureName.BURNING;
				sprite.setGesture(gesture,spriteDirection,controlDirection,true);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CONTROL EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ControlEvent event)
	{	if(event.getName().equals(ControlEvent.DOWN) || event.getName().equals(ControlEvent.LEFT) || event.getName().equals(ControlEvent.RIGHT) || event.getName().equals(ControlEvent.UP))
			controlDirection(event);
		else if(event.getName().equals(ControlEvent.DROPBOMB))
			controlDropBomb(event);
		else if(event.getName().equals(ControlEvent.JUMP))
			controlJump(event);
		else if(event.getName().equals(ControlEvent.PUNCHBOMB))
			controlPunchBomb(event);
		else if(event.getName().equals(ControlEvent.TRIGGERBOMB))
			controlTriggerBomb(event);
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
		{	spriteDirection = controlDirection;
			gesture = GestureName.WALKING;
			sprite.setGesture(gesture, spriteDirection,controlDirection,true);
		}
	}
	
	private void controlDropBomb(ControlEvent event)
	{	
//System.out.println("enters ("+sprite.getCurrentPosX()+")");		
		if(event.getMode())
		{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.HERO_CONSTIPATION);
//System.out.println("constipation ("+sprite.getCurrentPosX()+"): "+ablt.isActive());		
			if(!ablt.isActive())
			{	Bomb bomb = sprite.makeBomb();
				SpecificDrop action = new SpecificDrop(sprite,bomb);
				ActionAbility ability = sprite.modulateAction(action);
//System.out.println("drop ("+sprite.getCurrentPosX()+"): "+ablt.isActive());
//if(!ablt.isActive())
//	System.out.println();
				if(ability.isActive())
				{	sprite.dropBomb(bomb);
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
			{	ArrayList<Bomb> bombs = tile.getBombs();
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
			{	ArrayList<Bomb> bombs = neighbor.getBombs();
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
		{	sprite.triggerBomb(); // cette m�thode se charge des controles n�cessaires
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
		else if(event.getName().equals(EngineEvent.TILE_LOWENTER))
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
		{	blockedDirection = blockedDirection.getOpposite();
			sprite.setGesture(gesture,blockedDirection,blockedDirection,false);
		}
		else if(gesture.equals(GestureName.WALKING))
		{	gesture = GestureName.PUSHING;
			sprite.setGesture(gesture, spriteDirection,controlDirection,false);
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
				// put the protection on
				StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_REBIRTH_DURATION);
				double duration = ability.getStrength();
				ability = new StateAbility(StateAbilityName.HERO_FIRE_PROTECTION);
				ability.setStrength(1);
				ability.setTime(duration);
				sprite.addDirectAbility(ability);
				ability = new StateAbility(StateAbilityName.SPRITE_TWINKLE);
				ability.setStrength(1);
				ability.setTime(duration);
				sprite.addDirectAbility(ability);
				// make the sprite to appear
				gesture = GestureName.APPEARING;
				sprite.setGesture(gesture,spriteDirection,controlDirection,true);
			}	
		}
	}

	private void engIntersectionOff(EngineEvent event)
	{	//System.out.println(">>>SPR_INTERSECTION_OFF with"+event.getSource());
	}

	private void engIntersectionOn(EngineEvent event)
	{	//System.out.println(">>>SPR_INTERSECTION_ON with"+event.getSource());
	}

	private void engTileLowEnter(EngineEvent event)
	{	if(event.getSource()==sprite)
		{	if(gesture.equals(GestureName.PUSHING) || gesture.equals(GestureName.STANDING) || gesture.equals(GestureName.WALKING))
			{	Item item = sprite.getTile().getItem();
				if(item!=null)
				{	SpecificAction action = new SpecificGather(sprite,item);
					ActionAbility ability = sprite.modulateAction(action);
					if(ability.isActive())
					{	sprite.addItem(item);
						//TODO c'est bizarre : le gesture de l'item n'est pas mis � HIDING au cours de l'op�ration (?)
					}
				}
			}
		}
	}
	
	private void engTouchGround(EngineEvent event)
	{	//if(gesture.equals(GestureConstants.LANDING))
		{	Item item = sprite.getTile().getItem();
			if(item!=null)
			{	SpecificAction action = new SpecificGather(sprite,item);
				ActionAbility ability = sprite.modulateAction(action);
				if(ability.isActive())
				{	sprite.addItem(item);
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
			// on met �ventuellement � jour pour le rebond 
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
	{	if(!gesture.equals(GestureName.BURNING))
		{	SpecificAction specificAction = new SpecificExult(sprite);
			ActionAbility ability = sprite.modulateAction(specificAction);
			if(ability.isActive())
			{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
				double duration = ablt.getStrength();
				gesture = GestureName.EXULTING;							
				sprite.setGesture(gesture,spriteDirection,controlDirection,true,duration);
			}
			else
			{
				//NOTE programmer l'action (execution retardee) quand ce sera possible
			}		
		}
/*		
		StateAbility ability = sprite.computeAbility(StateAbility.HERO_CELEBRATION_DURATION);
		double duration = ability.getStrength();
		gesture = GestureConstants.EXULTING;							
		sprite.setGesture(gesture,spriteDirection,controlDirection,true,duration);
*/		
	}
	
	private void engDefeat(EngineEvent event)
	{	if(!gesture.equals(GestureName.BURNING))
		{	SpecificAction specificAction = new SpecificCry(sprite);
			ActionAbility ability = sprite.modulateAction(specificAction);
			if(ability.isActive())
			{	StateAbility ablt = sprite.modulateStateAbility(StateAbilityName.HERO_CELEBRATION_DURATION);
				double duration = ablt.getStrength();
				gesture = GestureName.CRYING;
				sprite.setGesture(gesture,spriteDirection,controlDirection,true,duration);
			}
			else
			{
				//NOTE programmer l'action quand ce sera possible
			}
		}		
/*		
		StateAbility ability = sprite.computeAbility(StateAbility.HERO_CELEBRATION_DURATION);
		double duration = ability.getStrength();
		gesture = GestureConstants.CRYING;							
		sprite.setGesture(gesture,spriteDirection,controlDirection,true,duration);
*/		
	}
	
	private void engEnter(EngineEvent event)
	{	gesture = GestureName.ENTERING;
		spriteDirection = event.getDirection();
		StateAbility ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
		double duration = ability.getStrength();
		if(duration<=0)
			duration = 1;
		sprite.setGesture(gesture,spriteDirection,Direction.NONE,true,duration);		
	}

	private void engStart(EngineEvent event)
	{	if(gesture.equals(GestureName.PREPARED))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void setWaitDelay()
	{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.HERO_WAIT_DURATION);
		double duration = ability.getStrength();
		sprite.addDelay(DelayManager.DL_WAIT,duration);		
	}

	private void die()
	{	// manage the items
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
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			blockedDirection = null;
			controlDirection = null;
		}
	}
}
