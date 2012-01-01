package org.totalboumboum.engine.player;

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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.hero.HeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactoryLoader;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractPlayer
{	
	public AbstractPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.profile = profile;
		
		// sprite
		color = this.profile.getSpriteColor();
		String folder = FilePaths.getHeroesPath()+File.separator+this.profile.getSpritePack();
		folder = folder + File.separator+this.profile.getSpriteFolder();
		HeroFactory tempHeroFactory = HollowHeroFactoryLoader.completeHeroFactory(folder,color,base);
		tempHeroFactory.setInstance(RoundVariables.instance);
		sprite = tempHeroFactory.makeSprite(tile);
		RoundVariables.level.insertSpriteTile(sprite);
		
		sprite.setPlayer(this);
	}

	/////////////////////////////////////////////////////////////////
	// PROFILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Profile profile;
	
	public Profile getProfile()
	{	return profile;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Hero sprite;
	
	public Hero getSprite()
	{	return sprite;
	}

	public String getId()
	{	return profile.getId();
	}

	public String getName()
	{	return profile.getName();
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** current color */
	protected PredefinedColor color;
	
	public PredefinedColor getColor()
	{	return color;
	}
	
	/////////////////////////////////////////////////////////////////
	// OUT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean playerOut = false;
	
	public void setOut()
	{	playerOut = true;
		RoundVariables.loop.playerOut(this);	
	}
	
	public boolean isOut()
	{	return playerOut;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	finished = true;
		color = null;
		profile = null;
		sprite = null;
	}
}
