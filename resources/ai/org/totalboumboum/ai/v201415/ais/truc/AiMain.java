package org.totalboumboum.ai.v201415.ais.truc;

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

import japa.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.v201415.adapter.agent.AiManager;
import org.totalboumboum.ai.v201415.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201415.ais.truc.v0.Truc;
import org.xml.sax.SAXException;

/**
 * Classe utilisée par le moteur du jeu pour retrouver les agents.
 * L'objet créé dans la méthode {@link #instantiateAgent()} 
 * doit être de la classe principale de l'agent.
 * <br/>
 * Les directives {@code import} doivent être modifiées de manière
 * à utiliser la version la plus appropriée de l'agent : {@code v1},
 * {@code v2}, {@code v3}, etc. 
 * 
 * @author Vincent Labatut
 */
public class AiMain extends AiManager
{
	/////////////////////////////////////////////////////////////////
	// AGENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ArtificialIntelligence instantiateAgent()
	{	return new Truc();
	}

	/////////////////////////////////////////////////////////////////
	// VERIFICATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * L'exécution de cette méthode permet
	 * de faire une vérification du code source
	 * de cet agent.
	 * <br/>
	 * Ici, on teste automatiquement certains des 
	 * points mentionnés dans le manuel de l'API.
	 * Mais <b>tous ne peuvent pas être testés depuis
	 * un programme</b> Reportez-vous au manuel pour
	 * plus d'informations.
	 * 
	 * @param args
	 *		Aucun argument n'est nécessaire. 
	 *
	 * @throws IOException
	 * 		Problème lors de l'accès aux fichiers. 
	 * @throws ParseException 
	 * 		Problème lors du parsing du code source.
	 * @throws IllegalArgumentException 
	 * 		Problème lors du chargement des critères, catégories ou combinaisons.
	 * @throws ParserConfigurationException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws SAXException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws IllegalAccessException 
	 * 		Problème lors du chargement des préférences.
	 * @throws InvocationTargetException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InstantiationException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws NoSuchMethodException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 */
	public static void main(String args[]) throws ParseException, IOException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException
	{	AiMain aiMain = new AiMain();
		
		// on applique le parser
		aiMain.parseSourceCode();
		
		// on vérifie si les préférences se chargent normalement
//		aiMain.loadPreferences();
	}
}
