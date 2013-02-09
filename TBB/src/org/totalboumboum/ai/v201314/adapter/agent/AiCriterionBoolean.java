package org.totalboumboum.ai.v201314.adapter.agent;

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

import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;

/**
 * Cette classe permet de définir un critère,
 * en le caractérisant par son nom et par
 * son domaine (l'ensemble des valeurs possibles
 * pour ce critère). Ici, les valeurs sont booléennes.
 * <br/>
 * Le critère peut être utilisé pour construire
 * un ou plusieurs cas ({@link AiCategory}). 
 * Une combinaison ({@link AiCombination}) contiendra
 * une ou plusieurs valeurs de différents critères. 
 * <br/>
 * Le nom du critère doit être unique pour l'agent.
 * Il ne peut pas y avoir plusieurs critères de même 
 * nom.
 * <br/>
 * Si vous désirez définir un critère booléen,
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
public abstract class AiCriterionBoolean<T extends ArtificialIntelligence> extends AiCriterion<T,Boolean>
{	
	/**
	 * Crée un nouveau critère à partir
	 * du nom passé en paramètre. Le domaine
	 * est binaire, donc il contient seulement
	 * les valeurs {@link Boolean#FALSE} et
	 * {@link Boolean#TRUE}.
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
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiCriterionBoolean(T ai, String name) throws StopRequestException
	{	// init
		super(ai,name);
			
		// init valeurs
		domain.add(Boolean.FALSE);
		domain.add(Boolean.TRUE);
	}
		
    /////////////////////////////////////////////////////////////////
	// DOMAIN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean convertString(String value) throws IllegalArgumentException
	{	Boolean result = null;
		if(value.equalsIgnoreCase(Boolean.toString(true)))
			result = true;
		else if(value.equalsIgnoreCase(Boolean.toString(false)))
			result = false;
		else
			throw new IllegalArgumentException("The value '"+value+"' provided for criterion '"+getName()+"' is invalid (should be a boolean)."); 
		
		return result;
	}
}
