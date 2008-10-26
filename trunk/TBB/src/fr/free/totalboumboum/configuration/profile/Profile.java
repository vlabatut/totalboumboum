package fr.free.totalboumboum.configuration.profile;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.engine.control.PlayerControl;

public class Profile
{
	/** name */
	private String name;
	/** sprite */
	private String spritePack;
	private String spriteName;
	private PredefinedColor[] spriteColors;
	/** artificial intelligence */
	private String aiName;
	private String aiPackname;
	//
	private Portraits portraits;
	
	private PlayerControl spriteControl;
	private int controlSettingsIndex = 0;
	
	public Profile()
	{	name = null;
		spritePack = null;
		spriteName = null;
		spriteColors = new PredefinedColor[GameConstants.MAX_PROFILES_COUNT];
		aiName = null;
		aiPackname = null;
	}

	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	public String getSpritePack()
	{	return spritePack;
	}
	public void setSpritePack(String spritePack)
	{	this.spritePack = spritePack;
	}

	public String getSpriteName()
	{	return spriteName;
	}
	public void setSpriteName(String spriteName)
	{	this.spriteName = spriteName;
	}

	public PredefinedColor[] getSpriteColors()
	{	return spriteColors;
	}
	public PredefinedColor getSpriteColor()
	{	return spriteColors[0];
	}
	public void setSpriteColor(PredefinedColor spriteColor)
	{	this.spriteColors[0] = spriteColor;
	}

	public boolean hasAi()
	{	return aiName != null;		
	}
	public String getAiName()
	{	return aiName;
	}
	public void setAiName(String aiName)
	{	this.aiName = aiName;
	}
	public String getAiPackname()
	{	return aiPackname;
	}
	public void setAiPackname(String aiPackname)
	{	this.aiPackname = aiPackname;
	}

	public PlayerControl getSpriteControl()
	{	return spriteControl;
	}
	public void setSpriteControl(PlayerControl spriteControl)
	{	this.spriteControl = spriteControl;
	}

	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof Profile)
		{	Profile temp = (Profile) o;
			result = temp.getName().equalsIgnoreCase(name);
		}
		return result;
	}

	public Portraits getPortraits()
	{	return portraits;
	}
	public void setPortraits(Portraits portraits)
	{	this.portraits = portraits;
	}

	public int getControlSettingsIndex()
	{	return controlSettingsIndex;
	}
	public void setControlSettingsIndex(int controlSettings)
	{	this.controlSettingsIndex = controlSettings;
	}
	
	public boolean isAi()
	{	return aiName!=null;	
	}
}
