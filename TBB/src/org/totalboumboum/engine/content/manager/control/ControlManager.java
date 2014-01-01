package org.totalboumboum.engine.content.manager.control;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
 * This class is used to control sprites through keyboard (human player)
 * or actions (artificial agent).
 * 
 * @author Vincent Labatut
 */
public abstract class ControlManager
{	
	/**
	 * Builds a sprite control manager.
	 * 
	 * @param sprite
	 * 		Controlled sprite.
	 */
	public ControlManager(Sprite sprite)
	{	this.sprite = sprite;
	}	

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Controlled sprite  */
	protected Sprite sprite;
	
	/**
	 * Returns the controlled sprite.
	 * 
	 * @return
	 * 		Controlled sprite.
	 */
	public Sprite getSprite()
	{	return sprite;
	}
	
	/**
	 * Changes the controlled sprite.
	 * 
	 * @param sprite
	 * 		New controlled sprite.
	 */
	public void setSprite(Sprite sprite)
	{	this.sprite = sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// SETTINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Controls for this sprite */
	protected ControlSettings controlSettings;
	
	/**
	 * Changes the controls for this sprite.
	 * 
	 * @param controlSettings
	 * 		New controls.
	 */
	public void setControlSettings(ControlSettings controlSettings)
	{	this.controlSettings = controlSettings;
	}
	
	/////////////////////////////////////////////////////////////////
	// CODES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Adds a new control code.
	 * 
	 * @param controlCode
	 * 		Code to add.
	 */
	public abstract void putControlCode(ControlCode controlCode);

	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Adds a new control event.
	 * 
	 * @param controlEvent
	 * 		Event to add.
	 */
	public abstract void putControlEvent(ControlEvent controlEvent);

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Updates controls.
	 */
	public abstract void update();
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Makes a copy of this manager.
	 * 
	 * @param sprite
	 * 		Sprite to manage.
	 * @return
	 * 		New control manager for the specified sprite.
	 */
	public abstract ControlManager copy(Sprite sprite);
}
