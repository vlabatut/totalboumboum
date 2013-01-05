package org.totalboumboum.engine.content.manager.control;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.control.ControlCode;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ControlManager
{	
	public ControlManager(Sprite sprite)
	{	this.sprite = sprite;
	}	

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** managed sprite  */
	protected Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	public void setSprite(Sprite sprite)
	{	this.sprite = sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// SETTINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ControlSettings controlSettings;
	
	public void setControlSettings(ControlSettings controlSettings)
	{	this.controlSettings = controlSettings;
	}
	
	/////////////////////////////////////////////////////////////////
	// CODES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void putControlCode(ControlCode controlCode);

	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void putControlEvent(ControlEvent controlEvent);

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void update();
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract ControlManager copy(Sprite sprite);
}
