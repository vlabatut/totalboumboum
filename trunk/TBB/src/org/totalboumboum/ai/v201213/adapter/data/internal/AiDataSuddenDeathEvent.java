package org.totalboumboum.ai.v201213.adapter.data.internal;

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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;

/**
 * Représente un évènement de la mort subite,
 * composé d'un instant exprimé en ms et d'un ensemble
 * de sprites destinés à apparaître à cet instant.
 * 
 * @author Vincent Labatut
 *
 */
public class AiDataSuddenDeathEvent extends AiSuddenDeathEvent
{	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste de sprites apparaissant à l'instant associé à cet évènement */
	private List<AiDataSprite> sprites = new ArrayList<AiDataSprite>();
	
	/** 
	 * Renvoie la liste de sprites apparaissant
	 * à l'instant associé à cet évènement. On
	 * peut récupérer la position des sprites simplement
	 * en accédant à leur case.
	 * 
	 * @return	
	 * 		Liste de sprite.
	 */
	public List<AiSprite> getSprites()
	{	List<AiSprite> result = new ArrayList<AiSprite>(sprites);
		return result;
	}
}
