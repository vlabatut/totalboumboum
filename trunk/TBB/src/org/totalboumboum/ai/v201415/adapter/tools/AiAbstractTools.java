package org.totalboumboum.ai.v201415.adapter.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.ai.v201415.adapter.data.AiZone;

/**
 * Classe abstraite servant de base à la définition d'outils de zone.
 * <br/>
 * Cette classe n'est pas destinée à être utilisée par le concepteur de l'agent.
 *  
 * @author Vincent Labatut
 */
public abstract class AiAbstractTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiAbstractTools(AiZone zone)
	{	this.zone = zone;
	
		updated = false;
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone utilisant cet objet */
	protected AiZone zone;

	/////////////////////////////////////////////////////////////////
	// UPDATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si les données ont été mises à jour durant la dernière itération */
	protected boolean updated;

	/**
	 * Permet à la zone d'indiquer aux outils
	 * qu'ils doivent réinitialiser leurs données.
	 * <br>
	 * Cette méthode ne doit pas être utilisée par le concepteur
	 * de l'agent, elle est destinée au traitement interne de l'API 
	 * uniquement.
	 */
	public void reset()
	{	updated = false;
	}
}
