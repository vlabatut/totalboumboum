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

/**
 * permet de d�finir une fonction de cout utilis�e lors de la recherche
 * avec l'algorithme A*
 */
public interface CostCalculator
{
	/** 
	 * calcule le cout de l'action consistant � aller de la case
	 * start � la case end, sachant que ces deux cases sont voisines.
	 * Il est possible de d�finir des couts �volu�s, en tenant compte par exemple des
	 * influences n�gatives dans ces cases (pour le joueur) comme la pr�sence de bombe 
	 * � proximit�, etc., et des influences positives telles que la pr�sence de bonus.
	 * 
	 * @param start	la case de d�part 
	 * @param end	la case d'arriv�e
	 * @return	le co�t du d�placement
	 */
	public double processCost(AiTile start, AiTile end);
}
