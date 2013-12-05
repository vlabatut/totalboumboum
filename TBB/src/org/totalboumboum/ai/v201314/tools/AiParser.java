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

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.Comment;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.tools.files.FileNames;

/**
 * Cette classe parse les codes sources définissant un agent et vérifie
 * que les appels à checkInterruption sont effectués correctement.
 * <br/>
 * La liste {@link #IGNORED_PACKAGES} permet, comme son nom l'indique, de spécifier
 * des packages que le parser devra ignorer dans le dossier principal de l'agent.
 * <br/>
 * Dans la fonction {@link #main(String[])}, la chaine de caractères {@code aiPack} représente le chemin 
 * du dossier contenant l'agent (ou les agents).
 * 
 * @author Vincent Labatut
 */
public class AiParser
{	/** Active/désactive la sortie texte <i>détaillée</i> */
	public static boolean verbose = false;
	/** Liste des versions à ignorer */
	public final static List<String> IGNORED_PACKAGES = new ArrayList<String>(Arrays.asList(new String[]
 	{	
		"v0",
		"v1","v1_1","v1_2","v1_3",
 //		"v2","v2_1","v2_2","v2_3",
 		"v3","v3_1","v3_2","v3_3",
 		"v4","v4_1","v4_2","v4_3",
 		"v5","v5_1"
 	}));
	/** Marqueur Eclipse */
	private final static String ECLIPSE_TAG = "TODO";
	
	/**
	 * Programme principal.
	 * Ne prend pas de paramètres.
	 * 
	 * @param args
	 * 		Rien du tout.
	 */
	public static void main(String[] args)
	{	// on définit le chemin du pack à analyser
//		String aiPack = "resources/ai/org/totalboumboum/ai/v201314/ais";
		String aiPack = "../TBBtemp/src/org/totalboumboum/ai/v201314/ais";
		
		// on lance l'analyse
		parseAiPack(aiPack);
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
	 */
	private static int parseFile(File file, int level)
	{	int result = Integer.MAX_VALUE;
		
		for(int i=0;i<level;i++)
			System.out.print("..");
		System.out.println("Analyse du fichier "+file.getPath());
		
        try
        {	// creates an input stream for the file to be parsed
            FileInputStream in = new FileInputStream(file);
            // parse the file
            CompilationUnit cu = JavaParser.parse(in);
            in.close();
 
            // display trace
            AiVisitor visitor = new AiVisitor(level);
            visitor.visit(cu,null);
            result = visitor.getErrorCount();
            
            // comments must be tested a posteriori
            List<Comment> comments = cu.getComments();
            for(Comment comment: comments)
            {	// NOTE when there are several comments in a row,
                // the parser might not detect them
            	String content = comment.getContent();
        		if(content.contains(ECLIPSE_TAG))
        		{	int line = comment.getBeginLine();
        			visitor.printErr("Erreur ligne "+line+" : le commentaire contient un marqueur TODO. Vous devez tous les retirer avant de rendre votre agent.");
        			result++;
        		}
            }
            
            // display total
            if(result>0)
            {	for(int i=0;i<level;i++)
    				System.out.print("..");
            	System.out.println("   total pour le fichier "+file.getName()+" : "+result);
            }
        }
        catch(ParseException e)
        {	e.printStackTrace();
        }
        catch(IOException e)
        {	e.printStackTrace();
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
	 */
	private static int parseFolder(File folder, int level)
	{	int result = 0;
		if(IGNORED_PACKAGES.contains(folder.getName()))
			System.out.println("Paquetage "+folder.getPath()+" ignoré");
		else if(!folder.getPath().contains(".svn"))
		{	System.out.println("Analyse du paquetage "+folder.getPath());
		
			File[] files = folder.listFiles();
			for(File file: files)
			{	if(file.isDirectory())
					result = result + parseFolder(file,level+1);
				else
				{	String filename = file.getName();
					if(filename.endsWith(FileNames.EXTENSION_JAVA))
						result = result + parseFile(file,level+1);
					else if(verbose)
					{	for(int i=0;i<level;i++)
							System.out.print("..");
						System.out.println("Le fichier "+file.getPath()+" n'a pas été reconnu comme un fichier source Java");
					}
				}
			}
			
	    	System.out.println("total pour le paquetage "+folder.getName()+" : "+result);
		}
		return result;
	}

	/**
	 * Analyse un package correspondant à un agent.
	 * 
	 * @param aiPath
	 * 		Le chemin du package à analyser.
	 */
	public static void parseAi(String aiPath)
	{	File aiFolder = new File(aiPath);
		parseAi(aiFolder);
	}
	
	/**
	 * Analyse un package correspondant à un agent.
	 * 
	 * @param aiFolder
	 * 		Un objet représentant le package à analyser.
	 */
	public static void parseAi(File aiFolder)
	{	 if(!aiFolder.getPath().contains(".svn"))
		{	System.out.println("----------------------------------------------------------------------");
			System.out.println("Analyse de l'agent "+aiFolder.getPath());
			System.out.println("----------------------------------------------------------------------");
			int errorCount = parseFolder(aiFolder,0);
			System.out.println("total pour l'agent "+aiFolder.getName()+" : "+errorCount);
			System.out.print("\n\n");
		}
	}
	
	/**
	 * Analyse un dossier contenant plusieurs agents
	 * (un pack).
	 * 
	 * @param aiPack
	 * 		Chemin du dossier à analyser.
	 */
	public static void parseAiPack(String aiPack)
	{	//System.out.println("Analyse du pack "+aiPack);
		File folder = new File(aiPack);
		File[] files = folder.listFiles();
		for(File file: files)
		{	if(file.isDirectory())
				parseAi(file);
		}		
	}
}
