package org.totalboumboum.ai.v201112.adapter.agent;

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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant l'action de déposer une bombe
 * pour l'agent. En particulier, elle doit implémenter la méthode
 * {@link #considerBombing} de l'algorithme général.
 * <br/>
 * Si cette méthode n'est pas surchargée, alors ce gestionnaire
 * ne fait rien, donc l'agent ne posera jamais de bombe.
 * 
 * @param <T> 
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class AiBombHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit être appelée par une classe héritant de celle-ci
	 * grâce au mot-clé {@code super}.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected AiBombHandler(T ai) throws StopRequestException
    {	super(ai);
		print("    init bomb handler");
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit poser une bombe ou pas. Cette décision
	 * dépend des valeurs d'utilité courantes.
	 * <br/>
	 * La méthode renvoie un booléen {@code true}
	 * si l'agent doit poser une bombe, et
	 * {@code false} sinon.
	 * 
	 * @return
	 * 		Renvoie {@code true} ssi l'agent doit poser une bombe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract boolean considerBombing() throws StopRequestException;
}
