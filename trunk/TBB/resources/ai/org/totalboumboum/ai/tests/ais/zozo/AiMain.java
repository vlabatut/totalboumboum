package org.totalboumboum.ai.tests.ais.zozo;

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
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.v201314.adapter.agent.AiManager;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.tests.ais.zozo.v1.Agent;
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
	{	return new Agent();
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
	 * @throws URISyntaxException 
	 * 		Problème lors du chargement des critères, catégories ou combinaisons.
	 */
	public static void main(String args[]) throws ParseException, IOException, IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, URISyntaxException
	{	@SuppressWarnings("unused")
		AiMain aiMain = new AiMain();
		
		// on applique le parser
//		aiMain.parseSourceCode();
		
		// on vérifie si les préférences se chargent normalement
//		aiMain.loadPreferences();
	
		generateXmlPreferences();
	}
	
	/////////////////////////////////////////////////////////////////
	// CONCEPTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Permet de faciliter l'écriture du fichier
	 * de préférences en automatisant la génération
	 * d'éléments XML, quand c'est possible.
	 * <br/>
	 * Il faut modifier directement les variables
	 * initialisées au début de la méthode pour les
	 * adapter à vos propres critères et catégories.
	 */
	private static void generateXmlPreferences()
	{	// nom de la catégorie
		String cat = "ATTACK_CLOSED";
		// noms des critères, par ordre de préférence
		List<String> critNames = Arrays.asList(
			"RETREAT",
			"SUPERTHREAT",
			"DISTARG",
			"DISTANCE");
		// domaines complets des critères, dans le même ordre que ci-dessus
		// les valeurs de chaque domaine sont rangées par ordre de préférence
		List<List<String>> critDomains = Arrays.asList(
			Arrays.asList("true ","false"),
			Arrays.asList("2","1","0"),
			Arrays.asList("3","2","1","0"),
			Arrays.asList("0","1","2","3","4"));
		
		// cette boucle va afficher dans la console le code xml correspondant
		// vous pouvez ensuite le copier-coller dans votre fichier de préférences
		// bien sûr, il peut être nécessaire de faire des modifications, en
		// particulier dans l'ordre des combinaisons produites.
		int critIndx[] = new int[critNames.size()];
		boolean goOn = true;
		while(goOn)
		{	// print current values
			System.out.print("<combination category=\""+cat+"\" values=\"");
			for(int i=0;i<critNames.size();i++)
				System.out.print(critDomains.get(i).get(critIndx[i])+" ");
			System.out.println("\" />");
			
			// increment indices
			boolean car = true;
			int i = critNames.size()-1;
			while(i>=0 && car)
			{	int idx = critIndx[i] + 1;
				int d = critDomains.get(i).size();
				if(idx==d)
				{	car = true;
					idx = 0;
				}
				else
					car = false;
				critIndx[i] = idx;
				i--;
			}
			goOn = !(i<0 && car);
		}
	}
}
