package fr.free.totalboumboum.data.profile;

import com.sun.org.apache.xpath.internal.operations.Equals;

import fr.free.totalboumboum.engine.control.PlayerControl;

public class Profile
{
	/** name */
	private String name;
	/** controls */
	private ControlSettings controlSettings;
	/** sprite */
	private String spritePack;
	private String spriteName;
	private PredefinedColor spriteColor;
	/** artificial intelligence */
//NOTE ça serait intéressant de charger l'IA une fois pour toutes au début du tournoi, et non pas avant chaque round
	private String ai;
	//
	private Portraits portraits;
	
	private PlayerControl spriteControl;
	
	public Profile()
	{	name = null;
		controlSettings = new ControlSettings();
		spritePack = null;
		spriteName = null;
		spriteColor = null;
		ai = null;
	}

	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	public ControlSettings getControlSettings()
	{	return controlSettings;
	}
	public void setControlSettings(ControlSettings controlSettings)
	{	this.controlSettings = controlSettings;
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

	public PredefinedColor getSpriteColor()
	{	return spriteColor;
	}
	public void setSpriteColor(PredefinedColor spriteColor)
	{	this.spriteColor = spriteColor;
	}

	public String getAi()
	{	return ai;
	}
	public void setAi(String ai)
	{	this.ai = ai;
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
}
