package org.totalboumboum.ai.v200910.adapter.communication;

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
 * 
 * noms donn�s aux diff�rentes actions qu'une IA peut effectuer : 
 * NONE (ne rien faire), MOVE (se d�placer, avec la direction à pr�ciser), 
 * DROP_BOMB (poser une bombe), PUNCH (frapper une bombe)... 
 * 
 * @author Vincent Labatut
 *
 */
public enum AiActionName
{
	DROP_BOMB,MOVE,NONE,PUNCH;	
}
