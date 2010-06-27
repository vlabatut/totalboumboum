package org.totalboumboum.engine.content.feature.gesture.modulation;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.gesture.GestureName;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractModulation implements Serializable
{	private static final long serialVersionUID = 1L;

	public AbstractModulation()
	{	strength = 0;
		frame = false;
	}

	/////////////////////////////////////////////////////////////////
	// GESTURE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** the gesture whose this modulation belongs to */
	protected GestureName gestureName; //debug
	
	public void setGestureName(GestureName gestureName)
	{	this.gestureName = gestureName;		
	}
	
	public GestureName getGestureName()
	{	return gestureName;		
	}
	
	/////////////////////////////////////////////////////////////////
	// FRAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** strength complete framing, or only modification */
	protected boolean frame;

	public boolean getFrame()
	{	return frame;
	}
	
	public void setFrame(boolean frame)
	{	this.frame = frame;
	}	

	/////////////////////////////////////////////////////////////////
	// STRENGTH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ability strength modulation */
	protected float strength;

	public float getStrength()
	{	return strength;
	}
	
	public void setStrength(float strength)
	{	this.strength = strength;
	}
}
