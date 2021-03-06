package org.totalboumboum.engine.content.sprite.hero;

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

import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Hero extends Sprite
{	
	public Hero()
	{	super();
	}	

	/////////////////////////////////////////////////////////////////
	// ROLE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Role getRole()
	{	return Role.HERO;
	}
}
