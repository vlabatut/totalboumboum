package org.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import org.totalboumboum.tools.classes.ClassTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

/**
 * Classe dont le rôle est de charger
 * les classes de l'agent dans la JVM,
 * puis de l'instancier pour obtenir un agent.
 *  
 * @author Vincent Labatut
 *
 */
public class AiLoader
{
	/**
	 * Charge les classes de l'agent et
	 * les instancie pour obtenir un agent.
	 * 
	 * @param name
	 * 		Nom du dossier de l'agent.
	 * @param packname
	 * 		Nom du paquetage de l'agent.
	 * @return
	 * 		L'objet gérant le nouvel agent.
	 * @throws FileNotFoundException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws ClassNotFoundException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws IllegalArgumentException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws SecurityException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws InstantiationException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws IllegalAccessException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws InvocationTargetException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 * @throws NoSuchMethodException
	 * 		Problème lors de l'accès aux classes de l'agent.
	 */
	public static AiAbstractManager<?> loadAi(String name, String packname) throws FileNotFoundException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{	AiAbstractManager<?> result;
		
		// check the file
		String packageFolder = FilePaths.getAisPath()+File.separator+packname+File.separator+FileNames.FILE_AIS+File.separator+name;
		String classFile = packageFolder+File.separator+FileNames.FILE_AI_MAIN_CLASS+FileNames.EXTENSION_CLASS;
		File file = new File(classFile);
		if(!file.exists())
			throw new FileNotFoundException(classFile);
		
		// load the class
		String packageName = ClassTools.getTbbPackage()+ClassTools.CLASS_SEPARATOR+FileNames.FILE_AI+ClassTools.CLASS_SEPARATOR+packname+ClassTools.CLASS_SEPARATOR+FileNames.FILE_AIS+ClassTools.CLASS_SEPARATOR+name;
		String classQualifiedName = packageName+ClassTools.CLASS_SEPARATOR+FileNames.FILE_AI_MAIN_CLASS;
		Class<?> tempClass = Class.forName(classQualifiedName);
		if(!AiAbstractManager.class.isAssignableFrom(tempClass))
			throw new ClassCastException(classQualifiedName);
		
		// build an instance
		result = (AiAbstractManager<?>)tempClass.getConstructor().newInstance();
		return result;
	}
}
