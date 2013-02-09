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

import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent.
 * <br/>
 * En particulier, toute classe héritant d'elle doit 
 * implémenter la méthode {@link #considerBombing} de 
 * l'algorithme général..
 * 
 * @param <T> 
 * 		Classe de l'agent concerné.
 * 
 * @author Vincent Labatut
 */
public abstract class AiBombHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit obligatoirement être appelée par une classe 
	 * héritant de celle-ci, grâce au mot-clé {@code super}.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected AiBombHandler(T ai)
    {	super(ai);
		print("    init bomb handler");
	}
		
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit poser une bombe ou pas. Cette décision
	 * dépend, entre autres, des valeurs de préférence 
	 * courantes, et éventuellement d'autres informations.
	 * <br/>
	 * La méthode renvoie {@code true} si l'agent doit 
	 * poser une bombe, et {@code false} sinon.
	 * 
	 * @return
	 * 		Renvoie {@code true} ssi l'agent doit poser une bombe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract boolean considerBombing() throws StopRequestException;
}
