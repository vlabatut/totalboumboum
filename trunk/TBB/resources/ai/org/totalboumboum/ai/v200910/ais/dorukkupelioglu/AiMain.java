package org.totalboumboum.ai.v200910.ais.dorukkupelioglu;

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

import org.totalboumboum.ai.v200910.adapter.AiManager;
import org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2c.DorukKupelioglu;

/**
 * classe utilisée par le moteur du jeu pour retrouver les IA
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("deprecation")
public class AiMain extends AiManager
{

	/**
	 * constructeur utilise pour creer une instance de l'IA
	 */
	public AiMain()
	{	super(new DorukKupelioglu());
	}

}
