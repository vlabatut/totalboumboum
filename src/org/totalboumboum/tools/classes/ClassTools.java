package org.totalboumboum.tools.classes;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.tools.files.FileNames;

/**
 * Methods related to Java class management.
 * 
 * @author Vincent Labatut
 */
public class ClassTools
{	/** Character used to separate class and package names */
	public static final String CLASS_SEPARATOR = ".";

	/**
	 * Returns the list of the names of classes located in a package inside a JAR file.
	 * 
	 * For instance: {@code getClasseNamesInPackage("C:/j2sdk1.4.1_02/lib/mail.jar", "com.sun.mail.handlers");}.
	 * NOTE not tested
	 * 
	 * @param jarName	
	 * 		JAR file path.
	 * @param packageName	
	 * 		Package qualified name.
	 * @param recursive	
	 * 		Whether subpackages should also be processed.
	 * @return	
	 * 		Array of classes.
	 */
	public static Class<?>[] getClassesInPackageJar(String jarName, String packageName, boolean recursive)
	{	List<Class<?>> classes = new ArrayList<Class<?>>();
		String packageFolder = packageName.replaceAll("\\.", "/");
		
		try
		{	JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
			JarEntry jarEntry = jarFile.getNextJarEntry();
			while(jarEntry!=null)
			{	String entryName = jarEntry.getName();
				if((entryName.startsWith(packageFolder)) && entryName.endsWith(FileNames.EXTENSION_CLASS))
				{	String subPack = entryName.substring(packageFolder.length()+1,entryName.length()-FileNames.EXTENSION_CLASS.length());
					if(recursive || !subPack.contains("/"))
					{	String entryPackage = jarEntry.getName().replaceAll("/", "\\.");
						String qualifName = entryPackage.substring(0, entryPackage.length() - FileNames.EXTENSION_CLASS.length());
						Class<?> clazz = Class.forName(qualifName);
						classes.add(clazz);
					}
				}
				jarEntry = jarFile.getNextJarEntry();
			}
			jarFile.close();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
		
		// sets up result
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
	
	/**
	 * Returns the list of the names of classes located in a package (not inside a JAR).
	 * 
	 * @param packageName	
	 * 		Package qualified name.
	 * @param recursive	
	 * 		Whether subpackages should also be processed.
	 * @return	
	 * 		Array of classes.
	 * 
	 * @throws ClassNotFoundException 
	 * 		Problem while accessing the specified class.
	 */
	public static Class<?>[] getClassesInPackage(String packageName, boolean recursive) throws ClassNotFoundException 
	{	// Get a File object for the package
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
		
		// process the file object corresponding to the package 
		List<Class<?>> classes = null;
		if (directory.exists())
			classes = getClassesInFolder(packageName, directory, recursive);
		else
			throw new ClassNotFoundException(packageName + " does not appear to be a valid package");
		
		// sets up result
		Class<?>[] result = new Class[classes.size()];
		classes.toArray(result);
		return result;
	}
	
	/**
	 * Helper method used by {@link #getClassesInPackage(String, boolean)}. 
	 * Returns the list of the names of classes located in a folder (not inside a JAR).
	 * 
	 * @param currentPackage	
	 * 		Qualified name of the current package.
	 * @param currentFolder	
	 * 		Current folder.
	 * @param recursive	
	 * 		Whether subpackages should also be processed.
	 * @return	
	 * 		List of classes.
	 * 
	 * @throws ClassNotFoundException 
	 * 		Problem while accessing the specified class.
	 */
	private static List<Class<?>> getClassesInFolder(String currentPackage, File currentFolder, boolean recursive) throws ClassNotFoundException
	{	List<Class<?>> result = new ArrayList<Class<?>>();
	
		// Get the list of the files contained in the package
		File[] files = currentFolder.listFiles();
		
		// process each one
		for (int i = 0; i < files.length; i++)
		{	String fileName = files[i].getName();
		
			// if it is a folder: explore recursively
			if(files[i].isDirectory())
			{	// (only if required)
				if(recursive)
				{	String tempPackage = currentPackage + CLASS_SEPARATOR + fileName;
					List<Class<?>> temp = getClassesInFolder(tempPackage, files[i], recursive);
					result.addAll(temp);
				}
			}
			
			// if it's an actual file
			else
			{	// we are only interested in .class files
				if (fileName.endsWith(FileNames.EXTENSION_CLASS))
				{	// removes the .class extension
					String className = currentPackage + CLASS_SEPARATOR + fileName.substring(0, fileName.length() - FileNames.EXTENSION_CLASS.length());
					// get the class;
					result.add(Class.forName(className));
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * Returns the list of the names of classes located in a package implementing
	 * the specified interface.
	 * 
	 * @param packageName	
	 * 		Package qualified name.
	 * @param inter
	 * 		Interface the classes must implement.
	 * @param recursive	
	 * 		Whether subpackages should also be processed.
	 * @return	
	 * 		Array of classes.
	 * 
	 * @throws ClassNotFoundException 
	 * 		Problem while accessing the specified class.
	 */
	public static Class<?>[] getClassesInPackageImplementing(String packageName, Class<?> inter, boolean recursive) throws ClassNotFoundException
	{	List<Class<?>> classes = new ArrayList<Class<?>>();
		Class<?>[] temp = getClassesInPackage(packageName,recursive);
		for(int i=0;i<temp.length;i++)
			if(inter.isAssignableFrom(temp[i]) 
					&& !temp[i].isInterface() 
					&& !Modifier.isAbstract(temp[i].getModifiers()))
				classes.add(temp[i]);
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

	/**
	 * Returns the class corresponding to the specified sprite role.
	 *  
	 * @param name
	 * 		Role name.
	 * @return
	 * 		Corresponding class.
	 */
	public static Class<?> getClassFromRole(String name)
	{	Class<?> result = null;
		if(name.equalsIgnoreCase("block"))
			result = Block.class;
		else if(name.equalsIgnoreCase("bomb"))
			result = Bomb.class;
		else if(name.equalsIgnoreCase("fire"))
			result = Fire.class;
		else if(name.equalsIgnoreCase("floor"))
			result = Floor.class;
		else if(name.equalsIgnoreCase("hero"))
			result = Hero.class;
		else if(name.equalsIgnoreCase("item"))
			result = Item.class;
		return result;
	}
	
	/**
	 * Returns the field associated to a specific value in the specified class.
	 * 
	 * @param value
	 * 		Value of interest.
	 * @param cls
	 * 		Concerned class.
	 * @return
	 * 		Field with this value.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the class fields.
	 * @throws IllegalAccessException
	 * 		Problem while accessing the class fields.
	 */
	public static Field getFieldFromValue(int value, Class<?> cls) throws IllegalArgumentException, IllegalAccessException
	{	Field result = null;
		Field[] fields = cls.getFields();
		int i = 0;
		while(i<fields.length && result==null)
		{	Type type = fields[i].getType();
			if(type.equals(int.class))
			{	int mod = fields[i].getModifiers();
				if(Modifier.isFinal(mod) && Modifier.isPublic(mod) && Modifier.isStatic(mod))
				{	int v = fields[i].getInt(cls);
					if(value==v)
						result = fields[i];				
				}
			}
			i++;
		}
		return result;
	}

	/**
	 * Gets the root package of the game.
	 * 
	 * @return
	 * 		Game main package (as a string).
	 */
	public static String getTbbPackage()
	{	return FileNames.FILE_ORG + CLASS_SEPARATOR + FileNames.FILE_TOTALBOUMBOUM;		
	}
}
