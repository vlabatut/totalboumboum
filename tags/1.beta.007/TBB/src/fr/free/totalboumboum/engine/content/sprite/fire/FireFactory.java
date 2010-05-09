package fr.free.totalboumboum.engine.content.sprite.fire;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class FireFactory extends SpriteFactory<Fire>
{	
	private Fireset fireset;
	
	public FireFactory(Level level)
	{	super(level);
	}	
	
	public Fire makeSprite()
	{	// init
		Fire result = new Fire(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// event
		EventManager eventManager = new FireEventManager(result);
		result.setEventManager(eventManager);
		// fireset name
		result.setFiresetName(fireset.getName());
		
		// result
//		result.initGesture();
		return result;
	}

	public void setFireset(Fireset fireset)
	{	this.fireset = fireset;	
	}
	
	public Fireset getFireset()
	{	return fireset;			
	}
	
	public void finish()
	{	if(!finished)
		{	super.finish();
			fireset = null;
		}
	}
}
