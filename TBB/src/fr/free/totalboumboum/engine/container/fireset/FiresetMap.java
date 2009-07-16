package fr.free.totalboumboum.engine.container.fireset;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;

public class FiresetMap
{
	/////////////////////////////////////////////////////////////////
	// FIRESETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,Fireset> firesets = new HashMap<String, Fireset>();
	
	public Fireset loadFireset(String folderPath, String name) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Fireset result = firesets.get(name);
		if(result==null)
		{	result = FiresetLoader.loadFireset(folderPath,firesets);
			result.setName(name);
			firesets.put(name,result);
		}
		return result;
	}
}
