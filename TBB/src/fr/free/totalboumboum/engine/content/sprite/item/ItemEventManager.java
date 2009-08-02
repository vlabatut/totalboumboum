package fr.free.totalboumboum.engine.content.sprite.item;

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

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.SpecificAppear;
import fr.free.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;

public class ItemEventManager extends EventManager
{	
	public ItemEventManager(Item sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// ACTION EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void processEvent(ActionEvent event)
	{	if(event.getAction() instanceof SpecificConsume)
			actionConsume(event);
		else if(event.getAction() instanceof SpecificGather)
			actionGather(event); 
	}

	private void actionConsume(ActionEvent event)
	{	if(gesture.equals(GestureName.STANDING))
		{	gesture = GestureName.BURNING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
		
	private void actionGather(ActionEvent event)
	{	//NOTE traitement effectué par l'itemMgr du sprite qui ramasse
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
		else if(event.getName().equals(EngineEvent.DELAY_OVER))
			engDelayOver(event);
		else if(event.getName().equals(EngineEvent.ROUND_ENTER))
			engEnter(event);
		else if(event.getName().equals(EngineEvent.ROUND_START))
			engStart(event);
	}	

	private void engAnimeOver(EngineEvent event)
	{	if(gesture.equals(GestureName.APPEARING))
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
		else if(gesture.equals(GestureName.BURNING))
		{	gesture = GestureName.ENDED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			sprite.endSprite();
		}
		else if(gesture.equals(GestureName.ENTERING))
		{	gesture = GestureName.PREPARED;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}

	private void engDelayOver(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE) && event.getStringParameter().equals(DelayManager.DL_ENTER))
		{	SpecificAction action = new SpecificAppear(sprite,sprite.getTile());
			ActionAbility actionAbility = sprite.modulateAction(action);
			// can appear >> appears
			if(actionAbility.isActive())
			{	gesture = GestureName.APPEARING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			// cannot appear >> wait for next iteration
			else
			{	sprite.addIterDelay(DelayManager.DL_ENTER,1);
			}
		}
	}
	
	private void engEnter(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE))
		{	spriteDirection = event.getDirection();
			SpecificAction action = new SpecificAppear(sprite,sprite.getTile());
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
		{	gesture = GestureName.STANDING;
			sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
//TODO 3) réformer les actions spécifiques paramétrées par tile
/*
 * TODO
 * 
 * pr thème: faut calculer le temps d'apparition max pour les floors, blocs, items, bombs
 * ca permet de savoir quand lancer l'affichage. on finit par les personnages
 * ça permettra aussi de faire commencer le décompte du temps non pas dès le début, mais dès que les persos sont prêts
 * voire faire apparaître du texte (ready, set, go)
 * 
 * tant qu'on y est, on va mettre ready-set-go, ce qui implique une durée minimale avant le début du match
 * p-ê à faire en même temps que le niveau apparait ? ou bien après ? ou pendant l'apparition des joueurs ?
 * 
 */
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
