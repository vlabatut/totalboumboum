package fr.free.totalboumboum.game.limit;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

public class LimitsLoader
{
	public static Limits loadLimitsElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Limits result = new Limits();

		List<Element> elements = root.getChildren();
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			Limit limit = LimitLoader.loadLimitElement(temp,folder);
			result.addLimit(limit);
		}
		
		return result;
	}
}
