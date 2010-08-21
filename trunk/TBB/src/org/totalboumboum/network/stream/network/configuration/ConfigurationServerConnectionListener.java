package org.totalboumboum.network.stream.network.configuration;

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

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.configuration.profile.SpriteInfo;

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface ConfigurationServerConnectionListener
{	
	public void profileAdded(Profile profile);
	public void profileRemoved(String id);
	public void spriteChanged(String id, SpriteInfo sprite);
}
