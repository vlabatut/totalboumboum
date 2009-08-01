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
	{	//NOTE traitement effectu� par l'itemMgr du sprite qui ramasse
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
	}

	private void engDelayOver(EngineEvent event)
	{	if(gesture.equals(GestureName.NONE) && event.getStringParameter().equals(DelayManager.DL_APPEAR))
		{	SpecificAction action = new SpecificAppear(sprite,sprite.getTile());
			ActionAbility ability = sprite.modulateAction(action);
			// can appear >> appears
			if(ability.isActive())
			{	gesture = GestureName.STANDING;
				sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
			}
			// cannot appear >> wait for next iteration
			else
			{	sprite.addIterDelay(DelayManager.DL_APPEAR,1);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/*
	 * the action always succeeds, because if not possible, the item
	 * will appear later (as soon as possible, actually). 
	 */
	public void appear(Direction dir)
	{	//gesture = GestureName.NONE;
		//spriteDirection = Direction.NONE;
		//sprite.setGesture(gesture,spriteDirection,Direction.NONE,true);
		sprite.addIterDelay(DelayManager.DL_APPEAR,1);
	}
//TODO 2) l'inclure dans l'init du niveau 3) r�former les actions sp�cifiques param�tr�es par tile
/*
 * TODO
 * virer la possibilit� que le joueur n'ai pas d'ability d'apparition
 * l'ability d'apparition (et toutes les necessaires) sont � initialiser en dur (au cas o�)
 * 
 * pr th�me: faut calculer le temps d'apparition max pour les floors, blocs, items, bombs
 * ca permet de savoir quand lancer l'affichage. on finit par les personnages
 * �a permettra aussi de faire commencer le d�compte du temps non pas d�s le d�but, mais d�s que les persos sont pr�ts
 * voire faire appara�tre du texte (ready, set, go)
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
