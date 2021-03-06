package org.totalboumboum.ai.v201415.adapter.agent;

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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.tools.classes.ClassTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Classe dont le rôle est de charger un fichier
 * XML contenant les préférences de l'agent.
 *  
 * @author Vincent Labatut
 */
public class AiPreferenceLoader
{	
    /////////////////////////////////////////////////////////////////
	// HANDLERS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map utilisée pour stocker les préférences des agents chargés */
	private static final Map<Class<?>,AiPreferenceHandler<?>> handlers = new HashMap<Class<?>, AiPreferenceHandler<?>>();
	
	/**
	 * Renvoie le gestionnaire pour la classe de l'agent spécifiée.
	 * 
	 * @param ai
	 * 		Agent à traiter.
	 * @return
	 * 		Le gestionnaire de préférence correspondant.
	 */
	static AiPreferenceHandler<?> getHandler(ArtificialIntelligence ai)
	{	Class<?> clazz = ai.getClass();
		AiPreferenceHandler<?> result = handlers.get(clazz);
		return result;
	}
	
    /////////////////////////////////////////////////////////////////
	// LOADING					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Charge les préférences d'un agent,
	 * stockés dans un fichier XML.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param packName
	 * 		Paquetage contenant l'agent.
	 * @param aiName
	 * 		Dossier spécifique à l'agent.
	 * @param ai
	 * 		Objet représentant l'agent concerné.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Problème lors du chargement des critères, catégories ou combinaisons.
	 * @throws ParserConfigurationException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws SAXException
	 * 		Problème lors de l'accès au fichier XML.
	 * @throws IOException
	 * 		Problème lors de l'accès à un fichier.
	 * @throws IllegalAccessException 
	 * 		Problème lors du chargement des préférences.
	 * @throws InvocationTargetException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InstantiationException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws NoSuchMethodException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws URISyntaxException 
	 * 		Problème lors de la localisation du fichier de préférences.
	 */
	public static <T extends ArtificialIntelligence> void loadAiPreferences(String packName, String aiName, T ai) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, URISyntaxException
	{	// check if already loaded
		if(getHandler(ai)==null)
		{	// set dummy handler
			AiPreferenceHandler<T> handler = new AiPreferenceHandler<T>(ai,true)
			{	@Override
				protected Set<AiTile> selectTiles()
				{	return null;
				}

				@Override
				protected AiCategory identifyCategory(AiTile tile)
				{	return null;
				}	
			};
			handlers.put(ai.getClass(),handler);
			
			// set error message prefix
			String errMsg = "Error while loading agent "+packName+"/"+aiName+": ";
	
			// version path
			//String agentPath = FilePaths.getAisPath()+File.separator+packName+File.separator+FileNames.FILE_AIS+File.separator+aiName;
			String filename = FileNames.FILE_PREFERENCES+FileNames.EXTENSION_XML;
			URL url = ai.getClass().getResource(filename);
			if(url==null)
				throw new FileNotFoundException(errMsg+"cannot find the agent preference file for "+url);
			File tempFile = new File(url.toURI());
			String agentPath = tempFile.getParentFile().getPath();
			
			// init criteria
			initCriteria(agentPath, ai, errMsg);
			
			// load xml file
			File dataFile = new File(agentPath+File.separator+filename);
			String schemaFolder = FilePaths.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PREFERENCES+FileNames.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			loadPreferencesElement(root, ai, errMsg);
		}
	}
	
	/**
	 * Charge et instancie toutes les classes de l'agent
	 * qui correspondent à des critères. L'intégralité
	 * du package de l'agent est scanné à la recherche
	 * de classes compatibles.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param agentPath 
	 * 		Chemin du package de l'agent.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws IllegalAccessException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InvocationTargetException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InstantiationException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws NoSuchMethodException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws FileNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 */
	private static <T extends ArtificialIntelligence> void initCriteria(String agentPath, T ai, String errMsg) throws IllegalArgumentException, NoSuchMethodException, FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	// get the agent main folder
		File agentFolder = new File(agentPath);
		if(!agentFolder.exists())
			throw new FileNotFoundException(errMsg+"cannot find the agent folder "+agentPath);

		// process folder and subfolders
		scanFolderForCriteria(agentFolder, ai, errMsg);
	}

	/**
	 * Méthode secondaire utilisée pour scanner le
	 * package de l'agent à la recherche de classes
	 * représentant des critères. Toutes les classes
	 * de ce type qui sont détectées sont instanciées
	 * et stockées dans le gestionnaire de l'agent.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param packageFolder 
	 * 		Objet représentant le dossier contenant le code source de l'agent.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws IllegalAccessException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InvocationTargetException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws InstantiationException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws ClassNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws NoSuchMethodException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 * @throws FileNotFoundException 
	 * 		Problème lors de l'accès aux classes représentant des critères.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ArtificialIntelligence> void scanFolderForCriteria(File packageFolder, T ai, String errMsg) throws IllegalArgumentException, NoSuchMethodException, FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	//String folderPath = FilePaths.getAiPath();
		//File temp = new File(folderPath);
		//folderPath = temp.getAbsolutePath()+File.separator;
		String folderPath = packageFolder.getAbsolutePath();
		int idx = folderPath.indexOf(FileNames.FILE_ORG);
		if(idx==-1)
			throw new FileNotFoundException(errMsg+"invalid agent source code: no 'org' found in the package name "+folderPath);
		folderPath = folderPath.substring(0,idx);
		
		// get the list of criterion classes
		{	FileFilter filter = new FileFilter()
			{	@Override
				public boolean accept(File file)
				{	boolean result = false;
					String name = file.getName();
					int extLength = FileNames.EXTENSION_CLASS.length();
					if(name.length()>extLength)
					{	int length = name.length();
						String ext = name.substring(length-extLength,length);
						result = ext.equalsIgnoreCase(FileNames.EXTENSION_CLASS);
					}
					return result;
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
				if(AiCriterion.class.isAssignableFrom(tempClass))
				{	// create an instance
					AiCriterion<T, ?> criterion = null;
					try
					{	Constructor<?> constructor = tempClass.getConstructor(ai.getClass());
						criterion = (AiCriterion<T, ?>)constructor.newInstance(ai);
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
					AiPreferenceHandler<T> handler = (AiPreferenceHandler<T>)getHandler(ai);
					try
					{	handler.insertCriterion(criterion);
					}
					catch(IllegalArgumentException e)
					{	handler.displayPreferencesProblem();
						throw new IllegalArgumentException(errMsg+"["+className+"] "+e.getMessage());
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
	 * Traite un élément XML pour en extraire les préférences
	 * nécessaire à l'initialisation de l'objet représentant
	 * l'agent.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param root
	 * 		Element à traiter.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		S'il y a un problème avec les critères, cas ou combinaisons.
	 */
	private static <T extends ArtificialIntelligence> void loadPreferencesElement(Element root, T ai, String errMsg) throws IllegalArgumentException
	{	// load categories
		Element categoriesElt = root.getChild(XmlNames.CATEGORIES);
		loadCategoriesElement(categoriesElt,ai,errMsg);
		
		// load combinations
		Element tablesElt = root.getChild(XmlNames.TABLES);
		loadTablesElement(tablesElt, ai, errMsg);
	}
	
	/**
	 * Récupère la liste de catégories définies pour cet agent.
	 * Celles-ci sont initialisées et stockées dans son gestionnaire.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param root
	 * 		Element à traiter.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		S'il y a un problème avec les critères.
	 */
	private static <T extends ArtificialIntelligence> void loadCategoriesElement(Element root, T ai, String errMsg) throws IllegalArgumentException
	{	@SuppressWarnings("unchecked")
		List<Element> elements = root.getChildren(XmlNames.CATEGORY);
		for(Element element: elements)
			loadCategoryElement(element,ai,errMsg);
	}
	
	/**
	 * Analyse un élément décrivant une catégorie.
	 * La catégorie est initialisée et stockée dans le
	 * gestionnaire de l'agent.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param root
	 * 		Element à traiter.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		S'il y a un problème avec les critères.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ArtificialIntelligence> void loadCategoryElement(Element root, T ai, String errMsg) throws IllegalArgumentException
	{	// retrieve the category name
		String name = root.getAttributeValue(XmlNames.NAME);
		// get the preference handler
		AiPreferenceHandler<T> handler = (AiPreferenceHandler<T>)getHandler(ai);
	
		// read criterion list
		String criteriaStr = root.getAttributeValue(XmlNames.CRITERIA);
		String criteriaTab[] = criteriaStr.split(" ");
		List<AiCriterion<?,?>> categoryCriteria = new ArrayList<AiCriterion<?,?>>();
		for(String critName: criteriaTab)
		{	AiCriterion<T,?> crit = (AiCriterion<T,?>)handler.getCriterion(critName);
			if(crit==null)
				throw new IllegalArgumentException(errMsg+"criterion '"+critName+"' used in category '"+name+"' is undefined (you must first define the criterion as a class, using '"+critName+"' as its name).");
			categoryCriteria.add(crit);
		}
		
		// build category and complete map
		try
		{	AiCategory category = new AiCategory(name,categoryCriteria);
			handler.insertCategory(category);
		}
		catch(IllegalArgumentException e)
		{	handler.displayPreferencesProblem();
			throw new IllegalArgumentException(errMsg+e.getMessage());
		}
	}
	
	/**
	 * Analyse des listes de combinaisons (une
	 * pour chaque mode) ordonnées par préférence 
	 * décroissante.
	 *  
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param root
	 * 		Element à traiter.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Problème avec une valeur ou un critère.
	 */
	private static <T extends ArtificialIntelligence> void loadTablesElement(Element root, T ai, String errMsg) throws IllegalArgumentException
	{	@SuppressWarnings("unchecked")
		List<Element> elements = root.getChildren(XmlNames.TABLE);
		for(Element element: elements)
			loadTableElement(element,ai,errMsg);
	}
	
	/**
	 * Analyse une liste de combinaisons ordonnées
	 * par préférence décroissante, pour un mode donné.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param root
	 * 		Element à traiter.
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Problème avec une valeur ou un critère.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ArtificialIntelligence> void loadTableElement(Element root, T ai, String errMsg) throws IllegalArgumentException
	{	// get the mode
		String modeStr = root.getAttributeValue(XmlNames.MODE);
		AiMode mode = AiMode.valueOf(modeStr);
		
		// process combinations
		List<Element> combiElts = root.getChildren(XmlNames.COMBINATION);
		for(Element combiElt: combiElts)
			loadCombinationElement(combiElt,mode,ai,errMsg);
		
		// check missing combinations
		AiPreferenceHandler<T> handler = (AiPreferenceHandler<T>)getHandler(ai);
		Set<AiCombination> missingCombinations = handler.checkPreferences(mode);
		if(!missingCombinations.isEmpty())
		{	handler.displayPreferencesProblem();
			throw new IllegalArgumentException(errMsg+"some combinations are missing in the "+mode+" preference table:"+missingCombinations.toString());
		}
	}

	/**
	 * Charge une combinaison.
	 * 
	 * @param <T> 
	 * 		Classe de l'agent concerné.
	 * @param root
	 * 		Element à traiter.
	 * @param mode
	 * 		Mode de l'agent (attaque ou collecte).
	 * @param ai 
	 * 		Objet représentant l'agent.
	 * @param errMsg 
	 * 		Prefixe des messages d'erreur.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Problème avec une valeur ou un critère.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends ArtificialIntelligence> void loadCombinationElement(Element root, AiMode mode, T ai, String errMsg) throws IllegalArgumentException
	{	// get the preference handler
		AiPreferenceHandler<T> handler = (AiPreferenceHandler<T>)getHandler(ai);
		
		// get the category
		String categoryStr = root.getAttributeValue(XmlNames.CATEGORY);
		AiCategory category = handler.getCategory(categoryStr);
		if(category==null)
		{	handler.displayPreferencesProblem();
			throw new IllegalArgumentException(errMsg+"category '"+categoryStr+"' is used in a combination, but is is undefined (you must first define the category in the preference XML file).");
		}
		AiCombination combination = new AiCombination(category);
		
		// get the criterion values
		String valuesStr = root.getAttributeValue(XmlNames.VALUES);
		String valuesTab[] = valuesStr.split(" ");
		for(int i=0;i<valuesTab.length;i++)
		{	// convert the string
			String valueStr = valuesTab[i];
			try
			{	combination.setCriterionValue(i, valueStr);
			}
			catch(IllegalArgumentException e)
			{	handler.displayPreferencesProblem();
				throw new IllegalArgumentException(errMsg+e.getMessage());
			}
		}
		
		// insert the combination in the handler
		try
		{	handler.insertCombination(mode,combination);
		}
		catch(IllegalArgumentException e)
		{	handler.displayPreferencesProblem();
			throw new IllegalArgumentException(errMsg+e.getMessage());
		}
	}
}
