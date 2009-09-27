package fr.free.totalboumboum.ai.adapter200910.path.astar.cost;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;

public class BasicCostCalculator implements CostCalculator
{
	
	/** 
	 * Les deux cases sont suppos�es �tre voisines, on se contente de renvoyer leur distance.
	 * Il est possible de d�finir des couts plus �volu�s, en tenant compte par exemple des
	 * influences n�gatives dans cette case (pour le joueur) comme la pr�sence de bombe � proximit�, etc.,
	 * et des influences positives telles que la pr�sence de bonus.
	 * 
	 * @param start	la case de d�part
	 * @param end	la case d'arriv�e
	 * @return la distance entre ces cases (ici : 1, puisqu'elles sont voisines)
	 */ 
	@Override
	public double processCost(AiTile start, AiTile end)
	{	return 1;		
	}
}
