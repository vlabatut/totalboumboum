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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.ActionName;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class HeroFactory extends SpriteFactory<Hero>
{	
	public HeroFactory(Level level)
	{	super(level);
	}	
	
	/////////////////////////////////////////////////////////////////
	// ACTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected static final HashMap<GestureName,List<ActionName>> actions = new HashMap<GestureName, List<ActionName>>();		
	static
	{	List<ActionName> value;
		GestureName key;
		// NONE
		{	key = GestureName.NONE;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.LAND
			});
			actions.put(key,value);
		}
		
		
// TODO distinguer les actions qu'on peut faire et celles qu'on peut subir?
// ça serait p-ê plus simple (bien que moins logique) à définir dans un fichier séparé ?
// contre: rallonge temps de chargement déjà long
		
/* NOTE idée générale:
 *  - un évènement agit sur ou provoque l'action du sprite considéré
 *  - test automatique pr savoir si l'action est possible ou pas dans l'absolu
 *  - si possible, le manager tente de la mettre en oeuvre
 *  - dans tous les cas, il faut que l'acteur connaisse le résultat de cette action, donc on doit renvoyer d'une façon ou d'une autre un booleen pour l'indiquer
 */
		
		
		
		// APPEARING
		{	key = GestureName.APPEARING;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// BOUNCING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// BURNING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// CRYING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// ENDED
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// EXULTING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// HIDING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// JUMPING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// LANDING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// OSCILLATING
		// OSCILLATING_FAILING
		// PUNCHED
		// PUNCHING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// PUSHING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// SLIDING
		// SLIDING_FAILING
		// SPAWNING
		// STANDING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// STANDING_FAILING
		// WAITING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
		// WALKING
		{	key = GestureName.XXXXXXX;
			value = Arrays.asList(new ActionName[]
			{	ActionName.APPEAR,
				ActionName.CONSUME,
				ActionName.CRY,
				ActionName.DETONATE,
				ActionName.DROP,
				ActionName.EXULT,
				ActionName.GATHER,
				ActionName.JUMP,
				ActionName.LAND,
				ActionName.MOVEHIGH,
				ActionName.MOVELOW,
				ActionName.PUNCH,
				ActionName.PUSH,
				ActionName.TRIGGER
			});
			actions.put(key,value);
		}
	}
	
	@Override
	public HashMap<GestureName, List<ActionName>> getActions()
	{	return actions;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Hero makeSprite()
	{	// init
		Hero result = new Hero(level);
		
		// common managers
		initSprite(result);
	
		// specific managers
		// delay
//		double value = configuration.getHeroSetting(Configuration.HERO_SETTING_WAIT_DELAY);
//		result.addDelay(DelayManager.DL_WAIT,value);
		// event
		EventManager eventManager = new HeroEventManager(result);
		result.setEventManager(eventManager);
		
		// result
//		result.initGesture();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}

	/*
	{	key = GestureName.XXXXXXX;
		value = Arrays.asList(new ActionName[]
		{	ActionName.APPEAR,
			ActionName.CONSUME,
			ActionName.CRY,
			ActionName.DETONATE,
			ActionName.DROP,
			ActionName.EXULT,
			ActionName.GATHER,
			ActionName.JUMP,
			ActionName.LAND,
			ActionName.MOVEHIGH,
			ActionName.MOVELOW,
			ActionName.PUNCH,
			ActionName.PUSH,
			ActionName.TRIGGER
		});
		actions.put(key,value);
	}
	 */
	
}
