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

import java.io.File;
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

		if(type.equals(XmlTools.ELT_CONFRONTATION))
			result = loadLimitConfrontationElement(root);
		
		else if(type.equals(XmlTools.ELT_POINTS))
			result = loadLimitPointsElement(root,folder);
		
		else if(type.equals(XmlTools.ELT_SCORE))
			result = loadLimitScoreElement(root);
		
		else if(type.equals(XmlTools.ELT_TIME))
			result = loadLimitTimeElement(root);
		
		else if(type.equals(XmlTools.ELT_TOTAL))
			result = loadLimitLastStandingElement(root);
		
		return result;
	}

	private static LimitConfrontation loadLimitConfrontationElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		int value = Integer.parseInt(str);
		// result
		LimitConfrontation result = new LimitConfrontation(value);
		return result;
	}

    private static void loadPointsElement(Element root, String folder, LimitPoints result) throws ParserConfigurationException, SAXException, IOException
	{	PointsProcessor pp;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			pp = PointsProcessorLoader.loadPointProcessorFromFilePath(folder);
		}
		else
			pp = PointsProcessorLoader.loadPointProcessorFromName(name);
		result.setPointProcessor(pp);
	}
	private static LimitPoints loadLimitPointsElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		float value = Float.valueOf(str);
		// result
		LimitPoints result = new LimitPoints(value);
		// point processor
    	Element element = root.getChild(XmlTools.ELT_POINTS);
		loadPointsElement(element,folder,result);
		return result;
	}
	
	private static LimitScore loadLimitScoreElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		long value = Long.valueOf(str);
		// score
		str = root.getAttribute(XmlTools.ATT_TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase(Locale.ENGLISH).trim());
		// inf or sup limit ?
		str = root.getAttribute(XmlTools.ATT_SUP).getValue();
		boolean supLimit = Boolean.valueOf(str);
		// winner ?
		str = root.getAttribute(XmlTools.ATT_WIN).getValue();
		boolean win = Boolean.valueOf(str);
		// result
		LimitScore result = new LimitScore(value,score,supLimit,win);
		return result;
	}

	private static LimitTime loadLimitTimeElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		long value = Long.valueOf(str);
		// result
		LimitTime result = new LimitTime(value);
		return result;
	}

	private static LimitLastStanding loadLimitLastStandingElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		int value = Integer.valueOf(str);
		// result
		LimitLastStanding result = new LimitLastStanding(value);
		return result;
	}
}
