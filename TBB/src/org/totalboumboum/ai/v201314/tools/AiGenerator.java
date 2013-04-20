package org.totalboumboum.ai.v201314.tools;

import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.totalboumboum.ai.AiTools;
import org.totalboumboum.tools.classes.ClassTools;

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
 * Cette classe est utilisée pour générer des agents à compléter. A
 * partir d'un modèle existant, elle copie tous les fichiers et dossiers
 * en remplaçant les noms originaux par ceux spécifiés.
 * 
 * @author Vincent Labatut
 */
public class AiGenerator
{	/** Active/désactive la sortie texte */
	private static boolean verbose = true;

	/**
	 * Programme principal.
	 * Ne prend pas de paramètres.
	 * @param args
	 * 		Rien du tout.
	 * 
	 * @throws IOException
	 * 		Erreur en lisant/écrivant l'un des fichiers.
	 */
	public static void main(String[] args) throws IOException
	{	// on définit le chemin de l'agent à copier
		String oldPath = "resources/ai/org/totalboumboum/ai/v201314/ais/_example";
		//String aiPack = "../TBBtemp/src/org/totalboumboum/ai/v201314/ais";
	
		// on définit les noms des auteurs du nouvel agent
		List<List<String>> newNames = Arrays.asList(
			Arrays.asList("Prénom1","Nom1"),
			Arrays.asList("Prénom2","Nom2")
		);
		
		// on lance la copie
		copyAgent(oldPath, newNames);
	}
	
	private static void copyAgent(String oldPath, List<List<String>> newNames)
	{	// on récupère le chemin du pack contenant l'agent de référence
		String packName = AiTools.getPackPackage(oldPath);

		
		// le nouvel agent est créé dans le même package que l'agent de référence
		String packageName = "";
		for(List<String> name: newNames)
		{	String lastName = name.get(name.size()-1);
			lastName = lastName.toLowerCase(Locale.ENGLISH);
			lastName = Normalizer.normalize(lastName, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			packageName = packageName + lastName;
		}
		
		
	}
}
