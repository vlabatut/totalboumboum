package org.totalboumboum.engine.container.fireset;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;

import org.totalboumboum.engine.container.CachableSpriteContainer;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowFiresetMap extends AbstractFiresetMap<HollowFireset> implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public FiresetMap fill(double zoomFactor) throws IOException
	{	FiresetMap result = new FiresetMap();
	
		// firesets
		for(Entry<String,HollowFireset> entry: firesets.entrySet())
		{	String key = entry.getKey();
			HollowFireset hollowFireset = entry.getValue();
			Fireset fireset = hollowFireset.fill(zoomFactor);
			result.addFireset(key,fireset);
		}
		
		return result;
	}
}
