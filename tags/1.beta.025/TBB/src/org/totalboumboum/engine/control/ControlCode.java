package org.totalboumboum.engine.control;

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

import java.io.Serializable;

/**
 * Represents the activation/deactivation
 * of a specific keyby the user.
 * 
 * @author Vincent Labatut
 */
public class ControlCode implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a control code with the specified
	 * key and mode.
	 * 
	 * @param keyCode
	 * 		Concerned key.
	 * @param mode
	 * 		Whether the key was pressed or released.
	 */
	public ControlCode(int keyCode, boolean mode)
	{	this.keyCode = keyCode;
		this.mode = mode;
	}

	/////////////////////////////////////////////////////////////////
	// CODE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Code representing the concerned key */
	private int keyCode;

	/**
	 * Returns the code representing 
	 * the concerned key.
	 * 
	 * @return
	 * 		Code representing the concerned key.
	 */
	public int getKeyCode()
	{	return keyCode;
	}

	/////////////////////////////////////////////////////////////////
	// MODE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the key was pressed or released */ 
	private boolean mode;

	/**
	 * Returns {@code true} if the key is pressed
	 * or {@code false} if it is released.
	 * 
	 * @return
	 * 		A boolean representing the key state.
	 */
	public boolean getMode()
	{	return mode;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	private boolean finished = false;
	
	/**
	 * Cleanly finishes this object.
	 */
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = keyCode + " " + mode;
		return result;
	}
}
