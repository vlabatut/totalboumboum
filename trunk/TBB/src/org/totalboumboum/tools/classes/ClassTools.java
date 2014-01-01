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
	 * For instance: {@code getClasseNamesInPackage("C:/j2sdk1.4.1_02/lib/mail.jar", "com.sun.mail.handlers");}.
	 * 
	 * @param jarName	
	 * 		JAR file path.
	 * @param packageName	
	 * 		Package qualified name.
	 * @return	
	 * 		Array of classes.
	 */
	public static Class<?>[] getClassesInPackageJar(String jarName,String packageName)
	{	List<Class<?>> classes = new ArrayList<Class<?>>();
		packageName = packageName.replaceAll("\\.", "/");
		try
		{	JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
			JarEntry jarEntry;
			while (true)
			{	jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null)
					break;
				if((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class")))
				{	String temp = jarEntry.getName().replaceAll("/", "\\.");
					classes.add(Class.forName(temp.substring(0, temp.length() - 6)));
				}
			}
			jarFile.close();
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
	
	/**
	 * Returns the list of the names of classes located in a package (not inside a JAR).
	 * 
	 * @param packageName	
	 * 		Package qualified name.
	 * @return	
	 * 		Array of classes.
	 * 
	 * @throws ClassNotFoundException 
	 * 		Problem while accessing the specified class.
	 */
	public static Class<?>[] getClassesInPackage(String packageName) throws ClassNotFoundException 
	{	List<Class<?>> classes = new ArrayList<Class<?>>();
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
	
	/**
	 * Returns the list of the names of classes located in a package implementing
	 * the specified interface.
	 * 
	 * @param packageName	
	 * 		Package qualified name.
	 * @param inter
	 * 		Interface the classes must implement.
	 * @return	
	 * 		Array of classes.
	 * 
	 * @throws ClassNotFoundException 
	 * 		Problem while accessing the specified class.
	 */
	public static Class<?>[] getClassesInPackageImplementing(String packageName, Class<?> inter) throws ClassNotFoundException
	{	List<Class<?>> classes = new ArrayList<Class<?>>();
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
