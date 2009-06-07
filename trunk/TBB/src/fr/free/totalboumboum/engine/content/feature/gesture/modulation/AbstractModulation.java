package fr.free.totalboumboum.engine.content.feature.gesture.modulation;

import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;

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


public abstract class AbstractModulation
{	
	protected GestureName gestureName; //debug
	
	/** 
	 * modification de la force de l'abileté
	 */
	protected float strength;
	/** 
	 * masquage de la force
	 */
	protected boolean frame;
	
	public AbstractModulation()
	{	strength = 0;
		frame = false;
	}
	
	public boolean getFrame()
	{	return frame;
	}
	public void setFrame(boolean frame)
	{	this.frame = frame;
	}	

	public float getStrength()
	{	return strength;
	}
	public void setStrength(float strength)
	{	this.strength = strength;
	}
	
	public void setGestureName(GestureName gestureName)
	{	this.gestureName = gestureName;		
	}
	public GestureName getGestureName()
	{	return gestureName;		
	}

	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}	
}
