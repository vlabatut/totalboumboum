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
 * Cette classe permet de définir un critère,
 * en le caractérisant par son nom et par
 * son domaine (l'ensemble des valeurs possibles
 * pour ce critère).
 * <br/>
 * Les valeurs d'un critère peuvent être de n'importe
 * quelle classe étendant {@link Object}, à condition
 * qu'elles soient toutes de la même classe pour un
 * critère donné.
 * <br/>
 * Cette classe est une classe abstraite qui doit
 * être surclassée en indiquant le type de valeurs
 * du critère (paramètre {@code T}. Le plus simple
 * est d'utiliser comme base l'une des classes filles 
 * proposées dans l'API :
 * <ul>
 * 		<li>{@link AiUtilityCriterionBoolean} pour des valeurs booléennes</li>
 * 		<li>{@link AiUtilityCriterionInteger} pour des valeurs entières</li>
 * 		<li>{@link AiUtilityCriterionString} pour des chaînes de caractères</li>
 * </ul>
 * Dans la classe fille créée, la méthode 
 * {@link #processValue(AiTile)}
 * doit obligatoirement être définie.
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
 * 
 * @param <T> 
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiUtilityCriterion<T> implements Comparable<AiUtilityCriterion<?>>
{	
	/**
	 * Crée un nouveau critère à partir
	 * du nom passé en paramètre.
	 * <br/>
	 * <b>Attention </b>: le nom du
	 * critère doit être unique pour
	 * un cas donné. Il ne peut pas
	 * y avoir deux critères de même
	 * nom dans le même cas.
	 * 
	 * @param name
	 * 		Nom du nouveau critère.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiUtilityCriterion(String name) throws StopRequestException
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le nom de ce critère */
	String name;

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
	public final String getName()
	{	return name;
	}
	
    /////////////////////////////////////////////////////////////////
	// DOMAIN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les valeurs possibles pour ce critère */
	final Set<T> domain = new TreeSet<T>();
	
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
/*	public Set<Comparable<?>> getDomain()
	{	return domain;
	}
*/	
	/**
	 * Indique si le domaine de définition de ce
	 * critère contient la valeur passée en paramètre.
	 * 
	 * @param value
	 * 		La valeur à tester.
	 * @return
	 * 		{@code true} ssi la valeur appartient au domaine de ce critère.
	 */
	public final boolean hasValue(Object value)
	{	boolean result = domain.contains(value);
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère
	 * pour la case passée en paramètre.
	 * <br/>
	 * Il est probable que la case, bien que
	 * nécessaire, ne soit pas une information
	 * suffisante pour réaliser ce calcul. De plus,
	 * il est possible qu'il soit nécessaire
	 * d'utiliser des résultats de calculs communs
	 * (par exemple communs à plusieurs critéres
	 * différents). Dans ces cas-là, il est recommandé
	 * d'utiliser un gestionnaire spécifique, dans
	 * lequel on stocke ces données, et auquel on 
	 * accède directement depuis cette méthode.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * @return
	 * 		La valeur de ce critère pour la case spécifiée.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public abstract T processValue(AiTile tile) throws StopRequestException;
	
    /////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public final boolean equals(Object o)
	{	boolean result = false;
		if(o!=null && o instanceof AiUtilityCriterion)
		{	AiUtilityCriterion<?> criterion = (AiUtilityCriterion<?>)o;
			result = compareTo(criterion)==0;
		}
		return result;
	}

	@Override
	public final int compareTo(AiUtilityCriterion<?> criterion)
	{	int result = name.compareTo(criterion.getName());
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
		result.append("={");
		Iterator<T> it = domain.iterator();
		while(it.hasNext())
		{	T value = it.next();
			result.append(value.toString());
			if(it.hasNext())
				result.append(", ");
		}
		result.append("}");
		return result.toString();
	}
}
