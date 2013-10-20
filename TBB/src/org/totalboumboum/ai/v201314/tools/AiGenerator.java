package org.totalboumboum.ai.v201314.tools;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.totalboumboum.tools.classes.ClassTools;
import org.totalboumboum.tools.files.FileNames;

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
			
//			Arrays.asList("Emre","Asil"),
//			Arrays.asList("Tülin","İzer"),
//			Arrays.asList("Miray","Yüce")
			
//			Arrays.asList("Neşe","Güneş"),
//			Arrays.asList("Esra","Sağlam"),
//			Arrays.asList("Siyabend","Turgut")
			
//			Arrays.asList("Hakan","Çetin"),
//			Arrays.asList("Yiğit","Özel")

//			Arrays.asList("İsmail","Arık"),
//			Arrays.asList("Tuğba","Günaydın"),
//			Arrays.asList("Çağdaş","Kochan")
			
//			Arrays.asList("Gözde","Engin"),
//			Arrays.asList("Barış","Serhan"),
//			Arrays.asList("Garip","Tipici")
			
//			Arrays.asList("Selen","Oralı"),
//			Arrays.asList("Arman","Osmanoğlu")

//			Arrays.asList("Fırat","Akyol"),
//			Arrays.asList("Mustafa","Kaptan"),
//			Arrays.asList("Gökberk","Koçak")

//			Arrays.asList("Özkan","Çiftçi"),
//			Arrays.asList("Akın","Kaplanoğlu"),
//			Arrays.asList("Erol","Köseoğlu")

//			Arrays.asList("Emre","Acar"),
//			Arrays.asList("Yankı","Sesyılmaz")

//			Arrays.asList("Berrenur","Saylam"),
//			Arrays.asList("Kübra","Sönmez")
			
//			Arrays.asList("Emre","Derin"),
//			Arrays.asList("Oktay","Koçak"),
//			Arrays.asList("Emin Can","Zorluoğlu")

//			Arrays.asList("Mustafa","Dönmez"),
//			Arrays.asList("Charlotte","Labat Camy")
		);
		
		// on lance la copie
		copyAgent(oldPath, newNames);
	}
	
	/**
	 * Effectue la copie du code source d'un agent
	 * de référence, afin d'obtenir un nouvel agent,
	 * prêt à être implémenté par les auteurs spécifiés.
	 * 
	 * @param oldPackagePath
	 * 		Chemin du package de l'agent de référence.
	 * @param newNames
	 * 		Noms des auteurs du nouvel agent.
	 * @throws IOException
	 * 		Problème lors de la lecture/écriture des fichiers.
	 */
	private static void copyAgent(String oldPackagePath, List<List<String>> newNames) throws IOException
	{	System.out.println("Copie de l'agent "+oldPackagePath);
		// on récupère les chemins nécessaires
		File oldPackage = new File(oldPackagePath);
		String oldPackageName = oldPackage.getName();
		File pack = oldPackage.getParentFile();
		String packPath = pack.getAbsolutePath();
		String packName = pack.getParentFile().getName();

		// nom qualifié du package contenant l'agent de référence
		String packQualifiedName = ClassTools.getTbbPackage();
		packQualifiedName = packQualifiedName + ClassTools.CLASS_SEPARATOR + FileNames.FILE_AI;
		packQualifiedName = packQualifiedName + ClassTools.CLASS_SEPARATOR + packName;
		packQualifiedName = packQualifiedName + ClassTools.CLASS_SEPARATOR + FileNames.FILE_AIS;
//		String oldPackageQualifiedName = packQualifiedName + ClassTools.CLASS_SEPARATOR + oldPackageName;
		
		// le nom du nouvel agent est obtenu à partir des noms des auteurs
		String newPackageName = "";
		for(List<String> name: newNames)
		{	String lastName = name.get(name.size()-1);
			lastName = lastName.toLowerCase(Locale.ENGLISH);
			lastName = Normalizer.normalize(lastName, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			newPackageName = newPackageName + lastName;
		}
		String newPackagePath = packPath + File.separator + newPackageName;
//		String newPackageQualifiedName = packQualifiedName + ClassTools.CLASS_SEPARATOR + newPackageName;
		
		// on copie tous les fichiers
		File source = new File(oldPackagePath);
		File target = new File(newPackagePath);
		System.out.println("Package de destination : "+newPackagePath);
		copyDirectory(source, target, oldPackageName, newPackageName, newNames);
	}

	/**
	 * Copie et adapte le contenu d'un dossier
	 * de l'agent de référence.
	 * 
	 * @param source
	 * 		Dossier à copier.
	 * @param target
	 * 		Copie du dossier.
	 * @param oldPackageName
	 * 		Nom du package de l'agent de référence.
	 * @param newPackageName
	 * 		Nom du package du nouvel agent.
	 * @param newNames
	 * 		Noms des auteurs du nouvel agent.
	 * @throws IOException
	 * 		Problème lors de la lecture/écriture des fichiers.
	 */
	public static void copyDirectory(File source, File target, String oldPackageName, String newPackageName, List<List<String>> newNames) throws IOException
	{	if(source.getAbsolutePath().contains(".svn"))
		{	if(verbose)
				System.out.println("ignore\t"+source.getAbsolutePath());
		}
		else
		{	if(verbose)
				System.out.println("traite\t"+source.getAbsolutePath());
			if (source.isDirectory())
			{	if (!target.exists())
					target.mkdir();
	            String[] files = source.list();
	            for(int i=0;i<files.length;i++)
	            {	String fileName = files[i];
	            	File src = new File(source, fileName);
	            	File trgt = new File(target, fileName);
	            	copyDirectory(src,trgt,oldPackageName,newPackageName, newNames);
	            }
			}
			else
			{	copyFile(source,target,oldPackageName,newPackageName,newNames);
	        }
		}
	}
	
	/**
	 * Copie et adapte le contenu d'un fichier
	 * de l'agent de référence.
	 * 
	 * @param source
	 * 		Fichier à copier.
	 * @param target
	 * 		Copie du fichier.
	 * @param oldPackageName
	 * 		Nom du package de l'agent de référence.
	 * @param newPackageName
	 * 		Nom du package du nouvel agent.
	 * @param newNames
	 * 		Noms des auteurs du nouvel agent.
	 * @throws IOException
	 * 		Problème lors de la lecture/écriture des fichiers.
	 */
	public static void copyFile(File source, File target, String oldPackageName, String newPackageName, List<List<String>> newNames) throws IOException
	{	// on ouvre la source
		String sourceName = source.getName();
		FileInputStream fileIn = new FileInputStream(source);
		InputStreamReader reader = new InputStreamReader(fileIn);
		Scanner scanner = new Scanner(reader);
		
		// on ouvre la cible
		FileOutputStream fileOut = new FileOutputStream(target);
		OutputStreamWriter writer = new OutputStreamWriter(fileOut);
		PrintWriter printWriter = new PrintWriter(writer);
		
		boolean authors = false;
		while(scanner.hasNextLine())
		{	boolean ignore = false;
			// on lit une ligne
			String line = scanner.nextLine();
			
			// on remplace éventuellement le nom du package
			line = line.replaceAll("\\."+oldPackageName, "."+newPackageName);
			
			// on remplace éventuellement le nom des auteurs
			if(line.contains("@author"))
			{	// fichier java
				if(!sourceName.equals(FileNames.FILE_AI_MAIN_CLASS+FileNames.EXTENSION_JAVA))
				{	ignore = true;
					if(!authors)
					{	authors = true;
						for(List<String> names: newNames)
						{	String l = " * @author";
							for(String name: names)
								l = l + " " + name;
							printWriter.println(l);
						}
					}
				}
			}
			else if(line.contains("<author name="))
			{	// fichier xml
				ignore = true;
				if(!authors)
				{	authors = true;
					for(List<String> names: newNames)
					{	String l = "\t\t<author name=\"";
						for(String name: names)
							l = l + name + " ";
						l = l.substring(0,l.length()-1);
						l = l + "\"/>";
						printWriter.println(l);
					}
				}
			}
			
			// on enregistre la ligne
			if(!ignore)
				printWriter.println(line);
		}
		
		// on referme le tout
		scanner.close();
        printWriter.close();
	}
}
