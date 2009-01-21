package fr.free.totalboumboum.engine.player;

import java.io.Serializable;

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

public class PlayerLocation implements Serializable
{	private static final long serialVersionUID = 1L;

	private int number; //debug
	private int col;
	private int line;
	
	public int getNumber()
	{	return number;
	}
	public void setNumber(int number)
	{	this.number = number;
	}

	public int getCol()
	{	return col;
	}
	public void setCol(int col)
	{	this.col = col;
	}
	
	public int getLine()
	{	return line;
	}
	public void setLine(int line)
	{	this.line = line;
	}
}
