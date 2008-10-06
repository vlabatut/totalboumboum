package fr.free.totalboumboum.ai.adapter200809;

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

import java.util.concurrent.Callable;

public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean stopRequest = false;
	
	public synchronized void stopRequest()
	{	stopRequest = true;		
	}
	
	protected synchronized void checkInterruption() throws StopRequestException
	{	if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public AiAction call()
	{	AiAction result;
		try
		{	result = processAction();		
		}
		catch (StopRequestException e)
		{	result = new AiAction(AiActionName.NONE);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiZone percepts;
	
	public abstract AiAction processAction() throws StopRequestException;
	
	public AiZone getPercepts()
	{	return percepts;
	}
	public void setPercepts(AiZone percepts)
	{	this.percepts = percepts;
	}
	
	void finish()
	{	percepts = null;
	}
}
