package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v0;

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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.v201314.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.test.InitData;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.AiMain;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * Cette classe est un exemple de la manière de tester
 * un agent hors-ligne, c'est à dire sans lancer le jeu lui-même.
 * L'intérêt est de pouvoir définir programmatiquement exactement
 * la zone que l'on veut en utilisant {@link AiSimZone} : on peut
 * placer tous les objets et agents où on veut, dans l'état qu'on 
 * veut. Pour cet exemple, on utilise la zone créée dans les exemples
 * de l'API, i.e. la classe {@link InitData}.
 * <br/>
 * La limitation est qu'on ne peut pas enchaîner des actions :
 * pour ça on a besoin du moteur du jeu, bien sûr. Donc ce genre
 * de test doit passer par le jeu complet, pas seulement l'API.
 *
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class Test
{
	/**
	 * Méthode de test.
	 * 
	 * @param args
	 * 		Pas pris en compte.
	 * @throws IOException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws SAXException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws ParserConfigurationException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws InvocationTargetException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws InstantiationException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws IllegalAccessException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws ClassNotFoundException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws NoSuchMethodException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws IllegalArgumentException 
	 * 		Problème au chargement des préférences de l'agent.
	 * @throws URISyntaxException 
	 * 		Problème lors de la localisation du fichier de préférences.
	 */
	public static void main(String args[]) throws IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, IOException, URISyntaxException
	{	// on initialise la zone (cf. InitData)
		AiSimZone zone = InitData.initZone1();
		
		// on rajoute un adversaire
		AiSimTile tile = zone.getTile(4,3);
		int bombRange = 3;
		int bombNumber = 1;
		PredefinedColor color = PredefinedColor.BLACK;
		zone.createHero(tile,color,bombNumber,bombRange,false);
		
		// on initialise l'agent
		AiMain aiMain = new AiMain();
		aiMain.loadPreferences();
		Agent agent = (Agent)aiMain.instantiateAgent();
		agent.setZone(zone);
		
		// on veut afficher les calculs de l'agent
		agent.setVerbose(true);
		
		// le premier appel initialise les gestionnaires
		agent.call();
		// le deuxième sert à finaliser l'initialisation
		agent.call();
		// le troisième demande à l'agent de calculer son action
		agent.call();
	}
}
