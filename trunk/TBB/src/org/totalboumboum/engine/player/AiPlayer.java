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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.ai.AiLoader;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * Represents a player controled by
 * an artificial intelligence (a program,
 * by opposition to a human player).
 * 
 * @author Vincent Labatut
 */
public class AiPlayer extends ControlledPlayer
{	
	/**
	 * Builds a new AI-controlled player.
	 * 
	 * @param profile
	 * 		Profile associated to this player.
	 * @param base
	 * 		Factory for the appropriate sprite.
	 * @param tile
	 * 		Starting position in the zone (tile).
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws SAXException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws IOException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws SecurityException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws InstantiationException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws IllegalAccessException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws InvocationTargetException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws NoSuchMethodException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws URISyntaxException
	 * 		Problem while accessing the player profile or agent program.
	 */
	public AiPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, URISyntaxException
	{	super(profile,base,tile);

		// artificial intelligence
//		if(this.profile.getAiName() != null)
    	{	ai = AiLoader.loadAi(profile.getAiName(),profile.getAiPackname());
    		ai.init(RoundVariables.instance.getName(),this);
    	}
	}

	/////////////////////////////////////////////////////////////////
	// AI				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Artificial intelligence manager, used to handle the extra thread */
	private AiAbstractManager<?,?> ai = null;
	
	/**
	 * Updates the agent depending on the
	 * last game evolution.
	 * 
	 * @param aisPause
	 * 		Whether the agent is paused, or not.
	 * @return
	 * 		{@code true} iff the agent thread actually returned an action request.
	 */
	public boolean updateAi(boolean aisPause)
	{	boolean result = ai.update(aisPause);
		return result;
	}
	
	/**
	 * Record the agent percepts.
	 * 
	 * @throws IOException 
	 * 		Problem while capturing the agent percepts.
	 */
	public void recordAi() throws IOException
	{	ai.writePercepts();
	}
	
	/**
	 * Intializes all objects necessary
	 * for the agent to run properly.
	 */
	public void initAi()
	{	ai.initAgent();
	}
	
	/**
	 * Returns the object used to handle the agent thread.
	 * 
	 * @return
	 * 		An ai manager object.
	 */
	public AiAbstractManager<?,?> getArtificialIntelligence()
	{	return ai;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			// ai
			if(ai!=null)
			{	ai.finish();
				ai = null;
			}
		}
	}
}
