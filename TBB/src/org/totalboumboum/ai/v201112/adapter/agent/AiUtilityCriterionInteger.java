package org.totalboumboum.ai.v201112.adapter.agent;

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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

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
 * Le nom du critère doit être unique pour 
 * le(s) cas qui l'utilise(nt).
 * <br/>
 * Si vous désirez définir un critère binaire,
 * vous devez créer une classe fille de celle-ci,
 * dans laquelle vous surchargez la méthode
 * {@link #processValue}. Cette méthode
 * prend une case en paramètre et doit calculer la
 * valeur de ce critère pour cette case-là.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiUtilityCriterionInteger extends AiUtilityCriterion<Integer>
{	
	/**
	 * Crée un nouveau critère à partir
	 * du nom et du domaine passés
	 * en paramètres. Le domaine contient
	 * les entiers compris entre {@code inf}
	 * et {@code sup} (inclus). 
	 * <br/>
	 * <b>Attention </b>: le nom du
	 * critère doit être unique pour
	 * un cas donné. Il ne peut pas
	 * y avoir deux critères de même
	 * nom dans le même cas.
	 * 
	 * @param name
	 * 		Nom du nouveau critère.
	 * @param inf
	 * 		Valeur minimale.
	 * @param sup
	 * 		Valeur maximale.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiUtilityCriterionInteger(String name, int inf, int sup) throws StopRequestException
	{	// init nom
		super(name);
		
		// init valeurs
		for(int i=inf;i<=sup;i++)
			this.domain.add(i);
	}
}
