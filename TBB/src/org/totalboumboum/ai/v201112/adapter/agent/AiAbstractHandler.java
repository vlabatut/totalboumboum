package org.totalboumboum.ai.v201112.adapter.agent;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

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

/**
 * Classe servant de base à la définition de gestionnaires
 * utilisés pour décomposer le traitement réalisé par un agent.
 * Cf. les différentes spécialisations de cette classe.
 * 
 * @author Vincent Labatut
 */
public abstract class AiAbstractHandler<T extends ArtificialIntelligence>
{	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit être appelée par une classe héritant de celle-ci
	 * grâce au mot-clé {@code this}.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected AiAbstractHandler(T ai) throws StopRequestException
    {	this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected T ai;

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si l'agent doit utiliser la sortie texte (pour le débogage) */
	public boolean verbose = false;
	
	/**
	 * Cette méthode affiche à l'écran le message passé en paramètre,
	 * à condition que {@link #verbose} soit {@code true}. Elle
	 * préfixe automatiquement la couleur de l'agent et le moment
	 * de l'affichage. Utilisez-la pour tout affichage de message,
	 * car elle vous permet de désactiver tous vos messages simplement
	 * en faisant {@code verbose = false;}.
	 * 
	 * @param msg
	 * 		Le message à afficher dans la console.
	 * @return
	 * 		Le temps à l'instant de l'affichage.
	 */
	protected final long print(String msg)
	{	long time = System.currentTimeMillis();
		if(verbose)
		{	StringBuffer message = new StringBuffer();
			message.append("[");
			message.append(time);
			message.append(":");
			message.append(ai.colorStr);
			message.append("]");
			message.append(" ");
			message.append(msg);
			System.out.println(message);
		}
		return time;
	}
}
