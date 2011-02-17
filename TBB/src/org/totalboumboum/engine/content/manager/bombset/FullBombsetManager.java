package org.totalboumboum.engine.content.manager.bombset;

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

import java.util.Iterator;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import org.totalboumboum.engine.content.feature.action.detonate.SpecificDetonate;
import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.feature.action.trigger.SpecificTrigger;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FullBombsetManager extends BombsetManager
{	
	public FullBombsetManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// DROP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Bomb makeBomb()
	{	Bomb result = bombset.makeBomb(sprite);
		return result;
	}
	
	@Override
	public void dropBomb(SpecificDrop dropAction)
	{	// init
		Bomb bomb = (Bomb)dropAction.getTarget();
		Direction direction = dropAction.getDirection();

		// can the bomb appear here?
		SpecificAppear action = new SpecificAppear(bomb,direction);
		ActionAbility ablt = bomb.modulateAction(action);
//System.out.println(sprite.getCurrentPosX()+": "+ablt.isActive());		
		if(ablt.isActive())
		{	int flameRange = processBombRange();	
			int droppedBombLimit = processBombNumberMax();
			if(droppedBombs.size()<droppedBombLimit)
			{	if(bomb!=null)
				{	bomb.setFlameRange(flameRange); //NOTE maybe it should be more consistent to use a specific StateAbility, initialized automatically from the owner when the bomb is made (by the bombfactory)?
					//Tile tile = sprite.getTile();
					SpecificAction specificAction = new SpecificAppear(bomb);
					ablt = bomb.modulateAction(specificAction);
					if(ablt.isActive())
					{	RoundVariables.level.insertSpriteTile(bomb);
//						bomb.setCurrentPosX(tile.getPosX());
//						bomb.setCurrentPosY(tile.getPosY());
						ActionEvent evt = new ActionEvent(dropAction);
						bomb.processEvent(evt);
						droppedBombs.offer(bomb);
						// stats
						StatisticAction statAction = StatisticAction.DROP_BOMB;
						long statTime = sprite.getLoopTime();
						String statActor = sprite.getPlayer().getId();
						//String statTarget = bomb.getBombName();
						StatisticEvent statEvent = new StatisticEvent(statActor,statAction,null,statTime);
						sprite.addStatisticEvent(statEvent);
//System.out.println("droppedBombCount:"+droppedBombCount);
					}
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// TRIGGER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void triggerBomb()
	{	boolean found = false;
		Iterator<Bomb> b = droppedBombs.iterator();
		while(!found && b.hasNext())
		{	Bomb bomb = b.next();
			// check if the bomb is remote controlled
			StateAbility triggerAbility = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_CONTROL);
			if(triggerAbility.isActive())
			{	// check if the bomb can explode
				SpecificAction action = new SpecificDetonate(bomb);
				ActionAbility detonateAbility = bomb.modulateAction(action);
				if(detonateAbility.isActive())
				{	found = true;
					// make it explode
					SpecificAction specificAction = new SpecificTrigger(sprite,bomb);
					ActionEvent event = new ActionEvent(specificAction);
					bomb.processEvent(event);				
				}
			}
		}
	}
	
	@Override
	public void triggerAllBombs()
	{	for(Bomb bomb: droppedBombs)
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_ON_DEATH_EXPLODE);
			if(ability.isActive())
			{	// set failure probability to zero
				StateAbility failureAbility = new StateAbility(StateAbilityName.BOMB_FAILURE_PROBABILITY);
				failureAbility.setStrength(0);
				failureAbility.setFrame(true);
				bomb.addDirectAbility(failureAbility);
				
				// check if the bomb can explose
				SpecificAction action = new SpecificDetonate(bomb);
				ActionAbility detonateAbility = bomb.modulateAction(action);
				if(detonateAbility.isActive())
				{	// make it explode
					SpecificAction specificAction = new SpecificTrigger(sprite,bomb);
					ActionEvent event = new ActionEvent(specificAction);
					bomb.processEvent(event);				
				}
			}
		}
	}	
	
	/////////////////////////////////////////////////////////////////
	// UPDATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void update()
	{	// dropped bombs
		Iterator<Bomb> i = droppedBombs.iterator();
		while(i.hasNext())
		{	Bomb bomb = i.next();
			if(bomb.getCurrentGesture().getName()==GestureName.ENDED)
			{	i.remove();
//System.out.println("droppedBombCount:"+droppedBombCount);	
			}
		}
		
		// diarrhea disease
		StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_DIARRHEA);
//if(sprite instanceof Hero)
//System.out.println("diarrhea ("+sprite.getCurrentPosX()+"): "+ab.isActive());		
		if(ab.isActive())
		{	ControlEvent event = new ControlEvent(ControlEvent.DROPBOMB,true);
			sprite.putControlEvent(event);
//			event = new ControlEvent(ControlEvent.DROPBOMB,false);
//			sprite.putControlEvent(event);
		}
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public BombsetManager copy(Sprite sprite)
	{	BombsetManager result = new FullBombsetManager(sprite); 
		return result;
	}
}
