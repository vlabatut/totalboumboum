package org.totalboumboum.ai.v201314.ais._example;

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

import japa.parser.ParseException;

import java.io.IOException;
import java.net.URLDecoder;

import org.totalboumboum.ai.v201314.adapter.agent.AiManager;
import org.totalboumboum.ai.v201314.ais._example.v0.Example;
import org.totalboumboum.tools.classes.ClassTools;

/**
 * Classe utilisée par le moteur du jeu pour retrouver les agents.
 * L'objet créé dans le constructeur de cette classe doit être de la
 * classe principale de l'agent.
 * <br/>
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
	 * <br/>
	 * L'objet créé dans le constructeur de cette classe doit être de la
	 * classe principale de l'agent.
	 */
	public AiMain()
	{	super(new Example());		
	}

	/**
	 * L'exécution de cette méthode permet
	 * de faire une vérification du code source
	 * de cet agent.
	 * 
	 * @param args
	 *		Aucun argument n'est nécessaire. 
	 * @throws IOException
	 * 		Problème lors de l'accès aux fichiers. 
	 * @throws ParseException 
	 * 		Problème lors de l'analyse du code source.
	 */
	public static void main(String args[]) throws ParseException, IOException
	{	String path = AiMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		String pckg = AiMain.class.getPackage().getName();
		String temp[] = pckg.split("\\"+ClassTools.CLASS_SEPARATOR);
		for(String t: temp)
			decodedPath = decodedPath + "/" + t;
		checkSourceCode(decodedPath);
	}
}
