package org.totalboumboum.ai.v201314.ais._simplet.criterion;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais._simplet.CommonTools;
import org.totalboumboum.ai.v201314.ais._simplet.Agent;

/**
 * Cette classe représente le critère de localité.
 * Il est entier : la valeur représente la distance
 * entre la case et l'agent, plafonnée à {@value #LOCALITY_LIMIT}.
 * La valeur 0 représente une case très éloignée, la valeur
 * {@value #LOCALITY_LIMIT} une case peu éloignée.
 * 
 * @author Vincent Labatut
 */
public class CriterionLocality extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "LOCALITY";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		L'agent concerné. 
	 */
	public CriterionLocality(Agent ai)
	{	// init nom
		super(ai,NAME,0,LOCALITY_LIMIT);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Valeur maximale pour ce critère */
	public static final int LOCALITY_LIMIT = 5;

	@Override
	public Integer processValue(AiTile tile)
	{	ai.checkInterruption();
		CommonTools commonTools = ai.commonTools;
	
		// grosse approximation : on utilise seulement la distance
		AiZone zone = ai.getZone();
		AiTile currentTile = commonTools.currentTile;
		int distance = zone.getTileDistance(currentTile,tile);
		if(distance>LOCALITY_LIMIT)
			distance = LOCALITY_LIMIT;
		
		int result = LOCALITY_LIMIT - distance;
		
		return result;
	}
}
