package org.totalboumboum.game.points;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.match.MatchLoader;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Processes an XML element describing a {@code PointsProcessor} object,
 * and returns it as a {@code PointsProcessor}. Can do the same thing from
 * an XML file, too.
 * 
 * @author Vincent Labatut
 */
public class PointsProcessorLoader
{
	/**
	 * Processes the specified XML element in order
	 * to identify and instanciate the corresponding
	 * {@code PointProcessor} object.
	 * 
	 * @param root
	 * 		XML element describing the {@code PointsProcessor}.
	 * @param folder
	 * 		Folder containing the currently processed XML file.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while parsing the XML file.
	 * @throws SAXException
	 * 		Problem while parsing the XML file.
	 * @throws IOException
	 * 		Problem while parsing the XML file.
	 */
    public static AbstractPointsProcessor loadPointProcessorFromElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	AbstractPointsProcessor result;
		// local
		String localStr = root.getAttribute(XmlNames.LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlNames.NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			result = loadPointProcessorFromFilePath(folder);
		}
		else
			result = loadPointProcessorFromName(name);
		return result;
	}
	
    /**
     * Open an XML file describing a {@code PointsProcessor}
     * and returns it. The file is located in a non-default
     * folder.
     * 
     * @param folderPath
     * 		XML file path.
     * @return
     * 		The {@code PointsProcessor} read.
     * 
	 * @throws ParserConfigurationException
	 * 		Problem while parsing the XML file.
	 * @throws SAXException
	 * 		Problem while parsing the XML file.
	 * @throws IOException
	 * 		Problem while parsing the XML file.
     */
	public static AbstractPointsProcessor loadPointProcessorFromFilePath(String folderPath) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_POINT+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		AbstractPointsProcessor result = loadPointProcessorElement(root);
		return result;
	}

    /**
     * Open an XML file describing a {@code PointsProcessor}
     * and returns it. The file is located in the default
     * {@code PointsProcessor} folder.
     * 
     * @param name
     * 		XML file path.
     * @return
     * 		The {@code PointsProcessor} read.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while parsing the XML file.
	 * @throws SAXException
	 * 		Problem while parsing the XML file.
	 * @throws IOException
	 * 		Problem while parsing the XML file.
	 */
	public static AbstractPointsProcessor loadPointProcessorFromName(String name) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = FilePaths.getPointsPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+name+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_POINT+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		AbstractPointsProcessor result = loadPointProcessorElement(root);
		return result;
	}

	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
    @SuppressWarnings("unchecked")
	private static AbstractPointsProcessor loadPointProcessorElement(Element root)
	{	// init
    	AbstractPointsProcessor result;
    	Element element;
		// point processor
    	List<Element> elts = root.getChildren(); 
		element = elts.get(1);
		result = loadGeneralPointElement(element);
		// notes
		element = root.getChild(XmlNames.NOTES);
		List<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		//
		return result;
	}		

	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	public static AbstractPointsProcessor loadGeneralPointElement(Element root)
	{	AbstractPointsProcessor result = null;
		String type = root.getName();

		if(type.equals(XmlNames.TOTAL))
			result = loadTotalElement(root);

		else if(type.equals(XmlNames.SCORES))
			result = loadScoresElement(root);
		else if(type.equals(XmlNames.CONSTANT))
			result = loadConstantElement(root);

		else if(type.equals(XmlNames.MAXIMUM))
			result = loadMaximumElement(root);
		else if(type.equals(XmlNames.MINIMUM))
			result = loadMinimumElement(root);		
		else if(type.equals(XmlNames.SUMMATION))
			result = loadSummationElement(root);
		else if(type.equals(XmlNames.RANKINGS))
			result = loadRankingsElement(root);
		else if(type.equals(XmlNames.DISCRETIZE))
			result = loadDiscretizeElement(root);
		else if(type.equals(XmlNames.RANKPOINTS))
			result = loadRankpointsElement(root);
		
		else if(type.equals(XmlNames.ADDITION))
			result = loadAdditionElement(root);
		else if(type.equals(XmlNames.SUBTRACTION))
			result = loadSubtractionElement(root);
		else if(type.equals(XmlNames.MULTIPLICATION))
			result = loadMultiplicationElement(root);
		else if(type.equals(XmlNames.DIVISION))
			result = loadDivisionElement(root);
		
		return result;
	}

	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	private static PointsProcessorTotal loadTotalElement(Element root)
	{	PointsProcessorTotal result = new PointsProcessorTotal();
		return result;
	}

	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	private static PointsProcessorScores loadScoresElement(Element root)
	{	// type
		String str = root.getAttribute(XmlNames.TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase(Locale.ENGLISH).trim());
		// result
		PointsProcessorScores result = new PointsProcessorScores(score);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	private static PointsProcessorConstant loadConstantElement(Element root)
	{	// value
		String str = root.getAttribute(XmlNames.VALUE).getValue();
		float value = Float.valueOf(str);
		// result
		PointsProcessorConstant result = new PointsProcessorConstant(value);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	private static PointsProcessorMaximum loadMaximumElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		AbstractPointsProcessor source = loadGeneralPointElement(src);
		// result
		PointsProcessorMaximum result = new PointsProcessorMaximum(source);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	private static PointsProcessorMinimum loadMinimumElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		AbstractPointsProcessor source = loadGeneralPointElement(src);
		// result
		PointsProcessorMinimum result = new PointsProcessorMinimum(source);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	private static PointsProcessorSummation loadSummationElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		AbstractPointsProcessor source = loadGeneralPointElement(src);
		// result
		PointsProcessorSummation result = new PointsProcessorSummation(source);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorRankings loadRankingsElement(Element root)
	{	// invert
		String str = root.getAttribute(XmlNames.INVERT).getValue();
		boolean invert = Boolean.valueOf(str);
		// sources
		List<AbstractPointsProcessor> sources = new ArrayList<AbstractPointsProcessor>();
		List<Element> srcs = root.getChildren();
		Iterator<Element> it = srcs.iterator();
		while(it.hasNext())
		{	Element src = it.next();
			AbstractPointsProcessor source = loadGeneralPointElement(src);
			sources.add(source);
		}
		// result
		PointsProcessorRankings result = new PointsProcessorRankings(sources,invert);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorDiscretize loadDiscretizeElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		AbstractPointsProcessor source = loadGeneralPointElement(src);
		// thresholds
		Element thresholdsElt = root.getChild(XmlNames.THRESHOLDS);
		List<Element> thresholds = thresholdsElt.getChildren(XmlNames.THRESHOLD);
		float thresh[] = new float[thresholds.size()];
		for(int i=0;i<thresh.length;i++)
		{	Element temp = thresholds.get(i);
			String str = temp.getAttribute(XmlNames.VALUE).getValue();
			float value = Float.valueOf(str);
			thresh[i] = value;
		}
		// values
		Element valuesElt = root.getChild(XmlNames.VALUES);
		List<Element> values = valuesElt.getChildren(XmlNames.VALUE);
		float vals[] = new float[values.size()];
		for(int i=0;i<vals.length;i++)
		{	Element temp = values.get(i);
			String str = temp.getAttribute(XmlNames.VALUE).getValue();
			float value = Float.valueOf(str);
			vals[i] = value;
		}
		// result
		PointsProcessorDiscretize result = new PointsProcessorDiscretize(source,thresh,vals);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorRankpoints loadRankpointsElement(Element root)
	{	// share
		String str = root.getAttribute(XmlNames.EXAEQUO_SHARE).getValue();
		boolean exaequoShare = Boolean.valueOf(str);
		// invert
		str = root.getAttribute(XmlNames.INVERT).getValue();
		boolean invert = Boolean.valueOf(str);
		// sources
		Element rankingsElt = root.getChild(XmlNames.RANKINGS);
		List<AbstractPointsProcessor> sources = new ArrayList<AbstractPointsProcessor>();
		List<Element> srcs = rankingsElt.getChildren();
		Iterator<Element> it = srcs.iterator();
		while(it.hasNext())
		{	Element src = it.next();
			AbstractPointsProcessor source = loadGeneralPointElement(src);
			sources.add(source);
		}
		// values
		Element valuesElt = root.getChild(XmlNames.VALUES);
		List<Element> values = valuesElt.getChildren(XmlNames.VALUE);
		float vals[] = new float[values.size()];
		for(int i=0;i<vals.length;i++)
		{	Element temp = values.get(i);
			str = temp.getAttribute(XmlNames.VALUE).getValue();
			float value = Float.valueOf(str);
			vals[i] = value;
		}
		// result
		PointsProcessorRankpoints result = new PointsProcessorRankpoints(sources,vals,invert,exaequoShare);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorAddition loadAdditionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		AbstractPointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		AbstractPointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsProcessorAddition result = new PointsProcessorAddition(leftSource,rightSource);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorSubstraction loadSubtractionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		AbstractPointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		AbstractPointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsProcessorSubstraction result = new PointsProcessorSubstraction(leftSource,rightSource);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorMultiplication loadMultiplicationElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		AbstractPointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		AbstractPointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsProcessorMultiplication result = new PointsProcessorMultiplication(leftSource,rightSource);
		return result;
	}
	
	/**
	 * Processes the specified element in order to extract
	 * a {@code PointsProcessor} object.
	 * 
	 * @param root
	 * 		XML element to process.
	 * @return
	 * 		Resulting {@code PointsProcessor} object.
	 */
	@SuppressWarnings("unchecked")
	private static PointsProcessorDivision loadDivisionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		AbstractPointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		AbstractPointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsProcessorDivision result = new PointsProcessorDivision(leftSource,rightSource);
		return result;
	}
}
