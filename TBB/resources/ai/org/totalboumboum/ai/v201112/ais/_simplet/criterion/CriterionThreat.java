package org.totalboumboum.ai.v201112.ais._simplet.criterion;

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

import org.totalboumboum.ai.v201112.ais._simplet.CommonTools;
import org.totalboumboum.ai.v201112.ais._simplet.Simplet;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Cette classe représente le critère de menace envers l'adversaire.
 * Il est entier : la valeur comprise entre 1 et {@value #THREAT_LIMIT}
 * représente la distance entre la case et la cible.
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("deprecation")
public class CriterionThreat extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "THREAT";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		L'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionThreat(Simplet ai) throws StopRequestException
	{	// init nom
		super(NAME,1,THREAT_LIMIT);
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
	public static final int THREAT_LIMIT = 4;
	
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		CommonTools commonTools = ai.commonTools;
		
		// on récupère la distance à la cible : plus c'est prêt, mieux c'est
		// (-> très mauvaise stratégie !)
		int distance = commonTools.getDistanceToTarget(tile);
		// on ne veut quand même pas être sur le joueur, ça c'est très mauvais 
		if(distance<1)
			distance = THREAT_LIMIT;
		// si on est trop loin, ça ne fait plus de différence
		else if(distance>THREAT_LIMIT)
			distance = THREAT_LIMIT;
		int result = THREAT_LIMIT - distance + 1;
		
		return result;
	}
}
