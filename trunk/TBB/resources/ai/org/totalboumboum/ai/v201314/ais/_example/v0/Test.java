package org.totalboumboum.ai.v201314.ais._example.v0;

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

import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.test.InitData;
import org.totalboumboum.ai.v201314.ais._example.AiMain;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Cette classe est un exemple de la manière de tester
 * un agent hors-ligne, c'est à dire sans lancer le jeu lui-même.
 * </br>
 * L'intérêt est de pouvoir définir programmatiquement exactement
 * la zone que l'on veut en utilisant {@link AiSimZone} : on peut
 * placer tous les objets et agents où on veut, dans l'état qu'on 
 * veut. Pour cet exemple, on utilise la zone créée dans les exemples
 * de l'API, i.e. la classe {@link InitData}.
 * <br/>
 * Cette classe montre également comment charger une zone préalablement
 * enregistrée depuis le jeu, pendant une partie. On retrouve la zone
 * exactement telle qu'elle était quand l'enregistrement a été réalisé.
 * <br/>
 * La limitation est qu'on ne peut pas enchaîner des actions :
 * pour ça on a besoin du moteur du jeu, bien sûr. Donc ce genre
 * de test doit passer par le jeu complet, pas seulement l'API.
 *
 * @author Vincent Labatut
 */
public class Test
{
	/**
	 * Méthode de test.
	 * 
	 * @param args
	 * 		Pas pris en compte.
	 * 
	 * @throws Exception 
	 * 		Problème quelconque.
	 */
	public static void main(String args[]) throws Exception
	{	// on initialise l'agent
		AiMain aiMain = new AiMain();
		aiMain.loadPreferences();
		Agent agent = (Agent)aiMain.instantiateAgent();

		// on initialise sa zone
		AiZone zone;
//		zone = usePredefinedZone();			// ici on utilise une zone créée manuellement dans la méthode
		zone = useRecordedZone(aiMain);		// là on utilise une zone enregistrée depuis le jeu (CAPSLOCK+NUMERO, cf. le manuel)
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
	
	/**
	 * Construit une zone à partir de zéro.
	 * Il peut donc s'agir de n'importe quoi,
	 * tout ce qui peut servir à tester l'agent.
	 * <br/>
	 * L'intérêt est de pouvoir faire des tests
	 * sans avoir besoin de créer réellement la 
	 * zone (sous forme de fichiers XML).
	 * 
	 * @return
	 * 		La zone crée.
	 * 
	 * @throws Exception
	 * 		Problème quelconque.
	 */
	public static AiZone usePredefinedZone() throws Exception
	{	// on initialise la zone (cf. InitData)
		AiSimZone result = InitData.initZone1();
		
		// on rajoute un adversaire
		AiSimTile tile = result.getTile(4,3);
		int bombRange = 3;
		int bombNumber = 1;
		PredefinedColor color = PredefinedColor.BLACK;
		result.createHero(tile,color,bombNumber,bombRange,false);
		
		return result;
	}
	
	/**
	 * Charge une zone qui avait été préalablement enregistrée
	 * dans un fichier, pendant le jeu. La zone obtenue est
	 * exactement similaire à la zone telle qu'elle était au
	 * moment de l'enregistrement. 
	 * <br/>
	 * Cela permet de tester des situations très spécifiques,
	 * qui seraient difficiles à reproduire mais que l'on a 
	 * pu enregistrer pendant le jeu.
	 * 
	 * @param aiMain 
	 * 		On a besoin de l'objet déjà créé pour effectuer le chargement.
	 * @return
	 * 		La zone crée.
	 * 
	 * @throws Exception
	 * 		Problème quelconque.
	 */
	public static AiZone useRecordedZone(AiMain aiMain) throws Exception
	{	// il faut d'abord indiquer le nom du fichier ici
		String fileName = "2013-11-18 22-59-28-991.data";
		
		// et ensuite, on peut charger son contenu
		AiZone result = aiMain.readPercepts(fileName);
		
		return result;
	}
}
