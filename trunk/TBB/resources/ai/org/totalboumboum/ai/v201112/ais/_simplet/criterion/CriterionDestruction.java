package org.totalboumboum.ai.v201112.ais._simplet.criterion;

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

import java.util.Set;

import org.totalboumboum.ai.v201112.ais._simplet.CommonTools;
import org.totalboumboum.ai.v201112.ais._simplet.Simplet;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Cette classe représente le critère de destruction.
 * Il est entier : la valeur comprise entre 0 et {@value #DESTRUCTION_LIMIT} représente
 * le nombre de murs qu'une bombe posée dans la case concernée détruirait.
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("deprecation")
public class CriterionDestruction extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "DESTRUCTION";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionDestruction(Simplet ai) throws StopRequestException
	{	// init nom
		super(NAME,0,DESTRUCTION_LIMIT);
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
	public static final int DESTRUCTION_LIMIT = 4;

	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		CommonTools commonTools = ai.commonTools;
		
		// on récupère les cases contenant des murs destructibles à portée
		Set<AiTile> softwalls = commonTools.getThreatenedSoftwallTiles(tile);
		int result = softwalls.size();
		// c'est possible de faire plus avec des bombes pénétrantes
		// mais on choisit d'ignorer ça par simplification
		if(result>DESTRUCTION_LIMIT)
			result = DESTRUCTION_LIMIT;
		
		return result;
	}
}
