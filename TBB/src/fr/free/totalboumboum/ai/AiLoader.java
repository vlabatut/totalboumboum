package fr.free.totalboumboum.ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

import fr.free.totalboumboum.tools.FileTools;

public class AiLoader
{
	public static AbstractAiManager<?> loadAi(String name, String packname) throws FileNotFoundException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{	AbstractAiManager<?> result;
		
		// check the file
		String packageFolder = FileTools.getAiPath()+File.separator+packname+File.separator+name;
		String classFile = packageFolder+File.separator+FileTools.FILE_AI+FileTools.EXTENSION_CLASS;
		File file = new File(classFile);
		if(!file.exists())
			throw new FileNotFoundException(classFile);
		
		// load the class
		String packageName = packname+FileTools.CLASS_SEPARATOR+name;
		String classQualifiedName = packageName+FileTools.CLASS_SEPARATOR+FileTools.FILE_AI;
		Class<?> tempClass = Class.forName(classQualifiedName);
		if(!AbstractAiManager.class.isAssignableFrom(tempClass))
			throw new ClassCastException(classQualifiedName);
		
		// build an instance
		result = (AbstractAiManager<?>)tempClass.getConstructor().newInstance();
		return result;
	}
}
