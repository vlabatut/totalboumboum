package org.totalboumboum.ai.v201112.adapter.path.dybref;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * implément la plus simple d'une fonction successeur : 
 * on prend les 4 cases voisines, en ne gardant que celles qui sont traversables
 * par le personnage considéré.
 * Une version plus complexe et plus efficace consisterait à utiliser la profondeur
 * du noeud de recherche pour calculer le temps nécessaire pour arriver jusqu'à la case
 * courante, et à vérifier qu'aucune bombe ne sera en train d'exploser dans les cases voisines
 * quand le joueur y sera. En d'autres termes, on peut calculer si le joueur a le temps
 * de passer sur une case avant qu'elle ne soit prise dans une explosion.
 * 
 * @author Vincent Labatut
 *
 */
public class DybrefSuccessorCalculator
{
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * fonction successeur dédiée à l'algorithme Dybref : on considère les 4 cases voisines 
	 * de la case courante, plus la case courante dans le cadre de l'action d'attente.
	 * On ne conserve les cases que le personnage de référence peut traverser (TODO ?). 
	 * 
	 * @param node	
	 * 		le noeud de recherche courant
	 * @return	
	 * 		une liste des cases successeurs
	 */
	public List<AiTile> processSuccessors(DybrefNode node) throws StopRequestException
	{				

		return result;
	}
}
