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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Cette classe permet de définir une catégorie,
 * en le caractérisant par son nom et par
 * l'ensemble des critères {@link AiCriterion}
 * nécessaires pour la décrire.
 * <br/>
 * Une catégorie ne peut pas contenir plusieurs fois
 * le même critère. Deux catégories ne peuvent pas avoir
 * le même nom.
 * <br/>
 * Une combinaison ({@link AiCombination})
 * correspond à une catégorie donnée, et indique une
 * valeur spécifique pour chacun des critères
 * décrivant la catégorie. Chaque valeur doit bien
 * sûr appartenir au domaine de définition du critère
 * correspondant. 
 * 
 * @author Vincent Labatut
 */
public final class AiCategory implements Comparable<AiCategory>
{	
	/**
	 * Crée une nouvelle catégorie à partir
	 * du nom et de l'ensemble de
	 * critères passés en paramètres.
	 * <br/>
	 * <b>Attention </b>: chaque critère
	 * ne peut apparaître qu'une seule fois
	 * dans une catégorie donnée. Dans le cas contraire, 
	 * la méthode lève une {@link IllegalArgumentException}.
	 * 
	 * @param name
	 * 		Nom de la nouvelle catégorie.
	 * @param criteria
	 * 		Ensemble des critères décrivant cette catégorie.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le même critère apparaît plusieurs fois.
	 */
	public AiCategory(String name, List<AiCriterion<?,?>> criteria)
	{	this.name = name;
		initCriteria(criteria);
	}
	
    /////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le nom de cette catégorie */
	private String name;

	/**
	 * Renvoie le nom de cette catégorie.
	 * 
	 * @return
	 * 		Le nom de cette catégorie.
	 */
	public String getName()
	{	return name;
	}
	
    /////////////////////////////////////////////////////////////////
	// CRITERIA			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les critères servant à décrire cette catégorie */
	private final List<AiCriterion<?,?>> criteria = new ArrayList<AiCriterion<?,?>>();
	/** Version immuable de l'ensemble de critères */
	private final List<AiCriterion<?,?>> externalCriteria = Collections.unmodifiableList(criteria);
	
	/**
	 * Renvoie la liste des critères nécessaires
	 * pour décrire cette catégorie, dans l'ordre de
	 * leur définition.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyé par cette méthode 
	 * ne doit pas être modifié par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		L'ensemble des critères décrivant cette catégorie.
	 */
	public List<AiCriterion<?,?>> getCriteria()
	{	return externalCriteria;
	}

	/**
	 * Initialise la liste des critères de cette catégorie.
	 * <br/>
	 * <b>Attention </b>: chaque critère
	 * ne peut apparaître qu'une seule fois
	 * dans une catégorie donnée. Dans le cas contraire, 
	 * la méthode lève une {@link IllegalArgumentException}.
	 * 
	 * @param criteria
	 * 		Ensemble des critères à ajouter à cette catégorie.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si  un critère apparait deux fois.
	 */
	private void initCriteria(List<AiCriterion<?,?>> criteria) throws IllegalArgumentException
	{	for(AiCriterion<?,?> criterion: criteria)
		{	if(this.criteria.contains(criterion))
				throw new IllegalArgumentException("Trying to add criterion '"+criterion.getName()+"' twice while defining category '"+name+"' (each criterion must appear at most once).");
			this.criteria.add(criterion);
		}
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode calculant la combinaison décrivant
	 * une case donnée, pour cette catégorie.
	 * Cette méthode utilise indirectement
	 * {@link AiCriterion#fetchValue}.
	 * 
	 * @param tile
	 * 		La case à évaluer.
	 * @return
	 * 		Combinaison décrivant la case. 
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiCombination processCombination(AiTile tile) throws StopRequestException
	{	AiCombination result = new AiCombination(this);
		for(AiCriterion<?,?> criterion: criteria)
			processCombinationCriterion(criterion,tile,result);
		return result;
	}
	 
	/**
	 * Méthode auxiliaire de {@link #processCombination},
	 * chargée d'initialiser une valeur la combinaison.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param <U> 
	 * 		Classe du domaine de définition du critère.
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
	private <T extends ArtificialIntelligence, U> 
		void processCombinationCriterion(AiCriterion<T,U> criterion, AiTile tile, AiCombination result) 
		throws StopRequestException
	{	U value = criterion.fetchValue(tile);
		result.setCriterionValue(criterion,value);
	}
	
    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiCategory)
		{	AiCategory category = (AiCategory)o;
			result = compareTo(category)==0;
		}
		return result;
	}

	@Override
	public int compareTo(AiCategory category)
	{	int result = name.compareTo(category.getName());
		return result;
	}
	
	@Override
    public int hashCode()
	{	int result = getName().hashCode();
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Réalise une copie de cette catégorie,
	 * en utilisant les critères passés en paramètre
	 * au lieu des originaux.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param criterionMap
	 * 		Map des critères à utiliser.
	 * @return
	 * 		Copie de cette catégorie, utilisant les nouveaux critères.
	 */
	protected <T extends ArtificialIntelligence> AiCategory clone(Map<String,AiCriterion<T,?>> criterionMap)
	{	List<AiCriterion<?,?>> crits = new ArrayList<AiCriterion<?,?>>();
		for(AiCriterion<?,?> criterion: criteria)
		{	String name = criterion.getName();
			AiCriterion<?,?> crit = criterionMap.get(name);
			crits.add(crit);
		}
		AiCategory result = new AiCategory(name, crits);
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
		Iterator<AiCriterion<?,?>> it = criteria.iterator();
		while(it.hasNext())
		{	AiCriterion<?,?> criterion = it.next();
			result.append(criterion.getName());
			if(it.hasNext())
				result.append("×");
		}
		result.append(")");
		return result.toString();
	}
}
