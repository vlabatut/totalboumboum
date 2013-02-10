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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Cette classe permet de définir une combinaison,
 * en la décrivant par la categorie ({@link AiCategory})
 * à laquelle elle est associée, et un ensemble de valeurs.
 * <br/> 
 * La categorie détermine les critères ({@link AiCriterion})
 * que doivent décrire ces valeurs. Chaque valeur
 * est associée à un critère particulier.
 * <br/>
 * Les valeurs doivent forcément être définies dans
 * les domaines des critères correspondant.
 * 
 * @author Vincent Labatut
 */
public final class AiCombination
{	
	/**
	 * Crée une nouvelle combinaison à partir
	 * de la categorie passée en paramètre.
	 * <br/>
	 * Les valeurs sont à initialiser ultérieurement.
	 * 
	 * @param category
	 * 		La categorie associée à cette combinaison.
	 */
	public AiCombination(AiCategory category)
	{	// la categorie
		this.category = category;
		
		// les critères de la categorie
		criteria = category.getCriteria();
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
	public AiCombination(AiCombination combination)
	{	category = combination.category;
		criteria = combination.criteria;
		values.putAll(combination.values);
	}
	
    /////////////////////////////////////////////////////////////////
	// CATEGORY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La categorie dont dépend cette combinaison */
	private AiCategory category;
	/** Les critères de la categorie dont dépend cette combinaison */
	private List<AiCriterion<?,?>> criteria;
	
	/**
	 * Renvoie la categorie dont
	 * dépend cette combinaison.
	 * 
	 * @return
	 * 		La categorie correspondant à cette combinaison.
	 */
	public final AiCategory getCategory()
	{	return category;
	}
	
    /////////////////////////////////////////////////////////////////
	// VALUES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les valeurs (de critères) servant à décrire cette combinaison */
	private final Map<AiCriterion<?,?>,Object> values = new HashMap<AiCriterion<?,?>,Object>();
	
	/**
	 * Modifie la valeur associée au critère spécifié.
	 * <br/>
	 * Si le critère spécifié n'appartient pas à
	 * la categorie associée à cette combinaison, ou bien si
	 * la valeur spécifiée n'appartient pas au domaine
	 * de définition du critère spécifié, une 
	 * {@link IllegalArgumentException} est levée.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent.
	 * @param <U> 
	 * 		Classe du domaine de définition du critère.
	 * 
	 * @param index
	 * 		Le numéro du critère concerné par la valeur.
	 * @param value
	 * 		La valeur à affecter au critère.
	 * 
	 * @throws	IllegalArgumentException
	 * 		La valeur associée au critère n'est pas contenue dans son domaine de validation.
	 */
	final <T extends ArtificialIntelligence,U>  void setCriterionValue(int index, String value) throws IllegalArgumentException
	{	@SuppressWarnings("unchecked")
		AiCriterion<T,U> criterion = (AiCriterion<T,U>)criteria.get(index);
		
		U val = criterion.convertString(value);
		
		if(!criterion.hasValue(val))
			throw new IllegalArgumentException("The specified value '"+val+"' does not belong to criterion '"+criterion.getName()+" definition domain.");

		values.put(criterion,val);
	}
	
	/**
	 * Modifie la valeur associée au critère spécifié.
	 * <br/>
	 * Si le critère spécifié n'appartient pas à la
	 * categorie associée à cette combinaison, ou bien si
	 * la valeur spécifiée n'appartient pas au domaine
	 * de définition du critère spécifié, une 
	 * {@link IllegalArgumentException} est levée.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent.
	 * @param <U> 
	 * 		Classe du domaine de définition du critère.
	 * 
	 * @param criterion
	 * 		Le nom du critère concerné par la valeur.
	 * @param value
	 * 		La valeur à affecter au critère.
	 */
	final <T extends ArtificialIntelligence,U>  void setCriterionValue(AiCriterion<T,U> criterion, U value) 
	{	if(!criteria.contains(criterion))
		{	PredefinedColor color = criterion.ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("The specified criterion '"+criterion.getName()+"' is not defined for the category '"+category.getName()+"' associated to this combination ("+color+" player).");
		}
		if(!criterion.hasValue(value))
		{	PredefinedColor color = criterion.ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("The specified value '"+value+"' does not belong to criterion '"+criterion.getName()+"' definition domain ("+color+" player).");
		}

		values.put(criterion,value);
	}

	/**
	 * Renvoie la valeur associée au critère spécifié
	 * dans cette combinaison.
	 * 
	 * @param criterion
	 * 		Le critère dont on veut la valeur associée.
	 * @return
	 * 		La valeur associée au critère spécifié,
	 * 		ou {@code null} si aucune valeur n'a encore
	 * 		été associée au critère.
	 */
	public final Object getCriterionValue(AiCriterion<?,?> criterion)
	{	Object result = values.get(criterion);
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public final boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiCombination)
		{	AiCombination combination = (AiCombination)o;
			result = true;
			Iterator<AiCriterion<?,?>> it = criteria.iterator();
			while(it.hasNext())
			{	AiCriterion<?,?> criterion = it.next();
				Object value1 = values.get(criterion);
				Object value2 = combination.getCriterionValue(criterion);
				result = value1.equals(value2);
			}
		}
		return result;
	}
	
	@Override
    public final int hashCode()
	{	int result = toString().hashCode();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public final String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(category.getName());
		result.append("=(");
		int i = 0;
		for(AiCriterion<?,?> criterion: criteria)
		{	Object value = values.get(criterion);
			result.append(criterion.getName());
			result.append("=");
			result.append(value);
			if(i<values.size()-1)
				result.append(", ");
			i++;
		}
		result.append(")");
		return result.toString();
	}
}
