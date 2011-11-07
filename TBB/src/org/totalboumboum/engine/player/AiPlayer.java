package org.totalboumboum.engine.player;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.AiAbstractManager;
import org.totalboumboum.ai.AiLoader;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiPlayer extends ControlledPlayer
{	
	public AiPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
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
	/** artificial intelligence */
	private AiAbstractManager<?> ai = null;
	
	public boolean updateAi(boolean aisPause)
	{	boolean result = ai.update(aisPause);
		return result;
	}
	
	public void initAi()
	{	ai.initAgent();
	}
	
	public AiAbstractManager<?> getArtificialIntelligence()
	{	return ai;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
