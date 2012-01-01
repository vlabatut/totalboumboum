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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
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
public final class AiUtilityCase implements Comparable<AiUtilityCase>
{	
	/**
	 * Crée un nouveau cas à partir
	 * du nom et de l'ensemble de
	 * critères passés en paramètres.
	 * <br/>
	 * <b>Attention </b>: chaque critère
	 * doit avoir un nom unique pour le cas.
	 * Il ne peut pas y avoir deux critères 
	 * de même nom dans le même cas. Dans le
	 * case contraire, la méthode lève une
	 * {@link IllegalArgumentException}.
	 * 
	 * @param name
	 * 		Nom du nouveau critère.
	 * @param domain
	 * 		Ensemble des valeurs possible pour ce critère.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si plusieurs critères portent le même nom.
	 */
	public AiUtilityCase(String name, Set<AiUtilityCriterion<?>> criteria)
	{	// on initialise le nom
		this.name = name;
		
		// on initialise les critères
		for(AiUtilityCriterion<?> criterion: criteria)
		{	if(this.criteria.contains(criterion))
				throw new IllegalArgumentException("A case ("+name+") cannot contain several criteria with the same name ("+criterion.getName()+").");
			else
				this.criteria.add(criterion);
		}
	}
	
    /////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le nom de ce cas */
	private String name;

	/**
	 * Renvoie le nom de ce cas.
	 * 
	 * @return
	 * 		Le nom de ce cas.
	 */
	public String getName()
	{	return name;
	}
	
    /////////////////////////////////////////////////////////////////
	// CRITERIA			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les critères servant à décrire ce cas */
	private final Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
	
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
	public Set<AiUtilityCriterion<?>> getCriteria()
	{	return criteria;
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode calculant la combinaison décrivant
	 * une case donnée, pour ce cas.
	 * Cette méthode utilise indirectement
	 * {@link AiUtilityCriterion#evaluateCriterion}.
	 * 
	 * @param tile
	 * 		La case à évaluer.
	 * @param parameters
	 * 		Liste d'objets transmise à {@link AiUtilityCriterion#evaluateCriterion}.
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiUtilityCombination processCombination(AiTile tile) throws StopRequestException
	{	AiUtilityCombination result = new AiUtilityCombination(this);
		for(AiUtilityCriterion<?> criterion: criteria)
			processCombinationCriterion(criterion,tile,result);
		return result;
	}
	
	/**
	 * Méthode auxiliaire de {@link #processCombination},
	 * chargée d'initialiser une valeur la combinaison.
	 * 
	 * @param <T>
	 * 		Le type de la valeur.
	 * @param criterion
	 * 		Le critère à traiter.
	 * @param tile
	 * 		La case à évaluer.
	 * @param result
	 * 		La combinaison à mettre à jour.
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private <T> void processCombinationCriterion(AiUtilityCriterion<T> criterion, AiTile tile, AiUtilityCombination result) throws StopRequestException
	{	T value = criterion.processValue(tile);
		result.setCriterionValue(criterion,value);
	}
	
    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiUtilityCase)
		{	AiUtilityCase caze = (AiUtilityCase)o;
			result = compareTo(caze)==0;
		}
		return result;
	}

	@Override
	public int compareTo(AiUtilityCase caze)
	{	int result = name.compareTo(caze.getName());
		return result;
	}
	
	@Override
    public int hashCode()
	{	int result = getName().hashCode();
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(name);
		result.append("=(");
		Iterator<AiUtilityCriterion<?>> it = criteria.iterator();
		while(it.hasNext())
		{	AiUtilityCriterion<?> criterion = it.next();
			result.append(criterion.getName());
			if(it.hasNext())
				result.append("×");
		}
		result.append(")");
		return result.toString();
	}
}
