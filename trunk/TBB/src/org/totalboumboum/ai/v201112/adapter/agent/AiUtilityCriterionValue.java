package org.totalboumboum.ai.v201112.adapter.agent;

import java.util.HashSet;
import java.util.Set;

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

/**
 * TODO
 * Cette classe permet de définir un critère,
 * en le caractérisant par son nom et par
 * son domaine (l'ensemble des valeurs possibles
 * pour ce critère).
 * <br/>
 * Le critère peut être utilisé pour construire
 * un ou plusieurs cas ({@link AiUtilityCase}). 
 * Une combinaison ({@link AiUtilityCombination}) contiendra
 * une ou plusieurs valeurs de différents critères. 
 * <br/>
 * Le nom du critère doit être unique pour 
 * le(s) cas qui l'utilise(nt).
 * 
 * @author Vincent Labatut
 */
public interface AiUtilityCriterionValue extends Comparable<AiUtilityCriterionValue>
{
	
}
