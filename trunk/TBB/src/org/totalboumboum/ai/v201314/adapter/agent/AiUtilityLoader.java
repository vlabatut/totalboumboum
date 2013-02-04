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
public class AiUtilityLoader<T extends ArtificialIntelligence>
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
	public void loadAiPreferences(String packName, String aiName, T ai) throws ParserConfigurationException, SAXException, IOException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	// init criteria
		initCriteria(aiName, packName, ai);
/*
 * TODO TODO
 * chargement statique : difficile à mettre en place sans changer profondément les classes d'utilité
 * 
 * - soit charger tout sous forme de texte, et réinstancier la table des préférences à chaque création d'agent
 * 
 * - soit charger directement les objets de façon statique
 * 		- mais alors on n'a pas accès à une instance d'agent 
 * 			>> faut changer les classes d'utilité
 * 			- mais peut etre plus besoin d'agent maintenant, si vérif est faite au chargement?
 * 			- au pire, on peut passer l'agent en paramètre à chaque appel
 * 		- critère = type énum?
 * 			- difficile à définir
 * 			- comment les manipuler dans les classes abstraites de l'API?
 * 		- pb: forcer les étudiants à implémenter certaines méthodes statiques dans leur classe
 * 			- définir des maps (clées=classe) gérées par l'API contenant tout ce qui est chargé
 * 			- à chaque instanciation, on copie dans l'objet les maps de critères etc.
 * 			- on peut même garder la référence à l'objet ai si on CLONE ces objets (methode clone prenant un ai en param)
 * 			- 
 * 
 * TODO TODO
 * 	- dans case, pas besoin d'ai 
 * 		(sauf si détermination du cas dans la classe du cas. là, on a besoin d'ai)
 * 		>> il faut faire le test d'unicité (du nom) lors de l'insertion
 *  - dans critère, besoin d'ai pour utilisation console durant calul du critère
 */
		
		String path = FilePaths.getAisPath()+File.separator+packName+File.separator+FileNames.FILE_AIS+File.separator+aiName;
		File dataFile = new File(path+File.separator+FileNames.FILE_PREFERENCES+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PREFERENCES+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadPreferencesElement(root, ai);
	}
	
	// IllegalArgumentException, ClassCastException
	private void initCriteria(String aiName, String packName, T ai) throws NoSuchMethodException, FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException
	{	// init qualified package name
		String packageName = ClassTools.getTbbPackage()+ClassTools.CLASS_SEPARATOR+
		FileNames.FILE_AI+ClassTools.CLASS_SEPARATOR+
		packName+ClassTools.CLASS_SEPARATOR+
		FileNames.FILE_AIS+ClassTools.CLASS_SEPARATOR+
		aiName+ClassTools.CLASS_SEPARATOR+
		FileNames.FILE_CRITERION;
		
		/*
		 * TODO TODO
		 * pq mettre des contraintes sur la localisation des critères ?
		 * il suffit de chercher les classes héritant de critere général
		 * dans tout le package de l'agent, et basta.
		 */
		
//		Package classPackage = clazz.getPackage();
//		String packageName = classPackage .getName()+ClassTools.CLASS_SEPARATOR+"criterion";
//		Package critPackage = Package.getPackage(packageName);

		// get the list of criterion classes
		String packagePath = FilePaths.getAisPath()+File.separator+packName+File.separator+FileNames.FILE_AIS+File.separator+aiName+File.separator+FileNames.FILE_CRITERION;
		File packageFolder = new File(packagePath);
		if(!packageFolder.exists())
			throw new FileNotFoundException("In package "+packageName+": package "+FileNames.FILE_CRITERION+" does not exist (it should contain all criterion classes).");
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	String name = pathname.getName();
				int length = name.length();
				int extLength = FileNames.EXTENSION_CLASS.length();
				String ext = name.substring(length-extLength,length);
				return ext.equalsIgnoreCase(FileNames.EXTENSION_CLASS);
			}			
		};
		File[] files = packageFolder.listFiles(filter);

		// process each one of them
		for(File file: files)
		{	// get the class name
			String className = file.getName();
			className = className.substring(0,className.length()-FileNames.EXTENSION_CLASS.length());
			
			// load the class
			String classQualifiedName = packageName+ClassTools.CLASS_SEPARATOR+className;
			Class<?> tempClass = Class.forName(classQualifiedName);
			if(!AiUtilityCriterion.class.isAssignableFrom(tempClass))
				throw new ClassCastException("In package "+packageName+": Class "+className+" is not a subclass of AiUtilityCriterion (this package must contain only criterion classes).");

			// create an instance
			AiUtilityCriterion<T, ?> criterion = null;
			try
			{	Constructor<?> constructor = tempClass.getConstructor();
				criterion = (AiUtilityCriterion<T, ?>)constructor.newInstance(ai);
			}
			catch (NoSuchMethodException e)
			{	throw new NoSuchMethodException("In class "+classQualifiedName+": No constructor could be found.");
			}
			catch (IllegalArgumentException e)
			{	throw new IllegalArgumentException("In class "+classQualifiedName+": One constructor must be parameterless.");
			}
			catch (InstantiationException e)
			{	throw new InstantiationException("In class "+classQualifiedName+": Cannot perform instantiation (maybe the class is abstract, or an interface, etc.)");
			}
			catch (IllegalAccessException e)
			{	throw new IllegalAccessException("In class "+classQualifiedName+": Cannot access the constructor (maybe a visibility problem, e.g. private constructor).");
			}
//InvocationTargetException:::: "In class "+classQualifiedName+": The parameterless constructor threw an exception. 			
			AiUtilityHandler<T> handler = (AiUtilityHandler<T>)ai.getUtilityHandler();
			try
			{	handler.insertCriterion(criterion);
			}
			catch(IllegalArgumentException e)
			{	throw new IllegalArgumentException("In class "+classQualifiedName+": A criterion with the same name ("+name+") already exists for this agent.");
				// TODO à compléter
			}
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
	private void loadPreferencesElement(Element root, T ai)
	{	// load categories
		Element categoriesElt = root.getChild(XmlNames.CATEGORIES);
		loadCategoriesElement(categoriesElt,ai);
		
		// load combinations
		Element tablesElt = root.getChild(XmlNames.TABLES);
		loadTablesElement(tablesElt, ai);
	}
	
	private void loadCategoriesElement(Element root, T ai)
	{	List<Element> elements = root.getChildren(XmlNames.CATEGORY);
		for(Element element: elements)
			loadCategoryElement(element,ai);
	}
	
	// IllegalArgumentException
	private void loadCategoryElement(Element root, T ai)
	{	String name = root.getAttributeValue(XmlNames.NAME);

/* TODO TODO TODO
 * 	
 * - modifier case pour ne plus avoir besoin d'ai
 * - les critères doivent être instanciés une fois pour toutes (dans la 1ère méthode de chargement ?)
 * 	 et meme stockés dans la classe statique
 * 		- pour ça faut qu'ils soient chargés dynamiquement
 * 		- donc faut connaitre leur emplacement >> forcer l'utilisation du package 'criterion'
 * 		- les noms aussi doivent être standardisés
 * 		- faut une factory pour passer du nom string à la classe elle-même (ou on peut faire ça direct ici?)
 * - TODO plutot qu'en statique, il suffit de :
 * 		- charger toutes les classes d'un agent
 * 		- le mettre en cache dans le loader
 * 		- à chaque round avec cet agent, on le clone
 */
		AiUtilityHandler<T> handler = (AiUtilityHandler<T>)ai.getUtilityHandler();
	
		// read criterion list
		List<Element> elements = root.getChildren(XmlNames.CRITERION);
		Set<AiUtilityCriterion<?,?>> categoryCriteria = new TreeSet<AiUtilityCriterion<?,?>>();
		for(Element element: elements)
		{	String critName = element.getAttributeValue(XmlNames.NAME);
			AiUtilityCriterion<T,?> crit = handler.getCriterion.get(name);
			if(categoryCriteria.contains(crit))
				throw new IllegalArgumentException("In class "+clazz.getName()+": A category ("+name+") cannot contain the same criterion ("+critName+") more than once.");
			categoryCriteria.add(crit);
		}
		
/*
 * TODO TODO
 * 1) créer la liste de critères sans vérification
 * 2) controler les redondances à la création du cas
 * 3) controler l'unicité du nom du cas à l'insertion dans le handler		
 */
		
		// build category and complete map
		AiUtilityCase category = new AiUtilityCase(name,categoryCriteria);
		categories.put(name,category);
	}
	
	private void loadTablesElement(Element root, T ai)
	{	List<Element> elements = root.getChildren(XmlNames.TABLE);
		for(Element element: elements)
			loadTableElement(element,ai);
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
	private void loadTableElement(Element root, T ai)
	{	// get the mode
		String modeStr = root.getAttributeValue(XmlNames.MODE);
		AiMode mode = AiMode.valueOf(modeStr);
		
		// process combinations
		List<Element> combiElts = root.getChildren(XmlNames.COMBINATION);
		for(Element combiElt: combiElts)
			loadCombinationElement(combiElt,mode,ai);
	}
	
	/*
	 * TODO TODO
	 * normaliser les messages d'erreur : in class ou in package ou in agent?
	 */
	
	private void loadCombinationElement(Element root, AiMode mode, T ai)
	{	// get the category
		String categoryStr = root.getAttributeValue(XmlNames.CATEGORY);
		AiUtilityCase category = categories.get(categoryStr);
		if(category==null)
			throw new IllegalArgumentException("In class ?????: A combination was defined for category "+categoryStr+", which was not declared before.");
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
			AiUtilityCriterion<T,?> criterion = criteria.get(criterionStr);
			if(criterion==null)
				throw new IllegalArgumentException("In class ?????: A criterion was specified for combination ?????, which was not declared before.");
			String valueStr = valueElt.getValue();
			combination.setCriterionValue(criterion, valueStr);
		}
	}
}
