package org.totalboumboum.engine.content.feature.gesture.anime.color;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.io.Serializable;
import java.util.HashMap;

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ColorRulesMap implements Serializable
{	private static final long serialVersionUID = 1L;

	public ColorRulesMap()
	{	ColorLess colorLess = new ColorLess();
		setColorRule(colorLess);		
	}

	/////////////////////////////////////////////////////////////////
	// LOCAL PATH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String localPath = "";
	
	public String getLocalPath()
	{	return localPath;
	}

	public void setLocalPath(String localPath)
	{	this.localPath = localPath;
	}

	/////////////////////////////////////////////////////////////////
	// MAP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<PredefinedColor,ColorRule> colorRules = new HashMap<PredefinedColor, ColorRule>(); 
	
	public ColorRule getColorRule(PredefinedColor color)
	{	return colorRules.get(color);
	}

	public void setColorRule(ColorRule colorRule)
	{	PredefinedColor color = colorRule.getColor();
		colorRules.put(color,colorRule);
		colorRule.setParent(this);
	}
}
