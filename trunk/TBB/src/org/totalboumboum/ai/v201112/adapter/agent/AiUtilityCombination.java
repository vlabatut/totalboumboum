package org.totalboumboum.ai.v201112.adapter.agent;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Cette classe permet de définir une combinaison,
 * en la décrivant par le cas ({@link AiUtilityCase})
 * auquel elle est associée, et un ensemble de valeurs. 
 * Le cas détermine les critères ({@link AiUtilityCriterion})
 * que doivent décrire ces valeurs.
 * <br/>
 * Les valeurs doivent forcément être définies dans
 * les domaines des critères correspondant.
 * 
 * @author Vincent Labatut
 */
public class AiUtilityCombination implements Comparable<AiUtilityCombination>
{	
	/**
	 * Crée une nouvelle combinaison à partir
	 * du cas passé en paramètre.
	 * <br/>
	 * Les valeurs sont à initialiser ultérieurement.
	 * 
	 * @param caze
	 * 		Le cas associé à cette combinaison.
	 */
	public AiUtilityCombination(AiUtilityCase caze)
	{	// le cas
		this.caze = caze;
		
		// les critères du cas
		Set<AiUtilityCriterion> criteriaSet = caze.getCriteria();
		for(AiUtilityCriterion criterion: criteriaSet)
			criteria.put(criterion.getName(),criterion);
	}
	
	/**
	 * Crée une nouvelle combinaison qui est une 
	 * copie de celle passée en paramètre.
	 * <br/>
	 * Note : les valeurs contenues ne sont pas duppliquées
	 * (les objets existants sont réutilisés).
	 * 
	 * @param combination
	 * 		La combinaison à recopier.
	 */
	public AiUtilityCombination(AiUtilityCombination combination)
	{	this.caze = combination.caze;
		this.criteria.putAll(combination.criteria);
	}
	
    /////////////////////////////////////////////////////////////////
	// CASE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le cas dont dépend cette combinaison */
	private AiUtilityCase caze;
	/** Les critères du cas dont dépend cette combinaison */
	private final HashMap<String,AiUtilityCriterion> criteria = new HashMap<String, AiUtilityCriterion>();
	
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
	
	/**
	 * Modifie la valeur associée au critère spécifié.
	 * <br/>
	 * Si le critère spécifié n'appartient pas au
	 * cas associé à cette combinaison, ou bien si
	 * la valeur spécifiée n'appartient pas au domaine
	 * de définition du critère spécifié, une {@link IllegalArgumentException}
	 * est levée.
	 * 
	 * @param criterionName
	 * 		Le nom du critère concerné par la valeur.
	 * @param criterionValue
	 * 		La valeur à affecter au critère.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le critère ou la valeur sont incompatibles.
	 */
	public void setCriterionValue(String criterionName, Comparable<?> criterionValue)
	{	AiUtilityCriterion criterion = criteria.get(criterionName);
		if(criterion==null)
			throw new IllegalArgumentException("The specified criterion ("+criterionName+") is not defined for the case ("+caze+") associated to this combination.");
		if(criterion.hasValue(criterionValue))
			values.put(criterionName,criterionValue);
		else
			throw new IllegalArgumentException("The specified value ("+criterionValue+") does not belong to the criterion ("+criterionName+") definition domain.");
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

    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiUtilityCombination)
		{	AiUtilityCombination combination = (AiUtilityCombination)o;
			result = compareTo(combination)==0;
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int compareTo(AiUtilityCombination combination)
	{	int result = caze.compareTo(combination.getCase());
		if(result!=0)
		{	Set<String> critNames = new TreeSet<String>();
			critNames.addAll(values.keySet());
			critNames.addAll(combination.values.keySet());
			Iterator<String> it = critNames.iterator();
			while(it.hasNext() && result==0)
			{	String critName = it.next();	
				Comparable value1 = values.get(critName);
				Comparable value2 = combination.values.get(critName);
				if(value1==null)
					result = -1;
				else if(value2==null)
					result = +1;
				else
					result = value1.compareTo(value2);
			}
		}
		return result;
	}
	
	@Override
    public int hashCode()
	{	int result = toString().hashCode();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "(";
		for(int i=0;i<values.size();i++)
		{	Comparable<?> value = values.get(i);
			result = result + value.toString();
			if(i<values.size()-1)
				result = result + ", ";
		}
		result = result + ")";
		return result;
	}
}
