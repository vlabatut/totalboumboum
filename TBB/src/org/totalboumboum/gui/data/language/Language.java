package org.totalboumboum.gui.data.language;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
 * 
 * @author Vincent Labatut
 *
 */
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
			result = key/*"N/A"*/;
		return result;
	}
	
	public HashMap<String, String> getTexts()
	{	return texts;
	}
	
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
