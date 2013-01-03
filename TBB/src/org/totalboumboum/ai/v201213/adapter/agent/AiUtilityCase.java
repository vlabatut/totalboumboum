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

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Cette classe permet de définir un cas,
 * en le caractérisant par son nom et par
 * l'ensemble des critères {@link AiUtilityCriterion}
 * nécessaires pour le décrire.
 * <br/>
 * Un cas ne peut pas contenir plusieurs fois
 * le même critère. Deux cas ne peuvent pas avoir
 * le même nom.
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
	 * ne peut apparaître qu'une seule fois
	 * dans un cas donné. Dans le cas contraire, 
	 * la méthode lève une {@link IllegalArgumentException}.
	 * 
	 * @param ai 
	 * 		Agent de référence.
	 * @param name
	 * 		Nom du nouveau critère.
	 * @param criteria
	 * 		Ensemble des valeurs possible pour ce critère.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 * @throws IllegalArgumentException
	 * 		Si le même critère apparaît plusieurs fois.
	 */
	public AiUtilityCase(ArtificialIntelligence ai, String name, Set<AiUtilityCriterion<?,?>> criteria) throws StopRequestException
	{	// vérifie l'unicité du nom
		AiUtilityHandler<?> handler = ai.getUtilityHandler();
		if(handler.checkCriterionName(name))
		{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("A case with the same name ("+name+") already exists for this agent ("+color+" player).");
		}
		
		// on initialise le nom
		this.name = name;
		
		// on initialise les critères
		for(AiUtilityCriterion<?,?> criterion: criteria)
		{	if(this.criteria.contains(criterion))
			{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
				throw new IllegalArgumentException("A case ("+name+") cannot contain several times the same criterion (criterion="+criterion.getName()+", player="+color+").");
			}
			else
				this.criteria.add(criterion);
		}
		
		// on ajoute à la liste de cas du gestionnaire d'utilité
		handler.insertCase(this);
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
	public final String getName()
	{	return name;
	}
	
    /////////////////////////////////////////////////////////////////
	// CRITERIA			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les critères servant à décrire ce cas */
	private final Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
	/** Version immuable de l'ensemble de critères */
	private final Set<AiUtilityCriterion<?,?>> externalCriteria = Collections.unmodifiableSet(criteria);
	
	/**
	 * Renvoie l'ensemble des critères nécessaires
	 * pour décrire ce cas.
	 * <br/>
	 * <b>Attention :</b> l'ensemble renvoyé par cette méthode 
	 * ne doit pas être modifié par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		L'ensemble des critères décrivant ce cas.
	 */
	public final Set<AiUtilityCriterion<?,?>> getCriteria()
	{	return externalCriteria;
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode calculant la combinaison décrivant
	 * une case donnée, pour ce cas.
	 * Cette méthode utilise indirectement
	 * {@link AiUtilityCriterion#fetchValue}.
	 * 
	 * @param tile
	 * 		La case à évaluer.
	 * @return
	 * 		Combinaison décrivant la case. 
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public final AiUtilityCombination processCombination(AiTile tile) throws StopRequestException
	{	AiUtilityCombination result = new AiUtilityCombination(this);
		for(AiUtilityCriterion<?,?> criterion: criteria)
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
	private final <T extends ArtificialIntelligence, U> 
		void processCombinationCriterion(AiUtilityCriterion<T,U> criterion, AiTile tile, AiUtilityCombination result) 
		throws StopRequestException
	{	U value = criterion.fetchValue(tile);
		result.setCriterionValue(criterion,value);
	}
	
    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public final boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiUtilityCase)
		{	AiUtilityCase caze = (AiUtilityCase)o;
			result = compareTo(caze)==0;
		}
		return result;
	}

	@Override
	public final int compareTo(AiUtilityCase caze)
	{	int result = name.compareTo(caze.getName());
		return result;
	}
	
	@Override
    public final int hashCode()
	{	int result = getName().hashCode();
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public final String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(name);
		result.append("=(");
		Iterator<AiUtilityCriterion<?,?>> it = criteria.iterator();
		while(it.hasNext())
		{	AiUtilityCriterion<?,?> criterion = it.next();
			result.append(criterion.getName());
			if(it.hasNext())
				result.append("×");
		}
		result.append(")");
		return result.toString();
	}
}
