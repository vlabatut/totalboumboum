package fr.free.totalboumboum.game.limit;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

public class LimitsLoader
{
	public static Limits loadLimitsElement(Element root)
	{	Limits result = new Limits();

		List<Element> elements = root.getChildren();
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			Limit limit = LimitLoader.loadLimitElement(temp);
			result.addLimit(limit);
		}
		
		return result;
	}
}
