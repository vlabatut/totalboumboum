package fr.free.totalboumboum.tools;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;


public class ClassTools
{	
	
	/**
	 * Renvoie la liste des noms des classes situ�es dans un package dans un Jar. 
	 * exemple : getClasseNamesInPackage("C:/j2sdk1.4.1_02/lib/mail.jar", "com.sun.mail.handlers");
	 * @param jarName	chemin du Jar
	 * @param packageName	nom complet du package
	 * @return	liste des classes
	 */
	public static Class<?>[] getClassesInPackageJar(String jarName,String packageName)
	{	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
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
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
	
	/**
	 * Renvoie la liste des noms des classes situ�es dans un package (pas dans un Jar). 
	 * @param packageName	nom complet du package
	 * @return	liste des classes
	 */
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
}