package org.totalboumboum.game.round;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.loop.LocalLoop;
import org.totalboumboum.gui.tools.MessageDisplayer;
import org.totalboumboum.tools.GameData;


public class RoundVariables
{
	/////////////////////////////////////////////////////////////////
	// INSTANCE PATH		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Instance instance;
	
    public static void setInstance(Instance instance)
    {	RoundVariables.instance = instance;
    }

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Level level;
	public static LocalLoop loop;
	
	public static void setLoop(LocalLoop loop)
	{	RoundVariables.loop = loop;
		RoundVariables.level = loop.getLevel();		
	}
	
	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static double zoomFactor;
	public static double toleranceCoefficient = 1;
	public static double scaledTileDimension;

	public static void setZoomFactor(double zoomFactor)
	{	RoundVariables.zoomFactor = zoomFactor;
		toleranceCoefficient = zoomFactor*GameData.TOLERANCE;
		scaledTileDimension = GameData.STANDARD_TILE_DIMENSION*zoomFactor;
	}

	/////////////////////////////////////////////////////////////////
	// PRE-ROUND MESSAGES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static MessageDisplayer messageDisplayers[] = null;
	
	public static void initMessageDisplayers(String texts[])
	{	if(messageDisplayers == null)
		{	messageDisplayers = new MessageDisplayer[texts.length];
			Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
			double coef = 0.9;
			Font displayedTextFont = loop.getPanel().getMessageFont(dim.width*coef,dim.height*coef);
			displayedTextFont = displayedTextFont.deriveFont(Font.BOLD);
			int xc = (int)Math.round(dim.width/2);
			int yc = (int)Math.round(dim.height/2);
			for(int i=0;i<texts.length;i++)
			{	if(texts[i]!=null)
				{	MessageDisplayer temp = new MessageDisplayer(displayedTextFont,xc,yc);
					temp.setFatten(3);
					temp.setTextColor(new Color(204, 18,128));
					temp.updateText(texts[i]);
					messageDisplayers[i] = temp;
				}
				else
					messageDisplayers[i] = null;
			}
		}
	}
}