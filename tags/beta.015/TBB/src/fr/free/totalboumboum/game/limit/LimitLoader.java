package fr.free.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import fr.free.totalboumboum.statistics.detailed.Score;
import fr.free.totalboumboum.tools.XmlTools;

public class LimitLoader
{
	public static Limit loadLimitElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Limit result = null;
		String type = root.getName();
		Element pointProcessorElt = root.getChild(XmlTools.POINTS);
		PointsProcessor pointProcessor = PointsProcessorLoader.loadPointProcessorFromElement(pointProcessorElt,folder);

		if(type.equals(XmlTools.CONFRONTATION))
			result = loadLimitConfrontationElement(root,pointProcessor);
		
		else if(type.equals(XmlTools.POINTS))
			result = loadLimitPointsElement(root,folder,pointProcessor);
		
		else if(type.equals(XmlTools.SCORE))
			result = loadLimitScoreElement(root,pointProcessor);
		
		else if(type.equals(XmlTools.TIME))
			result = loadLimitTimeElement(root,pointProcessor);
		
		else if(type.equals(XmlTools.LAST_STANDING))
			result = loadLimitLastStandingElement(root,pointProcessor);
		
		return result;
	}

	private static LimitConfrontation loadLimitConfrontationElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.VALUE).getValue();
		int threshold = Integer.parseInt(str);
		// comparator
		str = thresholdElt.getAttribute(XmlTools.COMPARATOR).getValue();
		Comparisons comparatorCode = Comparisons.valueOf(str.toUpperCase(Locale.ENGLISH));
		// result
		LimitConfrontation result = new LimitConfrontation(threshold,comparatorCode,pointProcessor);
		return result;
	}

	private static LimitPoints loadLimitPointsElement(Element root, String folder, PointsProcessor pointProcessor) throws ParserConfigurationException, SAXException, IOException
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.VALUE).getValue();
		float threshold = Float.parseFloat(str);
		// comparator
		str = thresholdElt.getAttribute(XmlTools.COMPARATOR).getValue();
		Comparisons comparatorCode = Comparisons.valueOf(str.toUpperCase(Locale.ENGLISH));
		// point processor
    	Element thresholdPointProcessorElt = root.getChild(XmlTools.SOURCE);
		PointsProcessor thresholdPointProcessor = PointsProcessorLoader.loadPointProcessorFromElement(thresholdPointProcessorElt,folder);
		// result
		LimitPoints result = new LimitPoints(threshold,comparatorCode,pointProcessor,thresholdPointProcessor);
		return result;
	}
	
	private static LimitScore loadLimitScoreElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.VALUE).getValue();
		long threshold = Long.parseLong(str);
		// comparator
		str = thresholdElt.getAttribute(XmlTools.COMPARATOR).getValue();
		Comparisons comparatorCode = Comparisons.valueOf(str.toUpperCase(Locale.ENGLISH));
		// score
		str = root.getAttribute(XmlTools.TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase(Locale.ENGLISH).trim());
		// result
		LimitScore result = new LimitScore(threshold,comparatorCode,score,pointProcessor);
		return result;
	}

	private static LimitTime loadLimitTimeElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.VALUE).getValue();
		long threshold = Long.parseLong(str);
		// comparator
		str = thresholdElt.getAttribute(XmlTools.COMPARATOR).getValue();
		Comparisons comparatorCode = Comparisons.valueOf(str.toUpperCase(Locale.ENGLISH));
		// result
		LimitTime result = new LimitTime(threshold,comparatorCode,pointProcessor);
		return result;
	}

	private static LimitLastStanding loadLimitLastStandingElement(Element root, PointsProcessor pointProcessor)
	{	// threshold
		Element thresholdElt = root.getChild(XmlTools.THRESHOLD);
		String str = thresholdElt.getAttribute(XmlTools.VALUE).getValue();
		int threshold = Integer.parseInt(str);
		// comparator
		str = thresholdElt.getAttribute(XmlTools.COMPARATOR).getValue();
		Comparisons comparatorCode = Comparisons.valueOf(str.toUpperCase(Locale.ENGLISH));
		// result
		LimitLastStanding result = new LimitLastStanding(threshold,comparatorCode,pointProcessor);
		return result;
	}
}
