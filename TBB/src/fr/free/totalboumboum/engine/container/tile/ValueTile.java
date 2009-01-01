package fr.free.totalboumboum.engine.container.tile;

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

public class ValueTile implements Serializable
{	private static final long serialVersionUID = 1L;

	private float proba;
	private String block;
	private String item;
	private String floor;
	
	public ValueTile(String floor, String block, String item, float proba)
	{	this.block = block;
		this.item = item;
		this.floor = floor;
		this.proba = proba;
	}
	
	public String getBlock()
	{	return block;
	}
	public String getItem()
	{	return item;
	}
	public String getFloor()
	{	return floor;
	}
	
	public float getProba()
	{	return proba;	
	}
	public void setProba(float proba)
	{	this.proba = proba;
	}
}
