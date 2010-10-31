package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;

/**
 * représente un évènement, caractérisé par un sprite effectuant une action
 * et par l'action qu'il effectue.
 * 
 * @author Vincent Labatut
 *
 */
public class AiEvent
{	
	/**
	 * construit un évènement caractérisé par un sprite et par
	 * l'action qu'il effectue.
	 * 
	 * @param sprite	le sprite effectuant l'action
	 * @param action	l'action effectuée par le sprite
	 */
	public AiEvent(AiSprite sprite, AiAction action)
	{	this.sprite = sprite;
		this.action = action;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiSprite sprite;
	
	/**
	 * renvoie le sprite effectuant l'action
	 * 
	 * @return	une représentation du sprite effectuant l'action
	 */
	public AiSprite getSprite()
	{	return sprite;
	}

	/////////////////////////////////////////////////////////////////
	// ACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiAction action;

	/**
	 * renvoie l'action effectuée par le sprite
	 * 
	 * @return	une représentation de l'action effectuée par le sprite
	 */
	public AiAction getAction()
	{	return action;
	}
}
