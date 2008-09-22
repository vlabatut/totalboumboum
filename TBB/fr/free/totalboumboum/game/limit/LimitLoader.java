package fr.free.totalboumboum.game.limit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LimitLoader
{
	public static Limit loadLimitElement(Element root)
	{	Limit result = null;
		String type = root.getName();

		if(type.equals(XmlTools.ELT_CONFRONTATION))
			result = loadLimitConfrontationElement(root);
		
		else if(type.equals(XmlTools.ELT_POINTS))
			result = loadLimitPointsElement(root);
		
		else if(type.equals(XmlTools.ELT_SCORE))
			result = loadLimitScoreElement(root);
		
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
	private static LimitPoints loadLimitPointsElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		float value = Float.valueOf(str);
		// result
		LimitPoints result = new LimitPoints(value);
		return result;
	}
	
	private static LimitScore loadLimitScoreElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		long value = Long.valueOf(str);
		// score
		str = root.getAttribute(XmlTools.ATT_TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase().trim());
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
}
