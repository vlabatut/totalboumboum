package fr.free.totalboumboum.gui.data.language;

import java.util.HashMap;

public class Language
{
	private final HashMap<String, String> texts = new HashMap<String,String>();
	private String name;
	
	public void setName(String name)
	{	this.name = name;	
	}
	public String getName()
	{	return name;
	}
	
	public void addText(String key, String value)
	{	texts.put(key, value);		
	}
	
	public String getText(String key)
	{	String result = texts.get(key);
		if(result==null)
			result = "N/A";
		return result;
	}
}
