package org.totalboumboum.engine.player;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AbstractAiManager;
import org.totalboumboum.ai.AiLoader;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.HeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HeroFactoryLoader;
import org.totalboumboum.engine.control.PlayerControl;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.FileTools;
import org.xml.sax.SAXException;


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
	
	public Player(Profile profile, HeroFactory base, Tile tile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.profile = profile;
		// sprite
		color = this.profile.getSpriteColor();
		String folder = FileTools.getHeroesPath()+File.separator+this.profile.getSpritePack();
		folder = folder + File.separator+this.profile.getSpriteFolder();
		HeroFactory tempHeroFactory = HeroFactoryLoader.completeHeroFactory(folder,color,base);
		tempHeroFactory.setInstance(RoundVariables.instance);
		sprite = tempHeroFactory.makeSprite(tile);
//		tile.addSprite(sprite);
		RoundVariables.level.insertSpriteTile(sprite);
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
    		ai.init(RoundVariables.instance.getName(),this);
    	}
	}
	
	public void update(boolean aisPause)
	{	if(hasAi())
			ai.update(aisPause);
	}
	
	public int getId()
	{	return profile.getId();
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
	
	public boolean hasAi()
	{	return ai!=null;	
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
		RoundVariables.loop.playerOut(this);	
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
