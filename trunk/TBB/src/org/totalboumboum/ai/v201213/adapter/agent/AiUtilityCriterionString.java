package org.totalboumboum.ai.v201213.adapter.agent;

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

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * Cette classe permet de définir un critère,
 * en le caractérisant par son nom et par
 * son domaine (l'ensemble des valeurs possibles
 * pour ce critère). Ici, les valeurs sont entières,
 * comprises dans un intervale défini à la construction.
 * <br/>
 * Le critère peut être utilisé pour construire
 * un ou plusieurs cas ({@link AiUtilityCase}). 
 * Une combinaison ({@link AiUtilityCombination}) contiendra
 * une ou plusieurs valeurs de différents critères. 
 * <br/>
 * Le nom du critère doit être unique pour l'agent.
 * Il ne peut pas y avoir plusieurs critères de même 
 * nom.
 * <br/>
 * Si vous désirez définir un critère binaire,
 * vous devez créer une classe fille de celle-ci,
 * dans laquelle vous surchargez la méthode
 * {@link #processValue}. Cette méthode
 * prend une case en paramètre et doit calculer la
 * valeur de ce critère pour cette case-là.
 * 
 * @param <T>
 * 		Agent de référence.
 *  
 * @author Vincent Labatut
 */
public abstract class AiUtilityCriterionString<T extends ArtificialIntelligence> extends AiUtilityCriterion<T,String>
{	
	/**
	 * Crée un nouveau critère à partir
	 * du nom et du domaine passés
	 * en paramètres. Le domaine contient
	 * un certain nombre de symboles
	 * représentés par des chaînes de
	 * caractères.
	 * <br/>
	 * <b>Attention </b>: le nom du
	 * critère doit être unique pour
	 * un cas donné. Il ne peut pas
	 * y avoir deux critères de même
	 * nom dans le même cas.
	 * 
	 * @param ai 
	 * 		Agent concerné.
	 * @param name
	 * 		Nom du nouveau critère.
	 * @param domain
	 * 		Ensemble des valeurs possible pour ce critère.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiUtilityCriterionString(T ai, String name, Set<String> domain) throws StopRequestException
	{	// init
		super(ai,name);
		
		// init valeurs
		this.domain.addAll(domain);
	}
}
