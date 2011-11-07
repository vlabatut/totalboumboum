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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * 
 * Classe gérant l'action de déposer une bombe
 * pour l'agent. En particulier, elle doit implémenter la méthode
 * {@link #considerBombing} de l'algorithme général.<br/>
 * Si cette méthode n'est pas surchargée, alors ce gestionnaire
 * ne fait rien, donc l'agent ne posera jamais de bombe.
 * 
 * @author Vincent Labatut
 */
public abstract class AiBombHandler extends AiAbstractHandler
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
	protected AiBombHandler(ArtificialIntelligence ai) throws StopRequestException
    {	super(ai);
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit poser une bombe ou pas. Cette décision
	 * dépend des valeurs d'utilité courantes.<br/>
	 * La méthode renvoie un booléen {@code true}
	 * si l'agent doit poser une bombe, et
	 * {@code false} sinon.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors la valeur {@code false} est systématiquement
	 * renvoyée (i.e. : pas de bombe posée).
	 * 
	 * @return
	 * 		Renvoie {@code true} ssi l'agent doit poser une bombe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected boolean considerBombing() throws StopRequestException
	{	
		// méthode à surcharger
		return false;
	}
}
