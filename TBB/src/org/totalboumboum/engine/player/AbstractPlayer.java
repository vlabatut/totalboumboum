package org.totalboumboum.engine.player;

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
 * Represents a generic player
 * during a round.
 * 
 * @author Vincent Labatut
 */
public abstract class AbstractPlayer
{	
	/**
	 * Builds a new player.
	 * 
	 * @param profile
	 * 		Profile associated to this player (profiles are round-independent)
	 * @param base
	 * 		Base for the associated sprite.
	 * @param tile
	 * 		Starting tile.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while reading the player data.
	 * @throws SAXException
	 * 		Problem while reading the player data.
	 * @throws IOException
	 * 		Problem while reading the player data.
	 * @throws ClassNotFoundException
	 * 		Problem while reading the player data.
	 */
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
// NOTE should send a sprite creation event? what about insertion?		
		
		sprite.setPlayer(this);
	}

	/////////////////////////////////////////////////////////////////
	// PROFILE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Profile associated to this player */
	protected Profile profile;
	
	/**
	 * Returns the profile object associated to this player.
	 *  
	 * @return
	 * 		Profile object associated to this player 
	 */
	public Profile getProfile()
	{	return profile;
	}
	
	/**
	 * Returns the profile associated to this player.
	 *  
	 * @return
	 * 		Profile associated to this player 
	 */
	public String getId()
	{	return profile.getId();
	}

	/**
	 * Returns the profile name associated to this player.
	 *  
	 * @return
	 * 		Profile name associated to this player 
	 */
	public String getName()
	{	return profile.getName();
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Sprite associated to this player */
	protected Hero sprite;
	
	/**
	 * Returns the sprite associated to this player.
	 *  
	 * @return
	 * 		Profile associated to this player 
	 */
	public Hero getSprite()
	{	return sprite;
	}

	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** current color */
	protected PredefinedColor color;
	
	/**
	 * Returns the color associated to this player.
	 *  
	 * @return
	 * 		Color associated to this player 
	 */
	public PredefinedColor getColor()
	{	return color;
	}
	
	/////////////////////////////////////////////////////////////////
	// OUT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the player is currently out of the game */
	protected boolean playerOut = false;
	
	/**
	 * Mark this player has currently
	 * out of the game.
	 */
	public void setOut()
	{	playerOut = true;
		RoundVariables.loop.playerOut(this);	
	}
	
	/**
	 * Indicates if this player is currently
	 * out of the game.
	 * 	
	 * @return
	 * 		{@code true} iff this player is out of the game.
	 */
	public boolean isOut()
	{	return playerOut;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	protected boolean finished = false;
	
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
	{	finished = true;
		color = null;
		profile = null;
		sprite = null;
	}
}
