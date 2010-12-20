package org.totalboumboum.ai.v201011.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.tools.files.FileNames;

/**
 * cette méthode parse les codes sources définissant une IA et vérifie
 * que les appels à checkInterruption sont effectués correctement.
 * 
 * La liste IGNORED_PACKAGES permet, comme son nom l'indique, de spécifier
 * des packages que le parser devra ignorer dans le dossier principal de l'IA.
 * 
 * Dans la fonction main, la chaine de caractères aiPack représente le chemin 
 * du dossier contenant l'ia (ou les ia).
 * 
 * @author Vincent Labatut
 */
public class ParseAi
{	private static boolean verbose = false;
	private final static List<String> IGNORED_PACKAGES = Arrays.asList(new String[]
 	{	"v1","v1_1","v1_2","v1_3",
 		"v2","v2_1","v2_2","v2_3",
 		"v3","v3_1","v3_2","v3_3"
// 		"v4","v4_1","v4_2","v4_3",
// 		"v5_1"
 	});
	
	public static void main(String[] args) throws IOException, ParseException
	{	//String aiPack = "resources/ai/org/totalboumboum/ai/v200910/ais";
		String aiPack = "../TBBtemp/src/org/totalboumboum/ai/v201011/ais";
		parseAiPack(aiPack);
	}
	
	private static void parseFile(File file, int level) throws ParseException, IOException
	{	for(int i=0;i<level;i++)
			System.out.print("..");
		System.out.println("Analyse de la classe "+file.getPath());
		
		// creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(file);
        
        CompilationUnit cu;
        try
        {	// parse the file
            cu = JavaParser.parse(in);
        }
        finally
        {	in.close();
        }

        // display trace
        AiVisitor visitor = new AiVisitor(level);
        visitor.visit(cu,null);
        int errorCount = visitor.getErrorCount();
        if(errorCount>0)
        {	for(int i=0;i<level;i++)
				System.out.print("..");
        	System.out.println("   total : "+errorCount);
        }
	}
	
	private static void parseFolder(File folder, int level) throws ParseException, IOException
	{	if(IGNORED_PACKAGES.contains(folder.getName()))
			System.out.println("Paquetage "+folder.getPath()+" ignoré");
		else
		{	System.out.println("Analyse du paquetage "+folder.getPath());
		
			File[] files = folder.listFiles();
			for(File file: files)
			{	if(file.isDirectory())
					parseFolder(file,level+1);
				else
				{	String filename = file.getName();
					if(filename.endsWith(FileNames.EXTENSION_JAVA))
						parseFile(file,level+1);
					else if(verbose)
					{	for(int i=0;i<level;i++)
							System.out.print("..");
						System.out.println("Le fichier "+file.getPath()+" n'a pas été reconnu comme un source Java");
					}
				}
			}
		}
	}

	public static void parseAi(String aiPath) throws ParseException, IOException
	{	File aiFolder = new File(aiPath);
		parseAi(aiFolder);
	}
	private static void parseAi(File aiFolder) throws ParseException, IOException
	{	System.out.println("----------------------------------------------------------------------");
		System.out.println("Analyse de l'AI "+aiFolder.getPath());
		System.out.println("----------------------------------------------------------------------");
		parseFolder(aiFolder,0);
		System.out.print("\n\n");
	}
	
	public static void parseAiPack(String aiPack) throws ParseException, IOException
	{	File folder = new File(aiPack);
	String temp = folder.getAbsolutePath();
		File[] files = folder.listFiles();
		for(File file: files)
		{	if(file.isDirectory())
				parseAi(file);
		}		
	}
}
