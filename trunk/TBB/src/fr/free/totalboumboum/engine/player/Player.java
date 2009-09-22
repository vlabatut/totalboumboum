package fr.free.totalboumboum.engine.player;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.ai.AiLoader;
import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.engine.container.bombset.BombsetMap;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactory;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactoryLoader;
import fr.free.totalboumboum.engine.control.PlayerControl;
import fr.free.totalboumboum.tools.FileTools;

public class Player
{	
	private Profile profile;
	/** sprite */
	private Sprite sprite;
	/** artificial intelligence */
	private AbstractAiManager<?> ai = null;
	/** control */
	private PlayerControl spriteControl;
	/** current color */
	private PredefinedColor color;
	/** current controls */
	private ControlSettings controlSettings;
	
	public Player(Profile profile, HeroFactory base, BombsetMap bombsetMap, Tile tile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.profile = profile;
		// sprite
		color = this.profile.getSpriteColor();
		String folder = FileTools.getHeroesPath()+File.separator+this.profile.getSpritePack();
		folder = folder + File.separator+this.profile.getSpriteFolder();
		HeroFactory tempHeroFactory = HeroFactoryLoader.completeHeroFactory(folder,color,base,bombsetMap);
		sprite = tempHeroFactory.makeSprite(tile);
//		tile.addSprite(sprite);
		GameVariables.level.insertSpriteTile(sprite);
		// control settings
		int indexCtrSet = profile.getControlSettingsIndex();
		controlSettings = Configuration.getControlsConfiguration().getControlSettings().get(indexCtrSet);
		if(controlSettings == null)
			controlSettings = new ControlSettings();
		sprite.setControlSettings(controlSettings);
		//
		sprite.setPlayer(this);
		spriteControl = new PlayerControl(this);
	}

	public void initAi() throws FileNotFoundException, IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{	// artificial intelligence
    	if(this.profile.getAiName() != null)
    	{	ai = AiLoader.loadAi(profile.getAiName(), profile.getAiPackname());
    		ai.init(GameVariables.instanceName,this);
    	}
	}
	
	public void update(boolean aisPause)
	{	if(ai!=null)
			ai.update(aisPause);
	}
	
	public String getFileName()
	{	return profile.getFileName();
	}
	public String getName()
	{	return profile.getName();
	}
	public ControlSettings getControlSettings()
	{	return controlSettings;
	}
	public PlayerControl getSpriteControl()
	{	return spriteControl;
	}

	public AbstractAiManager<?> getArtificialIntelligence()
	{	return ai;
	}

	public Sprite getSprite()
	{	return sprite;
	}
	public PredefinedColor getColor()
	{	return color;
	}
	
	private boolean playerOut = false;
	
	public void setOut()
	{	playerOut = true;
		GameVariables.loop.playerOut(this);	
	}
	public boolean isOut()
	{	return playerOut;	
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// control
			spriteControl.finish();
			spriteControl = null;
			// ai
			if(ai!=null)
			{	ai.finish();
				ai = null;
			}
			// misc
			controlSettings = null;
			color = null;
			profile = null;
			sprite = null;
		}
	}
}
