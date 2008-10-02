package fr.free.totalboumboum.ai;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;

public class AiLoader
{
	public static Class<?>[] getClassesInPackage(String packageName) throws ClassNotFoundException 
	{	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		// Get a File object for the package
		File directory = null;
		try
		{	ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");
			String path = packageName.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null)
				throw new ClassNotFoundException("No resource for " + path);
			directory = new File(resource.getFile());
		}
		catch (NullPointerException x)
		{	throw new ClassNotFoundException(packageName + " (" + directory + ") does not appear to be a valid package");
		}
		if (directory.exists())
		{	// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++)
			{	// we are only interested in .class files
				if (files[i].endsWith(".class"))
				{	// removes the .class extension
					classes.add(Class.forName(packageName + '.' + files[i].substring(0, files[i].length() - 6)));
				}
			}
		}
		else
			throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
	
	public static Class<?>[] getClassesInPackageImplementing(String packageName, Class<?> inter) throws ClassNotFoundException
	{	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?>[] temp = getClassesInPackage(packageName);
		for(int i=0;i<temp.length;i++)
			if(inter.isAssignableFrom(temp[i]) 
					&& !temp[i].isInterface() 
					&& !Modifier.isAbstract(temp[i].getModifiers()))
				classes.add(temp[i]);
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}	

}
