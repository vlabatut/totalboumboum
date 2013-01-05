package org.totalboumboum.ai.v200910.adapter.path.astar.cost;

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

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * Classe étendant la classe abstraite CostCalculator de la manière la plus simple possible.
 * Ici, le cout pour passer d'une case à l'autre est simplement 1, quelles que soient
 * les cases considérées.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class BasicCostCalculator extends CostCalculator
{
	/** 
	 * Les deux cases sont supposées être voisines, 
	 * on se contente de renvoyer leur distance.
	 * 
	 * @param start	la case de départ
	 * @param end	la case d'arrivée
	 * @return la distance entre ces cases (ici : 1, puisqu'elles sont voisines)
	 */ 
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException
	{	return 1;		
	}
}
