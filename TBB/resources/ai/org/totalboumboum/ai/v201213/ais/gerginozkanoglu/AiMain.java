package org.totalboumboum.ai.v201213.ais.gerginozkanoglu;

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

import org.totalboumboum.ai.v201213.adapter.agent.AiManager;
import org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v0.GerginOzkanoglu;

/**
 * Classe utilisée par le moteur du jeu pour retrouver les agents.
 * L'objet créé dans le constructeur de cette classe doit être de la
 * classe principale de l'agent.
 * Les directives {@code imports} doivent être modifiées de manière
 * à utiliser la version la plus appropriée de l'agent : {@code v1},
 * {@code v2}, {@code v3}, etc. 
 * 
 * @author Vincent Labatut
 */
public class AiMain extends AiManager
{
	/**
	 * Constructeur utilisé pour créer une instance de l'IA.
	 * L'objet créé dans le constructeur de cette classe doit être de la
	 * classe principale de l'agent.
	 */
	public AiMain()
	{	super(new GerginOzkanoglu());		
	}
}