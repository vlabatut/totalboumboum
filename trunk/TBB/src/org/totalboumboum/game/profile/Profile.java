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
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Represents a player in the game in general (not in a specific Round, for this
 * we have {@link AbstractPlayer}. rather in the whole software, including stats, 
 * settings and other things).
 * <br/>
 * Each profile has a unique id (a UID, actually). In the case of artificial agents,
 * it is possible to affect the same source code to several profiles, for instance
 * to make one AI play against itself.
 * 
 * @author Vincent Labatut
 */
public class Profile implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new profile.
	 */
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
	/** Profile name */
	private String name;

	/**
	 * Returns the profile name.
	 * 
	 * @return
	 * 		Profile name.
	 */
	public String getName()
	{	return name;
	}
	
	/**
	 * Changes the profile name.
	 * 
	 * @param name
	 * 		New profile name.
	 */
	public void setName(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Default sprite for this profile */
	private SpriteInfo defaultSprite;
	/** Currently selected sprite for this profile (can differ from the default sprite) */
	private SpriteInfo selectedSprite;
	
	/**
	 * Returns the default sprite for this profile.
	 * 
	 * @return
	 * 		Default sprite for this profile.
	 */
	public SpriteInfo getDefaultSprite()
	{	return defaultSprite;
	}

	/**
	 * Changes the default sprite for this profile.
	 * 
	 * @param defaultSprite
	 * 		New default sprite for this profile.
	 */
	public void setDefaultSprite(SpriteInfo defaultSprite)
	{	this.defaultSprite = defaultSprite;
	}

	/**
	 * Returns the selected sprite for this profile.
	 * 
	 * @return
	 * 		Selected sprite for this profile.
	 */
	public SpriteInfo getSelectedSprite()
	{	return selectedSprite;
	}

	/**
	 * Changes the selected sprite for this profile.
	 * 
	 * @param selectedSprite
	 * 		New selected sprite for this profile.
	 */
	public void setSelectedSprite(SpriteInfo selectedSprite)
	{	this.selectedSprite = selectedSprite;
	}

	/**
	 * Returns the selected sprite color for this profile.
	 * 
	 * @return
	 * 		Sprite color.
	 */
	public PredefinedColor getSpriteColor()
	{	PredefinedColor result = selectedSprite.getColor();
		if(result==null)
			result = defaultSprite.getColor();
		return result;
	}

	/**
	 * Returns the selected sprite name for this profile.
	 * 
	 * @return
	 * 		Sprite name.
	 */
	public String getSpriteName()
	{	String result = selectedSprite.getName();
		if(result==null)
			result = defaultSprite.getName();
		return result;
	}

	/**
	 * Returns the selected sprite pack for this profile.
	 * 
	 * @return
	 * 		Sprite pack.
	 */
	public String getSpritePack()
	{	String result = selectedSprite.getPack();
		if(result==null)
			result = defaultSprite.getPack();
		return result;
	}

	/**
	 * Returns the selected sprite folder for this profile.
	 * 
	 * @return
	 * 		Sprite folder.
	 */
	public String getSpriteFolder()
	{	String result = selectedSprite.getFolder();
		if(result==null)
			result = defaultSprite.getFolder();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// LAST HOST		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Last host this profile was used with */
	private String lastHost = null;
	
	/**
	 * Changes the last host for this profile.
	 * 
	 * @param lastHost
	 * 		New last host.
	 */
	public void setLastHost(String lastHost)
	{	this.lastHost = lastHost;
	}
	
	/**
	 * Returns the last host for this profile.
	 * 
	 * @return
	 * 		Last host.
	 */
	public String getLastHost()
	{	return lastHost;		
	}
	
	/**
	 * Indicates whether this profile is used on a remote
	 * host (or the local one).
	 * 
	 * @return
	 * 		{@code true} iff this profile is used remotely.
	 */
	public boolean isRemote()
	{	String hostId = Configuration.getConnectionsConfiguration().getHostId();
		boolean result = !lastHost.equals(hostId);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// AI				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of the agent controling this profile */
	private String aiName;
	/** Pack of the agent controling this profile */
	private String aiPackname;

	/**
	 * Whether this profile is ai-controlled or not.
	 * 
	 * @return
	 * 		{@code true} iff this profile is controlled by an artificial agent.
	 */
	public boolean hasAi()
	{	return aiName != null;		
	}
	
	/**
	 * Returns the name of the agent controlling this profile.
	 *  
	 * @return
	 * 		Name of the agent.
	 */
	public String getAiName()
	{	return aiName;
	}
	
	/**
	 * Changes the name of the agent controlling this profile.
	 *  
	 * @param aiName
	 * 		New name of the agent.
	 */
	public void setAiName(String aiName)
	{	this.aiName = aiName;
	}
	
	/**
	 * Returns the pack of the agent controlling this profile.
	 *  
	 * @return
	 * 		Pack of the agent.
	 */
	public String getAiPackname()
	{	return aiPackname;
	}
	
	/**
	 * Changes the pack of the agent controlling this profile.
	 *  
	 * @param aiPackname
	 * 		New pack of the agent.
	 */
	public void setAiPackname(String aiPackname)
	{	this.aiPackname = aiPackname;
	}

	/////////////////////////////////////////////////////////////////
	// PORTRAITS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Images used to represent the agent */
	transient private Portraits portraits;
	
	/**
	 * Returns the images used to represent the agent.
	 * 
	 * @return
	 * 		Set of images.
	 */
	public Portraits getPortraits()
	{	return portraits;
	}
	
	/**
	 * Changes the images used to represent the agent.
	 * 
	 * @param portraits
	 * 		New set of images.
	 */
	public void setPortraits(Portraits portraits)
	{	this.portraits = portraits;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Keys used to control this profile */
	transient private LocalPlayerControl spriteControl;
	/** Number of the keyset used to control this agent */
	private int controlSettingsIndex = 0;
	
	/**
	 * Returns the index of this profile in-game controls.
	 * 
	 * @return
	 * 		Index of the controls.
	 */
	public int getControlSettingsIndex()
	{	return controlSettingsIndex;
	}
	
	/**
	 * Changes the index of this profile in-game controls.
	 * 
	 * @param controlSettings
	 * 		New index for the controls.
	 */
	public void setControlSettingsIndex(int controlSettings)
	{	this.controlSettingsIndex = controlSettings;
	}
	
	/**
	 * Returns the keys used to control this profile during game.
	 * 
	 * @return
	 * 		Keys controlling this profile.
	 */
	public LocalPlayerControl getSpriteControl()
	{	return spriteControl;
	}
	
	/**
	 * Changes the keys used to control this profile during game.
	 * 
	 * @param spriteControl
	 * 		New keys controlling this profile.
	 */
	public void setSpriteControl(LocalPlayerControl spriteControl)
	{	this.spriteControl = spriteControl;
	}

	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Unique id of this profile */
	private String id = null;
	
	/**
	 * Returns the unique id of this profile.
	 * 
	 * @return
	 * 		Unique id of this profile.
	 */
	public String getId()
	{	return id;	
	}
	
	/**
	 * Changes the unique id of this profile.
	 * 
	 * @param id
	 * 		New unique id for this profile.
	 */
	public void setId(String id)
	{	this.id = id;	
	}
	
	/////////////////////////////////////////////////////////////////
	// READY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this profile is ready (for remote profiles) */
	private boolean ready = false;
	
	/**
	 * Changes the readyness of this profile
	 * (for remote profiles).
	 * 
	 * @param ready
	 * 		If {@code true}, the agent is ready to start the game.
	 */
	public void setReady(boolean ready)
	{	this.ready = ready;
	}
	
	/**
	 * Returns the readyness of this profile
	 * (for remote profiles).
	 * 
	 * @return
	 * 		If {@code true}, the agent is ready to start the game.
	 */
	public boolean isReady()
	{	return ready;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Makes a copy of this profile.
	 * 
	 * @return
	 * 		Copy of this profile.
	 */
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
	
	/**
	 * Synchronizes this profile with the specified
	 * one, in terms of portraits and sprites.
	 * 
	 * @param profile
	 * 		Reference profile.
	 */
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
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof Profile)
		{	Profile temp = (Profile) o;
			result = temp.getId().equals(id);
		}
		return result;
	}

	/**
	 * Indicates if this profile and the specified one
	 * are equivalent in terms of ai, name, controls, and sprite.
	 * 
	 * @param profile
	 * 		Profile to which this one must be compared.
	 * @return
	 * 		{@code true} iff both profile are equivalent.
	 */
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
