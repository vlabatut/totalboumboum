package fr.free.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.tools.ClassTools;
import fr.free.totalboumboum.tools.FileTools;

public class AiLoader
{
	public static AbstractAiManager<?> loadAi(String name, String packname) throws FileNotFoundException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{	AbstractAiManager<?> result;
		
		// check the file
		String packageFolder = FileTools.getAiPath()+File.separator+packname+File.separator+name;
		String classFile = packageFolder+File.separator+FileTools.FILE_AI_MAIN_CLASS+FileTools.EXTENSION_CLASS;
		File file = new File(classFile);
		if(!file.exists())
			throw new FileNotFoundException(classFile);
		
		// load the class
		String packageName = packname+ClassTools.CLASS_SEPARATOR+name;
		String classQualifiedName = packageName+ClassTools.CLASS_SEPARATOR+FileTools.FILE_AI_MAIN_CLASS;
		Class<?> tempClass = Class.forName(classQualifiedName);
		if(!AbstractAiManager.class.isAssignableFrom(tempClass))
			throw new ClassCastException(classQualifiedName);
		
		// build an instance
		result = (AbstractAiManager<?>)tempClass.getConstructor().newInstance();
		return result;
	}
}
