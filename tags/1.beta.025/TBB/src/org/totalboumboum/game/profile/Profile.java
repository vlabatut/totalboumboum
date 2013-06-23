package org.totalboumboum.game.profile;

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

import java.io.Serializable;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.control.player.LocalPlayerControl;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Profile implements Serializable
{	private static final long serialVersionUID = 1L;

	public Profile()
	{	name = null;
		defaultSprite = new SpriteInfo();
		selectedSprite = new SpriteInfo();
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
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private SpriteInfo defaultSprite;
	private SpriteInfo selectedSprite;
	
	public SpriteInfo getDefaultSprite()
	{	return defaultSprite;
	}

	public void setDefaultSprite(SpriteInfo defaultSprite)
	{	this.defaultSprite = defaultSprite;
	}

	public SpriteInfo getSelectedSprite()
	{	return selectedSprite;
	}

	public void setSelectedSprite(SpriteInfo selectedSprite)
	{	this.selectedSprite = selectedSprite;
	}

	public PredefinedColor getSpriteColor()
	{	PredefinedColor result = selectedSprite.getColor();
		if(result==null)
			result = defaultSprite.getColor();
		return result;
	}

	public String getSpriteName()
	{	String result = selectedSprite.getName();
		if(result==null)
			result = defaultSprite.getName();
		return result;
	}

	public String getSpritePack()
	{	String result = selectedSprite.getPack();
		if(result==null)
			result = defaultSprite.getPack();
		return result;
	}

	public String getSpriteFolder()
	{	String result = selectedSprite.getFolder();
		if(result==null)
			result = defaultSprite.getFolder();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// LAST HOST		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String lastHost = null;

	public void setLastHost(String lastHost)
	{	this.lastHost = lastHost;
	}
	
	public String getLastHost()
	{	return lastHost;		
	}
	
	public boolean isRemote()
	{	String hostId = Configuration.getConnectionsConfiguration().getHostId();
		boolean result = !lastHost.equals(hostId);
		return result;
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
	transient private Portraits portraits;

	public Portraits getPortraits()
	{	return portraits;
	}
	
	public void setPortraits(Portraits portraits)
	{	this.portraits = portraits;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private LocalPlayerControl spriteControl;
	private int controlSettingsIndex = 0;

	public int getControlSettingsIndex()
	{	return controlSettingsIndex;
	}
	
	public void setControlSettingsIndex(int controlSettings)
	{	this.controlSettingsIndex = controlSettings;
	}
	
	public LocalPlayerControl getSpriteControl()
	{	return spriteControl;
	}
	
	public void setSpriteControl(LocalPlayerControl spriteControl)
	{	this.spriteControl = spriteControl;
	}

	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String id = null;
	
	public String getId()
	{	return id;	
	}
	
	public void setId(String id)
	{	this.id = id;	
	}
	
	/////////////////////////////////////////////////////////////////
	// READY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean ready = false;
	
	public void setReady(boolean ready)
	{	this.ready = ready;
	}
	
	public boolean isReady()
	{	return ready;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Profile copy()
	{	Profile result = new Profile();
		
		result.aiName = aiName;
		result.aiPackname = aiPackname;
		
		result.controlSettingsIndex = controlSettingsIndex;
		result.name = name;
		result.id = id;
		result.portraits = portraits; //TODO copy
		
		result.defaultSprite = defaultSprite.copy();
		result.selectedSprite = selectedSprite.copy();
		result.spriteControl = spriteControl;//TODO copy
		
		result.lastHost = lastHost;
		result.ready = ready;
		
		return result;
	}
	
	public void synch(Profile profile)
	{
		//this.aiName = profile.aiName;
		//this.aiPackname = profile.aiPackname;
		
		//this.controlSettingsIndex = profile.controlSettingsIndex;
		//this.name = profile.name;
		//this.id = profile.id;
		this.portraits = profile.portraits; 
		
		this.defaultSprite = profile.defaultSprite;
		this.selectedSprite = profile.selectedSprite;
		//this.spriteControl = profile.spriteControl;
		
		//this.lastHost = profile.lastHost;
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof Profile)
		{	Profile temp = (Profile) o;
			result = temp.getId().equals(id);
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
		result = result && controlSettingsIndex == profile.getControlSettingsIndex();

		result = result && ((selectedSprite==null && profile.getSelectedSprite()==null)
							|| (!selectedSprite.hasChanged(profile.getSelectedSprite())));
		result = result && !defaultSprite.hasChanged(profile.getDefaultSprite());
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		result = result + name;
		result = result + ":" + id;
		return result;
	}
}
