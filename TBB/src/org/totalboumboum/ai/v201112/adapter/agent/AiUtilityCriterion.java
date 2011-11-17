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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
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
public class AiUtilityCriterion implements Comparable<AiUtilityCriterion>
{	
	/**
	 * Crée un nouveau critère à partir
	 * du nom et du domaine passés
	 * en paramètres.
	 * <br/>
	 * <b>Attention </b>: le nom du
	 * critère doit être unique pour
	 * un cas donné. Il ne peut pas
	 * y avoir deux critères de même
	 * nom dans le même cas.
	 * 
	 * @param name
	 * 		Nom du nouveau critère.
	 * @param domain
	 * 		Ensemble des valeurs possible pour ce critère.
	 */
	public AiUtilityCriterion(String name, Set<Comparable<?>> domain)
	{	this.name = name;
		this.domain.addAll(domain);
	}
	
    /////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le nom de ce critère */
	private String name;

	/**
	 * Renvoie le nom de ce critère.
	 * <br/>
	 * Normalement, ce nom est supposé
	 * être unique pour tous les critères
	 * utilisés dans le même cas.
	 * 
	 * @return
	 * 		Le nom de ce critère.
	 */
	public String getName()
	{	return name;
	}
	
    /////////////////////////////////////////////////////////////////
	// DOMAIN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les valeurs possibles pour ce critère */
	private final Set<Comparable<?>> domain = new HashSet<Comparable<?>>();
	
	/**
	 * Renvoie le domaine, i.e. l'ensemble
	 * des valeurs possibles pour ce critère.
	 * <br/>
	 * <b>Attention </b>: Il ne faut surtout pas modifier
	 * cet ensemble. 
	 * 
	 * @return
	 * 		L'ensemble des valeurs possibles pour ce critère.
	 */
	public Set<Comparable<?>> getDomain()
	{	return domain;
	}
	
	/**
	 * Indique si le domaine de définition de ce
	 * critère contient la valeur passée en paramètre.
	 * 
	 * @param value
	 * 		La valeur à tester.
	 * @return
	 * 		{@code true} ssi la valeur appartient au domaine de ce critère.
	 */
	public boolean hasValue(Comparable<?> value)
	{	boolean result = domain.contains(value);
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiUtilityCriterion)
		{	AiUtilityCriterion criterion = (AiUtilityCriterion)o;
			result = compareTo(criterion)==0;
		}
		return result;
	}

	@Override
	public int compareTo(AiUtilityCriterion criterion)
	{	int result = name.compareTo(criterion.getName());
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
	{	String result = name + "={";
		Iterator<Comparable<?>> it = domain.iterator();
		while(it.hasNext())
		{	Comparable<?> value = it.next();
			result = result + value.toString();
			if(it.hasNext())
				result = result + ", ";
		}
		result = result + "}";
		return result;
	}
}
