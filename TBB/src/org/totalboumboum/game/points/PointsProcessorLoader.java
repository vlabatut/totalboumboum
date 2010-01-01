package org.totalboumboum.game.points;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class PointsProcessorLoader
{

    public static PointsProcessor loadPointProcessorFromElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	PointsProcessor result;
		// local
		String localStr = root.getAttribute(XmlTools.LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			result = loadPointProcessorFromFilePath(folder);
		}
		else
			result = loadPointProcessorFromName(name);
		return result;
	}
	
	public static PointsProcessor loadPointProcessorFromFilePath(String folderPath) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_POINT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		PointsProcessor result = loadPointProcessorElement(root);
		return result;
	}

	public static PointsProcessor loadPointProcessorFromName(String name) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getPointsPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+name+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_POINT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		PointsProcessor result = loadPointProcessorElement(root);
		return result;
	}

    @SuppressWarnings("unchecked")
	private static PointsProcessor loadPointProcessorElement(Element root)
	{	// init
    	PointsProcessor result;
    	Element element;
		// point processor
    	List<Element> elts = root.getChildren(); 
		element = elts.get(1);
		result = loadGeneralPointElement(element);
		// notes
		element = root.getChild(XmlTools.NOTES);
		ArrayList<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		//
		return result;
	}		

	public static PointsProcessor loadGeneralPointElement(Element root)
	{	PointsProcessor result = null;
		String type = root.getName();

		if(type.equals(XmlTools.TOTAL))
			result = loadTotalElement(root);

		else if(type.equals(XmlTools.SCORES))
			result = loadScoresElement(root);
		else if(type.equals(XmlTools.CONSTANT))
			result = loadConstantElement(root);

		else if(type.equals(XmlTools.MAXIMUM))
			result = loadMaximumElement(root);
		else if(type.equals(XmlTools.MINIMUM))
			result = loadMinimumElement(root);		
		else if(type.equals(XmlTools.SUMMATION))
			result = loadSummationElement(root);
		else if(type.equals(XmlTools.RANKINGS))
			result = loadRankingsElement(root);
		else if(type.equals(XmlTools.DISCRETIZE))
			result = loadDiscretizeElement(root);
		else if(type.equals(XmlTools.RANKPOINTS))
			result = loadRankpointsElement(root);
		
		else if(type.equals(XmlTools.ADDITION))
			result = loadAdditionElement(root);
		else if(type.equals(XmlTools.SUBTRACTION))
			result = loadSubtractionElement(root);
		else if(type.equals(XmlTools.MULTIPLICATION))
			result = loadMultiplicationElement(root);
		else if(type.equals(XmlTools.DIVISION))
			result = loadDivisionElement(root);
		
		return result;
	}

	private static PointsTotal loadTotalElement(Element root)
	{	PointsTotal result = new PointsTotal();
		return result;
	}

	private static PointsScores loadScoresElement(Element root)
	{	// type
		String str = root.getAttribute(XmlTools.TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase(Locale.ENGLISH).trim());
		// result
		PointsScores result = new PointsScores(score);
		return result;
	}
	private static PointsConstant loadConstantElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.VALUE).getValue();
		float value = Float.valueOf(str);
		// result
		PointsConstant result = new PointsConstant(value);
		return result;
	}
	
	private static PointsMaximum loadMaximumElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointsProcessor source = loadGeneralPointElement(src);
		// result
		PointsMaximum result = new PointsMaximum(source);
		return result;
	}
	private static PointsMinimum loadMinimumElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointsProcessor source = loadGeneralPointElement(src);
		// result
		PointsMinimum result = new PointsMinimum(source);
		return result;
	}
	private static PointsSummation loadSummationElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointsProcessor source = loadGeneralPointElement(src);
		// result
		PointsSummation result = new PointsSummation(source);
		return result;
	}
	@SuppressWarnings("unchecked")
	private static PointsRankings loadRankingsElement(Element root)
	{	// invert
		String str = root.getAttribute(XmlTools.INVERT).getValue();
		boolean invert = Boolean.valueOf(str);
		// sources
		ArrayList<PointsProcessor> sources = new ArrayList<PointsProcessor>();
		List<Element> srcs = root.getChildren();
		Iterator<Element> it = srcs.iterator();
		while(it.hasNext())
		{	Element src = it.next();
			PointsProcessor source = loadGeneralPointElement(src);
			sources.add(source);
		}
		// result
		PointsRankings result = new PointsRankings(sources,invert);
		return result;
	}
	@SuppressWarnings("unchecked")
	private static PointsDiscretize loadDiscretizeElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointsProcessor source = loadGeneralPointElement(src);
		// thresholds
		Element thresholdsElt = root.getChild(XmlTools.THRESHOLDS);
		List<Element> thresholds = thresholdsElt.getChildren(XmlTools.THRESHOLD);
		float thresh[] = new float[thresholds.size()];
		for(int i=0;i<thresh.length;i++)
		{	Element temp = thresholds.get(i);
			String str = temp.getAttribute(XmlTools.VALUE).getValue();
			float value = Float.valueOf(str);
			thresh[i] = value;
		}
		// values
		Element valuesElt = root.getChild(XmlTools.VALUES);
		List<Element> values = valuesElt.getChildren(XmlTools.VALUE);
		float vals[] = new float[values.size()];
		for(int i=0;i<vals.length;i++)
		{	Element temp = values.get(i);
			String str = temp.getAttribute(XmlTools.VALUE).getValue();
			float value = Float.valueOf(str);
			vals[i] = value;
		}
		// result
		PointsDiscretize result = new PointsDiscretize(source,thresh,vals);
		return result;
	}
	@SuppressWarnings("unchecked")
	private static PointsRankpoints loadRankpointsElement(Element root)
	{	// share
		String str = root.getAttribute(XmlTools.EXAEQUO_SHARE).getValue();
		boolean exaequoShare = Boolean.valueOf(str);
		// invert
		str = root.getAttribute(XmlTools.INVERT).getValue();
		boolean invert = Boolean.valueOf(str);
		// sources
		Element rankingsElt = root.getChild(XmlTools.RANKINGS);
		ArrayList<PointsProcessor> sources = new ArrayList<PointsProcessor>();
		List<Element> srcs = rankingsElt.getChildren();
		Iterator<Element> it = srcs.iterator();
		while(it.hasNext())
		{	Element src = it.next();
			PointsProcessor source = loadGeneralPointElement(src);
			sources.add(source);
		}
		// values
		Element valuesElt = root.getChild(XmlTools.VALUES);
		List<Element> values = valuesElt.getChildren(XmlTools.VALUE);
		float vals[] = new float[values.size()];
		for(int i=0;i<vals.length;i++)
		{	Element temp = values.get(i);
			str = temp.getAttribute(XmlTools.VALUE).getValue();
			float value = Float.valueOf(str);
			vals[i] = value;
		}
		// result
		PointsRankpoints result = new PointsRankpoints(sources,vals,invert,exaequoShare);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static PointsAddition loadAdditionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsAddition result = new PointsAddition(leftSource,rightSource);
		return result;
	}
	@SuppressWarnings("unchecked")
	private static PointsSubstraction loadSubtractionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsSubstraction result = new PointsSubstraction(leftSource,rightSource);
		return result;
	}
	@SuppressWarnings("unchecked")
	private static PointsMultiplication loadMultiplicationElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsMultiplication result = new PointsMultiplication(leftSource,rightSource);
		return result;
	}
	@SuppressWarnings("unchecked")
	private static PointsDivision loadDivisionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointsProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointsProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointsDivision result = new PointsDivision(leftSource,rightSource);
		return result;
	}
}
