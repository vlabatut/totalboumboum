package fr.free.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.io.IOException;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsProcessorLoader;
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.tools.XmlTools;

public class LimitLoader
{
	public static Limit loadLimitElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Limit result = null;
		String type = root.getName();
		PointsProcessor pointProcessor = PointsProcessorLoader.loadPointProcessorFromElement(root,folder);

		if(type.equals(XmlTools.ELT_CONFRONTATION))
			result = loadLimitConfrontationElement(root,pointProcessor);
		
		else if(type.equals(XmlTools.ELT_POINTS))
			result = loadLimitPointsElement(root,folder,pointProcessor);
		
		else if(type.equals(XmlTools.ELT_SCORE))
			result = loadLimitScoreElement(root,pointProcessor);
		
		else if(type.equals(XmlTools.ELT_TIME))
			result = loadLimitTimeElement(root,pointProcessor);
		
		else if(type.equals(XmlTools.ELT_LAST_STANDING))
			result = loadLimitLastStandingElement(root,pointProcessor);
		
		return result;
	}

	private static LimitConfrontation loadLimitConfrontationElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.ELT_THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.ATT_VALUE).getValue();
		int threshold = Integer.parseInt(str);
		// supLimit
		str = root.getAttribute(XmlTools.ATT_SUP).getValue();
		boolean supLimit = Boolean.valueOf(str);
		// result
		LimitConfrontation result = new LimitConfrontation(threshold,supLimit,pointProcessor);
		return result;
	}

	private static LimitPoints loadLimitPointsElement(Element root, String folder, PointsProcessor pointProcessor) throws ParserConfigurationException, SAXException, IOException
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.ELT_THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.ATT_VALUE).getValue();
		float threshold = Float.parseFloat(str);
		// supLimit
		str = root.getAttribute(XmlTools.ATT_SUP).getValue();
		boolean supLimit = Boolean.valueOf(str);
		// point processor
    	Element thresholdPointProcessorElt = root.getChild(XmlTools.ELT_SOURCE);
		PointsProcessor thresholdPointProcessor = PointsProcessorLoader.loadPointProcessorFromElement(thresholdPointProcessorElt,folder);
		// result
		LimitPoints result = new LimitPoints(threshold,supLimit,pointProcessor,thresholdPointProcessor);
		return result;
	}
	
	private static LimitScore loadLimitScoreElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.ELT_THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.ATT_VALUE).getValue();
		long threshold = Long.parseLong(str);
		// supLimit
		str = root.getAttribute(XmlTools.ATT_SUP).getValue();
		boolean supLimit = Boolean.valueOf(str);
		// score
		str = root.getAttribute(XmlTools.ATT_TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase(Locale.ENGLISH).trim());
		// result
		LimitScore result = new LimitScore(threshold,supLimit,score,pointProcessor);
		return result;
	}

	private static LimitTime loadLimitTimeElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.ELT_THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.ATT_VALUE).getValue();
		long threshold = Long.parseLong(str);
		// supLimit
		str = root.getAttribute(XmlTools.ATT_SUP).getValue();
		boolean supLimit = Boolean.valueOf(str);
		// result
		LimitTime result = new LimitTime(threshold,supLimit,pointProcessor);
		return result;
	}

	private static LimitLastStanding loadLimitLastStandingElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.ELT_THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.ATT_VALUE).getValue();
		int threshold = Integer.parseInt(str);
		// supLimit
		str = root.getAttribute(XmlTools.ATT_SUP).getValue();
		boolean supLimit = false/*Boolean.valueOf(str)*/;
		// result
		LimitLastStanding result = new LimitLastStanding(threshold,supLimit,pointProcessor);
		return result;
	}
}
