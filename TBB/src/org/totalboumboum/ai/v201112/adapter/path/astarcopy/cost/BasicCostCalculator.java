package org.totalboumboum.ai.v201112.adapter.path.astarcopy.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.astarcopy.AstarLocation;

/**
 * Classe étendant la classe abstraite CostCalculator de la manière la plus simple possible.
 * Ici, le coût pour passer d'une case à l'autre est simplement 1, quelles que soient
 * les cases considérées.
 * 
 * @author Vincent Labatut
 */
public class BasicCostCalculator extends CostCalculator
{
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Les deux cases sont supposées être voisines, 
	 * on se contente de renvoyer leur distance
	 * (exprimée en cases, donc forcément ici : 1).<br/>
	 * 
	 * @param current
	 * 		La case courante (voisine de la précédente). 
	 * @param next	
	 * 		La case suivante (voisine de la courante).
	 * @return	
	 * 		Le coût du déplacement entre la case courante et la case suivante.
	 */ 
	@Override
	public double processCost(AstarLocation current, AstarLocation next) throws StopRequestException
	{	return 1;		
	}

	/**
	 * le coût d'un chemin correspond ici à sa distance exprimée
	 * en cases.
	 * 
	 * @param path
	 * 		chemin à traiter
	 * @return
	 * 		le coût de ce chemin
	 */
	@Override
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getTileDistance();
		return result;
	}
}
