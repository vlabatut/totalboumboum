package fr.free.totalboumboum.engine.control;

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


public class ControlCode
{	private int keyCode;
	private boolean mode;
	
	public ControlCode(int keyCode, boolean mode)
	{	this.keyCode = keyCode;
		this.mode = mode;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean getMode() {
		return mode;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}
}
