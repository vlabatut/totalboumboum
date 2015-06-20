package org.totalboumboum.gui.data.language;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Represents all the GUI text for a given language.
 * 
 * @author Vincent Labatut
 */
public class Language
{		
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Language name */
	private String name;
	
	/**
	 * Change the language name.
	 * 
	 * @param name
	 * 		New language name.
	 */
	public void setName(String name)
	{	this.name = name;	
	}
	
	/**
	 * Returns the language name.
	 * 
	 * @return
	 * 		Current language name.
	 */
	public String getName()
	{	return name;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Texts associated to GUI components */
	private final HashMap<String, String> texts = new HashMap<String,String>();

	/**
	 * Adds a new text to this object.
	 * 
	 * @param key
	 * 		Key associated to the text, used to retrieve the text later.
	 * @param value
	 * 		Text itself.
	 */
	public void addText(String key, String value)
	{	texts.put(key, value);		
	}
	
	/**
	 * Returns the text associated to the specified key.
	 * 
	 * @param key
	 * 		Key of the text.
	 * @return
	 * 		Text associated to that key.
	 */
	public String getText(String key)
	{	String result = texts.get(key);
		if(result==null)
			result = key/*"N/A"*/;
		return result;
	}
	
	/**
	 * Returns the map containing all texts.
	 * 
	 * @return
	 * 		A maps of texts.
	 */
	public HashMap<String, String> getTexts()
	{	return texts;
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Makes a full copy of this object.
	 *  
	 * @return
	 * 		A clone of this object.
	 */
	public Language copy()
	{	Language result = new Language();
		result.setName(name);
		Iterator<Entry<String,String>> it = texts.entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,String> temp = it.next();
			String key = temp.getKey();
			String value = temp.getValue();
			result.addText(key,value);
		}
		return result;
	}
}
