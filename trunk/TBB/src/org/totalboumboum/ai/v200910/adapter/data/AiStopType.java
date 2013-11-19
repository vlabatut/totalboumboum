package org.totalboumboum.ai.v200910.adapter.data;

import java.io.Serializable;

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

/**
 * 
 * types de blocages possibles pour un sprite
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public enum AiStopType implements Serializable
{
	/** laisse passer n'importe quel sprite */
	NO_STOP,
	/** laisse passer seulement les sprites ayant un pouvoir spécial */
	WEAK_STOP,
	/** ne laisse passer aucun sprite */
	STRONG_STOP;		
}
