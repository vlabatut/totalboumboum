package org.totalboumboum.engine.loop.event.control;

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

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.StreamedEvent;

/**
 * This class represents events
 * specific to the remote control system.
 * 
 * @author Vincent Labatut
 */
public class RemotePlayerControlEvent extends StreamedEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new remote control event
	 * for the specified sprite, with the
	 * specified control code.
	 * 
	 * @param sprite
	 * 		Controled sprite.
	 * @param controlCode
	 * 		Type of control.
	 */
	public RemotePlayerControlEvent(Sprite sprite, ControlCode controlCode)
	{	super();
		this.spriteId = sprite.getId();
		this.controlCode = controlCode;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE ID			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Unique id of the controled sprite */
	private int spriteId;
	
	/**
	 * Returns the unique id of the controled
	 * sprite.
	 * 
	 * @return
	 * 		Sprite unique id.
	 */
	public int getSpriteId()
	{	return spriteId;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTROL CODE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Control code associated to the sprite */
	private ControlCode controlCode;

	/**
	 * Returns the type of control
	 * associated to the sprite.
	 * 
	 * @return
	 * 		Control code.
	 */
	public ControlCode getControlCode()
	{	return controlCode;	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "RemotePlayerControlEvent("+time+":"+spriteId+"): " + controlCode;
		return result;
	}
}

//TODO pb: le numéro de controle est perdu lors de la synchro, car
//le serveur n'a pas connaissance de la config locale...
//faut améliorer la synchro coté client de manière à ne pas écraser les controles
