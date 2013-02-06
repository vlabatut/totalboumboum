package org.totalboumboum.ai.v201314.adapter.agent;

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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.tools.classes.ClassTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Classe dont le rôle est de charger un fichier
 * XML contenant les préférences (utilité) de l'agent.
 *  
 * @author Vincent Labatut
 */
public class AiUtilityLoader
{
	/**
	 * Charge les informations décrivant un agent,
	 * stockés dans un fichier XML.
	 * 
	 * @param pack
	 * 		Paquetage contenant l'agent.
	 * @param folder
	 * 		Dossier spécifique à l'agent.
	 * @param agent
	 * 		Classe de l'agent concerné.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws SAXException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws IOException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws InvocationTargetException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InstantiationException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws IllegalAccessException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws NoSuchMethodException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 */
	public static <T extends ArtificialIntelligence> void loadAiPreferences(String packName, String aiName, T ai) throws ParserConfigurationException, SAXException, IOException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	// error message prefix
		String errMsg = "Error while loading agent "+packName+"/"+aiName+": ";

		// agent path
		String agentPath = FilePaths.getAisPath()+File.separator+packName+File.separator+FileNames.FILE_AIS+File.separator+aiName;
		
		// init criteria
		initCriteria(agentPath, ai, errMsg);
		
		// load xml file
		File dataFile = new File(agentPath+File.separator+FileNames.FILE_PREFERENCES+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PREFERENCES+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadPreferencesElement(root, ai, errMsg);
	}
	
	
	/*
	 * TODO TODO
	 * 	- dans case, pas besoin d'ai 
	 * 		(sauf si détermination du cas dans la classe du cas. là, on a besoin d'ai)
	 * 		>> il faut faire le test d'unicité (du nom) lors de l'insertion
	 *  - dans critère, besoin d'ai pour utilisation console durant calul du critère
	 */
	
	
	// IllegalArgumentException, ClassCastException
	private static <T extends ArtificialIntelligence> void initCriteria(String agentPath, T ai, String errMsg) throws NoSuchMethodException, FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	// get the agent main folder
		File agentFolder = new File(agentPath);
		if(!agentFolder.exists())
			throw new FileNotFoundException(errMsg+"cannot find the agent folder "+agentPath);

		// process folder and subfolders
		scanFolderForCriteria(agentFolder, ai, errMsg);
	}


	/*
	 * TODO TODO
	 * dans la doc, rajouter toutes les exceptions possibles, avec leur cause,
	 * dans la partie troubleshooting
	 */
	
	private static <T extends ArtificialIntelligence> void scanFolderForCriteria(File packageFolder, T ai, String errMsg) throws NoSuchMethodException, FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	String folderPath = FilePaths.getAisPath()+File.separator;

		// get the list of criterion classes
		{	FileFilter filter = new FileFilter()
			{	@Override
				public boolean accept(File file)
				{	String name = file.getName();
					int length = name.length();
					int extLength = FileNames.EXTENSION_CLASS.length();
					String ext = name.substring(length-extLength,length);
					return ext.equalsIgnoreCase(FileNames.EXTENSION_CLASS);
				}			
			};
			File[] files = packageFolder.listFiles(filter);
	
			// process each criterion class
			for(File file: files)
			{	// get the class name
				String className = file.getAbsolutePath();
				// remove file extension
				className = className.substring(0,className.length()-FileNames.EXTENSION_CLASS.length());
				// remove folder
				className = className.substring(folderPath.length(),className.length());
				// replace '/' with '.'
				className = className.replace(Character.toString(File.separatorChar),ClassTools.CLASS_SEPARATOR);
				
				// get the class
				Class<?> tempClass = Class.forName(className);
				// check if it is a criterion
				if(AiUtilityCriterion.class.isAssignableFrom(tempClass))
				{	// create an instance
					AiUtilityCriterion<T, ?> criterion = null;
					try
					{	Constructor<?> constructor = tempClass.getConstructor();
						criterion = (AiUtilityCriterion<T, ?>)constructor.newInstance(ai);
					}
					catch (NoSuchMethodException e)
					{	throw new NoSuchMethodException(errMsg+"No constructor could be found for class '"+className+"'.");
					}
					catch (IllegalArgumentException e)
					{	throw new IllegalArgumentException(errMsg+"No approproate constructor could be found for class '"+className+"': need one constructor whose only parameter has the agent's main class as a type.");
					}
					catch (InstantiationException e)
					{	throw new InstantiationException(errMsg+"Cannot perform instantiation of class '"+className+"': maybe the class is abstract, or it is an interface, etc.");
					}
					catch (IllegalAccessException e)
					{	throw new IllegalAccessException(errMsg+"Cannot access the constructor of class '"+className+"': it might be a visibility problem, e.g. a private constructor.");
					}

					// insert criterion in agent
					try
					{	AiUtilityHandler<T> handler = (AiUtilityHandler<T>)ai.getUtilityHandler();
						handler.insertCriterion(criterion);
					}
					catch(IllegalArgumentException e)
					{	throw new IllegalArgumentException(errMsg+e.getMessage());
					}
					catch (StopRequestException e)
					{	// theoretically impossible during loading
						e.printStackTrace();
					}
				}
			}
		}
		
		// get the list of folders
		{	FileFilter filter = new FileFilter()
			{	@Override
				public boolean accept(File file)
				{	boolean result = file.isDirectory();
					return result;
				}			
			};
			File[] folders = packageFolder.listFiles(filter);
			
			// process each folder
			for(File folder: folders)
				scanFolderForCriteria(folder, ai, errMsg);
		}
	}
	
	/**
	 * Traite un élément XML pour en extraire l'information
	 * nécessaire à l'initialisation de l'objet représentant
	 * l'agent.
	 * 
	 * @param root
	 * 		Element à traiter.
	 * @param result
	 * 		Objet à initialiser.
	 */
	private static <T extends ArtificialIntelligence> void loadPreferencesElement(Element root, T ai, String errMsg)
	{	// load categories
		Element categoriesElt = root.getChild(XmlNames.CATEGORIES);
		loadCategoriesElement(categoriesElt,ai,errMsg);
		
		// load combinations
		Element tablesElt = root.getChild(XmlNames.TABLES);
		loadTablesElement(tablesElt, ai, errMsg);
	}
	
	private static <T extends ArtificialIntelligence> void loadCategoriesElement(Element root, T ai, String errMsg)
	{	List<Element> elements = root.getChildren(XmlNames.CATEGORY);
		for(Element element: elements)
			loadCategoryElement(element,ai,errMsg);
	}
	
	// IllegalArgumentException
	private static <T extends ArtificialIntelligence> void loadCategoryElement(Element root, T ai, String errMsg)
	{	// retrieve the category name
		String name = root.getAttributeValue(XmlNames.NAME);

		// get the preference handler
		AiUtilityHandler<T> handler = null;
		try
		{	handler = (AiUtilityHandler<T>)ai.getUtilityHandler();
		}
		catch (StopRequestException e1)
		{	// theoretically impossible here
			e1.printStackTrace();
		}
	
		// read criterion list
		List<Element> elements = root.getChildren(XmlNames.CRITERION);
		Set<AiUtilityCriterion<?,?>> categoryCriteria = new TreeSet<AiUtilityCriterion<?,?>>();
		for(Element element: elements)
		{	String critName = element.getAttributeValue(XmlNames.NAME);
			AiUtilityCriterion<T,?> crit = (AiUtilityCriterion<T,?>)(handler.getCriterion(name));
			if(crit==null)
				throw new IllegalArgumentException(errMsg+"criterion '"+critName+"' used in category '"+name+"' is undefined (you must first define the criterion as a class, using '"+critName+"' as its name).");
			categoryCriteria.add(crit);
		}
		
		// build category and complete map
		try
		{	AiUtilityCase category = new AiUtilityCase(name,categoryCriteria);
			handler.insertCase(category);
		}
		catch(IllegalArgumentException e)
		{	throw new IllegalArgumentException(errMsg+e.getMessage());
		}
	}
	
	private static <T extends ArtificialIntelligence> void loadTablesElement(Element root, T ai, String errMsg)
	{	List<Element> elements = root.getChildren(XmlNames.TABLE);
		for(Element element: elements)
			loadTableElement(element,ai,errMsg);
	}
	
	/**
	 * Traite un élément XML pour en extraire l'information
	 * nécessaire à l'initialisation de l'objet représentant
	 * l'agent.
	 * 
	 * @param root
	 * 		Element à traiter.
	 * @param result
	 * 		Objet à initialiser.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ArtificialIntelligence> void loadTableElement(Element root, T ai, String errMsg)
	{	// get the mode
		String modeStr = root.getAttributeValue(XmlNames.MODE);
		AiMode mode = AiMode.valueOf(modeStr);
		
		// process combinations
		List<Element> combiElts = root.getChildren(XmlNames.COMBINATION);
		for(Element combiElt: combiElts)
			loadCombinationElement(combiElt,mode,ai,errMsg);
	}
	
	private static <T extends ArtificialIntelligence> void loadCombinationElement(Element root, AiMode mode, T ai, String errMsg)
	{	// get the preference handler
		AiUtilityHandler<T> handler = null;
		try
		{	handler = (AiUtilityHandler<T>)ai.getUtilityHandler();
		}
		catch (StopRequestException e1)
		{	// theoretically impossible here
			e1.printStackTrace();
		}
		
		// get the category
		String categoryStr = root.getAttributeValue(XmlNames.CATEGORY);
		AiUtilityCase category = handler.getCategory(categoryStr);
		if(category==null)
			throw new IllegalArgumentException(errMsg+"category '"+categoryStr+"' is used in a combination, but is is undefined (you must first define the category in the preference XML file).");
		AiUtilityCombination combination = new AiUtilityCombination(category);
		
/*
 * TODO TODO
 * - insérer directement la combinaison dans le gestionnaire
 * - dans le gestionnaire, vérifier la redondance de combinaison
 */
		
		// get the criterion values
		List<Element> valueElts = root.getChildren(XmlNames.VALUE);
		for(Element valueElt: valueElts)
		{	String criterionStr = valueElt.getAttributeValue(XmlNames.CRITERION);
			AiUtilityCriterion<T,?> criterion = (AiUtilityCriterion<T,?>)(handler.getCriterion(criterionStr));
			if(criterion==null)
				throw new IllegalArgumentException(errMsg+"criterion '"+criterionStr+"' is used in a combination, but is is undefined (you must first define the criterion as a class, using '"+criterionStr+"' as its name).");
			String valueStr = valueElt.getValue();
			combination.setCriterionValue(criterion, valueStr);
		}
	}
}
