package org.totalboumboum.ai;

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

import java.util.concurrent.ThreadFactory;

/**
 * 
 * Classe étendant ThreadFactory, pour permettre la création de
 * threads dédiés à l'exécution d'un agent.
 * 
 * @author Vincent Labatut
 *
 */
public class AiThreadFactory implements ThreadFactory
{	/** Nom des threads à créer */
	private String name;

	/**
	 * Construit un {@link ThreadFactory} portant
	 * le nom spécifié.
	 * 
	 * @param name
	 * 		Nom du thread.
	 */
	public AiThreadFactory(String name)
	{	this.name = name;
	}
	
	@Override
	public Thread newThread(Runnable r)
	{	Thread result = new Thread(r);
		result.setName(name);
		result.setPriority(Thread.MIN_PRIORITY);
		return result;
	}
}