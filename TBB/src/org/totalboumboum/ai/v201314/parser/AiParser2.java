package org.totalboumboum.ai.v201314.parser;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.tools.files.FileNames;

import spoon.processing.Builder;
import spoon.processing.ProcessingManager;
import spoon.processing.Processor;
import spoon.reflect.Factory;
import spoon.support.DefaultCoreFactory;
import spoon.support.QueueProcessingManager;
import spoon.support.StandardEnvironment;
import spoon.support.builder.CtResource;
import spoon.support.builder.FileFactory;

/**
 * This class implements an integrated command-line launcher for processing
 * programs at compile-time using the JDT-based builder (Eclipse). It takes
 * arguments that allow building, processing, printing, and compiling Java
 * programs. Launch with no arguments (see {@link #main(String[])}) for
 * detailed usage.
 * 
 * 
 * @see spoon.processing.Environment
 * @see spoon.reflect.Factory
 * @see spoon.processing.Builder
 * @see spoon.processing.ProcessingManager
 * @see spoon.processing.Processor
 */
public class AiParser2
{	/** Active/désactive la sortie texte */
	public boolean verbose = false;

	public AiParser2()
	{	processors = new ArrayList<Processor<?>>();
		processors.add(new AiMethodProcessor());
	}
	
    /////////////////////////////////////////////////////////////////
	// MAIN						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Programme principal.
	 * Ne prend pas de paramètres.
	 * @param args
	 * 		Rien du tout.
	 * 
	 * @throws ParseException
	 * 		Erreur en analysant un des fichiers source.
	 * @throws IOException
	 * 		Erreur en ouvrant un des fichiers source.
	 */
	public static void main(String[] args) throws Exception
	{	
		// on définit le chemin du pack à analyser
		String path = "resources/ai/org/totalboumboum/ai/v201314/ais";
		//String aiPack = "../TBBtemp/src/org/totalboumboum/ai/v201314/ais";

		// on crée un objet parser
		AiParser2 parser = new AiParser2();
		parser.parseAiPack(path);
	}
	
	/////////////////////////////////////////////////////////////////
	// LEVEL					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int level = 0;
	
	public int getLevel()
	{	return level;
	}

	public void setLevel(int level)
	{	this.level = level;
	}

	/////////////////////////////////////////////////////////////////
	// FILES					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste des versions à ignorer */
	public static List<String> ignoredPackages = new ArrayList<String>(Arrays.asList(new String[]
 	{	
//		"v0",
//		"v1","v1_1","v1_2","v1_3",
// 		"v2","v2_1","v2_2","v2_3",
// 		"v3","v3_1","v3_2","v3_3",
// 		"v4","v4_1","v4_2","v4_3",
// 		"v5","v5_1"
 	}));
	
	/**
	 * Analyse un fichier source.
	 * 
	 * @param file
	 * 		Objet représentant le fichier source à analyser.
	 * @param level
	 * 		Niveau hiérarchique dans l'arbre d'appels.
	 * @return 
	 * 		Le nombre d'erreurs pour le fichier traité.
	 * 
	 * @throws Exception 
	 */
	private int parseFile(File file) throws Exception
	{	for(int i=0;i<level;i++)
			System.out.print("..");
		System.out.println("Analyse du fichier "+file.getPath());
		
		buildModel(file);
        int result = applyProcessors();
        
        if(result>0)
        {	for(int i=0;i<level;i++)
				System.out.print("..");
        	System.out.println("   total pour le fichier "+file.getName()+" : "+result);
        }
        
        return result;
	}
	
	/**
	 * Analyse le contenu d'un dossier
	 * Le traitement est récursif si le dossier
	 * contient lui-même d'autres dossiers.
	 * 
	 * @param folder
	 * 		Dossier à traiter.
	 * @param level
	 * 		Objet représentant le dossier source à analyser.
	 * @return 
	 * 		Le nombre d'erreurs pour le fichier dossier.
	 * 
	 * @throws Exception 
	 */
	private int parseFolder(File folder) throws Exception
	{	int result = 0;
		if(ignoredPackages.contains(folder.getName()))
			System.out.println("Paquetage "+folder.getPath()+" ignoré");
		else
		{	System.out.println("Analyse du paquetage "+folder.getPath());
		
			File[] files = folder.listFiles();
			for(File file: files)
			{	if(file.isDirectory())
				{	level++;
					result = result + parseFolder(file);
				}
				else
				{	String filename = file.getName();
					if(filename.endsWith(FileNames.EXTENSION_JAVA))
					{	level++;
						result = result + parseFile(file);
					}
					else if(verbose)
					{	for(int i=0;i<level;i++)
							System.out.print("..");
						System.out.println("Le fichier "+file.getPath()+" n'a pas été reconnu comme un fichier source Java");
					}
				}
			}
		}
    	System.out.println("total pour le paquetage "+folder.getName()+" : "+result);
		return result;
	}

	/**
	 * Analyse un package correspondant à un agent.
	 * 
	 * @param aiPath
	 * 		Le chemin du package à analyser.
	 * 
	 * @throws Exception 
	 */
	public void parseAi(String aiPath) throws Exception
	{	File aiFolder = new File(aiPath);
		parseAi(aiFolder);
	}
	
	/**
	 * Analyse un package correspondant à un agent.
	 * 
	 * @param aiFolder
	 * 		Un objet représentant le package à analyser.
	 * 
	 * @throws Exception 
	 */
	public void parseAi(File aiFolder) throws Exception
	{	System.out.println("----------------------------------------------------------------------");
		System.out.println("Analyse de l'agent "+aiFolder.getPath());
		System.out.println("----------------------------------------------------------------------");
		level = 0;
		int errorCount = parseFolder(aiFolder);
		System.out.println("total pour l'agent "+aiFolder.getName()+" : "+errorCount);
		System.out.print("\n\n");
	}
	
	/**
	 * Analyse un dossier contenant plusieurs agents
	 * (un pack).
	 * 
	 * @param aiPack
	 * 		Chemin du dossier à analyser.
	 * 
	 * @throws Exception 
	 */
	public void parseAiPack(String aiPack) throws Exception
	{	//System.out.println("Analyse du pack "+aiPack);
		File folder = new File(aiPack);
		File[] files = folder.listFiles();
		for(File file: files)
		{	if(file.isDirectory())
				parseAi(file);
		}		
	}

    /////////////////////////////////////////////////////////////////
	// MODEL					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Factory factory = null;
	private List<Processor<?>> processors = null;
	
	private void buildModel(File file) throws FileNotFoundException
	{	StandardEnvironment environment = new StandardEnvironment();
		factory = new Factory(new DefaultCoreFactory(), environment);
		CtResource input = FileFactory.createResource(file);
		Builder builder = factory.getBuilder();
		try
		{	builder.addInputSource(input);
			builder.build();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
	
	private int applyProcessors()
	{	ProcessingManager processing = new QueueProcessingManager(factory);
		for (Processor<?> processor: processors)
			processing.addProcessor(processor);
		processing.process();
		
		int result = 0;
		return result;// TODO
	}
}
