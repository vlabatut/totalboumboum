package fr.free.totalboumboum.data.profile;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Portraits
{
	public static final String INGAME_LOST = "LOST";
	public static final String INGAME_NORMAL = "NORMAL";
	public static final String INGAME_OUT = "OUT";
	public static final String INGAME_WON = "WON";
	
	public static final String OUTGAME_BODY = "BODY";
	public static final String OUTGAME_HEAD = "HEAD";
	
	
	HashMap<String, BufferedImage> ingame = new HashMap<String, BufferedImage>();
	HashMap<String, BufferedImage> outgame = new HashMap<String, BufferedImage>();
	
	public void addIngamePortrait(String name, BufferedImage image)
	{	ingame.put(name, image);		
	}
	public BufferedImage getIngamePortrait(String name)
	{	return ingame.get(name);	
	}
	public boolean containsIngamePortrait(String name)
	{	return ingame.containsKey(name);	
	}

	public void addOutgamePortrait(String name, BufferedImage image)
	{	outgame.put(name, image);		
	}
	public BufferedImage getOutgamePortrait(String name)
	{	return outgame.get(name);	
	}
	public boolean containsOutgamePortrait(String name)
	{	return outgame.containsKey(name);	
	}
}

