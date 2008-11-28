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

import fr.free.totalboumboum.engine.control.PlayerControl;

public class Profile
{
	public Profile()
	{	name = null;
		spritePack = null;
		spriteFolder = null;
		spriteDefaultColor = null;
		aiName = null;
		aiPackname = null;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;

	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// FILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String fileName;

	public String getFileName()
	{	return fileName;
	}
	
	public void setFileName(String fileName)
	{	this.fileName = fileName;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String spritePack;
	private String spriteFolder;
	private String spriteName;
	private PredefinedColor spriteDefaultColor;
	private PredefinedColor spriteSelectedColor;

	public String getSpritePack()
	{	return spritePack;
	}
	
	public void setSpritePack(String spritePack)
	{	this.spritePack = spritePack;
	}

	public String getSpriteFolder()
	{	return spriteFolder;
	}
	
	public void setSpriteFolder(String spriteFolder)
	{	this.spriteFolder = spriteFolder;
	}

	public PredefinedColor getSpriteDefaultColor()
	{	return spriteDefaultColor;
	}
	
	public String getSpriteName()
	{	return spriteName;
	}
	
	public void setSpriteName(String spriteName)
	{	this.spriteName = spriteName;
	}

	public PredefinedColor getSpriteSelectedColor()
	{	PredefinedColor result = spriteSelectedColor;
		if(result==null)
			result = spriteDefaultColor;
		return result;
	}
	
	public void setSpriteSelectedColor(PredefinedColor spriteSelectedColor)
	{	this.spriteSelectedColor = spriteSelectedColor;		
	}
	
	public void setSpriteDefaultColor(PredefinedColor spriteDefaultColor)
	{	this.spriteDefaultColor = spriteDefaultColor;
	}

	/////////////////////////////////////////////////////////////////
	// AI				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String aiName;
	private String aiPackname;

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

	/////////////////////////////////////////////////////////////////
	// PORTRAITS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Portraits portraits;

	public Portraits getPortraits()
	{	return portraits;
	}
	
	public void setPortraits(Portraits portraits)
	{	this.portraits = portraits;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PlayerControl spriteControl;
	private int controlSettingsIndex = 0;

	public int getControlSettingsIndex()
	{	return controlSettingsIndex;
	}
	
	public void setControlSettingsIndex(int controlSettings)
	{	this.controlSettingsIndex = controlSettings;
	}
	
	public PlayerControl getSpriteControl()
	{	return spriteControl;
	}
	
	public void setSpriteControl(PlayerControl spriteControl)
	{	this.spriteControl = spriteControl;
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Profile copy()
	{	Profile result = new Profile();
		result.aiName = aiName;
		result.aiPackname = aiPackname;
		result.controlSettingsIndex = controlSettingsIndex;
		result.name = name;
		result.portraits = portraits; //TODO copy
		result.spriteDefaultColor = spriteDefaultColor;
		result.spriteSelectedColor = spriteSelectedColor;
		result.spriteControl = spriteControl;//TODO copy
		result.spriteFolder = spriteFolder;
		result.spritePack = spritePack;
		result.spriteName = spriteName;
		return result;
	}

	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof Profile)
		{	Profile temp = (Profile) o;
			result = temp.getFileName().equalsIgnoreCase(fileName);
		}
		return result;
	}

	public boolean isTheSame(Profile profile)
	{	boolean result = true;
		result = result && hasAi()==profile.hasAi();
		if(hasAi())
		{	result = result && aiName.equals(profile.getAiName());
			result = result && aiPackname.equals(profile.getAiPackname());
		}
		result = result && name.equals(profile.getName());
		result = result && spriteSelectedColor == profile.getSpriteSelectedColor();
		result = result && spriteDefaultColor == spriteDefaultColor;
		result = result && spriteFolder.equals(profile.getSpriteFolder());
		result = result && spritePack.equals(profile.getSpritePack());
		return result;
	}
}
