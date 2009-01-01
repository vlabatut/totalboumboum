package fr.free.totalboumboum.engine.content.sprite.bomb;

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

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class Bomb extends Sprite
{	private String bombName;
	
	public Bomb(Level level)
	{	super(level);
	}

	public String getBombName()
	{	return bombName;
	}
	public void setBombName(String bombName)
	{	this.bombName = bombName;
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
