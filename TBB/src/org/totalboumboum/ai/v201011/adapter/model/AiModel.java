package org.totalboumboum.ai.v201011.adapter.model;

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

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CalculusTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiModel
{	
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiZone predictZone(AiZone current, List<AiEvent> events)
	{	AiSimZone result = new AiSimZone(current);
		
		// apply specified events
		for(AiEvent event: events)
			applyEvent(event,current,result);
		
		// apply unspecified events
		for(int line=0;line<result.getHeight();line++)
			for(int col=0;col<result.getWidth();col++)
				predictTile(line,col,current,result,events);
		
		return result;
	}
	
	private void applyEvent(AiEvent event, AiZone current, AiSimZone result)
	{
		// TODO
	}
	
	private void predictTile(int line, int col, AiZone current,AiSimZone result, List<AiEvent> events)
	{
		// TODO
	}
	
}
