package org.totalboumboum.ai.v200910.tools;

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

import org.totalboumboum.tools.files.FileNames;

public class ParseAi
{
	public static void main(String[] args) throws IOException, ParseException
	{	String aiPack = "resources/ai/org/totalboumboum/ai/v200809/ais";
		parseAiPack(aiPack);
	}
	
	private static void parseFile(File file) throws ParseException, IOException
	{	System.out.println("Analyse de la classe "+file.getPath());
		
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
        AiVisitor visitor = new AiVisitor();
        visitor.visit(cu, null);
	}
	
	private static void parseFolder(File folder) throws ParseException, IOException
	{	System.out.println("Analyse du paquetage "+folder.getPath());
	
		File[] files = folder.listFiles();
		for(File file: files)
		{	if(file.isDirectory())
				parseFolder(file);
			else
			{	String filename = file.getName();
				if(filename.endsWith(FileNames.EXTENSION_JAVA))
					parseFile(file);
				else
					System.out.println("Le fichier "+file.getPath()+" n'a pas été reconnu comme un source Java");
			}
		}		
	}

	public static void parseAi(String aiPath) throws ParseException, IOException
	{	File aiFolder = new File(aiPath);
		parseAi(aiFolder);
	}
	private static void parseAi(File aiFolder) throws ParseException, IOException
	{	System.out.println("---------------------------------------------");
		System.out.println("Analyse de l'AI "+aiFolder.getPath());
		parseFolder(aiFolder);
		System.out.println();
	}
	
	public static void parseAiPack(String aiPack) throws ParseException, IOException
	{	File folder = new File(aiPack);
		File[] files = folder.listFiles();
		for(File file: files)
		{	if(file.isDirectory())
				parseAi(file);
		}		
	}
}
