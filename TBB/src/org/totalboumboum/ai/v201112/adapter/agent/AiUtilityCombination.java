package org.totalboumboum.ai.v201112.adapter.agent;

import java.util.HashMap;
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
 * Cette classe permet de définir un cas,
 * en le caractérisant par son nom et par
 * l'ensemble des critères {@link AiUtilityCriterion}
 * nécessaires pour le décrire.
 * <br/>
 * Les noms des critères sont utilisés par le
 * cas pour les distinguer, par conséquent
 * il ne peut pas y avoir deux critères de
 * même nom dans un cas donné.
 * <br/>
 * Une combinaison ({@link AiUtilityCombination})
 * correspond à un cas donné, et indique une
 * valeur spécifique pour chacun des critères
 * décrivant le cas. Chaque valeur doit bien
 * sûr appartenir au domaine de définition du critère
 * correspondant. 
 * 
 * @author Vincent Labatut
 */
public class AiUtilityCombination
{	
	/**
	 * Crée un nouveau cas à partir
	 * du nom et de l'ensemble de
	 * critères passés en paramètres.
	 * <br/>
	 * <b>Attention </b>: chaque critère
	 * doit avoir un nom unique pour le cas.
	 * Il ne peut pas y avoir deux critères 
	 * de même nom dans le même cas.
	 * 
	 * @param name
	 * 		Nom du nouveau critère.
	 * @param domain
	 * 		Ensemble des valeurs possible pour ce critère.
	 */
	public AiUtilityCombination(AiUtilityCase caze)
	{	this.caze = caze;
	}
	
    /////////////////////////////////////////////////////////////////
	// CASE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le cas dont dépend cette combinaison */
	private AiUtilityCase caze;

	/**
	 * Renvoie le cas dont
	 * dépend cette combinaison.
	 * 
	 * @return
	 * 		Le cas correspondant à cette combinaison.
	 */
	public AiUtilityCase getCase()
	{	return caze;
	}
	
    /////////////////////////////////////////////////////////////////
	// VALUES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les valeurs (de critères) servant à décrire cette combinaison */
	private final HashMap<String,Comparable<?>> values = new HashMap<String, Comparable<?>>();
	
	public void addCriterionValue(String criterionName, Comparable<?> criterionValue)
	{	
		values.put(criterionName,criterionValue);
	}
	
	/**
	 * Renvoie l'ensemble des critères nécessaires
	 * pour décrire ce cas.
	 * <br/>
	 * <b>Attention </b>: Il ne faut surtout pas modifier
	 * cet ensemble. 
	 * 
	 * @return
	 * 		L'ensemble des critères décrivant ce cas.
	 */
	public Comparable<?> getCriterionValue(String criterionName)
	{	Comparable<?> result = values.get(criterionName);
		return result;
	}
}
