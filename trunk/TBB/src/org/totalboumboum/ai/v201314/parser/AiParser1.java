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

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.ai.v201314.tools.AiVisitor;
import org.totalboumboum.tools.files.FileNames;

import spoon.AbstractLauncher;
import spoon.Launcher;
import spoon.support.builder.CtResource;
import spoon.support.builder.FileFactory;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.Switch;
import com.martiansoftware.jsap.stringparsers.FileStringParser;

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
public class AiParser1 extends Launcher
{	/** Active/désactive la sortie texte */
	public static boolean VERBOSE = false;
	/** Liste des versions à ignorer */
	public final static List<String> IGNORED_PACKAGES = new ArrayList<String>(Arrays.asList(new String[]
 	{	
//		"v0",
//		"v1","v1_1","v1_2","v1_3",
// 		"v2","v2_1","v2_2","v2_3",
// 		"v3","v3_1","v3_2","v3_3",
// 		"v4","v4_1","v4_2","v4_3",
// 		"v5","v5_1"
 	}));
	
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
		AiParser1 parser = new AiParser1(args);
		parser.parseAiPack(path);
	}
	
	/**
	 * Constructor.
	 * @param args 
	 * @throws JSAPException 
	 */
	public AiParser1(String[] args) throws JSAPException
	{	super(args);

		addProcessor(AiMethodProcessor.class.getCanonicalName());
		addProcessor(AiClassProcessor.class.getCanonicalName());
	}

	@Override
	protected JSAP defineArgs() throws JSAPException {
		JSAP jsap = super.defineArgs();
		Switch sw1;
		FlaggedOption opt2;
		
		// Verbose
		sw1 = (Switch)jsap.getByLongFlag("verbose");
sw1.setDefault("true");

		// Super Verbose
		sw1 = (Switch)jsap.getByLongFlag("vvv");
sw1.setDefault("true");

		// java compliance
		opt2 = (FlaggedOption)jsap.getByLongFlag("compliance");
		opt2.setDefault("6");

		// Disable output generation
		sw1 = (Switch)jsap.getByLongFlag("no");
sw1.setDefault("true");

		// Compile Output files
		sw1 = (Switch)jsap.getByLongFlag("compile");
		sw1.setDefault("false");

		return jsap;
	}

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
	private void parseFiles(List<File> files) throws Exception
	{	System.out.println("Analyse des fichiers ");
		
		// on initialise le parser avec les fichiers à traiter
		getInputSources().clear();
		for(File file: files)
		{	CtResource input = FileFactory.createResource(file);
			addInputResource(input);
		}
		
		// on lance le traitement
		run();

		// on récupère le nombre d'erreurs
//		getProcessorTypes()
        
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
	private List<File> scanFolder(File folder) throws Exception
	{	List<File> result = new ArrayList<File>();
		String name = folder.getName();
		
		// pour ignorer les dossiers svn
		if(!name.contains(".svn"))
		{	// packages à ignorer (versions antérieures)
			if(IGNORED_PACKAGES.contains(name))
				System.out.println("Paquetage "+folder.getPath()+" ignoré");
			// analyse du package
			else
			{	System.out.println("Analyse du paquetage "+folder.getPath());
			
				File[] files = folder.listFiles();
				for(File file: files)
				{	if(file.isDirectory())
						result.addAll(scanFolder(file));
					else
					{	String filename = file.getName();
						if(filename.endsWith(FileNames.EXTENSION_JAVA))
							result.add(file);
						else if(VERBOSE)
							System.out.println("Le fichier "+file.getPath()+" n'a pas été reconnu comme un fichier source Java");
					}
				}
			}
	    	System.out.println("total pour le paquetage "+name+" : "+result.size());
		}
		
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
	{	if(!aiFolder.getName().contains(".svn"))
		{	System.out.println("----------------------------------------------------------------------");
			System.out.println("Analyse de l'agent "+aiFolder.getPath());
			System.out.println("----------------------------------------------------------------------");
			List<File> files = scanFolder(aiFolder);
			parseFiles(files);
//		System.out.println("total pour l'agent "+aiFolder.getName()+" : "+errorCount);
			// TODO
			System.out.print("\n\n");
		}
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
}
