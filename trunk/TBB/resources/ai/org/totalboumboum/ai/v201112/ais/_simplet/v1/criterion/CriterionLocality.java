package org.totalboumboum.ai.v201112.ais._simplet.v1.criterion;

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

import org.totalboumboum.ai.v201112.ais._simplet.v1.CommonTools;
import org.totalboumboum.ai.v201112.ais._simplet.v1.Simplet;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Cette classe représente le critère de localité.
 * Il est entier : la valeur représente la distance
 * entre la case et l'agent, plafonnée à {@value #LOCALITY_LIMIT}.
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("deprecation")
public class CriterionLocality extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "LOCAL";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		L'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionLocality(Simplet ai) throws StopRequestException
	{	// init nom
		super(NAME,0,LOCALITY_LIMIT);
		ai.checkInterruption();
		
		// init agent
		this.ai = ai;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** L'agent associé au traitement */ 
	protected Simplet ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Valeur maximale pour ce critère */
	public static final int LOCALITY_LIMIT = 5;

	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
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
