package fr.free.totalboumboum.engine.container.bombset;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;

public class BombsetMap
{
	/////////////////////////////////////////////////////////////////
	// BOMBSETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Bombset bombset = null;
	private final HashMap<PredefinedColor,Bombset> bombsets = new HashMap<PredefinedColor, Bombset>();
	
	public void loadBombset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	bombset = BombsetLoader.loadBombset(folderPath);
	}

	public Bombset loadBombset(String folderPath, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Bombset result = bombsets.get(color);
		if(result==null)
		{	result = bombset.copy();
			BombsetLoader.loadBombset(folderPath,color,result);
			bombsets.put(color,result);
		}
		return result;
	}
}
