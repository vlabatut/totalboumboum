package fr.free.totalboumboum.engine.container.bombset;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.level.Level;

public class BombsetMap
{
	/////////////////////////////////////////////////////////////////
	// BOMBSETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Bombset bombset = null;
	private final HashMap<PredefinedColor,Bombset> bombsets = new HashMap<PredefinedColor, Bombset>();
	
	public void loadBombset(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	bombset = BombsetLoader.loadBombset(folderPath,level);
	}

	public Bombset loadBombset(String folderPath, Level level, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Bombset result = bombsets.get(color);
		if(result==null)
		{	result = bombset.copy();
			BombsetLoader.loadBombset(folderPath,level,color,result);
			bombsets.put(color,result);
		}
		return result;
	}
}
