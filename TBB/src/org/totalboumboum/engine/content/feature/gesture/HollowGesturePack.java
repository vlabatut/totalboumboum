package org.totalboumboum.engine.content.feature.gesture;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRulesMap;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowGesturePack extends AbstractGesturePack<HollowGesture> implements Serializable
{	private static final long serialVersionUID = 1L;

	public HollowGesturePack()
	{	// init the gesture pack with all possible gestures
		for(GestureName name: GestureName.values())
		{	HollowGesture gesture = new HollowGesture();
			gesture.setName(name);
			addGesture(gesture,name);			
		}
	}

	/////////////////////////////////////////////////////////////////
	// BOUND HEIGHT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* used at loading only */
	private transient double boundHeight = 0;
	
	public double getBoundHeight()
	{	return boundHeight;
	}

	public void setBoundHeight(double boundHeight)
	{	this.boundHeight = boundHeight;
	}

	/////////////////////////////////////////////////////////////////
	// COLOR RULES MAP	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* used at loading only */
	private transient ColorRulesMap colorRulesMap = null;
	
	public ColorRulesMap getColorRulesMap()
	{	return colorRulesMap;
	}

	public void setColorRulesMap(ColorRulesMap colorRulesMap)
	{	this.colorRulesMap = colorRulesMap;
	}

	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double scale;
	
	public double getScale()
	{	return scale;
	}
	
	public void setScale(double scale)
	{	this.scale = scale;
	}

	/////////////////////////////////////////////////////////////////
	// ANIMES REPLACEMENTS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<GestureName,GestureName> animesReplacements = new HashMap<GestureName, GestureName>();
	
	public HashMap<GestureName,GestureName> getAnimesReplacements()
	{	return animesReplacements;
	}

	public void setAnimesReplacements(HashMap<GestureName,GestureName> animesReplacements)
	{	this.animesReplacements = animesReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// SHADOWS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,String> shadowsFileNames = new HashMap<String,String>();
	private HashMap<String,ColorRulesMap> shadowsColorRulesMaps = new HashMap<String,ColorRulesMap>();

	public String getShadowFileName(String key)
	{	return shadowsFileNames.get(key);
	}
	
	public ColorRulesMap getShadowColorRulesMap(String key)
	{	return shadowsColorRulesMaps.get(key);
	}
	
	public void addShadowFileName(String key, String imageFileName, ColorRulesMap colorRulesMap)
	{	shadowsFileNames.put(key,imageFileName);
		shadowsColorRulesMaps.put(key,colorRulesMap);
	}
	public void addShadowFileName(String key, String imageFileName)
	{	shadowsFileNames.put(key,imageFileName);
		shadowsColorRulesMaps.put(key,colorRulesMap);
	}
	
	/////////////////////////////////////////////////////////////////
	// COMMON IMAGES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,String> commonImagesFileNames = new HashMap<String,String>();
	private HashMap<String,ColorRulesMap> commonImagesColorRulesMaps = new HashMap<String,ColorRulesMap>();

	public String getCommonImageFileName(String key)
	{	return commonImagesFileNames.get(key);
	}
	
	public ColorRulesMap getCommonImageRulesMap(String key)
	{	return commonImagesColorRulesMaps.get(key);
	}

	public void addCommonImageFileName(String key, String imageFileName, ColorRulesMap colorRulesMap)
	{	commonImagesFileNames.put(key,imageFileName);
		commonImagesColorRulesMaps.put(key,colorRulesMap);
	}
	public void addCommonImageFileName(String key, String imageFileName)
	{	commonImagesFileNames.put(key,imageFileName);
		commonImagesColorRulesMaps.put(key,colorRulesMap);
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data. All the content is keeped as is (same objects)
	 * but the containers are cloned, since their own content may be changed
	 * through inheritance.
	 */
	public HollowGesturePack copy()
	{	HollowGesturePack result = new HollowGesturePack();
		// gestures
		for(Entry<GestureName,HollowGesture> e: gestures.entrySet())
		{	HollowGesture cp = e.getValue().copy();
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// common images
		result.commonImagesFileNames.putAll(commonImagesFileNames);
		result.commonImagesColorRulesMaps.putAll(commonImagesColorRulesMaps);
		
		// shadows
		result.shadowsFileNames.putAll(shadowsFileNames);
		result.shadowsColorRulesMaps.putAll(shadowsColorRulesMaps);
		
		// misc
		result.spriteName = spriteName;
		result.scale = scale;
		return result;
	}
	
	/**
	 * used when generating an actual Factory from a HollowFactory.
	 * Images names are replaced by the actual images, scalable stuff
	 * is scaled, etc.
	 */
	public GesturePack fill(double zoomFactor, PredefinedColor color) throws IOException
	{	GesturePack result = new GesturePack();
		
		// gestures
		for(Entry<GestureName,HollowGesture> e: gestures.entrySet())
		{	Gesture cp = e.getValue().fill(zoomFactor,scale,color);
			GestureName nm = e.getKey();
			result.addGesture(cp,nm);
		}
		
		// misc
		result.setColor(color);
		
		return result;
	}
}
